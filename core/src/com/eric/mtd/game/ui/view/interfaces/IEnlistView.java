package com.eric.mtd.game.ui.view.interfaces;

/**
 * Interface for Enlist View. Used by the Enlist Presenter to communicate with
 * the view
 * 
 * @author Eric
 *
 */
public interface IEnlistView {
	public void enlistingState();

	public void placingTowerState();

	public void standByState();

	public void towerShowing(boolean rotatable);
}
