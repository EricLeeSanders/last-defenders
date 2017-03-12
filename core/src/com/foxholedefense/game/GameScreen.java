package com.foxholedefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.GameUIStage;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.IScreenChanger;
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

	public GameScreen(int intLevel, GameStateManager gameStateManager, IScreenChanger screenChanger, Resources resources, FHDAudio audio) {

		super(gameStateManager);
		this.player = new Player();
		this.resources = resources;
		ActorGroups actorGroups = new ActorGroups();
		LevelStateManager levelStateManager = new LevelStateManager();
		uiStateManager = new GameUIStateManager(levelStateManager);
		this.gameStateManager = gameStateManager;
		gameStage = new GameStage(intLevel, player, actorGroups, audio, levelStateManager, uiStateManager, getViewport(), resources);
		gameUIStage = new GameUIStage(player, actorGroups.getTowerGroup(), uiStateManager, levelStateManager, gameStateManager
						, screenChanger, super.getInputMultiplexer(), getViewport(), resources, audio, gameStage);


		super.show();
		audio.turnOffMusic();
		GLProfiler.enable();
		gameStage.loadFirstWave();
	}

	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
		gameUIStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
	    super.resize(width, height);
	}
	@Override
	public void render(float delta) {
//	    System.out.println(
//	            "  Drawcalls: " + GLProfiler.drawCalls +
//	                    ", Calls: " + GLProfiler.calls +
//	                    ", TextureBindings: " + GLProfiler.textureBindings +
//	                    ", ShaderSwitches:  " + GLProfiler.shaderSwitches +
//	                    ", vertexCount: " + GLProfiler.vertexCount.value
//	    );
//	    GLProfiler.reset();
		delta = delta * resources.getGameSpeed();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getCamera().update();
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
		Logger.info("Game Screen: resume");
		if(!uiStateManager.getState().equals(GameUIState.OPTIONS)){
			gameStateManager.setState(GameState.PLAY);
		}
	}
	@Override
	public void dispose() {
		Logger.info("Game Screen Dispose");
		gameStage.dispose();
		gameUIStage.dispose();
	    GLProfiler.disable();
	}

}
