package agentmanager;

import javax.ejb.Remote;

import agents.Agent;

@Remote
public interface AgentManagerRemote {
	public String startAgent(String id, String name);
	public Agent getAgentById(String agentId);
	public void stopAgent(String id);
}
