package com.foxholedefense.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorUtil {

	public static float calcYCenterFromBotLeft(float y, float height){
		return y + (height/2);
	}

	public static float calcXCenterFromBotLeft(float x, float width){
		return x + (width/2);
	}
	
	public static float calcXBotLeftFromCenter(float x, float width){
		return x - (width / 2);
	}
	
	public static float calcYBotLeftFromCenter(float y, float height){
		return y - (height / 2);
	}

}
