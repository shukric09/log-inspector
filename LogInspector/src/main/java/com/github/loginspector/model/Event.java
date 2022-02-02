package com.github.loginspector.model;

public class Event {
	private String id;
	private State state;
	private long timestamp;
	private LogType type;
	private String host;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public LogType getType() {
		return type;
	}
	public void setType(LogType type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", state=" + state + ", timestamp=" + timestamp + ", type=" + type + ", host=" + host
				+ "]";
	}
	


}
