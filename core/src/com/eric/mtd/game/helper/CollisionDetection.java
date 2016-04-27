package com.eric.mtd.game.helper;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.interfaces.ICollision;
import com.eric.mtd.game.model.actor.support.Sandbag;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.util.Logger;
import com.badlogic.gdx.math.Shape2D;

/**
 * A static class to handle all collisions that can occur during the game
 * 
 * @author Eric
 *
 */
public final class CollisionDetection {
	/**
	 * Checks for collision with the path boundaries when placing a tower or
	 * sand bag
	 * 
	 * @param pathBoundary
	 *            - Array of rectangles from TiledMap that represent the path
	 *            boundary
	 * @param placeActor
	 *            - The actor that is being places
	 * @return boolean - If there is a collision
	 */
	public static boolean CollisionWithPath(Array<Rectangle> pathBoundary, ICollision placeActor) {
		Shape2D body = placeActor.getBody();
		for (Rectangle boundry : pathBoundary) {
			if (body instanceof Polygon) {
				if (polygonAndRectangle((Polygon) body, boundry)) {
					if (Logger.DEBUG)
						System.out.println("intersect with path!");
					return true;
				}
			} else if (body instanceof Rectangle) {
				if (rectangleAndRectangle((Rectangle) body, boundry)) {
					if (Logger.DEBUG)
						System.out.println("intersect with path!");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks for collisions with actors when placing a tower or sand bag
	 * 
	 * @param Actors
	 *            - A collection of Actors that implement ICollision (towers and
	 *            sand bags)
	 * @param placeActor
	 *            - The actor that is being placed
	 * @return boolean - If there is a collision
	 */
	public static boolean CollisionWithActors(SnapshotArray<Actor> Actors, ICollision placeActor) {
		Polygon towerBody;
		Rectangle sandbagBody;
		Shape2D placeBody = placeActor.getBody();
		for (Actor actor : Actors) {
			if (actor instanceof Tower) { // Checks for collision with towers
				towerBody = ((GameActor) actor).getBody();
				if (!actor.equals(placeActor)) {
					if (placeBody instanceof Polygon) {
						if (polygonAndPolygon(towerBody, (Polygon) placeBody)) {
							if (Logger.DEBUG)
								System.out.println("intersect with tower!");
							return true;
						}
					} else if (placeBody instanceof Rectangle) {
						if (polygonAndRectangle(towerBody, (Rectangle) placeBody)) {
							if (Logger.DEBUG)
								System.out.println("intersect with Sandbag!");
							return true;
						}
					}
				}
			}
			if (actor instanceof Sandbag) { // Checks for collision with sand
											// bags
				if (!actor.equals(placeActor)) {
					sandbagBody = ((Sandbag) actor).getBody();
					if (placeBody instanceof Polygon) {
						if (polygonAndRectangle((Polygon) placeBody, sandbagBody)) {
							if (Logger.DEBUG)
								System.out.println("intersect with tower!");
							return true;
						}
					} else if (placeBody instanceof Rectangle) {
						if (rectangleAndRectangle(sandbagBody, (Rectangle) placeBody)) {
							if (Logger.DEBUG)
								System.out.println("intersect with Sandbag!");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks for collision between a bullet and its target
	 * 
	 * @param bullet
	 * @param target
	 * @return boolean - If there is a collision
	 */
	public static boolean bulletAndTarget(Rectangle bullet, Polygon target) {
		if (polygonAndRectangle(target, bullet)) {
			if (Logger.DEBUG)
				System.out.println("target hit!");
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a tower was hit when the user clicks
	 * 
	 * @param Towers
	 *            - Collection of Towers that are active
	 * @param clickCoord
	 *            - The coords where the player clicked
	 * @return boolean - If a tower was hit.
	 */
	public static GameActor towerHit(SnapshotArray<Actor> Towers, Vector2 clickCoord) {
		Rectangle clickRect = new Rectangle(clickCoord.x, clickCoord.y, 1, 1);
		for (Actor tower : Towers) {
			if (tower instanceof Tower) {
				Polygon towerBody = ((GameActor) tower).getBody();
				towerBody.getTransformedVertices();
				if (polygonAndRectangle(towerBody, clickRect)) {
					return (GameActor) tower;
				}
			}
		}
		return null;
	}

	/**
	 * Checks to see if the target is within range of the attacker
	 * 
	 * @param targetBody
	 * @param attackerRange
	 * @return boolean - If the target is within range
	 */
	public static boolean targetWithinRange(Polygon targetBody, Shape2D attackerRange) {
		if (attackerRange instanceof Circle) {
			return polygonAndCircle(targetBody, (Circle) attackerRange);
		} else if (attackerRange instanceof Polygon) {
			return polygonAndPolygon(targetBody, (Polygon) attackerRange);
		} else {
			return true;
		}
	}

	// Create lines between 2 vertices for each vertex. Check whether that line
	// intersects the circle.
	/**
	 * Checks for collision with a polygon and circle. Libgdx does not have a
	 * native way to do this. This method creates a line between 2 vertices for
	 * each vertex. Then checks to see if that line intersects the circle.
	 * 
	 * @param polygon
	 * @param circle
	 * @return boolean - If there is an intersection
	 */
	public static boolean polygonAndCircle(Polygon polygon, Circle circle) {
		float[] vertices = polygon.getTransformedVertices();
		Vector2 center = new Vector2(circle.x, circle.y);
		// Creating local variables for optimization
		Vector2 vectorA = new Vector2();
		Vector2 vectorB = new Vector2();
		float squareRadius = circle.radius * circle.radius;
		vectorA.set(vertices[vertices.length - 2], vertices[vertices.length - 1]);
		vectorB.set(vertices[0], vertices[1]);
		if (Intersector.intersectSegmentCircle(vectorA, vectorB, center, squareRadius))
			return true;
		for (int i = 2; i < vertices.length; i += 2) {
			vectorA.set(vertices[i - 2], vertices[i - 1]);
			vectorB.set(vertices[i], vertices[i + 1]);
			if (Intersector.intersectSegmentCircle(vectorA, vectorB, center, squareRadius))
				return true;
		}
		if (polygon.contains(circle.x, circle.y)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks for collision with a polygon and rectangle. Works by making the
	 * rectangle a polygon.
	 * 
	 * @param polygon
	 * @param rectangle
	 * @return boolean - If there is a collision
	 */
	public static boolean polygonAndRectangle(Polygon polygon, Rectangle rectangle) {
		Polygon rectPoly = new Polygon(new float[] { 0, 0, rectangle.width, 0, rectangle.width, rectangle.height, 0, rectangle.height });
		rectPoly.setPosition(rectangle.x, rectangle.y);
		return Intersector.overlapConvexPolygons(rectPoly, polygon);

	}

	/**
	 * Checks for a collision between 2 polygons
	 * 
	 * @param poly1
	 * @param poly2
	 * @return boolean - If there is a collision
	 */
	public static boolean polygonAndPolygon(Polygon poly1, Polygon poly2) {
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}

	/**
	 * Checks for a collision between 2 rectangles
	 * 
	 * @param rect1
	 * @param rect2
	 * @return boolean - If there is a collision
	 */
	public static boolean rectangleAndRectangle(Rectangle rect1, Rectangle rect2) {
		return Intersector.overlaps(rect1, rect2);
	}
}
