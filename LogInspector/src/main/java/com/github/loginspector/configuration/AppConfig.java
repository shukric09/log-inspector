package com.github.loginspector.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class AppConfig {
	private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);
	
	private int alertThreshold ;
	private int threadPoolSize ;
	private String outputDirectory;
	
	public AppConfig() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream("loginspector.properties")) {
		    props.load(resourceStream);
		    alertThreshold = Integer.parseInt(props.getProperty("alert.threshold.time", "10"));
		    threadPoolSize =Integer.parseInt(props.getProperty("thread.pool.size", "4"));
		    outputDirectory =props.getProperty("output.file.path", null);
		} catch (IOException e) {
			LOGGER.error("Unable to load property file : loginspector.properties"  );
		}
	}
	public int getAlertThreshold() {
		return alertThreshold;
	}
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	public String getOutputDirectory() {
		return outputDirectory;
	}

}
