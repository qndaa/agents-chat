package messagemanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AgentMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4750922547689000321L;
	//public Map<String, Serializable> userArgs;
	
	
	
	private String operation;
	private HashMap<String, String> params;
	private String sender;
	
	public AgentMessage(String operation, String sender) {
		this.operation = operation;
		this.params = new HashMap<String, String>();
		this.sender = sender;
	}
	
	
	public String getOperation() {
		return operation;
	}
	
	public void addParam(String key, String value) {
		this.params.put(key, value);
	}
	
	public String getParam(String key) {
		return this.params.get(key);
	}
	
	public String getSender() {
		return this.sender;
	}
}
