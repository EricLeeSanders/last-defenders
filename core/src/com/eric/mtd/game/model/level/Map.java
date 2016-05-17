package com.eric.mtd.game.model.level;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents a TiledMap
 * 
 * @author Eric
 *
 */
public class Map {
	private TiledMap tiledMap;
	private Queue<Vector2> pathCoords = new LinkedList<Vector2>();
	private Array<Rectangle> pathBoundaries = new Array<Rectangle>(false, 16);
	public Map(int intLevel) {
		Resources.loadMap(intLevel);
		tiledMap = Resources.getMap(intLevel);
		findPath();
		findPathBoundary();
		tiledMap.dispose();
		tiledMap = null;
	}

	/**
	 * Finds the path for the enemies to follow
	 */
	public void findPath() {
		PolylineMapObject path = (PolylineMapObject) tiledMap.getLayers().get("Path").getObjects().get("PathLine");
		float[] vertices = path.getPolyline().getVertices();
		float pathX = path.getPolyline().getX();
		float pathY = path.getPolyline().getY();

		for (int i = 0; i < vertices.length - 1; i = i + 2) {
			pathCoords.add(new Vector2(Math.abs(vertices[i] + pathX)*Resources.TILED_MAP_SCALE
					, Math.abs(vertices[i + 1] + pathY)*Resources.TILED_MAP_SCALE));
		}
	}
	
	/**
	 * Finds the path boundaries from the Tiled Map.
	 *  * 
	 * @param tiledMap
	 */
	private void findPathBoundary() {
		MapObjects boundaries = tiledMap.getLayers().get("PathBoundary").getObjects();
		for (MapObject boundry : boundaries) {
			if (boundry instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) boundry).getRectangle();
				if(Logger.DEBUG)System.out.println("Rect before: " + rect.x + "," + rect.y + ":" + rect.width + "," + rect.height);
				Rectangle pathBoundary = new Rectangle(rect.x*Resources.TILED_MAP_SCALE, rect.y*Resources.TILED_MAP_SCALE
						, rect.width*Resources.TILED_MAP_SCALE, rect.height*Resources.TILED_MAP_SCALE); //Required to create new Rectangle
																										//Otherwise, rectangle properties of the MapObject
																										//are altered and cached
				pathBoundaries.add(pathBoundary);
				if(Logger.DEBUG){
					System.out.println("added path boundary: " + pathBoundary.x + "," + pathBoundary.y + ":" + pathBoundary.width + "," + pathBoundary.height);
					System.out.println("Tiled Map Scale: " + Resources.TILED_MAP_SCALE);
				}
			}
		}
	}
	public Array<Rectangle> getPathBoundaries(){
		return pathBoundaries;
	}
	public Queue<Vector2> getPath() {
		return pathCoords;
	}

}
