package com.github.loginspector.handler;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.loginspector.configuration.ConfigurationManager;
import com.github.loginspector.model.Event;
import com.github.loginspector.model.EventAlert;
import com.github.loginspector.model.State;

/**
 * @author richa
 *
 */
public class LogInspectorHandler implements Callable<EventAlert>{
	private static final Logger LOGGER = LogManager.getLogger(LogInspectorHandler.class);
	String line;
	Map<String, Event> eventMap ;
	
	public LogInspectorHandler(String line, Map<String, Event> eventMap) {
		this.line = line;
		this.eventMap=eventMap;
		
	}
	@Override
	public EventAlert call() {
		return processEvent();
		
	}
	
	public EventAlert processEvent() {
		long alertThreshold= ConfigurationManager.getInstance().getConfig().getAlertThreshold();
		
		EventAlert eventAlert = null;
		try {
			Event currentEvent = new ObjectMapper().readValue(line, Event.class);
			synchronized(eventMap) {
				if (eventMap.containsKey(currentEvent.getId())) {
					LOGGER.debug("Processing existing event for id " + currentEvent.getId());
					Event existingEvent = eventMap.get(currentEvent.getId());
					long duration = getEventDuration(currentEvent,existingEvent);
					Boolean isAlertNeeded = duration > alertThreshold ? Boolean.TRUE : Boolean.FALSE;
					LOGGER.info("Total time taken by event for id " +currentEvent.getId() + " is : "+ duration + "ms hence alert is set to "  + isAlertNeeded);
					eventAlert= new EventAlert(currentEvent.getId(), duration, currentEvent.getType(), currentEvent.getHost(), isAlertNeeded);
					
				}else {
					LOGGER.debug("Registering new event for id " + currentEvent.getId());
					eventMap.put(currentEvent.getId(), currentEvent);
				}
			}
			
		} catch (JsonProcessingException e) {
			LOGGER.error("Unable to parse Json : " + line);
		}
		return eventAlert;
	}
	public long getEventDuration(Event currentEvent, Event existingEvent) {
		long duration = 0;
		if(currentEvent.getState().equals(State.FINISHED) && existingEvent.getState().equals(State.STARTED))
			duration = currentEvent.getTimestamp()-existingEvent.getTimestamp();
		else if (existingEvent.getState().equals(State.FINISHED) && currentEvent.getState().equals(State.STARTED))
			duration = existingEvent.getTimestamp()-currentEvent.getTimestamp();
			
		return duration;
	}

}
