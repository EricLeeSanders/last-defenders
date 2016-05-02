package com.eric.mtd.game.model.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Class that renders the TiledMap onto the screen. This has do be done
 * separately from the Model Level/Map because if the game pauses, it would stop
 * displaying the map.
 * 
 * @author Eric
 *
 */
public class MTDTiledMapRenderer implements Disposable {
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private Camera camera;

	public MTDTiledMapRenderer(int intLevel, Camera camera) {
		this.camera = camera;
		tiledMap = Resources.getMap(intLevel);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,Resources.TILED_MAP_SCALE);
		tiledMapRenderer.setView((OrthographicCamera) camera);
	}

	public void update() {
		//tiledMapRenderer.setView((OrthographicCamera) camera);
		tiledMapRenderer.render();
	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("MTDTiledMapRenderer Dispose");
		tiledMap.dispose();

	}

}
