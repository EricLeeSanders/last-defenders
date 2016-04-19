package com.eric.mtd.game.ui.view.interfaces;

/**
 * Interface for Game Over View. Used by the Game Over Presenter to communicate
 * with the view
 * 
 * @author Eric
 *
 */
public interface IGameOverView {
	public void setWavesCompleted(String wavesCompleted);

	public void standByState();

	public void gameOverState();
}
