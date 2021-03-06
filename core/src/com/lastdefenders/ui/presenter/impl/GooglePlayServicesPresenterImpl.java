package com.lastdefenders.ui.presenter.impl;

import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.ui.presenter.GooglePlayServicesPresenter;
import com.lastdefenders.ui.view.GooglePlayServicesView;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 6/28/2018.
 */

public class GooglePlayServicesPresenterImpl implements GooglePlayServicesPresenter {

    private GooglePlayServicesView view;
    private GooglePlayServices gps;
    private LDAudio audio;
    private boolean active;

    public GooglePlayServicesPresenterImpl(LDAudio audio,
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
    public void signIn() {
        audio.playSound(LDSound.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: signIn");
        gps.signIn();
        close();
    }

    @Override
    public void signOut() {
        audio.playSound(LDSound.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: signOut");
        gps.signOut();
        close();
    }

    @Override
    public void leaderboards() {
        audio.playSound(LDSound.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: leaderboards");
        gps.showLeaderboards();
    }

    @Override
    public void achievements() {
        audio.playSound(LDSound.SMALL_CLICK);
        Logger.info("GooglePlayServicesPresenterImpl: achievements");
        gps.showAchievements();
    }

    @Override
    public boolean isSignedIn() {
        return gps.isSignedIn();
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
