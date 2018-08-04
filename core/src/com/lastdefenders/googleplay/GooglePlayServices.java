package com.lastdefenders.googleplay;

/**
 * Created by Eric on 10/29/2016.
 */
public interface GooglePlayServices {

    boolean isGooglePlayServicesAvailable();

    void signIn();

    void signOut();

    void unlockAchievement(GooglePlayAchievement achievement);

    void submitScore(GooglePlayLeaderboard leaderboard, int score);

    void showAchievements();

    void showLeaderboard(GooglePlayLeaderboard leaderboard);

    void showLeaderboards();

    boolean isSignedIn();
}
