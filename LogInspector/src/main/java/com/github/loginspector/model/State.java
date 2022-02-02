package com.github.loginspector.model;


	public enum State {
	    
		STARTED("STARTED"),
	    FINISHED("FINISHED");

	    private final String value;

	    State(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }
}
