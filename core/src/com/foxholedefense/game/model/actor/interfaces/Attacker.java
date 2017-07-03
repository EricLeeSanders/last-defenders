package com.foxholedefense.game.model.actor.interfaces;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Interface for a class that can attack actors
 *
 * @author Eric
 */
public interface Attacker {

    Vector2 getPositionCenter();

    Shape2D getRangeShape();

    Vector2 getGunPos();

    float getAttack();

    Group getTargetGroup();
}
