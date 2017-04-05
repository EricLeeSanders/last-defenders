package com.foxholedefense.util;

import com.badlogic.gdx.math.Vector2;

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


	/**
	 * Calculates a rotation from the current position and the target
	 * position.
	 */
	public static float calculateRotation(float targetX, float targetY, float centerX, float centerY) {
		com.foxholedefense.util.datastructures.pool.FHDVector2 vector = com.foxholedefense.util.datastructures.pool.UtilPool.getVector2(targetX,targetY);
		com.foxholedefense.util.datastructures.pool.FHDVector2 centerVector = com.foxholedefense.util.datastructures.pool.UtilPool.getVector2(centerX, centerY);
		float rotation = calculateRotation(vector, centerVector);
		centerVector.free();
		vector.free();
		return rotation;
	}
	/**
	 * Calculates a rotation from the current position and the target
	 * position.
	 */
	public static float calculateRotation(Vector2 target, Vector2 centerVector) {
		com.foxholedefense.util.datastructures.pool.FHDVector2 targetCopy = com.foxholedefense.util.datastructures.pool.UtilPool.getVector2(target);
		float rotation =  targetCopy.sub(centerVector).angle();
		targetCopy.free();
		return rotation;
	}

	/**
	 * Calculates rotated coords from a given center point to a target point.
	 * The returned vector can be freed. Rotation needs to be in radians.
	 * @param targetX
	 * @param targetY
	 * @param centerX
	 * @param centerY
	 * @param rotation - in radians
     */
	public static com.foxholedefense.util.datastructures.pool.FHDVector2 getRotatedCoords(float targetX, float targetY, float centerX, float centerY, double rotation ) {
		// Math stuff here -
		// http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
		float cos = (float) Math.cos(rotation);
		float sin = (float) Math.sin(rotation);
		float newX = ((((targetX - centerX) * cos) - ((targetY - centerY) * sin)) + centerX);
		float newY = ((((targetX - centerX) * sin) + ((targetY - centerY) * cos)) + centerY);
		return com.foxholedefense.util.datastructures.pool.UtilPool.getVector2(newX, newY);
	}

}
