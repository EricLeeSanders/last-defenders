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
import java.lang.reflect.Method;

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

        // Set up auth state listener using reflection (Android-specific)
        setupAuthStateListener();
    }

    private void setupAuthStateListener() {
        try {
            // Check if the GPS implementation has the setAuthStateListener method
            Method setListenerMethod = gps.getClass().getMethod("setAuthStateListener",
                Class.forName("com.lastdefenders.android.GooglePlayServicesHelper$AuthStateListener"));

            // Create a lambda that matches the AuthStateListener interface
            Object listener = java.lang.reflect.Proxy.newProxyInstance(
                gps.getClass().getClassLoader(),
                new Class[] { Class.forName("com.lastdefenders.android.GooglePlayServicesHelper$AuthStateListener") },
                (proxy, method, args) -> {
                    if (method.getName().equals("onAuthStateResolved")) {
                        onAuthStateResolved((Boolean) args[0]);
                    }
                    return null;
                }
            );

            // Set the listener
            setListenerMethod.invoke(gps, listener);
        } catch (Exception e) {
            // Not Android implementation or method not available, ignore
            Logger.info("Auth state listener not available (expected on non-Android platforms)");
        }
    }

    private void onAuthStateResolved(boolean isAuthenticated) {
        Logger.info("GooglePlayServicesPresenterImpl: Auth state resolved - " + isAuthenticated);

        // Enable buttons now that we know the auth state
        if(view != null) {
            view.setButtonsEnabled(true);
        }
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
