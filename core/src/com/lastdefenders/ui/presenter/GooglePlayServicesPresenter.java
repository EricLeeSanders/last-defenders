package com.lastdefenders.ui.presenter;

/**
 * Created by Eric on 6/28/2018.
 */

public interface GooglePlayServicesPresenter {
    void showGPSView();
    void signIn();
    void signOut();
    void leaderboards();
    void achievements();
    boolean isSignedIn();
    void close();
    boolean isActive();
}
