package com.lastdefenders.levelselect;

/**
 * Created by Eric on 7/29/2017.
 */

public enum LevelName {
    THE_GREENLANDS("THE GREENLANDS"),
    THE_GOLD_COAST("THE GOLD COAST"),
    STARFISH_ISLAND("STARFISH ISLAND"),
    SERPENTINE_RIVER("SERPENTINE RIVER"),
    THE_BADLANDS("THE BADLANDS"),
    TUNDRA("TUNDRA"),
    WHISPERING_THICKET("WHISPERING THICKET");

    private String name;

    LevelName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
