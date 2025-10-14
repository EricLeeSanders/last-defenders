package com.lastdefenders.ui.presenter.impl;

import com.badlogic.gdx.Gdx;
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
        // If already signed in, show view immediately
        if(gps.isSignedIn()) {
            Logger.info("GooglePlayServicesPresenterImpl: Already signed in, showing view");
            view.setVisible(true);
            active = true;
            return;
        }

        // Not signed in, ask user if they want to sign in
        Logger.info("GooglePlayServicesPresenterImpl: Not signed in, requesting confirmation");
        gps.requestSignInWithConfirmation(new GooglePlayServices.SignInConfirmationCallback() {
            @Override
            public void onUserConfirmed() {
                Logger.info("GooglePlayServicesPresenterImpl: User confirmed sign-in, showing view");
                view.setVisible(true);
                active = true;
            }

            @Override
            public void onUserCancelled() {
                Logger.info("GooglePlayServicesPresenterImpl: User cancelled sign-in");
            }
        });
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
