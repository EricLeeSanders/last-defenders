package com.foxholedefense.game.ui.view.interfaces;

/**
 * Interface for HUD View. Used by the HUD Presenter to communicate with the
 * view
 *
 * @author Eric
 */
public interface IHUDView {
    void standByState();

    void supportState();

    void enlistingState();

    void inspectingState();

    void optionsState();

    void gameOverState();

    void quitState();

    void waveInProgressState();

    void setMoney(String money);

    void setLives(String lives);

    void setWaveCount(String waveCount);
}
