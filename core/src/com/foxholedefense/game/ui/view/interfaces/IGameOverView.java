package com.foxholedefense.game.ui.view.interfaces;

/**
 * Interface for Game Over View. Used by the Game Over Presenter to communicate
 * with the view
 *
 * @author Eric
 */
public interface IGameOverView {
    void setWavesCompleted(String wavesCompleted);

    void standByState();

    void gameOverState();
}
