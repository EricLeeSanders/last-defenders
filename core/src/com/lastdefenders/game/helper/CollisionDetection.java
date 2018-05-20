package com.lastdefenders.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.interfaces.Collidable;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

/**
 * A static class to handle all collisions that can occur during the game
 *
 * @author Eric
 */
public final class CollisionDetection {

    private static Rectangle clickRect = new Rectangle(0, 0, 0, 0);
    private static Polygon rectPoly = new Polygon();
    private static float rectanglePoints[] = new float[8];

    /**
     * Checks for collision with the path boundaries when placing a tower
     *
     * @param boundaries - Array of rectangles from TiledMap that represent the path boundary
     * @param placeActor - The actor that is being places
     * @return boolean - If there is a collision
     */
    public static boolean collisionWithPath(Array<Rectangle> boundaries, Collidable placeActor) {

        Shape2D body = placeActor.getBody();
        for (Rectangle boundary : boundaries) {
            if (body instanceof Polygon) {
                if (polygonAndRectangle((Polygon) body, boundary)) {
                    return true;
                }
            } else if (body instanceof Circle) {
                if (circleAndRectangle((Circle) body, boundary)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for collisions with actors when placing a tower
     *
     * @param Actors - A collection of Actors that implement Collidable
     * @param placeActor - The actor that is being placed
     * @return boolean - If there is a collision
     */
    public static boolean collisionWithActors(SnapshotArray<Actor> Actors, Collidable placeActor) {

        Shape2D placeBody = placeActor.getBody();
        for (Actor actor : Actors) {
            if (!actor.equals(placeActor) && actor instanceof Tower) {
                Shape2D towerBody = ((CombatActor) actor).getBody();
                if (shapesIntersect(placeBody, towerBody)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean shapesIntersect(Shape2D shape1, Shape2D shape2) {

        if (shape1 instanceof Circle) {
            if (shape2 instanceof Circle) {
                if (circleAndCircle((Circle) shape2, (Circle) shape1)) {
                    return true;
                }
            } else if (shape2 instanceof Polygon) {
                if (polygonAndCircle((Polygon) shape2, (Circle) shape1)) {
                    return true;
                }
            }
        } else if (shape1 instanceof Polygon) {
            if (shape2 instanceof Circle) {
                if (polygonAndCircle((Polygon) shape1, (Circle) shape2)) {
                    return true;
                }
            } else if (shape2 instanceof Polygon) {
                if (polygonAndPolygon((Polygon) shape1, (Polygon) shape2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Tower towerHit(SnapshotArray<Actor> Towers, Vector2 clickCoord) {

        clickRect.setPosition(clickCoord.x, clickCoord.y);
        clickRect.setSize(1, 1);
        for (Actor tower : Towers) {
            if (tower instanceof Tower) {
                Shape2D towerBody = ((CombatActor) tower).getBody();
                if (towerBody instanceof Polygon && polygonAndRectangle((Polygon) towerBody,
                    clickRect)) {
                    return (Tower) tower;
                } else if (towerBody instanceof Circle && circleAndRectangle((Circle) towerBody,
                    clickRect)) {
                    return (Tower) tower;
                }
            }
        }
        return null;
    }

    /**
     * Checks for collision with a polygon and circle. Libgdx does not have a
     * native way to do this. This method creates a line between 2 vertices for
     * each vertex. Then checks to see if that line intersects the circle.
     *
     * @return boolean - If there is an intersection
     */
    private static boolean polygonAndCircle(Polygon polygon, Circle circle) {

        float[] vertices = polygon.getTransformedVertices();
        LDVector2 center = UtilPool.getVector2(circle.x, circle.y);
        LDVector2 vectorA = UtilPool.getVector2();
        LDVector2 vectorB = UtilPool.getVector2();
        float squareRadius = circle.radius * circle.radius;
        vectorA.set(vertices[vertices.length - 2], vertices[vertices.length - 1]);
        vectorB.set(vertices[0], vertices[1]);
        if (Intersector.intersectSegmentCircle(vectorA, vectorB, center, squareRadius)) {
            return true;
        }
        for (int i = 2; i < vertices.length; i += 2) {
            vectorA.set(vertices[i - 2], vertices[i - 1]);
            vectorB.set(vertices[i], vertices[i + 1]);
            if (Intersector.intersectSegmentCircle(vectorA, vectorB, center, squareRadius)) {
                return true;
            }
        }

        UtilPool.freeObjects(center, vectorA, vectorB);

        return (polygon.contains(circle.x, circle.y));
    }

    private static boolean polygonAndRectangle(Polygon polygon, Rectangle rectangle) {

        rectanglePoints[0] = 0;
        rectanglePoints[1] = 0;
        rectanglePoints[2] = rectangle.width;
        rectanglePoints[3] = 0;
        rectanglePoints[4] = rectangle.width;
        rectanglePoints[5] = rectangle.height;
        rectanglePoints[6] = 0;
        rectanglePoints[7] = rectangle.height;
        rectPoly.setVertices(rectanglePoints);
        rectPoly.setPosition(rectangle.x, rectangle.y);
        return Intersector.overlapConvexPolygons(rectPoly, polygon);

    }

    /**
     * Checks for a collision between 2 polygons
     *
     * @return boolean - If there is a collision
     */
    private static boolean polygonAndPolygon(Polygon poly1, Polygon poly2) {

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    private static boolean circleAndRectangle(Circle circle, Rectangle rect) {

        return Intersector.overlaps(circle, rect);
    }

    private static boolean circleAndCircle(Circle circle1, Circle circle2) {

        return Intersector.overlaps(circle1, circle2);
    }
}
