package com.foxholedefense.game.model.level;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UtilPool;

/**
 * Represents a TiledMap
 * 
 * @author Eric
 *
 */
public class Map implements Disposable{
	private Array<FHDVector2> pathCoords = new SnapshotArray<FHDVector2>(true, 16);
	private Array<Rectangle> pathBoundaries = new SnapshotArray<Rectangle>(false, 32);
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	public Map(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		findPath();
		findBoundaries();
	}

	/**
	 * Finds the path for the enemies to follow
	 */
	private void findPath() {
		PolylineMapObject path = (PolylineMapObject) tiledMap.getLayers().get("Path").getObjects().get("PathLine");
		float[] vertices = path.getPolyline().getVertices();
		float pathX = path.getPolyline().getX();
		float pathY = path.getPolyline().getY();

		for (int i = 0; i < vertices.length - 1; i = i + 2) {
			pathCoords.add(UtilPool.getVector2(Math.abs(vertices[i] + pathX)*Resources.TILED_MAP_SCALE
					, Math.abs(vertices[i + 1] + pathY)*Resources.TILED_MAP_SCALE));
		}
	}

	/**
	 * Finds the path boundaries from the Tiled Map.
	 *  *
	 */
	private void findBoundaries() {
		MapObjects boundaries = tiledMap.getLayers().get("Boundary").getObjects();
		for (MapObject boundry : boundaries) {
			if (boundry instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) boundry).getRectangle();

				//Required to create new Rectangle otherwise rectangle properties of the MapObject are altered and cached
				Rectangle boundary = new Rectangle(rect.x*Resources.TILED_MAP_SCALE, rect.y*Resources.TILED_MAP_SCALE
						, rect.width*Resources.TILED_MAP_SCALE, rect.height*Resources.TILED_MAP_SCALE); 
				pathBoundaries.add(boundary);
			}
		}
	}
	public Array<Rectangle> getPathBoundaries(){
		return pathBoundaries;
	}
	public Array<FHDVector2> getPath() {

		return pathCoords;
	}
	
	@Override
	public void dispose() {
		Logger.info("Map: Disposed");
		tiledMap.dispose();
	}

}
