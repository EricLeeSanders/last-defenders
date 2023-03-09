package com.lastdefenders.googleplay;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Eric on 10/29/2016.
 */
public interface GooglePlayServices {

    boolean isDeviceCompatible();

    CompletableFuture<Boolean> signIn();

    void unlockAchievement(GooglePlayAchievement achievement);

    void submitScore(GooglePlayLeaderboard leaderboard, int score);

    void showAchievements();

    void showLeaderboard(GooglePlayLeaderboard leaderboard);

    void showLeaderboards();

    boolean isSignedIn();
}
