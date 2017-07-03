package com.foxholedefense.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserPreferences {
    private Preferences prefs = Gdx.app.getPreferences("FoxholeDefensePreferences");

    public Preferences getPreferences() {
        return prefs;
    }
}
