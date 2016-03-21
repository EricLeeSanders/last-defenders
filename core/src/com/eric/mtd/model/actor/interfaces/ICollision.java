package com.eric.mtd.model.actor.interfaces;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public interface ICollision {
	public Vector2 getPositionCenter();
	public void setPositionCenter(Vector2 pos);
	public Shape2D getBody();
}
