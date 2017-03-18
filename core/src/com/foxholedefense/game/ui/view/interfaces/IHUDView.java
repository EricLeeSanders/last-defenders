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
	public void standByState();
	
	public void supportState();

	public void enlistingState();
	
	public void inspectingState();

	public void optionsState();

	public void gameOverState();

	public void waveInProgressState();

	public void setMoney(String money);

	public void setLives(String lives);

	public void setWaveCount(String waveCount);
}
