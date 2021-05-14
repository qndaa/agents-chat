package ws;


import java.io.IOException;
import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerBean;
import chatmanager.ChatManagerRemote;
import models.User;
import util.JNDILookup;

@Singleton
@ServerEndpoint("/ws/{username}")
@LocalBean
public class WSEndPoint {
	
	static HashMap<Session, String> sessions = new HashMap<Session, String>();
	
	protected AgentManagerRemote agm() {
		return JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	}
	
	protected ChatManagerRemote chm() {
		return (ChatManagerRemote) JNDILookup.lookUp(JNDILookup.ChatManagerLookup, ChatManagerBean.class);
	}
	
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) {

		System.out.println("Sessia id: " + session.getId());
		System.out.println("Username: " + username);
		System.out.println("Number opened sesion: " + (sessions.keySet().size() + 1));
		
		if(!sessions.keySet().contains(session)) {
					
			agm().startAgent(session.getId(),  JNDILookup.UserAgentLookup);
			if (username.equals("")) {
				sessions.put(session, null);
			} else {
				sessions.put(session, username);
			}
			//sessions.put(session, (username == "") ? null : username);
			try {
				session.getBasicRemote().sendText("sessionId:" + session.getId());
				if (sessions.get(session) != null) {
					session.getBasicRemote().sendText("REDIRECT");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	@OnClose
	public void close(Session session) {
		agm().stopAgent(session.getId());
		System.out.println("Session with id: " + session.getId() + "closed!");
		sessions.remove(session);
		
	}
	
	@OnMessage
	public void echoTextMessage(String msg) {
		
		String tokens [] = msg.split(" ");
		String agentId = tokens[1].split(":")[1];
				
		if (tokens[0].equals("REGISTRATION")) {
			Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);

			if(session != null && session.isOpen()) {
				try {
					session.getBasicRemote().sendText("REGISTRATION " + tokens[2]);
				} catch (IOException e) {
					try {
						session.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		} else if (tokens[0].equals("LOGIN")) {
			Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);

			if(session != null && session.isOpen()) {
				try {
					session.getBasicRemote().sendText("LOGIN " + tokens[2] + " " + tokens[3]);
				} catch (IOException e) {
					try {
						session.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			
		} else if (tokens[0].equals("LOGOUT")) {
			Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);

			if(session != null && session.isOpen()) {
				try {
					session.getBasicRemote().sendText("LOGOUT " + tokens[2]);
				} catch (IOException e) {
					try {
						session.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			
		} else if (tokens[0].equals("REGISTERED_USERS") || tokens[0].equals("LOGGED_USERS")) {
			
			Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);

			if(session != null && session.isOpen()) {
				try {
					session.getBasicRemote().sendText(msg);
				} catch (IOException e) {
					try {
						session.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		
		/*
		Session session = sessions
				.stream()
				.filter(s->s.getId().equals(msgReceiver))
				.findFirst()
				.orElse(null);
		try {
			if(session.isOpen()) {
				
				session.getBasicRemote().sendText(msgContent);
					
			}
		} catch(IOException e) {
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} */
	} 
	
	@OnError
	public void error(Session session, Throwable t) {
		agm().stopAgent(session.getId());
		sessions.remove(session);
		t.printStackTrace();
	}
	

	public void send(String agentId, String msg) {
		Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);
		if(session != null && session.isOpen()) {
			try {
				System.out.println("Send!");
				session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				try {
					session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
	}
	
	public String getUserFromSession(String sessionId) {
		Session session = sessions.keySet().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
		if (session != null)
			return sessions.get(session);
		else
			return null;
	}

	public void putUserOnSession(User user, String sessionId) {
		Session session = sessions.keySet().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
		if (session != null)
			sessions.put(session, user.getUsername());
		
	}

	public void removeUserFromSession(String sessionId) {
		Session session = sessions.keySet().stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
		if (session != null)
			sessions.put(session, null);
		
		
	}

	public void sendAllActiveUsers(String registeredUsersString) {
		for(Session session : sessions.keySet())
			if(sessions.get(session) != null && session.isOpen()) {
				try {
					session.getBasicRemote().sendText(registeredUsersString);
				} catch (IOException e) {
					try {
						session.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		
	}

	public void sendMessages(String agentId, String allMessageString) {
		Session session = sessions.keySet().stream().filter(s -> s.getId().equals(agentId)).findFirst().orElse(null);
		if(session != null && session.isOpen()) {
			try {
				System.out.println("Send!");
				session.getBasicRemote().sendText(allMessageString);
			} catch (IOException e) {
				try {
					session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
	}
	
	public String getSessionFromUsername(String username) {
		for(Session session: sessions.keySet()) {
			if (sessions.get(session).equals(username)) {
				return session.getId();
			}
		}
		return null;
	}
	
}
