package com.foxholedefense.game.model.actor.interfaces;

public interface Targetable extends Collidable {

    boolean isDead();

    void setDead(boolean dead);

    boolean isActive();

    void takeDamage(float damage);
}
