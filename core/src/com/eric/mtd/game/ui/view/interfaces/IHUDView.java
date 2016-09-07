package com.eric.mtd.game.ui.view.interfaces;

/**
 * Interface for HUD View. Used by the HUD Presenter to communicate with the
 * view
 * 
 * @author Eric
 *
 */
public interface IHUDView {
	public void standByState();
	
	public void supportState();

	public void enlistingState();
	
	public void inspectingState();

	public void optionsState();

	public void gameOverState();

	public void waveInProgressState();

	public void changeSpeed(boolean normalSpeed);

	public void setMoney(String money);

	public void setLives(String lives);

	public void setWaveCount(String waveCount);
}
