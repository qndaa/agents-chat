package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import util.JNDILookup;

@Singleton
@ServerEndpoint("/ws")
@LocalBean
public class WSEndPoint {
	
	static List<Session> sessions = new ArrayList<Session>();
	
	protected AgentManagerRemote agm() {
		return JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	}
	
	@OnOpen
	public void onOpen(Session session) {
		if(!sessions.contains(session)) {
			agm().startAgent(session.getId(), JNDILookup.UserAgentLookup);
			sessions.add(session);
			try {
				session.getBasicRemote().sendText("sessionId:"+session.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@OnClose
	public void close(Session session) {
		// agm().stopAgent();
		sessions.remove(session);
	}
	
	@OnMessage
	public void echoTextMessage(String msg) {
		String msgReceiver = msg.split("RECEIVER:")[1].split("CONTENT:")[0];
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
		}
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		// agm().stopAgent();
		sessions.remove(session);
		t.printStackTrace();
	}
	
}
