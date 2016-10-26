package com.foxholedefense.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorUtil {
	public static Vector2 calcCenterFromBotLeft(Actor actor){
		return new Vector2(actor.getX() + (actor.getWidth()/2), actor.getY() + (actor.getHeight()/2));
	}
	public static float calcYCenterFromBotLeft(float y, float height){
		return y + (height/2);
	}
	public static float calcXCenterFromBotLeft(float x, float width){
		return x + (width/2);
	}
	public static Vector2 calcBotLeftFromCenter(float x, float y, Actor actor){
		return new Vector2(x - (actor.getWidth() / 2), y - (actor.getHeight() / 2));
	}
	
	public static Vector2 calcBotLeftFromCenter(Vector2 center, Actor actor){
		return new Vector2(calcBotLeftFromCenter(center.x, center.y, actor));
	}
	
	public static float calcXBotLeftFromCenter(float x, float width){
		return x - (width / 2);
	}
	
	public static float calcYBotLeftFromCenter(float y, float height){
		return y - (height / 2);
	}

}
