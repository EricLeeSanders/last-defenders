package com.foxholedefense.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.ui.GameUIStage;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Game Screen that creates the Game Stage and UI Stage as well as their
 * dependencies.
 * 
 * @author Eric
 *
 */
public class GameScreen extends AbstractScreen {

	private GameStage gameStage;
	private GameUIStage gameUIStage;
	private Player player;
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private Resources resources;
	private SpriteBatch spriteBatch;

	public GameScreen(int intLevel, GameStateManager gameStateManager, ScreenChanger screenChanger, Resources resources, FHDAudio audio) {

		super(gameStateManager);
		this.player = new Player();
		this.resources = resources;
		ActorGroups actorGroups = new ActorGroups();
		LevelStateManager levelStateManager = new LevelStateManager();
		uiStateManager = new GameUIStateManager(levelStateManager);
		this.gameStateManager = gameStateManager;
		spriteBatch = new SpriteBatch();
		gameStage = new GameStage(intLevel, player, actorGroups, audio, levelStateManager, uiStateManager, getViewport(), resources, spriteBatch);
		gameUIStage = new GameUIStage(player, actorGroups.getTowerGroup(), uiStateManager, levelStateManager, gameStateManager
						, screenChanger, super.getInputMultiplexer(), getViewport(), resources, audio, gameStage, spriteBatch);

		gameStage.setMessageDisplayer(gameUIStage.getMessageDisplayer());

		super.show();
		audio.turnOffMusic();
		gameStage.loadFirstWave();
		createBackListener();
		gameStateManager.setState(GameState.PLAY);
	}

	private void createBackListener(){
		InputProcessor backProcessor = new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {

				if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK) ) {
					Logger.info("GameScreen: Escape/Back pressed.");
					uiStateManager.setState(GameUIState.QUIT_MENU);
				}
				return false;
			}
		};


		super.addInputProcessor(backProcessor);
	}

	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
		gameUIStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
	    super.resize(width, height);
	}

	/**
	 * If the Game State is not play, then "pause" the Game Stage
	 */
	@Override
	public void renderElements(float delta) {
		if (gameStateManager.getState().equals(GameState.PLAY)) {
			if(resources.getGameSpeed() > 0) {
				gameStage.act(delta * resources.getGameSpeed());
			}
		}
		gameStage.draw();

		gameUIStage.act(delta);
		gameUIStage.draw();

	}

	@Override
	public void pause() {
		Logger.info("Game Screen: pausing");
		uiStateManager.setState(GameUIState.QUIT_MENU);
		gameStateManager.setState(GameState.PAUSE);
	}

	@Override
	public void resume() {
		Logger.info("Game Screen: resume");
		if(!gameStateManager.getState().equals(GameState.PAUSE)){
			gameStateManager.setState(GameState.PLAY);
		}
	}
	@Override
	public void dispose() {
		Logger.info("Game Screen Dispose");
		gameStage.dispose();
		gameUIStage.dispose();
		spriteBatch.dispose();
	}


}
