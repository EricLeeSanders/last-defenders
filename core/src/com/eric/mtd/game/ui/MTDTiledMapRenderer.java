package com.eric.mtd.game.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class MTDTiledMapRenderer implements Disposable{
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private GameStage camera;
	public MTDTiledMapRenderer(int intLevel, GameStage camera){
		this.camera = camera;
		tiledMap = Resources.getMap(intLevel);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        
	}
	public void update() {
		tiledMapRenderer.setView((OrthographicCamera) camera.getCamera()); //Question: okay to put this here?
		tiledMapRenderer.render();
	}
	@Override
	public void dispose() {
		if(Logger.DEBUG)System.out.println("MTDTiledMapRenderer Dispose");
		tiledMap.dispose();
		
	}

}
