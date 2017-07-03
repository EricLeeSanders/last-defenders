package com.foxholedefense.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 1/3/2017.
 */

class MapRenderer {

    private TiledMapRenderer tiledMapRenderer;

    public MapRenderer(TiledMap tiledMap, Camera camera) {

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Resources.TILED_MAP_SCALE);
        tiledMapRenderer.setView((OrthographicCamera) camera);
    }

    public void update() {

        tiledMapRenderer.render();
    }
}
