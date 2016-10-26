package com.foxholedefense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.FHDGame;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

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
	private Map map;
	private Resources resources;
	public GameStage(int intLevel, Player player, ActorGroups actorGroups, ActorFactory actorFactory, LevelStateManager levelStateManager, GameUIStateManager uiStateManager, Viewport viewport, Resources resources) {
		super(viewport);
		this.player = player;
		this.actorGroups = actorGroups;
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		this.intLevel = intLevel;
		this.resources = resources;
		createGroups();
		map = new Map(intLevel, getCamera(), resources.getMap(intLevel));
		level = new Level(intLevel, getActorGroups(),actorFactory, map);

	}
	
	/**
	 * Create the actor groups
	 */
	public void createGroups() {
		//Order matters
		this.addActor(getActorGroups().getDeathEffectGroup());
		this.addActor(getActorGroups().getLandmineGroup());
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getProjectileGroup());
		this.addActor(getActorGroups().getHealthBarGroup());
		this.addActor(getActorGroups().getSupportGroup());
	}

	/**
	 * Updates the level
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
			level.update(delta);
			isWaveOver();
		}
	}

	@Override
	public void draw() {
		map.update();
		super.draw();
	}	

	@Override
	public void dispose() {
		Logger.info("Game Stage Dispose");
		map.dispose();
		resources.unloadMap(intLevel);
		super.dispose();
	}

	/**
	 * Determine if the wave is over
	 */
	public boolean isWaveOver() {
		if (getActorGroups().getEnemyGroup().getChildren().size <= 0
			&& level.getSpawningEnemiesCount() <= 0
			&& getActorGroups().getProjectileGroup().getChildren().size <= 0
			&& !(levelStateManager.getState().equals(LevelState.GAME_OVER))) {
			
				Logger.info("Wave over");
				player.giveMoney((int) (100 * (float) level.getCurrentWave()));
				levelStateManager.setState(LevelState.STANDBY);
				player.setWaveCount(player.getWaveCount() + 1);
				level.loadWave(); //load the next wave
				healTowers();
				isLevelCompleted();
				return true;
		}
		return false;

	}
	
	/**
	 * Determine if the level is completed
	 */
	private boolean isLevelCompleted(){
		if(player.getWavesCompleted() == Level.MAX_WAVES){
			Logger.info("Level Over");
			uiStateManager.setState(GameUIState.LEVEL_COMPLETED);
			return true;
		}
		return false;
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
		return map;
	}

}
