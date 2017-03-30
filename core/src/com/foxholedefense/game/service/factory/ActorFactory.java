package com.foxholedefense.game.service.factory;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver;
import com.foxholedefense.game.model.actor.combat.enemy.*;
import com.foxholedefense.game.model.actor.combat.tower.*;
import com.foxholedefense.game.model.actor.effects.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.TowerHealEffect;
import com.foxholedefense.game.model.actor.effects.deatheffect.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.effects.deatheffect.VehicleExplosion;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.model.actor.projectile.AirStrikeBomb;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.game.model.actor.projectile.RPG;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.model.actor.support.SupportActor;
import com.foxholedefense.game.service.factory.interfaces.*;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

/**
 * Factory class for obtaining from a pool, various actors
 * 
 * @author Eric
 *
 */
// TODO: Doing a lot of things here
public class ActorFactory implements ICombatActorFactory, IHealthFactory, ISupportActorFactory, IProjectileFactory, IDeathEffectFactory, ISupplyDropFactory {
	private CombatActorPool<CombatActor> towerRiflePool;
	private CombatActorPool<CombatActor> towerTankPool;
	private CombatActorPool<CombatActor> towerFlameThrowerPool;
	private CombatActorPool<CombatActor> towerTurretPool;
	private CombatActorPool<CombatActor> towerSniperPool;
	private CombatActorPool<CombatActor> towerMachinePool;
	private CombatActorPool<CombatActor> towerRocketLauncherPool;
	private CombatActorPool<CombatActor> enemyRiflePool;
	private CombatActorPool<CombatActor> enemyTankPool;
	private CombatActorPool<CombatActor> enemyFlameThrowerPool;
	private CombatActorPool<CombatActor> enemyMachinePool;
	private CombatActorPool<CombatActor> enemyRocketLauncherPool;
	private CombatActorPool<CombatActor> enemySniperPool;
	private CombatActorPool<CombatActor> enemyHumveePool;
	private DeathEffectPool<DeathEffect> vehicleExplosionPool = new DeathEffectPool<DeathEffect>(VehicleExplosion.class);
	private DeathEffectPool<DeathEffect> bloodPool = new DeathEffectPool<DeathEffect>(BloodSplatter.class);
	private HealthPool healthPool = new HealthPool();
	private BulletPool bulletPool = new BulletPool();
	private RPGPool rpgPool = new RPGPool();
	private AirStrikeBombPool airStrikeBombPool = new AirStrikeBombPool();
	private ExplosionPool explosionPool = new ExplosionPool();
	private FlamePool flamePool = new FlamePool();
	private ArmorIconPool armorIconPool = new ArmorIconPool();
	private ArmorDestroyedEffectPool armorDestroyedEffectPool = new ArmorDestroyedEffectPool();
	private TowerHealEffectPool towerHealEffectPool = new TowerHealEffectPool();
	private SupplyDropPool supplyDropPool = new SupplyDropPool();
	private SupplyDropCratePool supplyDropCratePool = new SupplyDropCratePool();
	private SupportActorPool<Apache> apachePool = new SupportActorPool<Apache>(Apache.class);
	private SupportActorPool<AirStrike> airStrikePool = new SupportActorPool<AirStrike>(AirStrike.class);
	private SupportActorPool<LandMine> landMinePool = new SupportActorPool<LandMine>(LandMine.class);

	private SnapshotArray<ICombatActorObserver> combatActorObservers = new SnapshotArray<ICombatActorObserver>();
	private SnapshotArray<ITowerObserver> towerObservers = new SnapshotArray<ITowerObserver>();
	private SnapshotArray<IEnemyObserver> enemyObservers = new SnapshotArray<IEnemyObserver>();

	private FHDAudio audio;
	private ActorGroups actorGroups;
	private Resources resources;

	private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
	private Map<String, Array<AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<AtlasRegion>>();
	public ActorFactory(ActorGroups actorGroups, TextureAtlas actorAtlas, FHDAudio audio, Resources resources){
		this.actorGroups = actorGroups;
		this.audio = audio;
		this.resources = resources;
		initCombatActorPools(actorGroups);
		initTextures(actorAtlas);
		
	}
	private void  initCombatActorPools(ActorGroups actorGroups){

		Logger.info("Actor Factory: initializing Combat Actor Pools");

		towerRiflePool = new CombatActorPool<CombatActor>(TowerRifle.class, actorGroups.getEnemyGroup());
		towerTankPool = new CombatActorPool<CombatActor>(TowerTank.class, actorGroups.getEnemyGroup());
		towerFlameThrowerPool = new CombatActorPool<CombatActor>(TowerFlameThrower.class, actorGroups.getEnemyGroup());
		towerTurretPool = new CombatActorPool<CombatActor>(TowerTurret.class, actorGroups.getEnemyGroup());
		towerSniperPool = new CombatActorPool<CombatActor>(TowerSniper.class, actorGroups.getEnemyGroup());
		towerMachinePool = new CombatActorPool<CombatActor>(TowerMachineGun.class, actorGroups.getEnemyGroup());
		towerRocketLauncherPool = new CombatActorPool<CombatActor>(TowerRocketLauncher.class, actorGroups.getEnemyGroup());
		enemyRiflePool = new CombatActorPool<CombatActor>(EnemyRifle.class, actorGroups.getTowerGroup());
		enemyTankPool = new CombatActorPool<CombatActor>(EnemyTank.class, actorGroups.getTowerGroup());
		enemyFlameThrowerPool = new CombatActorPool<CombatActor>(EnemyFlameThrower.class, actorGroups.getTowerGroup());
		enemyMachinePool = new CombatActorPool<CombatActor>(EnemyMachineGun.class, actorGroups.getTowerGroup());
		enemyRocketLauncherPool = new CombatActorPool<CombatActor>(EnemyRocketLauncher.class, actorGroups.getTowerGroup());
		enemySniperPool = new CombatActorPool<CombatActor>(EnemySniper.class, actorGroups.getTowerGroup());
		enemyHumveePool = new CombatActorPool<CombatActor>(EnemyHumvee.class, actorGroups.getTowerGroup());

		Logger.info("Actor Factory: Combat Actor Pools initialized");
	}

	private void initTextures(TextureAtlas actorAtlas){

		Logger.info("Actor Factory: initializing textures");

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

		Logger.info("Actor Factory: textures initialized");
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
	@Override
	public Tower loadTower(String type) {
		Logger.info("Actor Factory: loading Tower: " + type);
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
	@Override
	public Enemy loadEnemy(String type) {

		Logger.info("Actor Factory: loading Enemy: " + type);

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
	 * Obtains a Death Effect from the pool
	 * 
	 * @param type - The type of Death Effect
	 * @return DeathEffect
	 */
	@Override
	public DeathEffect loadDeathEffect(DeathEffectType type){

		DeathEffect deathEffect = null;
		switch(type) {
		case BLOOD:
			deathEffect = bloodPool.obtain();
			break;
		case VEHCILE_EXPLOSION:
			deathEffect = vehicleExplosionPool.obtain();
			break;
		}

		actorGroups.getDeathEffectGroup().addActor(deathEffect);
		return deathEffect;
	}

	/**
	 * Obtains a health bar from the pool
	 * 
	 * @return HealthBar
	 */
	@Override
	public HealthBar loadHealthBar() {
		Logger.info("Actor Factory: loading healthbar");
		HealthBar healthBar = healthPool.obtain();
		actorGroups.getHealthGroup().addActor(healthBar);
		return healthBar;
	}

	@Override
	public ArmorIcon loadArmorIcon(){
		Logger.info("Actor Factory: loading ArmorIcon");
		ArmorIcon armorIcon = armorIconPool.obtain();
		actorGroups.getHealthGroup().addActor(armorIcon);
		return armorIcon;
	}

	@Override
	public ArmorDestroyedEffect loadArmorDestroyedEffect(){
		ArmorDestroyedEffect armorDestroyedEffect = armorDestroyedEffectPool.obtain();
		actorGroups.getHealthGroup().addActor(armorDestroyedEffect);
		return armorDestroyedEffect;
	}

	@Override
	public TowerHealEffect loadTowerHealEffect(){
		TowerHealEffect towerHealEffect = towerHealEffectPool.obtain();
		actorGroups.getHealthGroup().addActor(towerHealEffect);
		return towerHealEffect;
	}

	/**
	 * Obtains a bullet from the pool
	 * 
	 * @return Bullet
	 */
	@Override
	public Bullet loadBullet() {
		Bullet bullet = bulletPool.obtain();
		actorGroups.getProjectileGroup().addActor(bullet);
		return bullet;
	}

	/**
	 * Obtains an RPG from the pool
	 * 
	 * @return RPG
	 */
	@Override
	public RPG loadRPG() {
		RPG rpg = rpgPool.obtain();
		actorGroups.getProjectileGroup().addActor(rpg);
		return rpg;
	}
	
	/**
	 * Obtains an AirStrike Bomb from the pool
	 * 
	 * @return AirStrikeBomb
	 */
	@Override
	public AirStrikeBomb loadAirStrikeBomb() {
		AirStrikeBomb airStrikeBomb = airStrikeBombPool.obtain();
		actorGroups.getProjectileGroup().addActor(airStrikeBomb);
		return airStrikeBomb;
	}

	/**
	 * Obtains an Explosion from the pool
	 * 
	 * @return Explosion
	 */
	@Override
	public Explosion loadExplosion() {
		Explosion explosion = explosionPool.obtain();
		actorGroups.getProjectileGroup().addActor(explosion);
		return explosion;
	}

	/**
	 * Obtains a flame from the pool
	 * 
	 * @return Flame
	 */
	@Override
	public Flame loadFlame() {
		Flame flame = flamePool.obtain();
		actorGroups.getProjectileGroup().addActor(flame);
		return flame;
	}


	/**
	 * Obtains a Supply Drop from the pool
	 * 
	 * @return SupplyDrop
	 */
	@Override
	public SupplyDrop loadSupplyDrop() {
		SupplyDrop supplyDrop = supplyDropPool.obtain();
		actorGroups.getSupportGroup().addActor(supplyDrop);
		return supplyDrop;
	}

	/**
	 * Obtains a Supply Drop Crate from the pool
	 * 
	 * @return SupplyDropCrate
	 */
	@Override
	public SupplyDropCrate loadSupplyDropCrate() {
		SupplyDropCrate supplyDropCrate = supplyDropCratePool.obtain();
		actorGroups.getSupportGroup().addActor(supplyDropCrate);
		return supplyDropCrate;
	}
	
	/**
	 * Obtain a Support Actor from the pool
	 * 
	 * @param type - The type of support actor
	 * @return Support Actor
	 */
	@Override
	public SupportActor loadSupportActor(String type) {
		Logger.info("Actor Factory: loading support actor: " + type);
		SupportActor supportActor = null;
		if (type.equals("Apache")) {
			supportActor = apachePool.obtain();
		} else if(type.equals("AirStrike")) {
			supportActor = airStrikePool.obtain();
		} else if(type.equals("LandMine")) {
			supportActor = landMinePool.obtain();
		}
		return supportActor;
	}
	
	/**
	 * Create a Game Actor
	 * 
	 * @param type - Type of Game Actor
	 * @return CombatActor
	 */
	protected CombatActor createCombatActor(Class<? extends CombatActor> type, Group targetGroup) {

		Logger.info("Actor Factory: creating combat actor: " + type.getSimpleName());
		CombatActor actor = null;
		if (type.equals(TowerRifle.class)) {
			TextureRegion rifleRegion = loadedTextures.get("tower-rifle");
			actor = new TowerRifle(rifleRegion, towerRiflePool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = loadedTextures.get("tower-flame-thrower");
			actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = loadedTextures.get("tower-sniper");
			actor = new TowerSniper(sniperRegion, towerSniperPool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = loadedTextures.get("tower-machine-gun");
			actor = new TowerMachineGun(machineRegion, towerMachinePool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = loadedTextures.get("tower-rocket-launcher");
			actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = loadedTextures.get("tower-tank-body");
			TextureRegion turretRegion = loadedTextures.get("tower-tank-turret");
			actor = new TowerTank(tankRegion, turretRegion, towerTankPool, targetGroup, loadedTextures.get("range"), loadedTextures.get("range-red"), this, this);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = loadedTextures.get("tower-turret-turret");
			TextureRegion bagsRegion = loadedTextures.get("tower-turret-bags");
			actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, targetGroup, loadedTextures.get("range-turret"), loadedTextures.get("range-red-turret"), this, this, audio);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-rifle").toArray(TextureRegion.class);
			TextureRegion stationaryRegion = loadedTextures.get("enemy-rifle-stationary");
			actor = new EnemyRifle(stationaryRegion, animatedRegions, enemyRiflePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-flame-thrower").toArray(TextureRegion.class);
			TextureRegion stationaryRegion = loadedTextures.get("enemy-flame-thrower-stationary");
			actor = new EnemyFlameThrower(stationaryRegion, animatedRegions, enemyFlameThrowerPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = loadedTextures.get("enemy-humvee");
			actor = new EnemyHumvee(humveeRegion, new TextureRegion[]{humveeRegion}, enemyHumveePool, this);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-machine-gun").toArray(TextureRegion.class);
			TextureRegion stationaryRegion = loadedTextures.get("enemy-machine-gun-stationary");
			actor = new EnemyMachineGun(stationaryRegion, animatedRegions, enemyMachinePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-rocket-launcher").toArray(TextureRegion.class);
			TextureRegion stationaryRegion = loadedTextures.get("enemy-rocket-launcher-stationary");
			actor = new EnemyRocketLauncher(stationaryRegion, animatedRegions, enemyRocketLauncherPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] animatedRegions = loadedAtlasRegions.get("enemy-sniper").toArray(TextureRegion.class);
			TextureRegion stationaryRegion = loadedTextures.get("enemy-sniper-stationary");
			actor = new EnemySniper(stationaryRegion, animatedRegions, enemySniperPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = loadedTextures.get("enemy-tank-body");
			TextureRegion turretRegion = loadedTextures.get("enemy-tank-turret");
			actor = new EnemyTank(tankRegion, turretRegion, new TextureRegion[]{turretRegion}, enemyTankPool, targetGroup, this, this);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}
		if(actor instanceof Tower) {
			((Tower)actor).attachAllTower(towerObservers);
		} else if(actor instanceof Enemy) {
			((Enemy)actor).attachAllEnemy(enemyObservers);
		}
		actor.attachAllCombatActor(combatActorObservers);

		return actor;
	}
	

	/**
	 * Create a Health Bar
	 * 
	 * @return HealthBar
	 */
	protected HealthBar createHealthBarActor() {
		Logger.info("Actor Factory: creating healthbar");
		HealthBar healthBar = new HealthBar(healthPool, loadedTextures.get("healthbar-bg"), loadedTextures.get("healthbar-life"), loadedTextures.get("healthbar-armor"));
		return healthBar;

	}

	/**
	 * Create a Bullet
	 * 
	 * @return Bullet
	 */
	protected Bullet createBulletActor() {
		Bullet bullet = new Bullet(bulletPool, loadedTextures.get("bullet"));
		return bullet;

	}

	/**
	 * Create an RPG
	 * 
	 * @return RPG
	 */
	protected RPG createRPGActor() {
		RPG rpg = new RPG(rpgPool, explosionPool, loadedTextures.get("bullet"));
		return rpg;

	}
	
	/**
	 * Create an AirStrikeBomb
	 * 
	 * @return AirStrikeBomb
	 */
	protected AirStrikeBomb createAirStrikeBombActor() {
		AirStrikeBomb airStrikeBomb = new AirStrikeBomb(airStrikeBombPool, explosionPool, loadedTextures.get("bullet"));
		return airStrikeBomb;

	}

	/**
	 * Create an Explosion
	 * 
	 * @return Explosion
	 */
	protected Explosion createExplosionActor() {
		Array<AtlasRegion> atlasRegions = loadedAtlasRegions.get("explosion");
		Explosion explosion = new Explosion(explosionPool, atlasRegions, audio);
		return explosion;

	}

	/**
	 * Create a Flame
	 * 
	 * @return Flame
	 */
	protected Flame createFlameActor() {
		Array<AtlasRegion> atlasRegions = loadedAtlasRegions.get("flame");
		Flame flame = new Flame(flamePool, atlasRegions);
		return flame;

	}
	
	/**
	 * Create a SupplyDrop
	 * 
	 * @return SupplyDrop
	 */
	protected SupplyDrop createSupplyDropActor() {
		TextureRegion supplyDropRegion = loadedTextures.get("supply-drop");
		SupplyDrop supplyDrop = new SupplyDrop(supplyDropRegion, supplyDropPool, this);
		return supplyDrop;

	}
	
	/**
	 * Create a SupplyDropCrate
	 * 
	 * @return SupplyDropCrate
	 */
	protected SupplyDropCrate createSupplyDropCrateActor() {
		TextureRegion supplyDropCrateRegion = loadedTextures.get("supply-drop-crate");
		TextureRegion rangeTexture = loadedTextures.get("range-black");
		SupplyDropCrate supplyDropCrate = new SupplyDropCrate(supplyDropCrateRegion, rangeTexture, supplyDropCratePool, actorGroups.getTowerGroup(), this);
		return supplyDropCrate;

	}

	protected ArmorIcon createArmorIcon(){
		Logger.info("Actor Factory: creating ArmorIcon");
		return new ArmorIcon(armorIconPool, loadedTextures.get("shield"), armorDestroyedEffectPool);
	}

	protected ArmorDestroyedEffect createArmorDestroyedEffect(){
		Label label = new Label("", resources.getSkin());
		return new ArmorDestroyedEffect(loadedAtlasRegions.get("shield-destroyed"), armorDestroyedEffectPool,label);
	}

	protected TowerHealEffect createTowerHealEffect(){
		return new TowerHealEffect(towerHealEffectPool, resources.getSkin());
	}
	
	/**
	 * Create a Death Effect
	 * 
	 * @return DeathEffect
	 */
	protected DeathEffect createDeathEffect(Class<? extends DeathEffect> type) {
		
		if (type.equals(BloodSplatter.class)) {
			Array<AtlasRegion> atlasRegions = loadedAtlasRegions.get("blood-splatter");
			return new BloodSplatter(bloodPool, atlasRegions);
		} else if(type.equals(VehicleExplosion.class)){
			Array<AtlasRegion> atlasRegions = loadedAtlasRegions.get("smoke-ring");
			return new VehicleExplosion(vehicleExplosionPool, atlasRegions);	
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

	}

	/**
	 * Create a Support Actor
	 * 
	 * @return SupportActor
	 */
	protected SupportActor createSupportActor(Class<? extends SupportActor> type) {
		Logger.info("Actor Factory: creating support actor: " + type.getSimpleName());
		Group targetGroup = actorGroups.getEnemyGroup();
		if (type.equals(Apache.class)) {
			TextureRegion [] textureRegions = loadedAtlasRegions.get("apache").toArray(TextureRegion.class);
			TextureRegion rangeTexture = loadedTextures.get("range");
			TextureRegion stationaryRegion = loadedTextures.get("apache-stationary");
			return new Apache(apachePool, targetGroup, this,stationaryRegion, textureRegions, rangeTexture, audio);
		} else if(type.equals(AirStrike.class)){
			TextureRegion textureRegion = loadedTextures.get("airstrike");
			TextureRegion rangeTexture = loadedTextures.get("range-black");
			return new AirStrike(airStrikePool, targetGroup, this, textureRegion, rangeTexture, audio);
		} else if (type.equals(LandMine.class)){
			TextureRegion textureRegion = loadedTextures.get("landmine");
			TextureRegion rangeTexture = loadedTextures.get("range");
			return new LandMine(landMinePool, targetGroup, this, textureRegion, rangeTexture);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

	}


	public Map<String, TextureRegion> getLoadedTextures() {
		return loadedTextures;
	}

	public Map<String, Array<AtlasRegion>> getLoadedAtlasRegions() {
		return loadedAtlasRegions;
	}
	public class CombatActorPool<T extends CombatActor> extends Pool<CombatActor> {
		private final Class<? extends CombatActor> type;
		private final Group targetGroup;
		public CombatActorPool(Class<? extends CombatActor> type, Group targetGroup){
			this.type = type;
			this.targetGroup = targetGroup;
		}

		@Override
		protected CombatActor newObject() {
			return createCombatActor(type, targetGroup);
		}

	}


	
	public class HealthPool extends Pool<HealthBar> {
		@Override
		protected HealthBar newObject() {
			return createHealthBarActor();
		}
	}

	public class ExplosionPool extends Pool<Explosion> {
		@Override
		protected Explosion newObject() {
			return createExplosionActor();
		}
	}

	public class BulletPool extends Pool<Bullet> {
		@Override
		protected Bullet newObject() {
			return createBulletActor();
		}
	}

	public class RPGPool extends Pool<RPG> {
		@Override
		protected RPG newObject() {
			return createRPGActor();
		}
	}

	public class AirStrikeBombPool extends Pool<AirStrikeBomb> {
		@Override
		protected AirStrikeBomb newObject() {
			return createAirStrikeBombActor();
		}
	}

	public class FlamePool extends Pool<Flame> {
		@Override
		protected Flame newObject() {
			return createFlameActor();
		}
	}

	public class ArmorIconPool extends Pool<ArmorIcon> {
		@Override
		protected ArmorIcon newObject() {
			return createArmorIcon();
		}
	}

	public class ArmorDestroyedEffectPool extends Pool<ArmorDestroyedEffect> {
		@Override
		protected ArmorDestroyedEffect newObject() {
			return createArmorDestroyedEffect();
		}
	}

	public class TowerHealEffectPool extends Pool<TowerHealEffect> {
		@Override
		protected TowerHealEffect newObject() {
			return createTowerHealEffect();
		}
	}

	public class SupplyDropPool extends Pool<SupplyDrop> {
		@Override
		protected SupplyDrop newObject() {
			return createSupplyDropActor();
		}
	}
	
	public class SupplyDropCratePool extends Pool<SupplyDropCrate> {
		@Override
		protected SupplyDropCrate newObject() {
			return createSupplyDropCrateActor();
		}
	}
	
	public class DeathEffectPool<T extends DeathEffect> extends Pool<DeathEffect> {
		private final Class<? extends DeathEffect> type;

		public DeathEffectPool(Class<? extends DeathEffect> type) {
			this.type = type;
		}

		@Override
		protected DeathEffect newObject() {
			return createDeathEffect(type);
		}

	}
	
	public class SupportActorPool<T extends SupportActor> extends Pool<SupportActor> {
		private final Class<? extends SupportActor> type;

		public SupportActorPool(Class<? extends SupportActor> type) {
			this.type = type;
		}

		@Override
		protected SupportActor newObject() {
			return createSupportActor(type);
		}

	}
}