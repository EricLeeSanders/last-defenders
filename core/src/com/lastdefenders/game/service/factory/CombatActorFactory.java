package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.combat.CombatActor;
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
import com.lastdefenders.game.model.actor.combat.event.EventManagerImpl;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerAttributes;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.combat.tower.TowerTurret;
import com.lastdefenders.game.model.actor.combat.tower.state.TowerStateManager;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class CombatActorFactory {

    private CombatActorPool<TowerRifle> towerRiflePool = new CombatActorPool<>(TowerRifle.class);
    private CombatActorPool<TowerTank> towerTankPool = new CombatActorPool<>(TowerTank.class);
    private CombatActorPool<TowerFlameThrower> towerFlameThrowerPool = new CombatActorPool<>(
        TowerFlameThrower.class);
    private CombatActorPool<TowerHumvee> towerHumveePool = new CombatActorPool<>(TowerHumvee.class);
    private CombatActorPool<TowerSniper> towerSniperPool = new CombatActorPool<>(TowerSniper.class);
    private CombatActorPool<TowerMachineGun> towerMachinePool = new CombatActorPool<>(
        TowerMachineGun.class);
    private CombatActorPool<TowerRocketLauncher> towerRocketLauncherPool = new CombatActorPool<>(
        TowerRocketLauncher.class);

    private CombatActorPool<EnemyRifle> enemyRiflePool = new CombatActorPool<>(EnemyRifle.class);
    private CombatActorPool<EnemyTank> enemyTankPool = new CombatActorPool<>(EnemyTank.class);
    private CombatActorPool<EnemyFlameThrower> enemyFlameThrowerPool = new CombatActorPool<>(
        EnemyFlameThrower.class);
    private CombatActorPool<EnemyMachineGun> enemyMachinePool = new CombatActorPool<>(
        EnemyMachineGun.class);
    private CombatActorPool<EnemyRocketLauncher> enemyRocketLauncherPool = new CombatActorPool<>(
        EnemyRocketLauncher.class);
    private CombatActorPool<EnemySniper> enemySniperPool = new CombatActorPool<>(EnemySniper.class);
    private CombatActorPool<EnemyHumvee> enemyHumveePool = new CombatActorPool<>(EnemyHumvee.class);

    private SpawningEnemyPool spawningEnemyPool = new SpawningEnemyPool();

    private LDAudio audio;
    private ActorGroups actorGroups;
    private Resources resources;
    private EffectFactory effectFactory;
    private ProjectileFactory projectileFactory;
    private Player player;

    public CombatActorFactory(ActorGroups actorGroups, LDAudio audio, Resources resources,
        EffectFactory effectFactory, ProjectileFactory projectileFactory, Player player) {

        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.projectileFactory = projectileFactory;
        this.player = player;

    }

    /**
     * Obtains a tower from the pool
     *
     * @param type - The type of tower
     * @return Tower
     */
    public Tower loadTower(String type) {

        Logger.debug("Combat Actor Factory: loading Tower: " + type);
        Tower tower = null;
        switch (type) {
            case "Rifle":
                tower = (Tower) towerRiflePool.obtain();
                break;
            case "Tank":
                tower = (Tower) towerTankPool.obtain();
                break;
            case "Humvee":
                tower = (Tower) towerHumveePool.obtain();
                break;
            case "Sniper":
                tower = (Tower) towerSniperPool.obtain();
                break;
            case "MachineGun":
                tower = (Tower) towerMachinePool.obtain();
                break;
            case "RocketLauncher":
                tower = (Tower) towerRocketLauncherPool.obtain();
                break;
            case "FlameThrower":
                tower = (Tower) towerFlameThrowerPool.obtain();
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid Tower");
        }

        Logger.debug("CombatActorFactory:" + type + " tower (" + tower.ID +") loaded");

        return tower;
    }

    /**
     * Obtains an Enemy from the pool
     *
     * @param type - The type of enemy
     * @return Enemy
     */
    public Enemy loadEnemy(String type) {

        Logger.debug("Combat Actor Factory: loading Enemy: " + type);

        Enemy enemy = null;
        switch (type) {
            case "Rifle":
                enemy = (Enemy) enemyRiflePool.obtain();
                break;
            case "Tank":
                enemy = (Enemy) enemyTankPool.obtain();
                break;
            case "FlameThrower":
                enemy = (Enemy) enemyFlameThrowerPool.obtain();
                break;
            case "MachineGun":
                enemy = (Enemy) enemyMachinePool.obtain();
                break;
            case "RocketLauncher":
                enemy = (Enemy) enemyRocketLauncherPool.obtain();
                break;
            case "Sniper":
                enemy = (Enemy) enemySniperPool.obtain();
                break;
            case "Humvee":
                enemy = (Enemy) enemyHumveePool.obtain();
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid Enemy");
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
            audio, attributes);

        return  towerSniper;
    }

    private TowerFlameThrower createTowerFlameThrower(){
        TextureRegion flameThrowerRegion = resources.getTexture("tower-flame-thrower");
        TowerAttributes attributes = resources.getTowerAttribute(TowerFlameThrower.class);
        TowerFlameThrower towerFlameThrower = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, audio, attributes);

        return towerFlameThrower;
    }

    private TowerRifle createTowerRifle(){
        TextureRegion rifleRegion = resources.getTexture("tower-rifle");
        TowerAttributes attributes = resources.getTowerAttribute(TowerRifle.class);
        TowerRifle towerRifle = new TowerRifle(rifleRegion, towerRiflePool, actorGroups.getEnemyGroup(),
            resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory,
            audio, attributes);

        return towerRifle;
    }

    private TowerMachineGun createTowerMachineGun(){
        TextureRegion machineRegion = resources.getTexture("tower-machine-gun");
        TowerAttributes attributes = resources.getTowerAttribute(TowerMachineGun.class);
        TowerMachineGun towerMachineGun = new TowerMachineGun(machineRegion, towerMachinePool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, audio, attributes);

        return towerMachineGun;

    }

    private TowerRocketLauncher createTowerRocketLauncher(){
        TextureRegion rocketLauncherRegion = resources.getTexture("tower-rocket-launcher");
        TowerAttributes attributes = resources.getTowerAttribute(TowerRocketLauncher.class);
        TowerRocketLauncher towerRocketLauncher = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, audio, attributes);

        return towerRocketLauncher;
    }

    private TowerTank createTowerTank(){
        TextureRegion tankRegion = resources.getTexture("tower-tank-body");
        TextureRegion turretRegion = resources.getTexture("tower-tank-turret");
        TowerAttributes attributes = resources.getTowerAttribute(TowerTank.class);
        TowerTank towerTank = new TowerTank(tankRegion, turretRegion, towerTankPool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, audio, attributes);

        return towerTank;
    }

    private TowerHumvee createTowerHumvee(){
        TextureRegion turretRegion = resources.getTexture("tower-humvee-turret");
        TextureRegion bodyRegion = resources.getTexture("tower-humvee");
        TowerAttributes attributes = resources.getTowerAttribute(TowerHumvee.class);
        TowerHumvee towerHumvee = new TowerHumvee(bodyRegion, turretRegion, towerHumveePool,
            actorGroups.getEnemyGroup(), resources.getTexture("range"),
            resources.getTexture("range-red"), projectileFactory, audio, attributes);

        return towerHumvee;
    }

    private EnemyRifle createEnemyRifle(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rifle")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-rifle-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyRifle.class);
        EnemyRifle enemyRifle = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool,
            actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemyRifle;
    }

    private EnemyFlameThrower createEnemyFlameThrower(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-flame-thrower")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-flame-thrower-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyFlameThrower.class);
        EnemyFlameThrower enemyFlameThrower = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool,
            actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemyFlameThrower;
    }

    private EnemyHumvee createEnemyHumvee(){
        TextureRegion bodyRegion = resources.getTexture("enemy-humvee");
        TextureRegion turretRegion = resources.getTexture("enemy-humvee-turret");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyHumvee.class);
        EnemyHumvee enemyHumvee = new EnemyHumvee(bodyRegion, turretRegion, new TextureRegion[]{turretRegion},
            enemyHumveePool, actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemyHumvee;
    }

    private EnemyMachineGun createEnemyMachineGun(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-machine-gun")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-machine-gun-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyMachineGun.class);
        EnemyMachineGun enemyMachineGun = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool,
            actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return  enemyMachineGun;
    }

    private EnemyRocketLauncher createEnemyRocketLauncher(){

        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rocket-launcher")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources
            .getTexture("enemy-rocket-launcher-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyRocketLauncher.class);
        EnemyRocketLauncher enemyRocketLauncher = new EnemyRocketLauncher(stationaryRegion, animatedRegions,
            enemyRocketLauncherPool, actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemyRocketLauncher;
    }

    private EnemySniper createEnemySniper(){
        TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-sniper")
            .toArray(TextureRegion.class);
        TextureRegion stationaryRegion = resources.getTexture("enemy-sniper-stationary");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemySniper.class);
        EnemySniper enemySniper = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool,
            actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemySniper;
    }

    private EnemyTank createEnemyTank(){
        TextureRegion tankRegion = resources.getTexture("enemy-tank-body");
        TextureRegion turretRegion = resources.getTexture("enemy-tank-turret");
        EnemyAttributes attributes = resources.getEnemyAttributes(EnemyTank.class);
        EnemyTank enemyTank = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion},
            enemyTankPool, actorGroups.getTowerGroup(), projectileFactory, audio, attributes);

        return enemyTank;
    }

    /**
     * Create an {@link CombatActor}
     *
     * @param type - Type of Game Actor
     * @return CombatActor
     */
    private CombatActor createCombatActor(Class<? extends CombatActor> type) {

        String className = type.getSimpleName();
        Logger.debug("CombatActorFactory: creating combat actor: " + className);
        CombatActor actor;
        switch(className){
            case "TowerRifle":
                actor = createTowerRifle();
                break;
            case "TowerFlameThrower":
                actor = createTowerFlameThrower();
                break;
            case "TowerSniper":
                actor = createTowerSniper();
                break;
            case "TowerMachineGun":
                actor = createTowerMachineGun();
                break;
            case "TowerRocketLauncher":
                actor = createTowerRocketLauncher();
                break;
            case "TowerTank":
                actor = createTowerTank();
                break;
            case "TowerHumvee":
                actor = createTowerHumvee();
                break;
            case "EnemyRifle":
                actor = createEnemyRifle();
                break;
            case "EnemyFlameThrower":
                actor = createEnemyFlameThrower();
                break;
            case "EnemyHumvee":
                actor = createEnemyHumvee();
                break;
            case "EnemyMachineGun":
                actor = createEnemyMachineGun();
                break;
            case "EnemyRocketLauncher":
                actor = createEnemyRocketLauncher();
                break;
            case "EnemySniper":
                actor = createEnemySniper();
                break;
            case "EnemyTank":
                actor = createEnemyTank();
                break;
            default:
                throw new IllegalArgumentException(className + " is not a valid CombatActor");
        }
        if (actor instanceof Tower) {
            TowerStateManager towerStateManager = new TowerStateManager((Tower) actor,
                effectFactory);
            ((Tower) actor).setStateManager(towerStateManager);
        } else {
            EnemyStateManager enemyStateManager = new EnemyStateManager((Enemy) actor,
                effectFactory, player);
            ((Enemy) actor).setStateManager(enemyStateManager);
        }

        EventManager eventManager = new EventManagerImpl(actor, effectFactory);
        actor.setEventManager(eventManager);

        Logger.debug("CombatActorFactory: created actor: " + className + " : " + actor.ID);

        return actor;
    }

    private SpawningEnemy createSpawningEnemy() {

        Logger.debug("CombatActorFactory: creating SpawningEnemy");

        return new SpawningEnemy(spawningEnemyPool);
    }

    public class CombatActorPool<T extends CombatActor> extends Pool<Actor> {

        private final Class<T> type;

        public CombatActorPool(Class<T> type) {

            this.type = type;
        }

        @Override
        protected CombatActor newObject() {

            return createCombatActor(type);
        }

    }

    public class SpawningEnemyPool extends Pool<SpawningEnemy> {

        @Override
        protected SpawningEnemy newObject() {

            return createSpawningEnemy();
        }
    }
}
