package com.github.loginspector;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.loginspector.handler.LogInspectorHandler;
import com.github.loginspector.handler.LogInspectorHandlerTest;
import com.github.loginspector.handler.LogInspectorServiceRunnerTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   LogInspectorServiceRunnerTest.class,
   LogInspectorHandlerTest.class
 
})

public class UnitTestSuite {

}
