package com.lastdefenders.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.datastructures.pool.UtilPool;

public class ActorUtil {

    /**
     * Calculates the bottom left point given a point and dimension
     */
    public static float calcBotLeftPointFromCenter(float point, float dimension) {

        return point - (dimension / 2);
    }

    /**
     * Calculates a rotation from the current position and the target
     * position.
     */
    public static float calculateRotation(float targetX, float targetY, float centerX,
        float centerY) {

        LDVector2 vector = UtilPool.getVector2(targetX, targetY);
        LDVector2 centerVector = UtilPool.getVector2(centerX, centerY);
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

        LDVector2 targetCopy = UtilPool.getVector2(target);
        float rotation = targetCopy.sub(centerVector).angle();
        targetCopy.free();
        return rotation;
    }

    /**
     * Calculates rotated coords from a given center point to a target point.
     * The returned vector can be freed. Rotation needs to be in radians.
     *
     * @param rotation - in radians
     */
    public static LDVector2 calculateRotatedCoords(float targetX, float targetY, float centerX,
        float centerY, double rotation) {
        // Math stuff here -
        // http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
        float cos = (float) Math.cos(rotation);
        float sin = (float) Math.sin(rotation);
        float newX = ((((targetX - centerX) * cos) - ((targetY - centerY) * sin)) + centerX);
        float newY = ((((targetX - centerX) * sin) + ((targetY - centerY) * cos)) + centerY);
        return UtilPool.getVector2(newX, newY);
    }

    public static float getResolutionScaledStageWidthCoordinate(float x, Viewport viewport){

        return x * (viewport.getWorldWidth() / Resources.VIRTUAL_WIDTH);
    }

    public static float getResolutionScaledStageHeightCoordinate(float y, Viewport viewport){

        return y * (viewport.getWorldHeight() / Resources.VIRTUAL_HEIGHT);
    }

}
