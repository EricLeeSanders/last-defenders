package com.eric.mtd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.game.ui.GameUIStage;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Game Screen that creates the Game Stage and UI Stage as well as their
 * dependencies.
 * 
 * @author Eric
 *
 */
public class GameScreen extends AbstractScreen {
	private Label framesLabel;
	private GameStage gameStage;
	private GameUIStage gameUIStage;
	private Player player;
	private GameStateManager gameStateManager;

	public GameScreen(int intLevel, GameStateManager gameStateManager, ScreenStateManager screenStateManager) {
		super(gameStateManager);
		this.player = new Player();
		ActorGroups actorGroups = new ActorGroups();
		LevelStateManager levelStateManager = new LevelStateManager();
		GameUIStateManager uiStateManager = new GameUIStateManager(levelStateManager);
		this.gameStateManager = gameStateManager;
		gameStage = new GameStage(intLevel, player, actorGroups, levelStateManager, uiStateManager);
		//gameStage.setViewport(getViewport());
		gameUIStage = new GameUIStage(intLevel, player, actorGroups, uiStateManager, levelStateManager, gameStateManager, screenStateManager, super.getInputMultiplexer());
		//gameUIStage.setViewport(getViewport());
		super.show();

	}

	public void createFramesField() {
		framesLabel = new Label("0", Resources.getSkin(Resources.SKIN_JSON));
		framesLabel.setColor(1f, 1f, 1f, 0.30f);
		framesLabel.setFontScale(0.5f);
		framesLabel.setPosition(200, 310);
		gameUIStage.addActor(framesLabel);
	}

	@Override
	public void show() {
		if (Logger.DEBUG)
			createFramesField();
	}
	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
		gameUIStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
	    super.resize(width, height);
	}
    //getViewport().setScreenSize(width, height); // update the size of ViewPort
	@Override
	public void render(float delta) {
		delta = delta * MTDGame.gameSpeed;
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getCamera().update();
		if (Logger.DEBUG)
			framesLabel.setText(Integer.valueOf(Gdx.graphics.getFramesPerSecond()).toString());
		renderElements(delta);

	}

	/**
	 * If the Game State is not play, then "pause" the Game Stage
	 */
	@Override
	public void renderElements(float delta) {
		if (gameStateManager.getState().equals(GameState.PLAY)) {
			gameStage.act(delta);
		}
		gameStage.draw();

		gameUIStage.act(delta);
		gameUIStage.draw();

	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Game Screen Dispose");
		gameStage.dispose();
		gameUIStage.dispose();
	}

}
