package com.eric.mtd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.ui.GameUIStage;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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
	private GameUIStateManager uiStateManager;

	public GameScreen(int intLevel, GameStateManager gameStateManager, ScreenStateManager screenStateManager) {
		super(gameStateManager);
		this.player = new Player();
		ActorGroups actorGroups = new ActorGroups();
		LevelStateManager levelStateManager = new LevelStateManager();
		uiStateManager = new GameUIStateManager(levelStateManager);
		this.gameStateManager = gameStateManager;
		gameStage = new GameStage(intLevel, player, actorGroups, levelStateManager, uiStateManager, getViewport());
		//gameStage.setViewport(getViewport());
		gameUIStage = new GameUIStage(player, actorGroups, uiStateManager, levelStateManager, gameStateManager
						, screenStateManager, super.getInputMultiplexer(), getViewport(), gameStage.getMap());
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
		GLProfiler.enable();
		createFramesField();
	}
	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
		gameUIStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
	    super.resize(width, height);
	}
	@Override
	public void render(float delta) {
	  /*  System.out.println(
	            "  Drawcalls: " + GLProfiler.drawCalls +
	                    ", Calls: " + GLProfiler.calls +
	                    ", TextureBindings: " + GLProfiler.textureBindings +
	                    ", ShaderSwitches:  " + GLProfiler.shaderSwitches +
	                    ", vertexCount: " + GLProfiler.vertexCount.value
	    );
	    GLProfiler.reset();*/
		delta = delta * MTDGame.gameSpeed;
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getCamera().update();
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
	public void resume() {
		Resources.gameResume();
		if(!uiStateManager.getState().equals(GameUIState.OPTIONS)){
			gameStateManager.setState(GameState.PLAY);
		}
	}
	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Game Screen Dispose");
		gameStage.dispose();
		gameUIStage.dispose();
	    GLProfiler.disable();
	}

}
