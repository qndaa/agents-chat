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
	public void handleMessage(Message msg) {
		TextMessage tmsg = (TextMessage) msg;
		String receiver;
		String sender;
		String option;
		String response = "";
		try {
			receiver = (String) tmsg.getObjectProperty("receiver");
			sender = (String) tmsg.getObjectProperty("sender");
			
			if (receiver.equals(agentId)) {
				option = (String) tmsg.getObjectProperty("option");
				sender = (String) tmsg.getObjectProperty("sender");
				
				switch (option) {
				
				case "GET_ALL_USERS":
					List<User> users = chatManager.loggedInUsers();
					// TODO If no users are logged in return some form of message
					for (User user : users) {
						response += user.toString() + "\n";
					}
					if(users.size() == 0)
						response += "No users loggedIn";
					
					ws.echoTextMessage("RECEIVER:"+sender+"CONTENT:"+response);

					break;
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
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
