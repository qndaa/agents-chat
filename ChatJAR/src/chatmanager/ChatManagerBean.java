package chatmanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import models.User;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Stateful
@LocalBean
public class ChatManagerBean implements ChatManagerRemote, ChatManagerLocal {

	private List<User> users = new ArrayList<User>();
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
	}

	@Override
	public boolean register(User user) {
		// TODO Check if user with username already exists
		users.add(user);
		return true;
	}

	@Override
	public boolean login(String username, String password) {
		boolean exists = users.stream().anyMatch(u->u.getUsername().equals(username) && u.getPassword().equals(password));
		return exists;
	}

	@Override
	public List<User> loggedInUsers() {
		return users;
	}

}
