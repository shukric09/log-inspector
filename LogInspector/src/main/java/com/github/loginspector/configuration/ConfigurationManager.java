package com.github.loginspector.configuration;

public class ConfigurationManager {
	private static ConfigurationManager _INSTANCE;
	private AppConfig config;
	
	private ConfigurationManager() {
		config = new AppConfig();
    }

	public static ConfigurationManager getInstance() {
		  if (_INSTANCE == null)
			  _INSTANCE = new ConfigurationManager();
	        return _INSTANCE;
	}
	public AppConfig getConfig() {
		return config;
	}
    

   

}
