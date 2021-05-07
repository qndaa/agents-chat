package agents;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagemanager.MessageManagerRemote;
import util.JNDILookup;
import ws.WSEndPoint;

@Stateful
@Remote(Agent.class)
public class ChatAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;

	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private CachedAgentsRemote cachedAgents;

	@EJB
	WSEndPoint ws;
	
	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Chat Agent!");
	}

	//private List<String> chatClients = new ArrayList<String>();

	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
	}

	@Override
	public void handleMessage(Message message) {
		TextMessage tmsg = (TextMessage) message;

		// TODO: Implement handling messages that are specified for each Node 
		// register node, add node, etc. (server-server)
	}

	@Override
	public String init(String id) {
		agentId = id;
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}

	@Override
	public String getAgentId() {
		return agentId;
	}
}
