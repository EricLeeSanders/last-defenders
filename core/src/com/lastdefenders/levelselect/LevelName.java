package com.lastdefenders.levelselect;

/**
 * Created by Eric on 7/29/2017.
 */

public enum LevelName {
    THE_GREENLANDS("The Greenlands"),
    THE_GOLD_COAST("The Gold Coast"),
    STARFISH_ISLAND("Starfish Island"),
    SERPENTINE_RIVER("Serpentine River"),
    THE_BADLANDS("The Badlands"),
    TUNDRA("Tundra"),
    WHISPERING_THICKET("Whispering Thicket");

    private String name;

    LevelName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static LevelName getLevel(String levelName){
        for(LevelName l : LevelName.values()){
            if(l.toString().equals(levelName)){
                return l;
            }
        }

        throw new IllegalArgumentException("LevelName: " + levelName + " does not exist.");
    }

}
