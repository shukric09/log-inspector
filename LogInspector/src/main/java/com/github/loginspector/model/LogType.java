package com.github.loginspector.model;

public enum LogType {
    APPLICATION_LOG("APPLICATION_LOG");

    private final String value;

    LogType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
