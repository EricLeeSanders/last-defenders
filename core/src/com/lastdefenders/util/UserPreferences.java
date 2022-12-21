package com.lastdefenders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserPreferences {

    private static final String SHOW_TUTORIAL_TIPS = "showTutorialTips";
    private static final String SHOW_TOWER_RANGES = "showTowerRanges";
    private static final String MASTER_VOLUME = "masterVolume";
    private static final String SOUND_ENABLED = "soundEnabled";
    private static final String MUSIC_ENABLED = "musicEnabled";
    public static final String AD_REMOVAL_PURCHASED = "adRemovalPurchased";

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

    public boolean getShowTowerRanges(){
        return prefs.getBoolean(SHOW_TOWER_RANGES, false);
    }

    public void setShowTowerRanges(boolean showTowerRanges){
        prefs.putBoolean(SHOW_TOWER_RANGES, showTowerRanges);
        prefs.flush();
    }

    public float getMasterVolume(){
        return prefs.getFloat(MASTER_VOLUME, 1);
    }

    public void setMasterVolume(float volume){
        prefs.putFloat(MASTER_VOLUME, volume);
        prefs.flush();
    }

    public boolean getSoundEnabled(){
        return prefs.getBoolean(SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean soundEnabled){
        prefs.putBoolean(SOUND_ENABLED, soundEnabled);
        prefs.flush();
    }

    public boolean getMusicEnabled(){
        return prefs.getBoolean(MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled){
        prefs.putBoolean(MUSIC_ENABLED, musicEnabled);
        prefs.flush();
    }

    public void setAdRemovalPurchased(boolean adRemovalPurchased){
        prefs.putBoolean(AD_REMOVAL_PURCHASED, adRemovalPurchased);
        prefs.flush();
    }

    public boolean getAdRemovalPurchased(){
        return prefs.getBoolean(AD_REMOVAL_PURCHASED);
    }

}
