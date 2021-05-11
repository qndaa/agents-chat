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
	

	@Override
	public void getLoggedInUsers(String sender) {
		AgentMessage msg = new AgentMessage();
		msg.userArgs.put("sender", sender);
		msg.userArgs.put("receiver", sender);
		msg.userArgs.put("option", "GET_ALL_USERS");
		msm.post(msg);
	}


	@Override
	public String registration(User user, String id) {
		System.out.println(id + " " + user);
		
		return "OK";
		
	}
	
	@Override
	public String test() {
		return "OK";
	}
	
	
}
