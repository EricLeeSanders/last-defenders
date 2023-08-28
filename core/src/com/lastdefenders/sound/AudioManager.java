package com.lastdefenders.sound;

import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;

public class AudioManager {

    private SoundPlayer soundPlayer;
    private MusicPlayer musicPlayer;

    private AudioHelper audioHelper;
    private UserPreferences userPreferences;

    public AudioManager(SoundPlayer soundPlayer, MusicPlayer musicPlayer, AudioHelper audioHelper,
        UserPreferences userPreferences){
        this.soundPlayer = soundPlayer;
        this.musicPlayer = musicPlayer;
        this.audioHelper = audioHelper;
        this.userPreferences = userPreferences;

    }

    public void load(){
        this.soundPlayer.setEnabled(userPreferences.getSoundEnabled());
        this.musicPlayer.setEnabled(userPreferences.getMusicEnabled());
        this.audioHelper.setVolume(userPreferences.getMasterVolume());
        soundPlayer.load();
    }

    public SoundPlayer getSoundPlayer() {

        return soundPlayer;
    }

    public MusicPlayer getMusicPlayer() {

        return musicPlayer;
    }

    public void setVolume(float volume){
        this.audioHelper.setVolume(volume);
        musicPlayer.updateVolume(volume);
    }

    public float getVolume(){
        return this.audioHelper.getVolume();
    }

    public void saveMasterVolume() {

        Logger.info("Saving master volume");
        userPreferences.setMasterVolume(audioHelper.getVolume());
    }

    public void disposeAll(){
        soundPlayer.disposeAll();
        musicPlayer.disposeAll();
    }
}
