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
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.util.Resources;

/**
 * Represents a TiledMap
 * 
 * @author Eric
 *
 */
public class Map {
	private TiledMap tiledMap;
	private Queue<Vector2> pathCoords = new LinkedList<Vector2>();;
	private Array<Rectangle> pathBoundaries = new Array<Rectangle>();
	public Map(int intLevel) {
		Resources.loadMap(intLevel);
		tiledMap = Resources.getMap(intLevel);
		findPath();
		findPathBoundary();
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
	 * Finds the path boundaries from the Tiled Map
	 * 
	 * @param tiledMap
	 */
	private void findPathBoundary() {
		MapObjects boundaries = tiledMap.getLayers().get("PathBoundary").getObjects();
		for (MapObject boundry : boundaries) {
			if (boundry instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) boundry).getRectangle();
				rect.set(rect.x*Resources.TILED_MAP_SCALE, rect.y*Resources.TILED_MAP_SCALE
						, rect.width*Resources.TILED_MAP_SCALE, rect.height*Resources.TILED_MAP_SCALE);
				pathBoundaries.add(rect);
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
