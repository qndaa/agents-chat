package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.User;

public interface ChatRest {
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test();
	
	@POST
	@Path("/users/registration/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void registration(User user, @PathParam("id") String id);
	
	@POST
	@Path("/users/login/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(User user, @PathParam("id") String id);
	
	
	@GET 
	@Path("/users/loggedIn/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getLoggedInUsers(@PathParam("id") String id);
	
	@GET
	@Path("/users/registered/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getRegistredUsers(@PathParam("id") String id);
	
	
	@POST
	@Path("/messages/all/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendAllUsersMessage(@PathParam("id") String id);
	
	
	@POST
	@Path("/messages/user/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void sendUserMessage(@PathParam("id") String id);
	
	@GET
	@Path("/messages/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getMessagesForUser(@PathParam("id") String id);
	
	
	@DELETE
	@Path("/users/loggedIn/{id}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@PathParam("id") String id, @PathParam("username") String username);
	

}
