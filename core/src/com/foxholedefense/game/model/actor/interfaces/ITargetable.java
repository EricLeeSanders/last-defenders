package com.foxholedefense.game.model.actor.interfaces;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public interface ITargetable {
	public Vector2 getPositionCenter();
	public void setPositionCenter(Vector2 pos);
	public Polygon getBody();
	public boolean isDead();
	public void setDead(boolean dead);
	public void takeDamage(float damage);
}
