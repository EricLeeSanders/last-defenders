package com.lastdefenders.android;

import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lastdefenders.log.EventLogBuilder;
import com.lastdefenders.log.EventLogger;
import java.util.HashMap;
import java.util.Map;

public class FirebaseEventLogger implements EventLogger {

    private FirebaseAnalytics firebaseAnalytics;
    private Map<String, String> defaultStringParams = new HashMap<>();
    private Map<String, Integer> defaultIntegerParams = new HashMap<>();

    public FirebaseEventLogger(FirebaseAnalytics firebaseAnalytics){
        this.firebaseAnalytics = firebaseAnalytics;

        setDefaultStringParams();
    }

    private void setDefaultStringParams(){
        defaultStringParams.put("version_name", BuildConfig.VERSION_NAME);
        defaultStringParams.put("build_type", BuildConfig.BUILD_TYPE);
    }

    @Override
    public void logEvent(EventLogBuilder eventLogBuilder) {
        Map<String, String> stringParams = eventLogBuilder.getStringParams();
        Map<String, Integer> integerParams = eventLogBuilder.getIntegerParams();

        stringParams.putAll(defaultStringParams);
        integerParams.putAll(defaultIntegerParams);

        Bundle bundle = new Bundle();

        for(Map.Entry<String, String> param : stringParams.entrySet()){
            bundle.putString(param.getKey(), param.getValue());
        }

        for(Map.Entry<String, Integer> param : integerParams.entrySet()){
            bundle.putInt(param.getKey(), param.getValue());
        }

       firebaseAnalytics.logEvent(eventLogBuilder.getEvent().getTag(), bundle);


    }

    @Override
    public void addDefaultStringParameter(String name, String value) {
        defaultStringParams.put(name, value);
    }

    @Override
    public void addDefaultIntegerParameter(String name, Integer value) {
        defaultIntegerParams.put(name, value);
    }


}
