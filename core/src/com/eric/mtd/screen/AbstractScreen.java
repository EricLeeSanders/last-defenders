package com.eric.mtd.screen;

import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * An abstract screen class that handles resizing/viewport/updates
 * 
 * @author Eric
 *
 */
public abstract class AbstractScreen implements Screen {
	private Viewport viewport;
	private OrthographicCamera camera;
	private InputMultiplexer imp;
	private GameStateManager gameStateManager;
	public AbstractScreen(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		camera = new OrthographicCamera();
		//viewport = new ExtendViewport(Resources.SCREEN_WIDTH, Resources.SCREEN_HEIGHT, camera);
		imp = new InputMultiplexer();
	}

	public abstract void renderElements(float delta);

	@Override
	public void render(float delta) {
		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderElements(delta);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		gameStateManager.setState(GameState.PAUSE);
	}

	@Override
	public void resume() {
		Resources.gameResume();
		gameStateManager.setState(GameState.PLAY);
		
	}

	@Override
	public void resize(int width, int height) {
	    camera.setToOrtho(false, width, height);
	    camera.update();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(imp);
		if (Logger.DEBUG)
			System.out.println("Processer Set");
	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("abstractscreen disposing");

	}

	public Camera getCamera() {
		return camera;
	}


	public InputMultiplexer getInputMultiplexer() {
		return imp;
	}

	protected void addInputProcessor(InputProcessor ip) {
		imp.addProcessor(ip);
	}

}
