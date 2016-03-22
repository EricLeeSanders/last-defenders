package com.eric.mtd.model.level;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.eric.mtd.MTDGame;
import com.eric.mtd.helper.Logger;
import com.eric.mtd.helper.Resources;
import com.eric.mtd.model.stage.GameStage;

public class Map implements Disposable{
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private Queue<Vector2> pathCoords; 
	public Map(int intLevel){
		Resources.loadMap(intLevel);
		tiledMap = Resources.getMap(intLevel);
        //tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        findPath();
	}
	public void update(float delta) {
		//Question: Not sure why the TiledMap needs to run constantly?\
		//tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
       // tiledMapRenderer.render();
	}
	public void findPath(){
		PolylineMapObject path = (PolylineMapObject)tiledMap.getLayers().get("Path").getObjects().get("PathLine");
		float[] vertices = path.getPolyline().getVertices();
		float pathX = path.getPolyline().getX();
		float pathY = path.getPolyline().getY();

		pathCoords = new LinkedList<Vector2>(); 
		for(int i = 0; i < vertices.length-1; i=i+2){
			pathCoords.add(new Vector2(Math.abs(vertices[i] + pathX), Math.abs(vertices[i+1]+pathY)));
		}
	}

	public Queue<Vector2> getPath(){
		return pathCoords;
	}
	@Override
	public void dispose() {
		if(Logger.DEBUG)System.out.println("Map Dispose");
		//tiledMap.dispose();
		
	}

}
