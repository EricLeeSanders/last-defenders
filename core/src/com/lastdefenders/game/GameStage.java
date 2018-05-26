package com.lastdefenders.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.PlayerObserver;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.model.actor.effects.label.WaveOverCoinEffect;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.service.actorplacement.AirStrikePlacement;
import com.lastdefenders.game.service.actorplacement.SupplyDropPlacement;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.actorplacement.TowerPlacement;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.HealthFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Game Stage class that contains all of the Actors and Groups. Responsible for
 * creating the MapRenderer and Level.
 *
 * @author Eric
 */
public class GameStage extends Stage implements PlayerObserver {

    private static final int WAVE_OVER_MONEY_MULTIPLIER = 100;
    private LevelStateManager levelStateManager;
    private GameUIStateManager uiStateManager;
    private Level level;
    private Player player;
    private LevelName levelName;
    private ActorGroups actorGroups;
    private Map map;
    private MapRenderer mapRenderer;
    private Resources resources;
    private TowerPlacement towerPlacement;
    private SupportActorPlacement supportActorPlacement;
    private AirStrikePlacement airStrikePlacement;
    private SupplyDropPlacement supplyDropPlacement;
    private CombatActorFactory combatActorFactory;
    private HealthFactory healthFactory;
    private SupportActorFactory supportActorFactory;
    private EffectFactory effectFactory;

    public GameStage(LevelName levelName, Player player, ActorGroups actorGroups, LDAudio audio,
        LevelStateManager levelStateManager, GameUIStateManager uiStateManager,
        Viewport viewport, Resources resources, SpriteBatch spriteBatch) {

        super(viewport, spriteBatch);
        this.player = player;
        this.actorGroups = actorGroups;
        this.levelStateManager = levelStateManager;
        this.uiStateManager = uiStateManager;
        this.levelName = levelName;
        this.resources = resources;
        TiledMap tiledMap = resources.getMap(levelName);
        map = new Map(tiledMap, resources.getTiledMapScale());
        map.init();
        createGroups();
        createFactories(audio);
        createPlacementServices(map);
        mapRenderer = new MapRenderer(tiledMap, resources.getTiledMapScale(), getCamera(), getBatch());
        FileWaveLoader fileWaveLoader = new FileWaveLoader(combatActorFactory, map);
        DynamicWaveLoader dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);
        level = new Level(levelName, getActorGroups(), healthFactory, fileWaveLoader,
            dynamicWaveLoader);
        player.attachObserver(this);

    }

    private void createFactories(LDAudio audio) {

        effectFactory = new EffectFactory(actorGroups, resources);
        healthFactory = new HealthFactory(actorGroups, resources);
        ProjectileFactory projectileFactory = new ProjectileFactory(actorGroups, audio, resources);
        supportActorFactory = new SupportActorFactory(actorGroups, audio, resources, effectFactory,
            projectileFactory);
        combatActorFactory = new CombatActorFactory(actorGroups, audio, resources, effectFactory,
            projectileFactory, player);
    }

    private void createPlacementServices(Map map) {

        Logger.info("Game Stage: creating placement services");
        towerPlacement = new TowerPlacement(map, actorGroups, combatActorFactory, healthFactory);
        supportActorPlacement = new SupportActorPlacement(supportActorFactory);
        airStrikePlacement = new AirStrikePlacement(supportActorFactory);
        supplyDropPlacement = new SupplyDropPlacement(supportActorFactory);
        Logger.info("Game Stage: placement services created");
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
        resources.unloadMap(levelName);
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
        int money = (int) (WAVE_OVER_MONEY_MULTIPLIER * (float) level.getCurrentWave());
        player.giveMoney(money);
        levelStateManager.setState(LevelState.STANDBY);
        player.setWaveCount(player.getWaveCount() + 1);
        if (isLevelCompleted()) {
            levelComleted();
        }
        WaveOverCoinEffect waveOverCoinEffect = effectFactory.loadEffect(WaveOverCoinEffect.class, true);
        waveOverCoinEffect.initialize(money);
        level.loadNextWave(); //load the next wave
        healTowers();
    }

    /**
     * Determine if the level is completed
     */
    private boolean isLevelCompleted() {

        if (player.getWavesCompleted() == Level.MAX_WAVES) {
            Logger.info("Game Stage: Level Over");
            return true;
        }
        return false;
    }

    private void levelComleted() {

        uiStateManager.setState(GameUIState.LEVEL_COMPLETED);
        levelStateManager.setState(LevelState.LEVEL_COMPLETED);
    }

    private void healTowers() {

        Logger.info("Game Stage: healing towers");
        for (Actor tower : actorGroups.getTowerGroup().getChildren()) {
            if (tower instanceof Tower) {
                if (((Tower) tower).isActive() && ((Tower) tower).getHealthPercent() < 1) {
                    TowerHealEffect effect = effectFactory.loadEffect(TowerHealEffect.class, true);
                    effect.initialize((Tower) tower);

                    ((Tower) tower).heal();
                }
            }
        }
    }

    @Override
    public void playerAttributeChange() {

        if (player.getLives() <= 0 && !levelStateManager.getState()
            .equals(LevelState.GAME_OVER)) { // end game
            Logger.info("Game Stage: game over");
            levelStateManager.setState(LevelState.GAME_OVER);
            uiStateManager.setState(GameUIState.GAME_OVER);
        }
    }

    private ActorGroups getActorGroups() {

        return actorGroups;
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

    public Map getMap(){

        return map;
    }
}
