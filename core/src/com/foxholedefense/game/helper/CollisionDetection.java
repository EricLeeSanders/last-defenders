package com.foxholedefense.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.ICollision;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.badlogic.gdx.math.Shape2D;
import com.foxholedefense.util.UtilPool;

/**
 * A static class to handle all collisions that can occur during the game
 *
 * @author Eric
 *
 */
public final class CollisionDetection {
	/**
	 * Checks for collision with the path boundaries when placing a tower
	 *
	 * @param boundaries - Array of rectangles from TiledMap that represent the
	 *            path boundary
	 * @param placeActor - The actor that is being places
	 * @return boolean - If there is a collision
	 */
	public static boolean CollisionWithPath(Array<Rectangle> boundaries, ICollision placeActor) {
		Shape2D body = placeActor.getBody();
		for (Rectangle boundary : boundaries) {
			if (body instanceof Polygon) {
				if (polygonAndRectangle((Polygon) body, boundary)){
					return true;
				}
			} else if (body instanceof Circle) {
				if(circleAndRectangle((Circle) body, boundary)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks for collisions with actors when placing a tower
	 *
	 * @param Actors - A collection of Actors that implement ICollision
	 * @param placeActor - The actor that is being placed
	 * @return boolean - If there is a collision
	 */
	public static boolean CollisionWithActors(SnapshotArray<Actor> Actors, ICollision placeActor) {
		Shape2D placeBody = placeActor.getBody();
		for (Actor actor : Actors) {
			if (!actor.equals(placeActor) && actor instanceof Tower) {
				Shape2D towerBody = ((CombatActor) actor).getBody();
				if (placeBody instanceof Circle) {
					if (towerBody instanceof Circle) {
						if (circleAndCircle((Circle) towerBody, (Circle) placeBody)) {
							return true;
						}
					} else if (towerBody instanceof Polygon){
						if(polygonAndCircle((Polygon) towerBody, (Circle) placeBody)) {
							return true;
						}
					}
				} else if (placeBody instanceof Polygon) {
					if (towerBody instanceof Circle) {
						if (polygonAndCircle((Polygon) placeBody, (Circle) towerBody)) {
							return true;
						}
					} else if (towerBody instanceof Polygon){
						if (polygonAndPolygon((Polygon) placeBody, (Polygon) towerBody)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks for collision between a LandMine and an Enemy
	 *
	 * @param targetBody - The body of the enemy
	 * @param landMineBody - the body of the landmine
	 * @return boolean - If there is a collision
	 */
	public static boolean landMineAndEnemy(Shape2D targetBody, Circle landMineBody){
		if (targetBody instanceof Polygon) {
			return (polygonAndCircle((Polygon)targetBody, landMineBody));
		} else if(targetBody instanceof Circle){
			return (circleAndCircle((Circle)targetBody, landMineBody));
		}

		return false;
	}


	public static boolean explosionAndTarget(Shape2D targetBody, Circle explosion){
		if (targetBody instanceof Polygon) {
			return (polygonAndCircle((Polygon)targetBody, explosion));
		} else if(targetBody instanceof Circle){
			return (circleAndCircle((Circle)targetBody, explosion));
		}

		return false;
	}

	public static boolean flameAndTarget(Shape2D targetBody, Polygon flame) {
		if (targetBody instanceof Polygon) {
			return (polygonAndPolygon(flame, (Polygon)targetBody));
		} else if(targetBody instanceof Circle){
			return (polygonAndCircle(flame, (Circle)targetBody));
		}

		return false;
	}

	/**
	 * Checks for collision between a bullet and its target
	 *
	 * @param targetBody - The body of the target
	 * @param bullet - The bullet
	 * @return boolean - If there is a collision
	 */
	public static boolean bulletAndTarget(Shape2D targetBody, Circle bullet) {
		if (targetBody instanceof Polygon) {
			return (polygonAndCircle((Polygon)targetBody, bullet));
		} else if(targetBody instanceof Circle){
			return (circleAndCircle((Circle)targetBody, bullet));
		}

		return false;
	}

	/**
	 * Checks to see if a tower was hit when the user clicks
	 *
	 * @param Towers - Collection of Towers that are active
	 * @param clickCoord - The coords where the player clicked
	 * @return CombatActor - CombatActor that was hit or null if none were hit
	 */
	private static Rectangle clickRect = new Rectangle(0,0,0,0);
	public static Tower towerHit(SnapshotArray<Actor> Towers, Vector2 clickCoord) {
		clickRect.setPosition(clickCoord.x, clickCoord.y);
		clickRect.setSize(1,1);
		for (Actor tower : Towers) {
			if (tower instanceof Tower) {
				Shape2D towerBody = ((CombatActor) tower).getBody();
				if (towerBody instanceof Polygon && polygonAndRectangle((Polygon) towerBody, clickRect)) {
					return (Tower) tower;
				} else if(towerBody instanceof Circle && circleAndRectangle((Circle) towerBody, clickRect)){
					return (Tower) tower;
				}
			}
		}
		return null;
	}

	/**
	 * Checks to see if the target is within range
	 *
	 * @param targetBody - The target body
	 * @param range - The range shape
	 * @return boolean - If the target is within range
	 */
	public static boolean targetWithinRange(Shape2D targetBody, Shape2D range) {
		if (targetBody instanceof Circle) {
			if (range instanceof Circle) {
				return circleAndCircle((Circle) targetBody, (Circle) range);
			} else if (range instanceof Polygon) {
				return polygonAndCircle((Polygon) range, (Circle) targetBody);
			}
		} else if (targetBody instanceof Polygon) {
			if (range instanceof Circle) {
				return polygonAndCircle((Polygon) targetBody, (Circle) range);
			} else if (range instanceof Polygon) {
				return polygonAndPolygon((Polygon) targetBody, (Polygon) range);
			}
		}
		return false;
	}

	/**
	 * Checks for collision with a polygon and circle. Libgdx does not have a
	 * native way to do this. This method creates a line between 2 vertices for
	 * each vertex. Then checks to see if that line intersects the circle.
	 *
	 * @param polygon
	 * @param circle
	 * @return boolean - If there is an intersection
	 */
	private static boolean polygonAndCircle(Polygon polygon, Circle circle) {
		float[] vertices = polygon.getTransformedVertices();
		FHDVector2 center = UtilPool.getVector2(circle.x, circle.y);
		FHDVector2 vectorA = UtilPool.getVector2();
		FHDVector2 vectorB = UtilPool.getVector2();
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

	/**
	 * Checks for collision with a polygon and rectangle. Works by making the
	 * rectangle a polygon.
	 *
	 * @param polygon
	 * @param rectangle
	 * @return boolean - If there is a collision
	 */
	private static Polygon rectPoly = new Polygon();
	private static float rectanglePoints [] = new float[8];
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
	 * @param poly1
	 * @param poly2
	 * @return boolean - If there is a collision
	 */
	private static boolean polygonAndPolygon(Polygon poly1, Polygon poly2) {
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}

	/**
	 * Checks for a collision between 2 rectangles
	 *
	 * @param rect1
	 * @param rect2
	 * @return boolean - If there is a collision
	 */
	private static boolean rectangleAndRectangle(Rectangle rect1, Rectangle rect2) {
		return Intersector.overlaps(rect1, rect2);
	}

	private static boolean circleAndRectangle(Circle circle, Rectangle rect){
		return Intersector.overlaps(circle, rect);
	}

	private static boolean circleAndCircle(Circle circle1, Circle circle2){
		return Intersector.overlaps(circle1, circle2);
	}

}
