package com.eric.mtd.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorUtil {
	public static Vector2 calcCenterFromBotLeft(Actor actor){
		return new Vector2(actor.getX() + (actor.getWidth()/2), actor.getY() + (actor.getHeight()/2));
	}
	public static Vector2 calcBotLeftFromCenter(Vector2 center, Actor actor){
		return new Vector2(center.x - (actor.getWidth() / 2), center.y - (actor.getHeight() / 2));
	}

}
