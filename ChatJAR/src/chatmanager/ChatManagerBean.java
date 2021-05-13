package chatmanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;

import models.Message;
import models.User;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Singleton
@LocalBean
public class ChatManagerBean implements ChatManagerRemote {
	
	private List<User> registredUsers = new ArrayList<User>();

	private List<User> loggedInUsers = new ArrayList<User>();
	
	private List<Message> allMessages = new ArrayList<Message>();
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
	
	}

	@Override
	public boolean registerRemote(User user) {
		// TODO Check if user with username already exists
		for (User u : registredUsers) {
			if (user.getUsername().equals(u.getUsername())) {
				System.out.println("Registration failed!");
				return false;
			}
			
		}
		registredUsers.add(user);
		System.out.println("User: " + user.toString() + "is registrated!");
		return true;
	}

	@Override
	public boolean login(User u) {
		
		if (isLogged(u)) {
			return true;
		}
		
		for (User user : registredUsers) {
			if(user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
				loggedInUsers.add(u);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<User> getLoggedInUsers() {
		return loggedInUsers;
	}

	@Override
	public boolean logoutUser(String username) {
		// TODO Auto-generated method stub
		for (User user : loggedInUsers) {
			if (user.getUsername().equals(username)) {
				loggedInUsers.remove(user);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<User> getRegistredUsers() {
		// TODO Auto-generated method stub
		return registredUsers;
	}

	@Override
	public List<Message> getAllMessage(String username) {
		List<Message> userMessages = new ArrayList<Message>();
		for(Message m : allMessages)
			if (m.getSender().equals(username) || m.getReceiver().equals(username))
				userMessages.add(m);
		return userMessages;
	}

	@Override
	public void addMessage(Message m) {
		// TODO Auto-generated method stub
		allMessages.add(m);
		
	}

	@Override
	public boolean isLogged(User user) {
		for (User u : loggedInUsers) {
			if (u.getUsername().equals(user.getUsername())) {
				return true;
			}
		}

		return false;
	}
	
	

}
