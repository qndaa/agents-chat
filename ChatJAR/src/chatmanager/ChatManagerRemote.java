package chatmanager;

import java.util.List;

import javax.ejb.Remote;

import models.Message;
import models.User;

@Remote
public interface ChatManagerRemote {
	public boolean login(User user);

	public boolean registerRemote(User user);

	public List<User> getLoggedInUsers();

	public boolean logoutUser(String username);
	
	public List<User> getRegistredUsers();
	
	public List<Message> getAllMessage(String username);

	public void addMessage(Message m);

}
