package com.foxholedefense.game.model.actor.interfaces;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Interface for a class that can attack actors
 * @author Eric
 *
 */
public interface IAttacker {
	public Shape2D getRangeShape();
	public Vector2 getGunPos();
	public float getAttack();
}
