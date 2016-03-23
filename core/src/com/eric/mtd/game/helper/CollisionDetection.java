package com.eric.mtd.game.helper;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.eric.mtd.game.model.actor.perks.Sandbag;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.badlogic.gdx.math.Shape2D;
public final class CollisionDetection {
/*	public static boolean towerAndPath(TiledMap tiledMap, Tower placeTower){
		MapObjects boundaries = tiledMap.getLayers().get("PathBoundary").getObjects();
		for(MapObject boundry : boundaries){
			if(boundry.getClass().equals(com.badlogic.gdx.maps.objects.RectangleMapObject.class)){
				RectangleMapObject boundryRectangle = (RectangleMapObject)boundry;
				if(polygonAndRectangle(placeTower.getBody(),boundryRectangle.getRectangle())){
					if(Logger.DEBUG)System.out.println("intersect with path!");
					return true;
				}
			}
		}
		return false;
	}
	public static boolean towerAndOtherTowers(SnapshotArray<Actor> Towers, Tower placeTower){
		for(Actor tower: Towers){
			if(tower instanceof Tower){
				if(!tower.equals(placeTower)){
				Polygon towerBody = ((GameActor)tower).getBody();
					if(Intersector.overlapConvexPolygons(placeTower.getBody(), towerBody)){
						if(Logger.DEBUG)System.out.println("intersect with tower!");
						return true;
					}
				}
			}
		}
		return false;
	}
	*/
	public static boolean CollisionWithPath( Array<RectangleMapObject> pathBoundary, ICollision placeActor){
		for(RectangleMapObject boundry : pathBoundary){
			Shape2D body = placeActor.getBody();
			if(body instanceof Polygon){
				if(polygonAndRectangle((Polygon) body,boundry.getRectangle())){
					if(Logger.DEBUG)System.out.println("intersect with path!");
					return true;
				}
			}
			else if (body instanceof Rectangle){ 
				if(rectangleAndRectangle((Rectangle) body,boundry.getRectangle())){
					if(Logger.DEBUG)System.out.println("intersect with path!");
					return true;
				}
			}
		}
		return false;
	}
	public static boolean CollisionWithActors(SnapshotArray<Actor> Actors, ICollision placeActor){
		for(Actor actor: Actors){
			if(actor instanceof Tower){
				if(!actor.equals(placeActor)){
					Polygon towerBody = ((GameActor)actor).getBody();
					Shape2D placeBody = placeActor.getBody();
					if(placeBody instanceof Polygon){
						if(polygonAndPolygon(towerBody, (Polygon)placeBody)){
							if(Logger.DEBUG)System.out.println("intersect with tower!");
							return true;
						}
					}
					else if(placeBody instanceof Rectangle){
						if(polygonAndRectangle(towerBody, (Rectangle) placeBody)){
							if(Logger.DEBUG)System.out.println("intersect with Sandbag!");
							return true;
						}
					}
				}
			}
			if(actor instanceof Sandbag){
				if(!actor.equals(placeActor)){
					Rectangle sandbagBody = ((Sandbag)actor).getBody();
					Shape2D placeBody = placeActor.getBody();
					if(placeBody instanceof Polygon){
						if(polygonAndRectangle((Polygon)placeBody,sandbagBody)){
							if(Logger.DEBUG)System.out.println("intersect with tower!");
							return true;
						}
					}
					else if(placeBody instanceof Rectangle){
						if(rectangleAndRectangle(sandbagBody, (Rectangle) placeBody)){
							if(Logger.DEBUG)System.out.println("intersect with Sandbag!");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean bulletAndTarget(Rectangle bullet, Polygon target){
		if(polygonAndRectangle(target,bullet)){
			if(Logger.DEBUG)System.out.println("target hit!");
			return true;
		}
		return false;
	}
	public static GameActor towerHit(SnapshotArray<Actor> Towers, Vector2 clickCoord){
		Rectangle clickRect = new Rectangle(clickCoord.x, clickCoord.y, 1, 1);
		for(Actor tower: Towers){
			if(tower instanceof Tower){
				Polygon towerBody = ((GameActor)tower).getBody();
				towerBody.getTransformedVertices();
				if(polygonAndRectangle(towerBody,clickRect)){
					return (GameActor)tower;
				}
			}
		}
		return null;
	}
	public static boolean targetWithinRange(Polygon targetBody, Circle towerRange){
		return polygonAndCircle(targetBody,towerRange);
	}
	
	//Create lines between 2 vertices for each vertex. Check whether that line intersects the circle.
	public static boolean polygonAndCircle(Polygon polygon, Circle circle){
		float []vertices=polygon.getTransformedVertices();
		Vector2 center=new Vector2(circle.x, circle.y);
		float squareRadius=circle.radius*circle.radius;
		for (int i=0;i<vertices.length;i+=2){
		    if (i==0){
		        if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
		            return true;
		    } else {
		        if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
		            return true;
		    }
		}
		if(polygon.contains(circle.x, circle.y)){
			return true;
		}
		return false;
	}
	
	/*public static boolean polygonAndPoint(Polygon polygon, Vector2 point){
		Array<Vector2> polygonVector = new Array<Vector2>();
		float [] polygonPoints = polygon.getTransformedVertices();
		for(int i = 0; i < polygonPoints.length-1; i++){
			Vector2 vector = new Vector2(polygonPoints[i],polygonPoints[i+1]);
			polygonVector.add(vector);
		}
		  return Intersector.isPointInPolygon(polygonVector, point);

	}*/
	
	public static boolean polygonAndRectangle(Polygon polygon, Rectangle rectangle){
		  Polygon rectPoly = new Polygon(new float[] { 0, 0, rectangle.width, 0, rectangle.width,
				  rectangle.height, 0, rectangle.height });
		  rectPoly.setPosition(rectangle.x, rectangle.y);
		  return Intersector.overlapConvexPolygons(rectPoly, polygon);

	}
	public static boolean polygonAndPolygon(Polygon poly1, Polygon poly2){
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}
	public static boolean rectangleAndRectangle(Rectangle rect1, Rectangle rect2){
		return Intersector.overlaps(rect1, rect2);
	}
}
