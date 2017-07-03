package com.foxholedefense;

/**
 * Created by Eric on 10/29/2016.
 */
public interface IPlayServices {

    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement();

    void submitScore(int highScore);

    void showAchievement();

    void showScore();

    boolean isSignedIn();
}
