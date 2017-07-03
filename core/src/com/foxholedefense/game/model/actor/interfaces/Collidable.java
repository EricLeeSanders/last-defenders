package com.foxholedefense.game.model.actor.interfaces;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Interfaces for classes that can collide with various objects and actors
 *
 * @author Eric
 */
public interface Collidable {
    Vector2 getPositionCenter();

    void setPositionCenter(Vector2 pos);

    Shape2D getBody();
}
