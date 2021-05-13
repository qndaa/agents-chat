package rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import chatmanager.ChatManagerBean;
import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;

@Stateless
@Path("/chat")
@LocalBean
@Remote(ChatRest.class)
public class ChatBeanRest implements ChatRest {
	
	
	@EJB
	MessageManagerRemote msm;
	
	@EJB
	ChatManagerRemote chm;
	
	@Override
	public String test() {
		return "OK";
	}
	

	@Override
	public void registration(User user, String id) {
		System.out.println(id + " " + user.toString());
		
		AgentMessage msg = new AgentMessage("REGISTRATION", id);
		msg.addParam("username", user.getUsername());
		msg.addParam("password", user.getPassword());
		msm.post(msg);
		
	}
	
	@Override
	public void login(User user, String id) {
		// TODO Auto-generated method stub
		AgentMessage msg = new AgentMessage("LOGIN", id);
		msg.addParam("username", user.getUsername());
		msg.addParam("password", user.getPassword());
		msm.post(msg);
		
		
	}

	
	@Override
	public void logout(String id, String username) {
		// TODO Auto-generated method stub
		AgentMessage msg = new AgentMessage("LOGOUT", id);
		msg.addParam("username", username);
		msm.post(msg);
		
	}
	

	@Override
	public void getLoggedInUsers(String sender) {
		AgentMessage msg = new AgentMessage("LOGGED_USERS", sender);
		msm.post(msg);
	}


	

	@Override
	public void getRegistredUsers(String sender) {
		// TODO Auto-generated method stub
		AgentMessage msg = new AgentMessage("REGISTERED_USERS", sender);
		msm.post(msg);
		
	}


	@Override
	public void sendAllUsersMessage(String sender, String subject, String content) {
		// TODO Auto-generated method stub
		System.out.println("DJole");
		AgentMessage msg = new AgentMessage("MESSAGE_ALL", sender);
		msg.addParam("subject", subject);
		msg.addParam("content", content);
		msm.post(msg);
	}


	@Override
	public void sendUserMessage(String sender, String reciver, String subject, String content) {
		// TODO Auto-generated method stub
		System.out.println("Djolde222");
		AgentMessage msg = new AgentMessage("MESSAGE", sender);
		msg.addParam("reciver", reciver);
		msg.addParam("subject", subject);
		msg.addParam("content", content);
		msm.post(msg);
	}


	@Override
	public void getMessagesForUser(String sender) {
		// TODO Auto-generated method stub

		AgentMessage msg = new AgentMessage("GET_MESSAGES", sender);
		msm.post(msg);
	}




	
	
	
}
