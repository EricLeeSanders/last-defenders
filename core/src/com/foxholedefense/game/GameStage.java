package com.foxholedefense.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxholedefense.game.model.PlayerObserver;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.effects.label.WaveOverCoinEffect;
import com.foxholedefense.game.model.actor.effects.label.TowerHealEffect;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.model.level.wave.impl.DynamicWaveLoader;
import com.foxholedefense.game.model.level.wave.impl.FileWaveLoader;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
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
public class GameStage extends Stage implements PlayerObserver {
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
	private MessageDisplayer messageDisplayer;
	private CombatActorFactory combatActorFactory;
	private HealthFactory healthFactory;
	private SupportActorFactory supportActorFactory;
	private EffectFactory effectFactory;

	public GameStage(int intLevel, Player player, ActorGroups actorGroups, FHDAudio audio,
					 LevelStateManager levelStateManager, GameUIStateManager uiStateManager,
					 Viewport viewport, Resources resources, SpriteBatch spriteBatch) {

		super(viewport, spriteBatch);
		this.player = player;
		this.actorGroups = actorGroups;
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		this.intLevel = intLevel;
		this.resources = resources;
		TiledMap tiledMap = resources.getMap(intLevel);
		map = new Map(tiledMap);
		createGroups();
		createFactories(audio);
		createPlacementServices(map);
		mapRenderer = new MapRenderer(tiledMap, getCamera());
		FileWaveLoader fileWaveLoader = new FileWaveLoader(combatActorFactory, map);
		DynamicWaveLoader dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);
		level = new Level(intLevel, getActorGroups(), healthFactory, fileWaveLoader, dynamicWaveLoader);
		player.attachObserver(this);

	}

	private void createFactories(FHDAudio audio){
		effectFactory = new EffectFactory(actorGroups, resources);
		healthFactory = new HealthFactory(actorGroups,resources);
		ProjectileFactory projectileFactory = new ProjectileFactory(actorGroups, audio, resources);
		supportActorFactory = new SupportActorFactory(actorGroups, audio, resources, effectFactory, projectileFactory);
		combatActorFactory = new CombatActorFactory(actorGroups, audio, resources, effectFactory, projectileFactory, player);
	}

	private void createPlacementServices(Map map){
		Logger.info("Game Stage: creating placement services");
		towerPlacement = new TowerPlacement(map, actorGroups, combatActorFactory, healthFactory);
		supportActorPlacement = new SupportActorPlacement(actorGroups, supportActorFactory);
		airStrikePlacement = new AirStrikePlacement(actorGroups, supportActorFactory);
		supplyDropPlacement = new SupplyDropPlacement(supportActorFactory);
		Logger.info("Game Stage: placement services created");
	}

	/**
	 * Loads the first wave of the level.
	 * This is done outside of the constructor
	 * so that the GameScreen and GameStageUI are
	 * fully constructed before loading the wave.
	 */
	public void loadFirstWave(){
		Logger.info("Game Stage: loading first wave");
		level.loadNextWave();
		Logger.info("Game Stage: first wave loaded");
	}

	/**
	 * Create the actor groups. Order matters
	 */
	private void createGroups() {
		Logger.info("Game Stage: creating groups");
		this.addActor(getActorGroups().getDeathEffectGroup());
		this.addActor(getActorGroups().getLandmineGroup());
		this.addActor(getActorGroups().getEnemyGroup());
		this.addActor(getActorGroups().getTowerGroup());
		this.addActor(getActorGroups().getProjectileGroup());
		this.addActor(getActorGroups().getHealthGroup());
		this.addActor(getActorGroups().getSupportGroup());
		this.addActor(getActorGroups().getEffectGroup());
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
	private boolean isWaveOver() {
		return getActorGroups().getEnemyGroup().getChildren().size <= 0
				&& level.getSpawningEnemiesCount() <= 0
				&& getActorGroups().getProjectileGroup().getChildren().size <= 0
				&& !(levelStateManager.getState().equals(LevelState.GAME_OVER));

	}

	private void waveOver(){
		Logger.info("Game Stage: Wave over");
		int money = (int) (WAVE_OVER_MONEY_MULTIPLIER * (float) level.getCurrentWave());
		player.giveMoney(money);
		levelStateManager.setState(LevelState.STANDBY);
		player.setWaveCount(player.getWaveCount() + 1);
		if(isLevelCompleted()){
			levelComleted();
		}
		WaveOverCoinEffect waveOverCoinEffect = effectFactory.loadLabelEffect(WaveOverCoinEffect.class);
		waveOverCoinEffect.initialize(money);
		level.loadNextWave(); //load the next wave
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
				if(((Tower)tower).isActive() && ((Tower) tower).getHealthPercent() < 1) {
					TowerHealEffect effect = effectFactory.loadLabelEffect(TowerHealEffect.class);
					effect.initialize((Tower) tower);

					((Tower) tower).heal();
				}
			}
		}
	}

	@Override
	public void playerAttributeChange() {
		if (player.getLives() <= 0 && !levelStateManager.getState().equals(LevelState.GAME_OVER)) { // end game
			Logger.info("Game Stage: game over");
			levelStateManager.setState(LevelState.GAME_OVER);
			uiStateManager.setState(GameUIState.GAME_OVER);
		}
	}

	private ActorGroups getActorGroups() {
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

	public MessageDisplayer getMessageDisplayer() {
		return messageDisplayer;
	}

	public void setMessageDisplayer(MessageDisplayer messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
	}

}
