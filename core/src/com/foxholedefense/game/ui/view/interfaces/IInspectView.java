package com.foxholedefense.game.ui.view.interfaces;

import com.foxholedefense.game.model.actor.combat.tower.Tower;

/**
 * Interface for Inspect View. Used by the Inspect Presenter to communicate with
 * the view
 * 
 * @author Eric
 *
 */
public interface IInspectView {
	void update(Tower selectedTower);

	void standByState();

	void inspectingState();

}
