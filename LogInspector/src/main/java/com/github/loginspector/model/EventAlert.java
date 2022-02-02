package com.github.loginspector.model;

public class EventAlert {
	private String eventId;
	private long duration;
	private LogType type;
	private String host;
	private boolean alert;
	public EventAlert(String eventId, long duration, LogType type, String host, boolean alert) {
		super();
		this.eventId = eventId;
		this.duration = duration;
		this.type = type;
		this.host = host;
		this.alert = alert;
	}
	public String getEventId() {
		return eventId;
	}
	public long getDuration() {
		return duration;
	}
	public LogType getType() {
		return type;
	}
	public String getHost() {
		return host;
	}
	public boolean isAlert() {
		return alert;
	}
	
}
