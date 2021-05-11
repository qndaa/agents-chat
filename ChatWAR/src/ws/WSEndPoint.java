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
import javax.websocket.server.ServerEndpoint;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerBean;
import chatmanager.ChatManagerRemote;
import util.JNDILookup;

@Singleton
@ServerEndpoint("/ws/{sessionId}")
@LocalBean
public class WSEndPoint {
	
	static HashMap<Session, String> sessions = new HashMap<Session, String>();
	
	protected AgentManagerRemote agm() {
		return JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	}
	
	protected ChatManagerRemote chm() {
		return JNDILookup.lookUp(JNDILookup.ChatAgentLookup, ChatManagerBean.class);
	}
	
	@OnOpen
	public void onOpen(Session session) {

		System.out.println("Sessia id: " + session.getId());
		System.out.println("Number opened sesion: " + sessions.keySet().size() + 1);
		
		if(!sessions.keySet().contains(session)) {
			
			//sessions.get(session);
			
			
			//agm().startAgent(session.getId(),  JNDILookup.UserAgentLookup);
			//sessions.put(session, )
			try {
				session.getBasicRemote().sendText("sessionId:" + session.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	@OnClose
	public void close(Session session) {
		// agm().stopAgent();
		System.out.println("Session with id: " + session.getId() + "closed!");
		sessions.remove(session);
	}
	
	@OnMessage
	public void echoTextMessage(String msg) {
		/*String msgReceiver = msg.split("RECEIVER:")[1].split("CONTENT:")[0];
		String msgContent = msg.split("RECEIVER:")[1].split("CONTENT:")[1];
		
		
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
		// agm().stopAgent();
		//sessions.remove(session);
		t.printStackTrace();
	}
	
}
