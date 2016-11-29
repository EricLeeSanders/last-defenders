package com.foxholedefense.game.service.factory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.*;
import com.foxholedefense.game.model.actor.combat.tower.*;
import com.foxholedefense.game.model.actor.deatheffect.BloodSplatter;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.deatheffect.VehicleExplosion;
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
public class ActorFactory implements ICombatActorFactory, IHealthBarFactory, ISupportActorFactory, IProjectileFactory, IDeathEffectFactory, ISupplyDropFactory {
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
	private SupplyDropPool supplyDropPool = new SupplyDropPool();
	private SupplyDropCratePool supplyDropCratePool = new SupplyDropCratePool();
	private SupportActorPool<Apache> apachePool = new SupportActorPool<Apache>(Apache.class);
	private SupportActorPool<AirStrike> airStrikePool = new SupportActorPool<AirStrike>(AirStrike.class);
	private SupportActorPool<LandMine> landMinePool = new SupportActorPool<LandMine>(LandMine.class);
	private FHDAudio audio;
	private ActorGroups actorGroups;
	private Map<String, TextureRegion> loadedTextures = new HashMap<String, TextureRegion>();
	private Map<String, Array<AtlasRegion>> loadedAtlasRegions = new HashMap<String, Array<AtlasRegion>>();
	public ActorFactory(ActorGroups actorGroups, TextureAtlas actorAtlas, FHDAudio audio){
		this.actorGroups = actorGroups;
		this.audio = audio;
		initCombatActorPools(actorGroups);
		initTextures(actorAtlas);
		
	}
	private void  initCombatActorPools(ActorGroups actorGroups){
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
	}

	private void initTextures(TextureAtlas actorAtlas){
		loadedTextures.put("range-red", actorAtlas.findRegion("range-red"));
		loadedTextures.put("range-white", actorAtlas.findRegion("range-white"));
		loadedTextures.put("range-red-turret", actorAtlas.findRegion("range-red-turret"));
		loadedTextures.put("range-white-turret", actorAtlas.findRegion("range-white-turret"));
		loadedTextures.put("range-black", actorAtlas.findRegion("range-black"));
		loadedTextures.put("airstrike", actorAtlas.findRegion("airstrike"));
		loadedTextures.put("apache1", actorAtlas.findRegion("apache1"));
		loadedTextures.put("apache2", actorAtlas.findRegion("apache2"));
		loadedTextures.put("apache3", actorAtlas.findRegion("apache3"));
		loadedTextures.put("bullet", actorAtlas.findRegion("bullet"));
		loadedTextures.put("healthbar-armor", actorAtlas.findRegion("healthbar-armor"));
		loadedTextures.put("healthbar-bg", actorAtlas.findRegion("healthbar-bg"));
		loadedTextures.put("healthbar-life", actorAtlas.findRegion("healthbar-life"));
		loadedTextures.put("humvee", actorAtlas.findRegion("humvee"));
		loadedTextures.put("landmine", actorAtlas.findRegion("landmine"));
		loadedTextures.put("rifle", actorAtlas.findRegion("rifle"));
		loadedTextures.put("rifle-left", actorAtlas.findRegion("rifle-left"));
		loadedTextures.put("rifle-right", actorAtlas.findRegion("rifle-right"));
		loadedTextures.put("supply-drop", actorAtlas.findRegion("supply-drop"));
		loadedTextures.put("supply-drop-crate", actorAtlas.findRegion("supply-drop-crate"));
		loadedTextures.put("tank", actorAtlas.findRegion("tank"));
		loadedTextures.put("tank-turret", actorAtlas.findRegion("tank-turret"));
		loadedTextures.put("turret-bags", actorAtlas.findRegion("turret-bags"));
		loadedTextures.put("turret-machine", actorAtlas.findRegion("turret-machine"));


		loadedAtlasRegions.put("explosion", actorAtlas.findRegions("explosion"));
		loadedAtlasRegions.put("flame", actorAtlas.findRegions("flame"));
		loadedAtlasRegions.put("blood-splatter", actorAtlas.findRegions("blood-splatter"));
		loadedAtlasRegions.put("smoke-ring", actorAtlas.findRegions("smoke-ring"));
	}
	/**
	 * Obtains a tower from the pool
	 * 
	 * @param type - The type of tower
	 * @return Tower
	 */
	@Override
	public Tower loadTower(String type) {
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
		enemy.setDead(false);
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
		HealthBar healthBar = healthPool.obtain();
		actorGroups.getHealthBarGroup().addActor(healthBar);
		return healthBar;
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
		Logger.info("Loading Supply Drop actor from the pool");
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
		Logger.info("Loading Supply Drop Crate actor from the pool");
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
		CombatActor actor = null;
		if (type.equals(TowerRifle.class)) {
			TextureRegion rifleRegion = loadedTextures.get("rifle");
			actor = new TowerRifle(rifleRegion, towerRiflePool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = loadedTextures.get("rifle");
			actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = loadedTextures.get("rifle");
			actor = new TowerSniper(sniperRegion, towerSniperPool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = loadedTextures.get("rifle");
			actor = new TowerMachineGun(machineRegion, towerMachinePool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = loadedTextures.get("rifle");
			actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this, audio);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = loadedTextures.get("tank");
			TextureRegion turretRegion = loadedTextures.get("tank-turret");
			actor = new TowerTank(tankRegion, turretRegion, towerTankPool, targetGroup, loadedTextures.get("range-white"), loadedTextures.get("range-red"), this, this);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = loadedTextures.get("turret-machine");
			TextureRegion bagsRegion = loadedTextures.get("turret-bags");
			actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, targetGroup, loadedTextures.get("range-white-turret"), loadedTextures.get("range-red-turret"), this, this, audio);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] regions = new TextureRegion[3];
			regions[0] = loadedTextures.get("rifle-left");
			regions[1] = loadedTextures.get("rifle-right");
			regions[2] = loadedTextures.get("rifle");
			actor = new EnemyRifle(regions, enemyRiflePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] regions = new TextureRegion[3];
			regions[0] = loadedTextures.get("rifle-left");
			regions[1] = loadedTextures.get("rifle-right");
			regions[2] = loadedTextures.get("rifle");
			actor = new EnemyFlameThrower(regions, enemyFlameThrowerPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = loadedTextures.get("Humvee");
			actor = new EnemyHumvee(new TextureRegion[]{humveeRegion}, enemyHumveePool, this);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] regions = new TextureRegion[3];
			regions[0] = loadedTextures.get("rifle-left");
			regions[1] = loadedTextures.get("rifle-right");
			regions[2] = loadedTextures.get("rifle");
			actor = new EnemyMachineGun(regions, enemyMachinePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] regions = new TextureRegion[3];
			regions[0] = loadedTextures.get("rifle-left");
			regions[1] = loadedTextures.get("rifle-right");
			regions[2] = loadedTextures.get("rifle");
			actor = new EnemyRocketLauncher(regions, enemyRocketLauncherPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] regions = new TextureRegion[3];
			regions[0] = loadedTextures.get("rifle-left");
			regions[1] = loadedTextures.get("rifle-right");
			regions[2] = loadedTextures.get("rifle");
			actor = new EnemySniper(regions, enemySniperPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = loadedTextures.get("tank");
			TextureRegion turretRegion = loadedTextures.get("tank-turret");
			actor = new EnemyTank(tankRegion, new TextureRegion[]{turretRegion}, enemyTankPool, targetGroup, this, this);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}
		return actor;
	}
	

	/**
	 * Create a Health Bar
	 * 
	 * @return HealthBar
	 */
	protected HealthBar createHealthBarActor() {
		HealthBar healthBar = new HealthBar(healthPool);
		return healthBar;

	}

	/**
	 * Create a Bullet
	 * 
	 * @return Bullet
	 */
	protected Bullet createBulletActor() {
		Bullet bullet = new Bullet(bulletPool);
		return bullet;

	}

	/**
	 * Create an RPG
	 * 
	 * @return RPG
	 */
	protected RPG createRPGActor() {
		RPG rpg = new RPG(rpgPool, explosionPool);
		return rpg;

	}
	
	/**
	 * Create an AirStrikeBomb
	 * 
	 * @return AirStrikeBomb
	 */
	protected AirStrikeBomb createAirStrikeBombActor() {
		AirStrikeBomb airStrikeBomb = new AirStrikeBomb(airStrikeBombPool, explosionPool);
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
		Logger.info("Creating Supply Drop Actor");
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
		Logger.info("Creating Supply Drop Crate Actor");
		TextureRegion supplyDropCrateRegion = loadedTextures.get("supply-drop-crate");
		SupplyDropCrate supplyDropCrate = new SupplyDropCrate(supplyDropCrateRegion, supplyDropCratePool, actorGroups.getTowerGroup());
		return supplyDropCrate;

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
		Group targetGroup = actorGroups.getEnemyGroup();
		if (type.equals(Apache.class)) {
			TextureRegion [] textureRegions = new TextureRegion[3];
			textureRegions[0] = loadedTextures.get("apache1");
			textureRegions[1] = loadedTextures.get("apache2");
			textureRegions[2] = loadedTextures.get("apache3");
			return new Apache(apachePool, targetGroup, this, textureRegions, audio);
		} else if(type.equals(AirStrike.class)){
			TextureRegion textureRegion = loadedTextures.get("airstrike");
			return new AirStrike(airStrikePool, targetGroup, this, textureRegion, audio);	
		} else if (type.equals(LandMine.class)){
			TextureRegion textureRegion = loadedTextures.get("landmine");
			return new LandMine(landMinePool, targetGroup, this, textureRegion);	
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

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