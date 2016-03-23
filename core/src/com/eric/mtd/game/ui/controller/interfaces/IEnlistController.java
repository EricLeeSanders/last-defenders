package com.eric.mtd.game.ui.controller.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;

public interface IEnlistController {
	public void enlistTower(String strEnlistTower);
	public void placeTower();
	public void createTower();
	public void cancelEnlist();
	public void moveTower(Vector2 coords);
	public void rotateTower();
	public boolean isTowerRotatable();
	public boolean canAffordTower(String tower);
}
