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

    private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
    private Map<String, Array<TextureAtlas.AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<TextureAtlas.AtlasRegion>>();
    public CombatActorFactory(ActorGroups actorGroups, FHDAudio audio, Resources resources, EffectFactory effectFactory, HealthFactory healthFactory, ProjectileFactory projectileFactory){
        this.actorGroups = actorGroups;
        this.audio = audio;
        this.resources = resources;
        this.effectFactory = effectFactory;
        this.healthFactory = healthFactory;
        this.projectileFactory = projectileFactory;
        initTextures(resources.getAsset(Resources.ACTOR_ATLAS, TextureAtlas.class));

    }


    private void initTextures(TextureAtlas actorAtlas){

        Logger.info("Combat Actor Factory: initializing textures");

        loadedTextures.put("range-red", actorAtlas.findRegion("range-red"));
        loadedTextures.put("range", actorAtlas.findRegion("range"));
        loadedTextures.put("range-red-turret", actorAtlas.findRegion("range-red-turret"));
        loadedTextures.put("range-turret", actorAtlas.findRegion("range-turret"));
        loadedTextures.put("range-black", actorAtlas.findRegion("range-black"));
        loadedTextures.put("airstrike", actorAtlas.findRegion("airstrike"));
        loadedTextures.put("bullet", actorAtlas.findRegion("bullet"));
        loadedTextures.put("healthbar-armor", actorAtlas.findRegion("healthbar-armor"));
        loadedTextures.put("healthbar-bg", actorAtlas.findRegion("healthbar-bg"));
        loadedTextures.put("healthbar-life", actorAtlas.findRegion("healthbar-life"));
        loadedTextures.put("humvee", actorAtlas.findRegion("humvee"));
        loadedTextures.put("landmine", actorAtlas.findRegion("landmine"));
        loadedTextures.put("tower-rifle", actorAtlas.findRegion("tower-rifle"));
        loadedTextures.put("tower-machine-gun", actorAtlas.findRegion("tower-machine-gun"));
        loadedTextures.put("tower-sniper", actorAtlas.findRegion("tower-sniper"));
        loadedTextures.put("tower-flame-thrower", actorAtlas.findRegion("tower-flame-thrower"));
        loadedTextures.put("tower-rocket-launcher", actorAtlas.findRegion("tower-rocket-launcher"));
        loadedTextures.put("tower-turret-turret", actorAtlas.findRegion("tower-turret-turret"));
        loadedTextures.put("tower-turret-bags", actorAtlas.findRegion("tower-turret-bags"));
        loadedTextures.put("tower-tank-body", actorAtlas.findRegion("tower-tank-body"));
        loadedTextures.put("tower-tank-turret", actorAtlas.findRegion("tower-tank-turret"));
        loadedTextures.put("enemy-rifle-stationary", actorAtlas.findRegion("enemy-rifle-stationary"));
        loadedTextures.put("enemy-machine-gun-stationary", actorAtlas.findRegion("enemy-machine-gun-stationary"));
        loadedTextures.put("enemy-sniper-stationary", actorAtlas.findRegion("enemy-sniper-stationary"));
        loadedTextures.put("enemy-flame-thrower-stationary", actorAtlas.findRegion("enemy-flame-thrower-stationary"));
        loadedTextures.put("enemy-rocket-launcher-stationary", actorAtlas.findRegion("enemy-rocket-launcher-stationary"));
        loadedTextures.put("enemy-sprinter-stationary", actorAtlas.findRegion("enemy-sprinter-stationary"));
        loadedTextures.put("enemy-tank-body", actorAtlas.findRegion("enemy-tank-body"));
        loadedTextures.put("enemy-tank-turret", actorAtlas.findRegion("enemy-tank-turret"));
        loadedTextures.put("enemy-humvee", actorAtlas.findRegion("enemy-humvee"));
        loadedTextures.put("supply-drop", actorAtlas.findRegion("supply-drop"));
        loadedTextures.put("supply-drop-crate", actorAtlas.findRegion("supply-drop-crate"));
        loadedTextures.put("apache-stationary", actorAtlas.findRegion("apache",1));
        loadedTextures.put("shield", actorAtlas.findRegion("shield"));

        loadedAtlasRegions.put("explosion", actorAtlas.findRegions("explosion"));
        loadedAtlasRegions.put("flame", actorAtlas.findRegions("flame"));
        loadedAtlasRegions.put("blood-splatter", actorAtlas.findRegions("blood-splatter"));
        loadedAtlasRegions.put("smoke-ring", actorAtlas.findRegions("smoke-ring"));
        loadedAtlasRegions.put("enemy-rifle", actorAtlas.findRegions("enemy-rifle"));
        loadedAtlasRegions.put("enemy-flame-thrower", actorAtlas.findRegions("enemy-flame-thrower"));
        loadedAtlasRegions.put("enemy-sniper", actorAtlas.findRegions("enemy-sniper"));
        loadedAtlasRegions.put("enemy-machine-gun", actorAtlas.findRegions("enemy-machine-gun"));
        loadedAtlasRegions.put("enemy-rocket-launcher", actorAtlas.findRegions("enemy-rocket-launcher"));
        loadedAtlasRegions.put("enemy-sprinter", actorAtlas.findRegions("enemy-sprinter"));
        loadedAtlasRegions.put("apache", actorAtlas.findRegions("apache"));
        loadedAtlasRegions.put("shield-destroyed", actorAtlas.findRegions("shield-destroyed"));

        Logger.info("Combat Actor Factory: textures initialized");
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
            TextureRegion rifleRegion = loadedTextures.get("tower-rifle");
            actor = new TowerRifle(rifleRegion, towerRiflePool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory, audio);
        } else if (type.equals(TowerFlameThrower.class)) {
            TextureRegion flameThrowerRegion = loadedTextures.get("tower-flame-thrower");
            actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory, audio);
        } else if (type.equals(TowerSniper.class)) {
            TextureRegion sniperRegion = loadedTextures.get("tower-sniper");
            actor = new TowerSniper(sniperRegion, towerSniperPool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory, audio);
        } else if (type.equals(TowerMachineGun.class)) {
            TextureRegion machineRegion = loadedTextures.get("tower-machine-gun");
            actor = new TowerMachineGun(machineRegion, towerMachinePool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory, audio);
        } else if (type.equals(TowerRocketLauncher.class)) {
            TextureRegion rocketLauncherRegion = loadedTextures.get("tower-rocket-launcher");
            actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory, audio);
        } else if (type.equals(TowerTank.class)) {
            TextureRegion tankRegion = loadedTextures.get("tower-tank-body");
            TextureRegion turretRegion = loadedTextures.get("tower-tank-turret");
            actor = new TowerTank(tankRegion, turretRegion, towerTankPool, actorGroups.getEnemyGroup(), loadedTextures.get("range"), loadedTextures.get("range-red"), effectFactory, projectileFactory);
        } else if (type.equals(TowerTurret.class)) {
            TextureRegion machineRegion = loadedTextures.get("tower-turret-turret");
            TextureRegion bagsRegion = loadedTextures.get("tower-turret-bags");
            actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, actorGroups.getEnemyGroup(), loadedTextures.get("range-turret"), loadedTextures.get("range-red-turret"), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemyRifle.class)) {
            TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-rifle").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = loadedTextures.get("enemy-rifle-stationary");
            actor = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool, actorGroups.getTowerGroup(), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemyFlameThrower.class)) {
            TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-flame-thrower").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = loadedTextures.get("enemy-flame-thrower-stationary");
            actor = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool, actorGroups.getTowerGroup(), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemyHumvee.class)) {
            TextureRegion humveeRegion = loadedTextures.get("enemy-humvee");
            actor = new EnemyHumvee(humveeRegion, new TextureRegion[]{humveeRegion}, enemyHumveePool, effectFactory);
        } else if (type.equals(EnemyMachineGun.class)) {
            TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-machine-gun").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = loadedTextures.get("enemy-machine-gun-stationary");
            actor = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool, actorGroups.getTowerGroup(), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemyRocketLauncher.class)) {
            TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-rocket-launcher").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = loadedTextures.get("enemy-rocket-launcher-stationary");
            actor = new EnemyRocketLauncher(stationaryRegion, animatedRegions, enemyRocketLauncherPool, actorGroups.getTowerGroup(), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemySniper.class)) {
            TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-sniper").toArray(TextureRegion.class);
            TextureRegion stationaryRegion = loadedTextures.get("enemy-sniper-stationary");
            actor = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool, actorGroups.getTowerGroup(), effectFactory, projectileFactory, audio);
        } else if (type.equals(EnemyTank.class)) {
            TextureRegion tankRegion = loadedTextures.get("enemy-tank-body");
            TextureRegion turretRegion = loadedTextures.get("enemy-tank-turret");
            actor = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion}, enemyTankPool, actorGroups.getTowerGroup(), effectFactory, projectileFactory);
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
