package com.lastdefenders.log;

import com.lastdefenders.log.EventLogger.LogEvent;
import java.util.HashMap;
import java.util.Map;

public class EventLogBuilder {

    private Map<String, String> stringParams = new HashMap<>();
    private Map<String, Integer> integerParams = new HashMap<>();
    private LogEvent event;

    public EventLogBuilder(LogEvent event){
        this.event = event;
    }

    public EventLogBuilder withDefaultStringParameters(Map<String, String> stringParams){
        this.stringParams.putAll(stringParams);
        return this;
    }

    public EventLogBuilder withDefaultIntegerParameters(Map<String, Integer> integerParams){
        this.integerParams.putAll(integerParams);
        return this;
    }

    public EventLogBuilder withStringParameter(String name, String value){
        this.stringParams.put(name, value);
        return this;
    }

    public EventLogBuilder withIntegerParameter(String name, Integer value){
        this.integerParams.put(name, value);
        return this;
    }

    public LogEvent getEvent(){
        return event;
    }

    public Map<String, String> getStringParams() {

        return stringParams;
    }

    public Map<String, Integer> getIntegerParams() {

        return integerParams;
    }
}
