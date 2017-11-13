package com.lastdefenders.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
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
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
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
    private CombatActorPool<TowerTurret> towerTurretPool = new CombatActorPool<>(TowerTurret.class);
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
        EffectFactory effectFactory,
        ProjectileFactory projectileFactory, Player player) {

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

        Logger.info("Combat Actor Factory: loading Tower: " + type);
        Tower tower = null;
        switch (type) {
            case "Rifle":
                tower = (Tower) towerRiflePool.obtain();
                break;
            case "Tank":
                tower = (Tower) towerTankPool.obtain();
                break;
            case "Turret":
                tower = (Tower) towerTurretPool.obtain();
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
        }

        return tower;
    }

    /**
     * Obtains an Enemy from the pool
     *
     * @param type - The type of enemy
     * @return Enemy
     */
    public Enemy loadEnemy(String type) {

        Logger.info("Combat Actor Factory: loading Enemy: " + type);

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
        }

        return enemy;
    }

    public SpawningEnemy loadSpawningEnemy(Enemy enemy, float spawnDelay) {

        SpawningEnemy spawningEnemy = spawningEnemyPool.obtain();
        spawningEnemy.setEnemy(enemy);
        spawningEnemy.setSpawnDelay(spawnDelay);

        return spawningEnemy;
    }

    /**
     * Create a Game Actor
     *
     * @param type - Type of Game Actor
     * @return CombatActor
     */
    private CombatActor createCombatActor(Class<? extends CombatActor> type) {

        Logger.info("Combat Actor Factory: creating combat actor: " + type.getSimpleName());
        CombatActor actor;
        if (type.equals(TowerRifle.class)) {
            TextureRegion rifleRegion = resources.getTexture("tower-rifle");
            actor = new TowerRifle(rifleRegion, towerRiflePool, actorGroups.getEnemyGroup(),
                resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory,
                audio);
        } else if (type.equals(TowerFlameThrower.class)) {
            TextureRegion flameThrowerRegion = resources.getTexture("tower-flame-thrower");
            actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool,
                actorGroups.getEnemyGroup(), resources.getTexture("range"),
                resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerSniper.class)) {
            TextureRegion sniperRegion = resources.getTexture("tower-sniper");
            actor = new TowerSniper(sniperRegion, towerSniperPool, actorGroups.getEnemyGroup(),
                resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory,
                audio);
        } else if (type.equals(TowerMachineGun.class)) {
            TextureRegion machineRegion = resources.getTexture("tower-machine-gun");
            actor = new TowerMachineGun(machineRegion, towerMachinePool,
                actorGroups.getEnemyGroup(), resources.getTexture("range"),
                resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerRocketLauncher.class)) {
            TextureRegion rocketLauncherRegion = resources.getTexture("tower-rocket-launcher");
            actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool,
                actorGroups.getEnemyGroup(), resources.getTexture("range"),
                resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerTank.class)) {
            TextureRegion tankRegion = resources.getTexture("tower-tank-body");
            TextureRegion turretRegion = resources.getTexture("tower-tank-turret");
            actor = new TowerTank(tankRegion, turretRegion, towerTankPool,
                actorGroups.getEnemyGroup(), resources.getTexture("range"),
                resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerTurret.class)) {
            TextureRegion machineRegion = resources.getTexture("tower-turret-turret");
            TextureRegion bagsRegion = resources.getTexture("tower-turret-bags");
            actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool,
                actorGroups.getEnemyGroup(), resources.getTexture("range-turret"),
                resources.getTexture("range-turret-red"), projectileFactory, audio);
        } else if (type.equals(EnemyRifle.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rifle")
                .toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-rifle-stationary");
            actor = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool,
                actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyFlameThrower.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-flame-thrower")
                .toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-flame-thrower-stationary");
            actor = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool,
                actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyHumvee.class)) {
            TextureRegion humveeRegion = resources.getTexture("enemy-humvee");
            actor = new EnemyHumvee(humveeRegion, new TextureRegion[]{humveeRegion},
                enemyHumveePool);
        } else if (type.equals(EnemyMachineGun.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-machine-gun")
                .toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-machine-gun-stationary");
            actor = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool,
                actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyRocketLauncher.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rocket-launcher")
                .toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources
                .getTexture("enemy-rocket-launcher-stationary");
            actor = new EnemyRocketLauncher(stationaryRegion, animatedRegions,
                enemyRocketLauncherPool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemySniper.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-sniper")
                .toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-sniper-stationary");
            actor = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool,
                actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyTank.class)) {
            TextureRegion tankRegion = resources.getTexture("enemy-tank-body");
            TextureRegion turretRegion = resources.getTexture("enemy-tank-turret");
            actor = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion},
                enemyTankPool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else {
            throw new NullPointerException(
                "Combat Actor Factory couldn't create: " + type.getSimpleName());
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

        return actor;
    }

    private SpawningEnemy createSpawningEnemy() {

        Logger.info("CombatActorFactory: creating SpawningEnemy");

        return new SpawningEnemy(spawningEnemyPool);
    }

    public class CombatActorPool<T extends CombatActor> extends Pool<CombatActor> {

        private final Class<? extends CombatActor> type;

        public CombatActorPool(Class<? extends CombatActor> type) {

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
