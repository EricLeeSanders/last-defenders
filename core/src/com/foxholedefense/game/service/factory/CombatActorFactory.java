package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyHumvee;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyMachineGun;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRifle;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.foxholedefense.game.model.actor.combat.enemy.EnemySniper;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyTank;
import com.foxholedefense.game.model.actor.combat.enemy.IEnemyObserver;
import com.foxholedefense.game.model.actor.combat.tower.ITowerObserver;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.TowerFlameThrower;
import com.foxholedefense.game.model.actor.combat.tower.TowerMachineGun;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.model.actor.combat.tower.TowerRocketLauncher;
import com.foxholedefense.game.model.actor.combat.tower.TowerSniper;
import com.foxholedefense.game.model.actor.combat.tower.TowerTank;
import com.foxholedefense.game.model.actor.combat.tower.TowerTurret;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 3/31/2017.
 */

public class CombatActorFactory {
    private CombatActorPool<CombatActor> towerRiflePool = new CombatActorPool<CombatActor>(TowerRifle.class);
    private CombatActorPool<CombatActor> towerTankPool = new CombatActorPool<CombatActor>(TowerTank.class);
    private CombatActorPool<CombatActor> towerFlameThrowerPool = new CombatActorPool<CombatActor>(TowerFlameThrower.class);
    private CombatActorPool<CombatActor> towerTurretPool = new CombatActorPool<CombatActor>(TowerTurret.class);
    private CombatActorPool<CombatActor> towerSniperPool = new CombatActorPool<CombatActor>(TowerSniper.class);
    private CombatActorPool<CombatActor> towerMachinePool = new CombatActorPool<CombatActor>(TowerMachineGun.class);
    private CombatActorPool<CombatActor> towerRocketLauncherPool = new CombatActorPool<CombatActor>(TowerRocketLauncher.class);
    private CombatActorPool<CombatActor> enemyRiflePool = new CombatActorPool<CombatActor>(EnemyRifle.class);
    private CombatActorPool<CombatActor> enemyTankPool = new CombatActorPool<CombatActor>(EnemyTank.class);
    private CombatActorPool<CombatActor> enemyFlameThrowerPool = new CombatActorPool<CombatActor>(EnemyFlameThrower.class);
    private CombatActorPool<CombatActor> enemyMachinePool = new CombatActorPool<CombatActor>(EnemyMachineGun.class);
    private CombatActorPool<CombatActor> enemyRocketLauncherPool = new CombatActorPool<CombatActor>(EnemyRocketLauncher.class);
    private CombatActorPool<CombatActor> enemySniperPool = new CombatActorPool<CombatActor>(EnemySniper.class);
    private CombatActorPool<CombatActor> enemyHumveePool = new CombatActorPool<CombatActor>(EnemyHumvee.class);

    private SnapshotArray<ICombatActorObserver> combatActorObservers = new SnapshotArray<ICombatActorObserver>();
    private SnapshotArray<ITowerObserver> towerObservers = new SnapshotArray<ITowerObserver>();
    private SnapshotArray<IEnemyObserver> enemyObservers = new SnapshotArray<IEnemyObserver>();

    private FHDAudio audio;
    private ActorGroups actorGroups;
    private Resources resources;
    private EffectFactory effectFactory;
    private HealthFactory healthFactory;
    private ProjectileFactory projectileFactory;

    public CombatActorFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources, EffectFactory effectFactory, HealthFactory healthFactory, ProjectileFactory projectileFactory){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.healthFactory = healthFactory;
        this.projectileFactory = projectileFactory;

    }


    public void attachCombatObserver(ICombatActorObserver observer){
        combatActorObservers.add(observer);
    }

    public void attachTowerObserver(ITowerObserver observer) {
        towerObservers.add(observer);
    }

    public void attachEnemyObserver(IEnemyObserver observer) {
        enemyObservers.add(observer);
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
        if (type.equals("Rifle")) {
            tower = (Tower) towerRiflePool.obtain();
        } else if (type.equals("Tank")) {
            tower = (Tower) towerTankPool.obtain();
        } else if (type.equals("Turret")) {
            tower = (Tower) towerTurretPool.obtain();
        } else if (type.equals("Sniper")) {
            tower = (Tower) towerSniperPool.obtain();
        } else if (type.equals("MachineGun")) {
            tower = (Tower) towerMachinePool.obtain();
        } else if (type.equals("RocketLauncher")) {
            tower = (Tower) towerRocketLauncherPool.obtain();
        } else if (type.equals("FlameThrower")) {
            tower = (Tower) towerFlameThrowerPool.obtain();
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
        if (type.equals("EnemyRifle")) {
            enemy = (Enemy) enemyRiflePool.obtain();
        } else if (type.equals("EnemyTank")) {
            enemy = (Enemy) enemyTankPool.obtain();
        } else if (type.equals("EnemyFlameThrower")) {
            enemy = (Enemy) enemyFlameThrowerPool.obtain();
        } else if (type.equals("EnemyMachineGun")) {
            enemy = (Enemy) enemyMachinePool.obtain();
        } else if (type.equals("EnemyRocketLauncher")) {
            enemy = (Enemy) enemyRocketLauncherPool.obtain();
        } else if (type.equals("EnemySniper")) {
            enemy = (Enemy) enemySniperPool.obtain();
        } else if (type.equals("EnemyHumvee")) {
            enemy = (Enemy) enemyHumveePool.obtain();
        }

        return enemy;
    }

    /**
     * Create a Game Actor
     *
     * @param type - Type of Game Actor
     * @return CombatActor
     */
    protected CombatActor createCombatActor(Class<? extends CombatActor> type) {

        Logger.info("Combat Actor Factory: creating combat actor: " + type.getSimpleName());
        CombatActor actor = null;
        if (type.equals(TowerRifle.class)) {
            TextureRegion rifleRegion = resources.getTexture("tower-rifle");
            actor = new TowerRifle(rifleRegion, towerRiflePool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerFlameThrower.class)) {
            TextureRegion flameThrowerRegion = resources.getTexture("tower-flame-thrower");
            actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerSniper.class)) {
            TextureRegion sniperRegion = resources.getTexture("tower-sniper");
            actor = new TowerSniper(sniperRegion, towerSniperPool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerMachineGun.class)) {
            TextureRegion machineRegion = resources.getTexture("tower-machine-gun");
            actor = new TowerMachineGun(machineRegion, towerMachinePool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerRocketLauncher.class)) {
            TextureRegion rocketLauncherRegion = resources.getTexture("tower-rocket-launcher");
            actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory, audio);
        } else if (type.equals(TowerTank.class)) {
            TextureRegion tankRegion = resources.getTexture("tower-tank-body");
            TextureRegion turretRegion = resources.getTexture("tower-tank-turret");
            actor = new TowerTank(tankRegion, turretRegion, towerTankPool, actorGroups.getEnemyGroup(), resources.getTexture("range"), resources.getTexture("range-red"), projectileFactory);
        } else if (type.equals(TowerTurret.class)) {
            TextureRegion machineRegion = resources.getTexture("tower-turret-turret");
            TextureRegion bagsRegion = resources.getTexture("tower-turret-bags");
            actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, actorGroups.getEnemyGroup(), resources.getTexture("range-turret"), resources.getTexture("range-red-turret"), projectileFactory, audio);
        } else if (type.equals(EnemyRifle.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rifle").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-rifle-stationary");
            actor = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyFlameThrower.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-flame-thrower").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-flame-thrower-stationary");
            actor = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyHumvee.class)) {
            TextureRegion humveeRegion = resources.getTexture("enemy-humvee");
            actor = new EnemyHumvee(humveeRegion, new TextureRegion[]{humveeRegion}, enemyHumveePool);
        } else if (type.equals(EnemyMachineGun.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-machine-gun").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-machine-gun-stationary");
            actor = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyRocketLauncher.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-rocket-launcher").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-rocket-launcher-stationary");
            actor = new EnemyRocketLauncher(stationaryRegion, animatedRegions, enemyRocketLauncherPool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemySniper.class)) {
            TextureRegion[] animatedRegions = resources.getAtlasRegion("enemy-sniper").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = resources.getTexture("enemy-sniper-stationary");
            actor = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool, actorGroups.getTowerGroup(), projectileFactory, audio);
        } else if (type.equals(EnemyTank.class)) {
            TextureRegion tankRegion = resources.getTexture("enemy-tank-body");
            TextureRegion turretRegion = resources.getTexture("enemy-tank-turret");
            actor = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion}, enemyTankPool, actorGroups.getTowerGroup(), projectileFactory);
        } else {
            throw new NullPointerException("Combat Actor Factory couldn't create: " + type.getSimpleName());
        }
        if(actor instanceof Tower) {
            ((Tower)actor).attachAllTower(towerObservers);
        } else if(actor instanceof Enemy) {
            ((Enemy)actor).attachAllEnemy(enemyObservers);
        }
        actor.attachAllCombatActor(combatActorObservers);

        return actor;
    }

    public class CombatActorPool<T extends CombatActor> extends Pool<CombatActor> {
        private final Class<? extends CombatActor> type;
        public CombatActorPool(Class<? extends CombatActor> type){
            this.type = type;
        }

        @Override
        protected CombatActor newObject() {
            return createCombatActor(type);
        }

    }

}
