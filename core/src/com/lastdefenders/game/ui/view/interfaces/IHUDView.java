package com.lastdefenders.game.ui.view.interfaces;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

    ImageButton getBtnWave();

    ImageButton getBtnEnlist();

    ImageButton getBtnSupport();

    ImageButton getBtnOptions();

    ImageButton getBtnPause();

    ImageButton getBtnResume();

    Label getLblMoney();

    Label getLblLives();

    Label getLblWaveCount();
}
