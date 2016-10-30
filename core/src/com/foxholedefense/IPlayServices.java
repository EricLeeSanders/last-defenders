package com.foxholedefense;

/**
 * Created by Eric on 10/29/2016.
 */
public interface IPlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement();
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
}