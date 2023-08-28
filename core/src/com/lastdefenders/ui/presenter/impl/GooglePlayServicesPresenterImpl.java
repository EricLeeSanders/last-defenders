package com.lastdefenders.ui.presenter.impl;

import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.LDSound.Type;
import com.lastdefenders.sound.MusicPlayer;
import com.lastdefenders.ui.presenter.GooglePlayServicesPresenter;
import com.lastdefenders.ui.view.GooglePlayServicesView;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 6/28/2018.
 */

public class GooglePlayServicesPresenterImpl implements GooglePlayServicesPresenter {

    private GooglePlayServicesView view;
    private GooglePlayServices gps;
    private AudioManager audio;
    private boolean active;

    public GooglePlayServicesPresenterImpl(AudioManager audio,
        GooglePlayServices gps){
        this.gps = gps;
        this.audio = audio;
    }

    public void setView(GooglePlayServicesView view){
        this.view = view;
    }

    @Override
    public void showGPSView(){
        view.setVisible(true);
        active = true;
    }

    @Override
    public void leaderboards() {
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: leaderboards");
        gps.showLeaderboards();
    }

    @Override
    public void achievements() {
        audio.getSoundPlayer().play(LDSound.Type.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: achievements");
        gps.showAchievements();
    }

    @Override
    public void close() {
        view.setVisible(false);
        active = false;
    }

    @Override
    public boolean isActive(){
        return active;
    }
}
