package com.eric.mtd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.level.Level;
import com.eric.mtd.game.model.level.MTDTiledMapRenderer;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Game Stage class that contains all of the Actors and Groups. Responsible for
 * creating the MapRenderer and Level.
 * 
 * @author Eric
 *
 */
public class GameStage extends Stage {

	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private Level level;
	private Player player;
	private int intLevel;
	private ActorGroups actorGroups;
	private MTDTiledMapRenderer mapRenderer;

	public GameStage(int intLevel, Player player, ActorGroups actorGroups, LevelStateManager levelStateManager, GameUIStateManager uiStateManager, Viewport viewport) {
		super(viewport);
		MTDGame.gameSpeed = (Resources.NORMAL_SPEED);
		this.player = player;
		this.actorGroups = actorGroups;
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		this.intLevel = intLevel;
		createGroups();
		level = new Level(intLevel, levelStateManager, getActorGroups());
		mapRenderer = new MTDTiledMapRenderer(intLevel, getCamera());

	}
	/**
	 * Create the actor groups
	 */
	public void createGroups() {
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getHealthBarGroup());
		this.addActor(getActorGroups().getProjectileGroup());
		this.addActor(getActorGroups().getSandbagGroup());
		this.addActor(getActorGroups().getSupportGroup());
	}

	/**
	 * Updates the level
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		level.update(delta);
		if (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
			isWaveOver();
		}
	}

	@Override
	public void draw() {
		mapRenderer.update();
		super.draw();
	}	

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Game Stage Dispose");
		mapRenderer.dispose();
		super.dispose();
	}

	/**
	 * Determine if the wave is over
	 */
	public void isWaveOver() {
		//System.out.println("enemies size: " + actorGroups.getEnemyGroup().getChildren().size);
		if (getActorGroups().getEnemyGroup().getChildren().size <= 0) {
			if (!(levelStateManager.getState().equals(LevelState.GAME_OVER))) {
				player.giveMoney((int) (100 * (float) level.getCurrentWave()));
				levelStateManager.setState(LevelState.STANDBY);
				player.setWaveCount(player.getWaveCount() + 1);
				healTowers();
			}
		}

	}
	private void healTowers(){
		for(Actor tower : actorGroups.getTowerGroup().getChildren()){
			if (tower instanceof Tower){
				((Tower)tower).heal();
			}
		}
	}
	public int getIntLevel() {
		return intLevel;
	}

	/**
	 * If an enemy reaches the end, subtract 1 life from the player
	 */
	public void enemyReachedEnd() {
		if (player.getLives() > 0) { // Only subtract if we have lives.
			player.setLives(player.getLives() - 1);
		}
		if (player.getLives() <= 0) { // end game
			levelStateManager.setState(LevelState.GAME_OVER);
			uiStateManager.setState(GameUIState.GAME_OVER);
		}
	}

	public ActorGroups getActorGroups() {
		return actorGroups;
	}

	public void setActorGroups(ActorGroups actorGroups) {
		this.actorGroups = actorGroups;
	}
	
	public Map getMap(){
		return level.getMap();
	}

}
