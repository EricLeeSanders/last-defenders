package com.foxholedefense.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Screen class for the Main Menu. Creates the view and stage
 * 
 * @author Eric
 *
 */
public class MenuScreen extends AbstractScreen {
	private MenuStage stage;
	private GameStateManager gameStateManager;

	public MenuScreen(IScreenChanger screenChanger, GameStateManager gameStateManager, Resources resources, FHDAudio audio) {
		super(gameStateManager);
		this.gameStateManager = gameStateManager;
		this.stage = new MenuStage(screenChanger, resources, audio, getViewport());
		super.addInputProcessor(stage);
		audio.playMusic();
		createBackListener();
	}

	private void createBackListener(){
		InputProcessor backProcessor = new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {

				if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK) ) {
					Logger.info("MenuScreen: Escape/Back pressed.");
					gameStateManager.setState(GameState.QUIT);
				}
				return false;
			}
		};
		super.addInputProcessor(backProcessor);
	}

	@Override
	public void show() {
		Logger.info("Menu Screen: show");
		super.show();

	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height); // update the size of Viewport
	    super.resize(width, height);
	}
	@Override
	public void renderElements(float delta) {
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
		Logger.info("Menu Screen: Dispose");
		stage.dispose();

	}
}