package com.eric.mtd.game.model.actor.interfaces;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Interfaces for classes that can collide with various objects and actors
 * 
 * @author Eric
 *
 */
public interface ICollision {
	public Vector2 getPositionCenter();

	public void setPositionCenter(Vector2 pos);

	public Shape2D getBody();
}
