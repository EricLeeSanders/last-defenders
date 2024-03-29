package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyAttributes;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyMachineGun;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemySniper;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerAttributes;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.combat.tower.state.TowerStateManager;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class CombatActorFactory {

    private TowerPool<TowerRifle> towerRiflePool = new TowerPool<>(TowerRifle.class);
    private TowerPool<TowerTank> towerTankPool = new TowerPool<>(TowerTank.class);
    private TowerPool<TowerFlameThrower> towerFlameThrowerPool = new TowerPool<>(
        TowerFlameThrower.class);
    private TowerPool<TowerHumvee> towerHumveePool = new TowerPool<>(TowerHumvee.class);
    private TowerPool<TowerSniper> towerSniperPool = new TowerPool<>(TowerSniper.class);
    private TowerPool<TowerMachineGun> towerMachinePool = new TowerPool<>(
        TowerMachineGun.class);
    private TowerPool<TowerRocketLauncher> towerRocketLauncherPool = new TowerPool<>(
        TowerRocketLauncher.class);

    private EnemyPool<EnemyRifle> enemyRiflePool = new EnemyPool<>(EnemyRifle.class);
    private EnemyPool<EnemyTank> enemyTankPool = new EnemyPool<>(EnemyTank.class);
    private EnemyPool<EnemyFlameThrower> enemyFlameThrowerPool = new EnemyPool<>(
        EnemyFlameThrower.class);
    private EnemyPool<EnemyMachineGun> enemyMachinePool = new EnemyPool<>(
        EnemyMachineGun.class);
    private EnemyPool<EnemyRocketLauncher> enemyRocketLauncherPool = new EnemyPool<>(
        EnemyRocketLauncher.class);
    private EnemyPool<EnemySniper> enemySniperPool = new EnemyPool<>(EnemySniper.class);
    private EnemyPool<EnemyHumvee> enemyHumveePool = new EnemyPool<>(EnemyHumvee.class);

    private SpawningEnemyPool spawningEnemyPool = new SpawningEnemyPool();

    private SoundPlayer soundPlayer;
    private ActorGroups actorGroups;
    private Resources resources;
    private EffectFactory effectFactory;
    private HealthFactory healthFactory;
    private ProjectileFactory projectileFactory;
    private Player player;

    public CombatActorFactory(ActorGroups actorGroups, SoundPlayer soundPlayer, Resources resources,
        EffectFactory effectFactory, HealthFactory healthFactory, ProjectileFactory projectileFactory, Player player) {

        this.actorGroups = actorGroups;
        this.soundPlayer = soundPlayer;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.projectileFactory = projectileFactory;
        this.player = player;
        this.healthFactory = healthFactory;

    }

    /**
     * Obtains a tower from the pool
     *
     * @param type - The type of tower
     * @param addToGroup - Add the Tower to the Tower Group
     * @return Tower
     */
    public <T extends Tower> T loadTower(String type, boolean addToGroup) {

        Logger.info("Combat Actor Factory: loading tower: " + type);

        TowerPool<? extends Tower> towerPool = null;

        switch (type) {
            case "Rifle":
                towerPool = towerRiflePool;
                break;
            case "Tank":
                towerPool = towerTankPool;
                break;
            case "Humvee":
                towerPool = towerHumveePool;
                break;
            case "Sniper":
                towerPool = towerSniperPool;
                break;
            case "MachineGun":
                towerPool = towerMachinePool;
                break;
            case "RocketLauncher":
                towerPool = towerRocketLauncherPool;
                break;
            case "FlameThrower":
                towerPool = towerFlameThrowerPool;
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid Tower");
        }

        @SuppressWarnings("unchecked")
        T tower = (T) towerPool.obtain();

        if(addToGroup){
            actorGroups.getTowerGroup().addActor(tower);
        }
        Logger.debug("CombatActorFactory:" + type + " tower (" + tower.ID +") loaded");

        return tower;
    }

    /**
     * Obtains an Enemy from the pool
     *
     * @param type - The type of enemy
     * @param addToGroup - Add the Enemy to the Enemy Group
     * @return Enemy
     */
    public <T extends Enemy> T  loadEnemy(String type, boolean addToGroup) {

        Logger.info("Combat Actor Factory: loading enemy: " + type);

        EnemyPool<? extends Enemy> enemyPool = null;

        switch (type) {
            case "Rifle":
                enemyPool = enemyRiflePool;
                break;
            case "Tank":
                enemyPool = enemyTankPool;
                break;
            case "FlameThrower":
                enemyPool = enemyFlameThrowerPool;
                break;
            case "MachineGun":
                enemyPool = enemyMachinePool;
                break;
            case "RocketLauncher":
                enemyPool = enemyRocketLauncherPool;
                break;
            case "Sniper":
                enemyPool = enemySniperPool;
                break;
            case "Humvee":
                enemyPool = enemyHumveePool;
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid Enemy");
        }

        @SuppressWarnings("unchecked")
        T enemy = (T) enemyPool.obtain();

        if(addToGroup){
            actorGroups.getEnemyGroup().addActor(enemy);
        }
        Logger.debug("CombatActorFactory:" + type + " enemy (" + enemy.ID +") loaded");

        return enemy;
    }

    public SpawningEnemy loadSpawningEnemy(Enemy enemy, float spawnDelay) {

        SpawningEnemy spawningEnemy = spawningEnemyPool.obtain();
        spawningEnemy.setEnemy(enemy);
        spawningEnemy.setSpawnDelay(spawnDelay);

        return spawningEnemy;
    }


    private TowerSniper createTowerSniper(){
        TextureRegion sniperRegion = resources.getTexture("tower-sniper");
        TowerAttributes attributes = resources.getTowerAttribute(TowerSniper.class);
        TowerSniper towerSniper = new TowerSniper(sniperRegion, towerSniperPool, actorGroups.getEnemyGroup(),
            resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory,
            soundPlayer, attributes);

        return  towerSniper;
    }

    private TowerFlameThrower createTowerFlameThrower(){
        TextureRegion flameThrowerRegion = resources.getTexture("tower-flame-thrower");
        TowerAttributes attributes = resources.getTowerAttribute(TowerFlameThrower.class);
        TowerFlameThrower towerFlameThrower = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, soundPlayer, attributes);

        return towerFlameThrower;
    }

    private TowerRifle createTowerRifle(){
        TextureRegion rifleRegion = resources.getTexture("tower-rifle");
        TowerAttributes attributes = resources.getTowerAttribute(TowerRifle.class);
        TowerRifle towerRifle = new TowerRifle(rifleRegion, towerRiflePool, actorGroups.getEnemyGroup(),
            resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory,
            soundPlayer, attributes);

        return towerRifle;
    }

    private TowerMachineGun createTowerMachineGun(){
        TextureRegion machineRegion = resources.getTexture("tower-machine-gun");
        TowerAttributes attributes = resources.getTowerAttribute(TowerMachineGun.class);
        TowerMachineGun towerMachineGun = new TowerMachineGun(machineRegion, towerMachinePool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, soundPlayer, attributes);

        return towerMachineGun;

    }

    private TowerRocketLauncher createTowerRocketLauncher(){
        TextureRegion rocketLauncherRegion = resources.getTexture("tower-rocket-launcher");
        TowerAttributes attributes = resources.getTowerAttribute(TowerRocketLauncher.class);
        TowerRocketLauncher towerRocketLauncher = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, soundPlayer, attributes);

        return towerRocketLauncher;
    }

    private TowerTank createTowerTank(){
        TextureRegion tankRegion = resources.getTexture("tower-tank-body");
        TextureRegion turretRegion = resources.getTexture("tower-tank-turret");
        TowerAttributes attributes = resources.getTowerAttribute(TowerTank.class);
        TowerTank towerTank = new TowerTank(tankRegion, turretRegion, towerTankPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, soundPlayer, attributes);

        return towerTank;
    }

    private TowerHumvee createTowerHumvee(){
        TextureRegion turretRegion = resources.getTexture("tower-humvee-turret");
        TextureRegion bodyRegion = resources.getTexture("tower-humvee");
        TowerAttributes attributes = resources.getTowerAttribute(TowerHumvee.class);
        TowerHumvee towerHumvee = new TowerHumvee(bodyRegion, turretRegion, towerHumveePool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, soundPlayer, attributes);

        return towerHumvee;
    }

    private EnemyRifle createEnemyRifle(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rifle")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-rifle-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyRifle.class);
        EnemyRifle enemyRifle = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool,
            actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemyRifle;
    }

    private EnemyFlameThrower createEnemyFlameThrower(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-flame-thrower")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-flame-thrower-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyFlameThrower.class);
        EnemyFlameThrower enemyFlameThrower = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool,
            actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemyFlameThrower;
    }

    private EnemyHumvee createEnemyHumvee(){
        TextureRegion bodyRegion = resources.getTexture("enemy-humvee");
        TextureRegion turretRegion = resources.getTexture("enemy-humvee-turret");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyHumvee.class);
        EnemyHumvee enemyHumvee = new EnemyHumvee(bodyRegion, turretRegion, new TextureRegion[]{turretRegion},
            enemyHumveePool, actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemyHumvee;
    }

    private EnemyMachineGun createEnemyMachineGun(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-machine-gun")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-machine-gun-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyMachineGun.class);
        EnemyMachineGun enemyMachineGun = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool,
            actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return  enemyMachineGun;
    }

    private EnemyRocketLauncher createEnemyRocketLauncher(){

        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rocket-launcher")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources
            .getTexture("enemy-rocket-launcher-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyRocketLauncher.class);
        EnemyRocketLauncher enemyRocketLauncher = new EnemyRocketLauncher(stationaryRegion, animatedRegions,
            enemyRocketLauncherPool, actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemyRocketLauncher;
    }

    private EnemySniper createEnemySniper(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-sniper")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-sniper-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemySniper.class);
        EnemySniper enemySniper = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool,
            actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemySniper;
    }

    private EnemyTank createEnemyTank(){
        TextureRegion tankRegion = resources.getTexture("enemy-tank-body");
        TextureRegion turretRegion = resources.getTexture("enemy-tank-turret");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyTank.class);
        EnemyTank enemyTank = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion},
            enemyTankPool, actorGroups.getTowerGroup(), projectileFactory, soundPlayer, attributes);

        return enemyTank;
    }

    /**
     * Create an {@link Enemy}
     *
     * @param type - Type of Enemy
     * @return Enemy
     */
    private Enemy createEnemy(Class<? extends Enemy> type){

        String className = type.getSimpleName();
        Logger.debug("CombatActorFactory: creating enemy: " + className);
        Enemy enemy;

        switch(className){
            case "EnemyRifle":
                enemy = createEnemyRifle();
                break;
            case "EnemyFlameThrower":
                enemy = createEnemyFlameThrower();
                break;
            case "EnemyHumvee":
                enemy = createEnemyHumvee();
                break;
            case "EnemyMachineGun":
                enemy = createEnemyMachineGun();
                break;
            case "EnemyRocketLauncher":
                enemy = createEnemyRocketLauncher();
                break;
            case "EnemySniper":
                enemy = createEnemySniper();
                break;
            case "EnemyTank":
                enemy = createEnemyTank();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid CombatActor");
        }

        EnemyStateManager enemyStateManager = new EnemyStateManager(enemy,
            effectFactory, player);
        enemy.setStateManager(enemyStateManager);

        HealthBar healthBar = healthFactory.createHealthBar(enemy);
        actorGroups.getHealthGroup().addActor(healthBar);

        Logger.debug("CombatActorFactory: created enemy: " + className + " : " + enemy.ID);

        return enemy;
    }

    /**
     * Create an {@link Tower}
     *
     * @param type - Type of Tower
     * @return Tower
     */
    private Tower createTower(Class<? extends Tower> type) {

        String className = type.getSimpleName();
        Logger.debug("CombatActorFactory: creating tower: " + className);
        Tower tower;


        switch(className){
            case "TowerRifle":
                tower = createTowerRifle();
                break;
            case "TowerFlameThrower":
                tower = createTowerFlameThrower();
                break;
            case "TowerSniper":
                tower = createTowerSniper();
                break;
            case "TowerMachineGun":
                tower = createTowerMachineGun();
                break;
            case "TowerRocketLauncher":
                tower = createTowerRocketLauncher();
                break;
            case "TowerTank":
                tower = createTowerTank();
                break;
            case "TowerHumvee":
                tower = createTowerHumvee();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid tower");
        }

        TowerStateManager towerStateManager = new TowerStateManager(tower,
            effectFactory);
        tower.setStateManager(towerStateManager);

        HealthBar healthBar = healthFactory.createHealthBar(tower);
        actorGroups.getHealthGroup().addActor(healthBar);

        Logger.debug("CombatActorFactory: created actor: " + className + " : " + tower.ID);

        return tower;
    }

    private SpawningEnemy createSpawningEnemy() {

        Logger.debug("CombatActorFactory: creating SpawningEnemy");

        return new SpawningEnemy(spawningEnemyPool);
    }

    public class EnemyPool<T extends Enemy> extends Pool<Actor> {

        private final Class<T> type;

        public EnemyPool(Class<T> type) {

            this.type = type;
        }

        @Override
        protected Enemy newObject() {

            return createEnemy(type);
        }

    }

    public class TowerPool<T extends Tower> extends Pool<Actor> {

        private final Class<T> type;

        public TowerPool(Class<T> type) {

            this.type = type;
        }

        @Override
        protected Tower newObject() {

            return createTower(type);
        }

    }

    public class SpawningEnemyPool extends Pool<SpawningEnemy> {

        @Override
        protected SpawningEnemy newObject() {

            return createSpawningEnemy();
        }
    }
}
