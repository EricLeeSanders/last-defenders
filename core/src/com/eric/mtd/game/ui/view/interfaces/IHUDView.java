package com.eric.mtd.game.ui.view.interfaces;

public interface IHUDView {
	public void standByState();
	public void enlistingState();
	public void optionsState();
	public void gameOverState();
	public void waveInProgress();
	public void changeSpeed(boolean normalSpeed);
	public void setMoney(String money);
	public void setLives(String lives);
	public void setWaveCount(String waveCount);
}
