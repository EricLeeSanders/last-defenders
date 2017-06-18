package com.foxholedefense.game.model.actor.interfaces;

public interface Targetable extends Collidable {
	boolean isDead();
	boolean isActive();
	void setDead(boolean dead);
	void takeDamage(float damage);
}
