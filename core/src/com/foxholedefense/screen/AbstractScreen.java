package com.foxholedefense.screen;

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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * An abstract screen class that handles resizing/viewport/updates
 * 
 * @author Eric
 *
 */
public abstract class AbstractScreen implements Screen {
	private OrthographicCamera camera;
	private InputMultiplexer imp;
	private GameStateManager gameStateManager;
	private Viewport viewport;
	public AbstractScreen(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		camera = new OrthographicCamera();
		imp = new InputMultiplexer();
		viewport = new FitViewport(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, getCamera());
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
		Logger.info("Abstract Screen Hiding");
	}

	@Override
	public void pause() {
		Logger.info("Abstract Screen: pausing");
		gameStateManager.setState(GameState.PAUSE);
	}

	@Override
	public void resume() {
		Logger.info("Abstract Screen: resuming");
		gameStateManager.setState(GameState.PLAY);
		
	}

	@Override
	public void resize(int width, int height) {
	    viewport.update(width, height, true);
	}

	@Override
	public void show() {
		Logger.info("Abstract Screen: show");
		Gdx.input.setInputProcessor(imp);
	}

	@Override
	public void dispose() {
		Logger.info("Abstract Screen: Disposing");

	}

	public Camera getCamera() {
		return camera;
	}

	public Viewport getViewport(){
		return viewport;
	}
	public InputMultiplexer getInputMultiplexer() {
		return imp;
	}

	protected void addInputProcessor(InputProcessor ip) {
		imp.addProcessor(ip);
	}

}
