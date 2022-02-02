package com.github.loginspector;


 
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.loginspector.handler.LogInspectorServiceRunner;

public class LogInspectorMain {
	private static final Logger LOGGER = LogManager.getLogger(LogInspectorMain.class);
	public static void main(String[] args) {
		LOGGER.info("Starting LogInspector..");
		
		if (args.length >0) {
			startRunner(args[0]);
         }else {
        	 LOGGER.fatal("Unable to start LogInspector. Please start main class with input file location e.g. LogInspectorMain C:\\Temp\\sample.log");
             throw new IllegalArgumentException("Please start main class with input file location e.g. C:\\Temp\\sample.log");
         }
		
	}
	private static void startRunner(String logFilePath) {
		LogInspectorServiceRunner runner = new LogInspectorServiceRunner(logFilePath);
		runner.process();
		
	}
}
