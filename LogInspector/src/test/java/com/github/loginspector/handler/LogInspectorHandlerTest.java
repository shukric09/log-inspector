package com.github.loginspector.handler;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.loginspector.model.Event;

public class LogInspectorHandlerTest {
	private static LogInspectorHandler handler;

	@Test
	public void _01_processEvent() throws FileNotFoundException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		handler = new LogInspectorHandler(
				"{\"id\":\"56CEEd390aEC85b0\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994159}",
				eventMap);
		assertEquals(null, handler.processEvent());
	}

	@Test
	public void _02_processEvent() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994120}",
				Event.class);
		eventMap.put("test123", event);

		handler = new LogInspectorHandler(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994100}",
				eventMap);
		assertEquals(20, handler.processEvent().getDuration());
		assertTrue(handler.processEvent().isAlert());
	}

	@Test
	public void _03_processEvent() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994120}",
				Event.class);
		eventMap.put("test123", event);

		handler = new LogInspectorHandler(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994119}",
				eventMap);
		assertEquals(1, handler.processEvent().getDuration());
		assertFalse(handler.processEvent().isAlert());
	}

	@Test
	public void _04_processEvent() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);
		eventMap.put("test123", event);

		handler = new LogInspectorHandler(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994119}",
				eventMap);
		assertEquals(4, handler.processEvent().getDuration());
		assertFalse(handler.processEvent().isAlert());
	}

	@Test
	public void _05_getEventDuration() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event1 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);
		Event event2 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);

		handler = new LogInspectorHandler("", eventMap);
		assertEquals(0, handler.getEventDuration(event1, event2));
		
	}
	
	@Test
	public void _06_getEventDuration() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event1 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);
		Event event2 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);

		handler = new LogInspectorHandler("", eventMap);
		assertEquals(0, handler.getEventDuration(event1, event2));
		
	}
	
	@Test
	public void _07_getEventDuration() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
		Map<String, Event> eventMap = new ConcurrentHashMap<>();
		Event event1 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994123}",
				Event.class);
		Event event2 = new ObjectMapper().readValue(
				"{\"id\":\"test123\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":null,\"timestamp\":1607823994130}",
				Event.class);

		handler = new LogInspectorHandler("", eventMap);
		assertEquals(7, handler.getEventDuration(event1, event2));
		
	}

}
