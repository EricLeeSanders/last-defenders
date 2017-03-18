package com.foxholedefense.game.model.actor.interfaces;

public interface ITargetable extends ICollision {
	boolean isDead();
	void setDead(boolean dead);
	void takeDamage(float damage);
}
