package com.lastdefenders.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.PlayerObserver;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.WaveOverCoinEffect;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.actorplacement.TowerPlacement;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.HealthFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.googleplay.GooglePlayAchievement;
import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.log.EventLogBuilder;
import com.lastdefenders.log.EventLogger;
import com.lastdefenders.log.EventLogger.LogEvent;
import com.lastdefenders.log.EventLogger.LogParam;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import java.util.HashMap;

/**
 * Game Stage class that contains all of the Actors and Groups. Responsible for
 * creating the MapRenderer and Level.
 *
 * @author Eric
 */
public class GameStage extends Stage implements PlayerObserver {

    private static final int WAVE_OVER_MONEY_ADDITION = 210;
    private static final int WAVE_OVER_MONEY_MULTIPLIER = 10;
    private LevelStateManager levelStateManager;
    private GameUIStateManager uiStateManager;
    private Level level;
    private Player player;
    private ActorGroups actorGroups;
    private Map map;
    private MapRenderer mapRenderer;
    private Resources resources;
    private TowerPlacement towerPlacement;
    private SupportActorPlacement supportActorPlacement;
    private CombatActorFactory combatActorFactory;
    private HealthFactory healthFactory;
    private SupportActorFactory supportActorFactory;
    private EffectFactory effectFactory;
    private GooglePlayServices playServices;
    private AdControllerHelper adControllerHelper;
    private EventLogger eventLogger;
    private java.util.Map<Class<? extends SupportActor>, SupportActorCooldown> supportActorCooldownMap = new HashMap<>();
    private java.util.Map<Class<? extends SupportActor>, SupportActorValidator> supportActorValidatorMap = new HashMap<>();

    public GameStage(LevelName levelName, Player player, ActorGroups actorGroups, AudioManager audio,
        LevelStateManager levelStateManager, GameUIStateManager uiStateManager,
        Viewport viewport, Resources resources, SpriteBatch spriteBatch,
        GooglePlayServices playServices, AdControllerHelper adControllerHelper,
        EventLogger eventLogger) {

        super(viewport, spriteBatch);
        this.player = player;
        this.actorGroups = actorGroups;
        this.levelStateManager = levelStateManager;
        this.uiStateManager = uiStateManager;
        this.resources = resources;
        this.playServices = playServices;
        this.adControllerHelper = adControllerHelper;
        this.eventLogger = eventLogger;

        initialize(levelName, audio);
    }

    private void initialize(LevelName levelName, AudioManager audio){
        eventLogger.addDefaultStringParameter(LogParam.LEVEL_NAME.getTag(), levelName.getName());
        TiledMap tiledMap = resources.getMap(levelName);
        map = new Map(tiledMap, resources.getTiledMapScale());
        map.init();
        createGroups();
        createFactories(audio);
        createPlacementServices(map);
        initSupportActorCooldowns();
        initSupportActorValidators();
        mapRenderer = new MapRenderer(tiledMap, resources.getTiledMapScale(), getCamera(), getBatch());
        FileWaveLoader fileWaveLoader = new FileWaveLoader(combatActorFactory, map);
        DynamicWaveLoader dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);
        level = new Level(levelName, getActorGroups(), fileWaveLoader,
            dynamicWaveLoader);
        player.attachObserver(this);

        eventLogger.logEvent(new EventLogBuilder(LogEvent.LEVEL_START));
    }

    private void createFactories(AudioManager audio) {

        effectFactory = new EffectFactory(actorGroups, resources);
        healthFactory = new HealthFactory(resources);
        ProjectileFactory projectileFactory = new ProjectileFactory(actorGroups, audio.getSoundPlayer(), resources);
        supportActorFactory = new SupportActorFactory(actorGroups, audio.getSoundPlayer(), resources, effectFactory,
            projectileFactory);
        combatActorFactory = new CombatActorFactory(actorGroups, audio.getSoundPlayer(), resources, effectFactory,
            healthFactory, projectileFactory, player);
    }

    private void createPlacementServices(Map map) {

        Logger.info("Game Stage: creating placement services");

        towerPlacement = new TowerPlacement(map, actorGroups, combatActorFactory, healthFactory);
        supportActorPlacement = new SupportActorPlacement(supportActorFactory, supportActorValidatorMap);
        Logger.info("Game Stage: placement services created");
    }

    private void initSupportActorCooldowns(){
        SupportActorCooldown apacheCooldown = createSupportActorCooldown(Apache.COOLDOWN_TIME);
        SupportActorCooldown airStrikeCooldown = createSupportActorCooldown(AirStrike.COOLDOWN_TIME);
        SupportActorCooldown landMineCooldown = createSupportActorCooldown(LandMine.COOLDOWN_TIME);
        SupportActorCooldown supplyDropCooldown = createSupportActorCooldown(SupplyDrop.COOLDOWN_TIME);

        supportActorCooldownMap.put(Apache.class, apacheCooldown);
        supportActorCooldownMap.put(AirStrike.class, airStrikeCooldown);
        supportActorCooldownMap.put(LandMine.class, landMineCooldown);
        supportActorCooldownMap.put(SupplyDrop.class, supplyDropCooldown);

        actorGroups.getCooldownGroup().addActor(apacheCooldown);
        actorGroups.getCooldownGroup().addActor(airStrikeCooldown);
        actorGroups.getCooldownGroup().addActor(landMineCooldown);
        actorGroups.getCooldownGroup().addActor(supplyDropCooldown);
    }

    private SupportActorCooldown createSupportActorCooldown(float cooldownTime){
        SupportActorCooldown cooldown = new SupportActorCooldown(cooldownTime);

        return cooldown;
    }

    private void initSupportActorValidators(){

        supportActorValidatorMap.put(Apache.class, createSupportActorValidator(Apache.COST, supportActorCooldownMap.get(Apache.class)));
        supportActorValidatorMap.put(AirStrike.class, createSupportActorValidator(AirStrike.COST, supportActorCooldownMap.get(AirStrike.class)));
        supportActorValidatorMap.put(LandMine.class, createSupportActorValidator(LandMine.COST, supportActorCooldownMap.get(LandMine.class)));
        supportActorValidatorMap.put(SupplyDrop.class, createSupportActorValidator(SupplyDrop.COST, supportActorCooldownMap.get(SupplyDrop.class)));

    }

    private SupportActorValidator createSupportActorValidator(int cost, SupportActorCooldown cooldown){

        SupportActorValidator validator = new SupportActorValidator(cost, cooldown, player);

        return validator;
    }

    /**
     * Loads the first wave of the level.
     * This is done outside of the constructor
     * so that the GameScreen and GameStageUI are
     * fully constructed before loading the wave.
     */
    public void loadFirstWave() {

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
        this.addActor(getActorGroups().getCooldownGroup());
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
            if (isWaveOver()) {
                waveOver();
            }
        }
    }

    @Override
    public void draw() {
        getBatch().setProjectionMatrix(getCamera().combined);
        mapRenderer.update();
        super.draw();
    }

    @Override
    public void dispose() {

        Logger.info("Game Stage Dispose");
        map.dispose();
        resources.unloadMap(level.getActiveLevel());
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

    private void waveOver() {

        Logger.info("Game Stage: Wave over");

        eventLogger.logEvent(
            new EventLogBuilder(LogEvent.WAVE_COMPLETE)
                .withIntegerParameter(LogParam.COMPLETED_WAVES.getTag(), this.getLevel().getCurrentWave())
        );

        adControllerHelper.incrementEventTriggered();

        int money = (int) (WAVE_OVER_MONEY_MULTIPLIER * (float) level.getCurrentWave()) + WAVE_OVER_MONEY_ADDITION;
        player.giveMoney(money);
        levelStateManager.setState(LevelState.STANDBY);
        player.setWaveCount(player.getWaveCount() + 1);
        if (isLevelComplete()) {
            levelComplete();
        }
        WaveOverCoinEffect waveOverCoinEffect = effectFactory.loadEffect(WaveOverCoinEffect.class, true);
        waveOverCoinEffect.initialize(money);
        resetTowersForNewWave();
        playServices.submitScore(GooglePlayLeaderboard.findByLevelName(level.getActiveLevel()), level.getCurrentWave());
        level.loadNextWave();
    }

    /**
     * Determine if the level is complete
     */
    private boolean isLevelComplete() {

        return (player.getWavesCompleted() == Level.WAVE_LEVEL_WIN_LIMIT);
    }

    private void levelComplete() {

        Logger.info("Game Stage: Level Over");

        eventLogger.logEvent(
            new EventLogBuilder(LogEvent.LEVEL_COMPLETE)
                .withIntegerParameter(LogParam.COMPLETED_WAVES.getTag(), this.getLevel().getCurrentWave())
        );

        uiStateManager.setState(GameUIState.LEVEL_COMPLETED);
        levelStateManager.setState(LevelState.LEVEL_COMPLETED);
        playServices.unlockAchievement(GooglePlayAchievement.findByLevelName(level.getActiveLevel()));
    }

    private void gameOver(){

        Logger.info("Game Stage: game over");

        eventLogger.logEvent(
            new EventLogBuilder(LogEvent.GAME_OVER)
                .withIntegerParameter(LogParam.COMPLETED_WAVES.getTag(), this.getLevel().getCurrentWave())
        );

        levelStateManager.setState(LevelState.GAME_OVER);
        uiStateManager.setState(GameUIState.GAME_OVER);
    }

    private boolean isGameOver(){

        return player.getLives() <= 0;

    }

    private void resetTowersForNewWave() {

        Logger.info("Game Stage: Resetting towers for new wave");
        for (Tower tower : actorGroups.getTowerGroup().getCastedChildren()) {
            tower.waveReset();
        }
    }

    @Override
    public void playerAttributeChange() {

        if (isGameOver() && !levelStateManager.getState().equals(LevelState.GAME_OVER)) {
            gameOver();
        }
    }

    public ActorGroups getActorGroups() {

        return actorGroups;
    }

    public TowerPlacement getTowerPlacement() {

        return towerPlacement;
    }

    public SupportActorPlacement getSupportActorPlacement() {

        return supportActorPlacement;
    }

    public CombatActorFactory getCombatActorFactory(){

        return combatActorFactory;
    }

    public Map getMap(){

        return map;
    }

    public Level getLevel(){
        return level;
    }

    public java.util.Map<Class<? extends SupportActor>, SupportActorValidator> getSupportActorValidatorMap(){
        return supportActorValidatorMap;
    }

    public java.util.Map<Class<? extends SupportActor>, SupportActorCooldown> getSupportActorCooldownMap(){
        return supportActorCooldownMap;
    }

}
