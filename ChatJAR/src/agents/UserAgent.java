package agents;

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
		default:
			System.out.print("Bad");
			break;
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
