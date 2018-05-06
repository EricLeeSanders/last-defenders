package com.lastdefenders.levelselect;

/**
 * Created by Eric on 7/29/2017.
 */

public enum LevelName {
    THE_GREENLANDS("THE GREENLANDS", "THE_GREENLANDS" ),
    THE_GOLD_COAST("THE GOLD COAST", "THE_GOLD_COAST"),
    STARFISH_ISLAND("STARFISH ISLAND", "STARFISH_ISLAND"),
    SERPENTINE_RIVER("SERPENTINE RIVER", "SERPENTINE_RIVER"),
    THE_BADLANDS("THE BADLANDS", "THE_BADLANDS"),
    TUNDRA("TUNDRA", "TUNDRA"),
    WHISPERING_THICKET("WHISPERING THICKET", "WHISPERING_THICKET"),
    TUTORIAL("TUTORIAL", "TUTORIAL");

    private String title;
    private String fileName;

    LevelName(String title, String fileName){
        this.title = title;
        this.fileName = fileName;
    }

    public String getTitle(){
        return title;
    }

    public String getFileName() { return fileName; }

}
