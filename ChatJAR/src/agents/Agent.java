package agents;

import java.io.Serializable;

import javax.jms.Message;

import messagemanager.AgentMessage;

public interface Agent extends Serializable {

	public String init(String id);
	public void handleMessage(AgentMessage message);
	public String getAgentId();
}
