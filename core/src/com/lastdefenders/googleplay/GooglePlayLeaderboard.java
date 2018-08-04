package com.lastdefenders.googleplay;

import com.lastdefenders.levelselect.LevelName;

/**
 * Created by Eric on 6/2/2018.
 */

public enum GooglePlayLeaderboard {

    SERPENTINE_RIVER_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQAA", LevelName.SERPENTINE_RIVER),
    STARFISH_ISLAND_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQAQ", LevelName.STARFISH_ISLAND),
    THE_BADLANDS_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQAg", LevelName.THE_BADLANDS),
    THE_GOLD_COAST_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQAw", LevelName.THE_GOLD_COAST),
    THE_GREENLANDS_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQBA", LevelName.THE_GREENLANDS),
    TUNDRA_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQBQ", LevelName.TUNDRA),
    WHISPERING_THICKET_MOST_WAVES_SURVIVED("CgkIuZaOm9ULEAIQBg", LevelName.WHISPERING_THICKET);

    private final String id;
    private final LevelName levelName;

    GooglePlayLeaderboard(String id, LevelName levelName){
        this.id = id;
        this.levelName = levelName;
    }

    public String getId(){
        return id;
    }

    public static GooglePlayLeaderboard findByLevelName(LevelName levelName){
        for(GooglePlayLeaderboard leaderboard : GooglePlayLeaderboard.values()){
            if(leaderboard.levelName.equals(levelName)){
                return leaderboard;
            }
        }

        throw new IllegalArgumentException(levelName + " does not exist.");
    }
}
