package com.lastdefenders.googleplay;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Eric on 10/29/2016.
 */
public interface GooglePlayServices {

    interface SignInConfirmationCallback {
        void onUserConfirmed();
        void onUserCancelled();
    }

    boolean isDeviceCompatible();

    CompletableFuture<Boolean> signIn();

    CompletableFuture<Boolean> signInAsync();

    void requestSignInWithConfirmation(SignInConfirmationCallback callback);

    void unlockAchievement(GooglePlayAchievement achievement);

    void submitScore(GooglePlayLeaderboard leaderboard, int score);

    void showAchievements();

    void showLeaderboard(GooglePlayLeaderboard leaderboard);

    void showLeaderboards();

    boolean isSignedIn();
}
