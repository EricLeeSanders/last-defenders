package com.foxholedefense.game.ui.view.interfaces;

import com.badlogic.gdx.math.Vector2;

/**
 * Interface for HUD View. Used by the HUD Presenter to communicate with the
 * view
 * 
 * @author Eric
 *
 */
public interface IHUDView extends IMessageDisplayer {
	void standByState();
	
	void supportState();

	void enlistingState();
	
	void inspectingState();

	void optionsState();

	void gameOverState();

	void waveInProgressState();

	void setMoney(String money);

	void setLives(String lives);

	void setWaveCount(String waveCount);
}
