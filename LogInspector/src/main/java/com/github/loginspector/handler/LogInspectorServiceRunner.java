package com.github.loginspector.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.loginspector.configuration.ConfigurationManager;
import com.github.loginspector.model.Event;
import com.github.loginspector.model.EventAlert;

public class LogInspectorServiceRunner {
	private static final Logger LOGGER = LogManager.getLogger(LogInspectorServiceRunner.class);
	private String logFilePath ;
	private ExecutorService pool;
	private String outputFilePath;
	
	public LogInspectorServiceRunner(String logFilePath) {
		ConfigurationManager configManager = ConfigurationManager.getInstance();
		LOGGER.info("Alert threshold is set to : " + configManager.getConfig().getAlertThreshold() + "ms");
		this.logFilePath=logFilePath;
		this.outputFilePath=configManager.getConfig().getOutputDirectory();
		this.pool = Executors.newFixedThreadPool(configManager.getConfig().getThreadPoolSize());  
	}

	public void process(){
		try {
			validateFilePath();
			List<EventAlert>  alertingEvents = processFile();
			persist(alertingEvents);
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to process file at " + logFilePath );
		}
		
		
	}

    public  List<EventAlert> processFile() {
    	LOGGER.info("Starting to read log file at : "+ logFilePath);
    	 List<EventAlert> alertingEvents = new ArrayList<EventAlert>();
    	long startTime = System.currentTimeMillis();
    	long noOfRecordsProcessed = 0;
    	try(BufferedReader in = new BufferedReader(new FileReader(logFilePath))) {
    	    String line;
    	    Map<String, Event> eventMap = new ConcurrentHashMap<>();
    	   
    	    while ((line = in.readLine()) != null) {
    	    	noOfRecordsProcessed++;
    	    	 Future<EventAlert> result = pool.submit(new LogInspectorHandler(line,eventMap));
    	    	 if(result.get()!=null)
    	    		alertingEvents.add(result.get());
    	    }
    	    
    	    awaitTerminationAfterShutdown(pool);
    	    long processingTime = System.currentTimeMillis()-startTime;
    	    LOGGER.info("Completed processing log file at : "+ logFilePath + ". "+noOfRecordsProcessed + " lines read in "+ processingTime + " ms" );
    	    
    	    
    	} catch ( IOException e) {
    		LOGGER.error("Unable to process file at " + logFilePath);
		} catch (InterruptedException |ExecutionException e) {
			LOGGER.error(e.getMessage());
		} 
		return alertingEvents;
	}
    

	public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

	public void validateFilePath() throws FileNotFoundException{
        LOGGER.info("Validating logFilePath : "+ logFilePath);
        	if(logFilePath!= null && logFilePath!="") {
        		File file = new File(logFilePath);
                if (!file.exists()) {
                	 LOGGER.error("Unable to find file at " + logFilePath );
                	 throw new FileNotFoundException("Unable to find file at " + logFilePath);
                }
        	}else {
        		LOGGER.error("Unable to find file at " + logFilePath );
        		throw new FileNotFoundException("Unable to find file at " + logFilePath);
        	}
        		
            
    }
	
	/* Instead of HSQL DB data is stored in an output file */
	 public void persist(List<EventAlert> alertingEvents) {
	    	LOGGER.info("Persisting events in outputfile : " + outputFilePath);
	    	try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))){
	    		for (EventAlert entry :  alertingEvents) {
	    			String outputRecord = "Event Id=" + entry.getEventId() + ", Duration=" + entry.getDuration() +
	    					", Type=" +  entry.getType() + ", Host=" + entry.getHost() +  ", Alert=" +  entry.isAlert();
	    			writer.write(outputRecord);
	    			writer.newLine();
	    		}
	    		
	    		}
	    		catch(IOException e){
	    			LOGGER.error("Unable to write events in file : " + outputFilePath);
	    		}
	    	LOGGER.info("Completed persisting events in outputfile : " + outputFilePath);
			
		}

}
