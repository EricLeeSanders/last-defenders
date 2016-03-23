package com.eric.mtd.game.ui.controller.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface IPerksController {
	public void createSandbag();
	public void placeSandbag();
	public void moveSandbag(Vector2 coords);
	public void rotateSandbag();
	public void cancelPerks();
}
