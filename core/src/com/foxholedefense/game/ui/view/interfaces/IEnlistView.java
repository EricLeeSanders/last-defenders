package com.foxholedefense.game.ui.view.interfaces;

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

	public void showBtnPlace();
	
	public void showBtnRotate();
}
