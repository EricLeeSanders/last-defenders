package com.foxholedefense.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
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
import com.foxholedefense.game.model.actor.effects.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.TowerHealEffect;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
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
	private static final int WAVE_OVER_MONEY_MULTIPLIER = 100;
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
	private IMessageDisplayer messageDisplayer;
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
		actorFactory = new ActorFactory(actorGroups, resources.getAsset(Resources.ACTOR_ATLAS, TextureAtlas.class), audio, resources);
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
		Logger.info("Game Stage: loading first wave");
		level.loadWave();
		Logger.info("Game Stage: first wave loaded");
	}

	public void createPlacementServices(ActorFactory actorFactory, Map map){
		Logger.info("Game Stage: creating placement services");
		towerPlacement = new TowerPlacement(map, actorGroups, actorFactory);
		supportActorPlacement = new SupportActorPlacement(actorGroups, actorFactory);
		airStrikePlacement = new AirStrikePlacement(actorGroups, actorFactory);
		supplyDropPlacement = new SupplyDropPlacement(actorFactory);
		Logger.info("Game Stage: placement services created");
	}

	/**
	 * Create the actor groups. Order matters
	 */
	public void createGroups() {
		Logger.info("Game Stage: creating groups");
		this.addActor(getActorGroups().getDeathEffectGroup());
		this.addActor(getActorGroups().getLandmineGroup());
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getProjectileGroup());
		this.addActor(getActorGroups().getHealthGroup());
		this.addActor(getActorGroups().getSupportGroup());
		Logger.info("Game Stage: groups created");
	}

	/**
	 * Updates the level
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
			level.update(delta);
			if(isWaveOver()){
				waveOver();
			}
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

				return true;
		}
		return false;

	}

	private void waveOver(){
		Logger.info("Game Stage: Wave over");
		int money = (int) (WAVE_OVER_MONEY_MULTIPLIER * (float) level.getCurrentWave());
		player.giveMoney(money);
		levelStateManager.setState(LevelState.STANDBY);
		player.setWaveCount(player.getWaveCount() + 1);
		if(isLevelCompleted()){
			levelComleted();
		} else {
			messageDisplayer.displayMessage("+ " + money, .75f, Color.YELLOW);
		}
		level.loadWave(); //load the next wave
		healTowers();
	}
	
	/**
	 * Determine if the level is completed
	 */
	private boolean isLevelCompleted(){
		if(player.getWavesCompleted() == Level.MAX_WAVES){
			Logger.info("Game Stage: Level Over");
			return true;
		}
		return false;
	}

	private void levelComleted(){
		uiStateManager.setState(GameUIState.LEVEL_COMPLETED);
	}
	
	private void healTowers(){
		Logger.info("Game Stage: healing towers");
		for(Actor tower : actorGroups.getTowerGroup().getChildren()){
			if (tower instanceof Tower){
				if(((Tower)tower).isActive()) {
					TowerHealEffect effect = actorFactory.loadTowerHealEffect();
					effect.initialize((Tower) tower);

					((Tower) tower).heal();
				}
			}
		}
	}

	/**
	 * If an enemy reaches the end, subtract 1 life from the player
	 */
	private void enemyReachedEnd() {
		Logger.info("Game Stage: enemy reached end");
		if (player.getLives() > 0) { // Only subtract if we have lives.
			player.setLives(player.getLives() - 1);
		}
		if (player.getLives() <= 0 && !levelStateManager.getState().equals(LevelState.GAME_OVER)) { // end game
			Logger.info("Game Stage: game over");
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
		Logger.info("Game Stage: notify enemy: " + event.name());
		if(event.equals(EnemyEvent.REACHED_END)){
			enemyReachedEnd();
		}
	}

	public IMessageDisplayer getMessageDisplayer() {
		return messageDisplayer;
	}

	public void setMessageDisplayer(IMessageDisplayer messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
	}
}
