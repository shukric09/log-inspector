package com.github.loginspector.handler;

import static junit.framework.TestCase.assertEquals;

import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;



public class LogInspectorServiceRunnerTest {
    private static LogInspectorServiceRunner runner;

    @BeforeClass
    public static void instantiate() {
    	runner = new LogInspectorServiceRunner("sample.txt");
    }

    @Test(expected = FileNotFoundException.class)   
    public void _01_validateFile() throws FileNotFoundException {
    	runner = new LogInspectorServiceRunner("");
        runner.validateFilePath();
    }
    
    @Test(expected = FileNotFoundException.class)   
    public void _02_validateFile() throws FileNotFoundException {
    	runner = new LogInspectorServiceRunner(null);
        runner.validateFilePath();
    }
    
    @Test
    public void _03_validateFile() throws FileNotFoundException {
    	runner = new LogInspectorServiceRunner("C:\\Temp\\sample.txt");
        runner.validateFilePath();
    }
    @Test
    public void _04_processFile() throws FileNotFoundException {
    	runner = new LogInspectorServiceRunner("C:\\Temp\\sample.txt");
        assertEquals(23, runner.processFile().size());
    }
    
    

   
}
