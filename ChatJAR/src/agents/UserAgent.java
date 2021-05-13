package agents;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import models.User;
import ws.WSEndPoint;
/**
 * Sledece nedelje cemo prebaciti poruke koje krajnji korisnik treba da vidi da se 
 * salju preko Web Socketa na front-end (klijentski deo aplikacije)
 * @author Aleksandra
 *
 */
@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;
	@EJB
	private CachedAgentsRemote cachedAgents;

	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	WSEndPoint ws;
	
	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}
	
	@Override
	public void handleMessage(AgentMessage msg) {
		
		System.out.println("Agent handled message: " + msg.getOperation());
		
		switch (msg.getOperation()) {
		case "REGISTRATION":	
			if(chatManager.registerRemote(new User(msg.getParam("username"), msg.getParam("password")))) {
				System.out.print(true);
				ws.echoTextMessage("REGISTRATION SENDER:" + agentId +  " REGISTRATION-SUCCESS");
				ws.sendAllActiveUsers(getRegisteredUsersString());
			} else {
				System.out.print(false);

				ws.echoTextMessage("REGISTRATION SENDER:" + agentId  + " REGISTRATION-FAILED");
			}
			break;
			
			
		case "LOGIN" :
			User user = new User(msg.getParam("username"), msg.getParam("password"));
			
			if (chatManager.login(user)) {
				ws.putUserOnSession(user, agentId);
				ws.echoTextMessage("LOGIN SENDER:" + agentId  + " LOGIN-SUCCESS " + user.getUsername());
				ws.echoTextMessage(getRegisteredUsersString());
				ws.sendAllActiveUsers(getLoggedInUsersString());
			} else {
				ws.echoTextMessage("LOGIN SENDER:" + agentId  + " LOGIN-FAILED " + user.getUsername());
			}
			break;
			
		case "LOGOUT":
		
			if(chatManager.logoutUser(msg.getParam("username"))){
				ws.removeUserFromSession(agentId);
				ws.echoTextMessage("LOGOUT SENDER:" + agentId  + " LOGOUT-SUCCESS");
				ws.sendAllActiveUsers(getLoggedInUsersString());

			} else {
				ws.echoTextMessage("LOGOUT SENDER:" + agentId  + " LOGOUT-FAILED");
			}
			
			break;
			
		case "LOGGED_USERS":
			if (ws.getUserFromSession(agentId) != null) {
				ws.echoTextMessage(getLoggedInUsersString());
			}
			
			break;
		case "REGISTERED_USERS":
			
			System.out.println("OVdje sam");

			
			if (ws.getUserFromSession(agentId) != null) {
				ws.echoTextMessage(getRegisteredUsersString());
			}
			
			break;
			
		case "MESSAGE_ALL":
			List<User> loggedInUsers = chatManager.getLoggedInUsers();
			System.out.print("Broj ulogovanih:" + loggedInUsers.size());
			for (User u : loggedInUsers) {
				addMessage(u, msg.getParam("subject"), msg.getParam("content"));
				
				if(ws.getSessionFromUsername(u.getUsername()) != null) {
					ws.sendMessages(ws.getSessionFromUsername(u.getUsername()), getAllMessageString(u.getUsername()));
				}
			}
			
			break;
	
		case "MESSAGE":
			
			models.Message m = new models.Message(ws.getUserFromSession(agentId), 
					msg.getParam("reciver"), 
					LocalDateTime.now(), msg.getParam("subject"), msg.getParam("content"));
			chatManager.addMessage(m);
			
			if (ws.getSessionFromUsername(msg.getParam("reciver")) != null) {
				ws.sendMessages(ws.getSessionFromUsername(msg.getParam("reciver")), getAllMessageString(msg.getParam("reciver")));	
			}
			
			if(ws.getUserFromSession(agentId) != null) {
				ws.sendMessages(agentId, getAllMessageString(ws.getUserFromSession(agentId)));
			}
			
			break;
			
		case "GET_MESSAGES":
			if(ws.getUserFromSession(agentId) != null) {
				ws.sendMessages(agentId, getAllMessageString(ws.getUserFromSession(agentId)));
			}
			
			
			
			break;
			

		default:
			System.out.print("Bad");
			break;
		}
		
	}
	
	
	private String getAllMessageString(String username) {
		List<models.Message> messages = chatManager.getAllMessage(username);
		
		StringBuilder sb = new StringBuilder();
		sb.append("MESSAGES&");
		
		System.out.println(messages.size());
		
		for (models.Message message : messages) {
			sb.append(message.getSender() + ";");
			sb.append(message.getReceiver() + ";");
			sb.append(message.getSubject() + ";");
			sb.append(message.getTime() + ";");
			sb.append(message.getContent() + "&");

		}
		
		
		return sb.toString().substring(0, sb.length()-1);

	}

	private void addMessage(User u, String subject, String content) {
		// TODO Auto-generated method stub
		String username = ws.getUserFromSession(agentId);
		System.out.print(username);
		if (username != null) {
			models.Message m = new models.Message(username, u.getUsername(), LocalDateTime.now(), subject, content); 
			chatManager.addMessage(m);
			
		}
		
	}

	public String getRegisteredUsersString() {
		List<User> users = chatManager.getRegistredUsers();
		
		
		StringBuilder sb = new StringBuilder();
		for (User u : users) {
			sb.append(u.getUsername());
			sb.append(",");
		}
		
		
		return "REGISTERED_USERS " + "SENDER:" + agentId + " " + sb.toString().substring(0, sb.length()-1);
	}
	
	
	public String getLoggedInUsersString() {

		List<User> users = chatManager.getLoggedInUsers();
			
		StringBuilder sb = new StringBuilder();
		for (User u : users) {
			sb.append(u.getUsername());
			sb.append(",");
		}
			
			
		return "LOGGED_USERS "  + "SENDER:" + agentId + " " +  sb.toString().substring(0, sb.length()-1);
	}

	@Override
	public String init(String id) {
		agentId = id;
		//agentId = generateId();
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}


	@Override
	public String getAgentId() {
		return agentId;
	}
}
