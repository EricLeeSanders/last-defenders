package com.lastdefenders.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;

public class SoundPlayer{

    private boolean enabled;
    private UserPreferences userPreferences;
    private AudioHelper audioHelper;

    public SoundPlayer(UserPreferences userPreferences, AudioHelper audioHelper) {

        this.userPreferences = userPreferences;
        this.audioHelper = audioHelper;
    }

    void load(){
        for(LDSound.Type sound : LDSound.Type.values()){
            sound.getLDSound().getSound().play(0);
        }
    }

    public void play(LDSound.Type type){

        LDSound ldSound = type.getLDSound();
        if(!enabled || !ldSound.isReady()){
            return;
        }
        ldSound.play(audioHelper.getVolume());
    }

    public void setEnabled(boolean enabled) {

        Logger.info("Setting sound to " + enabled);
        this.enabled = enabled;
        userPreferences.setSoundEnabled(enabled);
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void toggleEnabled(){
        setEnabled(!enabled);
    }

    public void disposeAll(){
        for(LDSound.Type sound : LDSound.Type.values()){
            sound.getLDSound().dispose();
        }
    }
}
