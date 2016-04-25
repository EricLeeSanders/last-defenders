package com.eric.mtd.game.model.level;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	private Queue<Vector2> pathCoords;
	public Map(int intLevel) {
		Resources.loadMap(intLevel);
		tiledMap = Resources.getMap(intLevel);
		findPath();
	}

	/**
	 * Finds the path for the enemies to follow
	 */
	public void findPath() {
		PolylineMapObject path = (PolylineMapObject) tiledMap.getLayers().get("Path").getObjects().get("PathLine");
		float[] vertices = path.getPolyline().getVertices();
		float pathX = path.getPolyline().getX();
		float pathY = path.getPolyline().getY();

		pathCoords = new LinkedList<Vector2>();
		Vector2 vertext; 
		for (int i = 0; i < vertices.length - 1; i = i + 2) {
			//vertext = AbstractScreen.camera.unproject(new Vector3(Math.abs(vertices[i] + pathX), Math.abs(vertices[i + 1] + pathY),0));
			//pathCoords.add(stage.getMyViewport().unproject(new Vector2(Math.abs(vertices[i] + pathX), Math.abs(vertices[i + 1] + pathY))));
			pathCoords.add(new Vector2(Math.abs(vertices[i] + pathX)/3, Math.abs(vertices[i + 1] + pathY)/3));
			//pathCoords.add(new Vector2(Math.abs(vertices[i] + pathX), Math.abs(vertices[i + 1] + pathY)));
		}
	}

	public Queue<Vector2> getPath() {
		return pathCoords;
	}

}
