package com.foxholedefense.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.IEnemyObserver;
import com.foxholedefense.game.model.actor.combat.tower.ITowerObserver;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Game Stage class that contains all of the Actors and Groups. Responsible for
 * creating the MapRenderer and Level.
 * 
 * @author Eric
 *
 */
public class GameStage extends Stage implements IEnemyObserver{

	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private Level level;
	private Player player;
	private int intLevel;
	private ActorGroups actorGroups;
	private Map map;
	private MapRenderer mapRenderer;
	private Resources resources;
	private TowerPlacement towerPlacement;
	private SupportActorPlacement supportActorPlacement;
	private AirStrikePlacement airStrikePlacement;
	private SupplyDropPlacement supplyDropPlacement;
	private ActorFactory actorFactory;
	public GameStage(int intLevel, Player player, ActorGroups actorGroups, FHDAudio audio,
					 LevelStateManager levelStateManager, GameUIStateManager uiStateManager,
					 Viewport viewport, Resources resources) {
		super(viewport);
		this.player = player;
		this.actorGroups = actorGroups;
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		this.intLevel = intLevel;
		this.resources = resources;
		createGroups();
		TiledMap tiledMap = resources.getMap(intLevel);
		map = new Map(tiledMap);
		mapRenderer = new MapRenderer(tiledMap, getCamera());
		actorFactory = new ActorFactory(actorGroups, resources.getAtlas(Resources.ACTOR_ATLAS), audio);
		actorFactory.attachEnemyObserver(this);
		level = new Level(intLevel, getActorGroups(),actorFactory, map);
		createPlacementServices(actorFactory, map);

	}

	/**
	 * Loads the first wave of the level.
	 * This is done outside of the constructor
	 * so that the GameScreen and GameStageUI are
	 * fully constructed before loading the wave.
	 */
	public void loadFirstWave(){
		level.loadWave();
	}

	public void createPlacementServices(ActorFactory actorFactory, Map map){
		towerPlacement = new TowerPlacement(map, actorGroups, actorFactory);
		supportActorPlacement = new SupportActorPlacement(actorGroups, actorFactory);
		airStrikePlacement = new AirStrikePlacement(actorGroups, actorFactory);
		supplyDropPlacement = new SupplyDropPlacement(actorFactory);
	}

	/**
	 * Create the actor groups. Order matters
	 */
	public void createGroups() {
		this.addActor(getActorGroups().getDeathEffectGroup());
		this.addActor(getActorGroups().getLandmineGroup());
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getProjectileGroup());
		this.addActor(getActorGroups().getHealthGroup());
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
		mapRenderer.update();
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

	/**
	 * If an enemy reaches the end, subtract 1 life from the player
	 */
	private void enemyReachedEnd() {
		if (player.getLives() > 0) { // Only subtract if we have lives.
			player.setLives(player.getLives() - 1);
		}
		if (player.getLives() <= 0 && !levelStateManager.getState().equals(LevelState.GAME_OVER)) { // end game
			levelStateManager.setState(LevelState.GAME_OVER);
			uiStateManager.setState(GameUIState.GAME_OVER);
		}
	}


	public void attachCombatObserver(ICombatActorObserver observer){
		actorFactory.attachCombatObserver(observer);
	}

	public void attachTowerObserver(ITowerObserver observer) {
		actorFactory.attachTowerObserver(observer);
	}

	public void attachEnemyObserver(IEnemyObserver observer) {
		actorFactory.attachEnemyObserver(observer);
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


	public TowerPlacement getTowerPlacement() {
		return towerPlacement;
	}

	public SupportActorPlacement getSupportActorPlacement() {
		return supportActorPlacement;
	}

	public AirStrikePlacement getAirStrikePlacement() {
		return airStrikePlacement;
	}

	public SupplyDropPlacement getSupplyDropPlacement() {
		return supplyDropPlacement;
	}

	@Override
	public void notifyEnemy(Enemy enemy, EnemyEvent event) {
		if(event.equals(EnemyEvent.REACHED_END)){
			enemyReachedEnd();
		}
	}
}
