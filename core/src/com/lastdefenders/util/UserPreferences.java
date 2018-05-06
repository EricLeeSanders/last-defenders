package com.lastdefenders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserPreferences {

    public static final String SHOW_TUTORIAL_TIPS = "showTutorialTips";

    private Preferences prefs = Gdx.app.getPreferences("LastDefendersPreferences");

    public Preferences getPreferences() {

        return prefs;
    }

    public boolean getShowTutorialTips(){
        return prefs.getBoolean(SHOW_TUTORIAL_TIPS, true);
    }

    public void setShowTutorialTips(boolean showTutorialTips){
        prefs.putBoolean(SHOW_TUTORIAL_TIPS, showTutorialTips);
        prefs.flush();
    }
}
