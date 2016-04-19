package com.eric.mtd.game.ui.view.interfaces;

import com.eric.mtd.game.model.actor.tower.Tower;

/**
 * Interface for Inspect View. Used by the Inspect Presenter to communicate with
 * the view
 * 
 * @author Eric
 *
 */
public interface IInspectView {
	public void update(Tower selectedTower);

	public void standByState();

	public void inspectingState();
}
