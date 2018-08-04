package com.lastdefenders.googleplay;

import com.lastdefenders.levelselect.LevelName;

/**
 * Created by Eric on 6/2/2018.
 */

public enum GooglePlayAchievement {

    SERPENTINE_RIVER_COMPLETED("CgkIuZaOm9ULEAIQBw", LevelName.SERPENTINE_RIVER),
    STARFISH_ISLAND_COMPLETED("CgkIuZaOm9ULEAIQCA", LevelName.STARFISH_ISLAND),
    THE_BADLANDS_COMPLETED("CgkIuZaOm9ULEAIQCQ", LevelName.THE_BADLANDS),
    THE_GOLD_COAST_COMPLETED("CgkIuZaOm9ULEAIQCg", LevelName.THE_GOLD_COAST),
    THE_GREENLANDS_COMPLETED("CgkIuZaOm9ULEAIQCw", LevelName.THE_GREENLANDS),
    TUNDRA_COMPLETED("CgkIuZaOm9ULEAIQDA", LevelName.TUNDRA),
    WHISPERING_THICKET_COMPLETED("CgkIuZaOm9ULEAIQDQ", LevelName.WHISPERING_THICKET);

    private final String id;
    private final LevelName levelName;

    GooglePlayAchievement(String id, LevelName levelName){
        this.id = id;
        this.levelName = levelName;
    }

    public String getId(){
        return id;
    }

    public static GooglePlayAchievement findByLevelName(LevelName levelName){
        for(GooglePlayAchievement achievement : GooglePlayAchievement.values()){
            if(achievement.levelName.equals(levelName)){
                return achievement;
            }
        }

        throw new IllegalArgumentException(levelName + " does not exist.");
    }
}
