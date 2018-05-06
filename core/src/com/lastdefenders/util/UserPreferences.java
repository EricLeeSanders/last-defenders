package com.lastdefenders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserPreferences {

    public static final String SHOW_FIRST_GAME_TIPS = "showFirstGameTips";

    private Preferences prefs = Gdx.app.getPreferences("LastDefendersPreferences");

    public Preferences getPreferences() {

        return prefs;
    }

    public boolean getShowFirstGameTips(){
        return prefs.getBoolean(SHOW_FIRST_GAME_TIPS, true);
    }

    public void setShowFirstGameTips(boolean showFirstGameTips){
        prefs.putBoolean(SHOW_FIRST_GAME_TIPS, showFirstGameTips);
        prefs.flush();
    }
}
