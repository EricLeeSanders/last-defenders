package com.eric.mtd.game.ui.controller.interfaces;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public interface IHUDController {
	public void options();
	public void quit();
	public void wave();
	public void enlist();
	public void perks();
	public int getMoney();
	public int getLives();
	public int getWaveCount();
	public void setGameSpeed(float speed);


}
