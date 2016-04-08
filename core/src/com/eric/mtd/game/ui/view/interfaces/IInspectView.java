package com.eric.mtd.game.ui.view.interfaces;

import com.eric.mtd.game.model.actor.tower.Tower;

public interface IInspectView {
	public void update(Tower selectedTower);
	public void standByState();
	public void inspectingState();
}
