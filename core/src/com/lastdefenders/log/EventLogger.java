package com.lastdefenders.log;

public interface EventLogger {
    enum LogEvent {
        LEVEL_START("level_start"),
        LEVEL_COMPLETE("level_complete"),
        WAVE_COMPLETE("wave_complete"),
        GAME_OVER("game_over");


        private final String tag;

        LogEvent(String tag){
            this.tag = tag;
        }

        public String getTag(){
            return this.tag;
        }

    }

    enum LogParam {
        LEVEL_NAME("level_name"),
        COMPLETED_WAVES("completed_waves");

        private final String tag;

        LogParam(String tag){
            this.tag = tag;
        }

        public String getTag(){
            return this.tag;
        }
    }

    void logEvent(EventLogBuilder eventLogBuilder);

    void addDefaultStringParameter(String name, String value);

    void addDefaultIntegerParameter(String name, Integer value);

}
