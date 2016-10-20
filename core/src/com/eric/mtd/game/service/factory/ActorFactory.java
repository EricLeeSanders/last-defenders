package com.eric.mtd.game.service.factory;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.enemy.*;
import com.eric.mtd.game.model.actor.combat.tower.*;
import com.eric.mtd.game.model.actor.deatheffect.DeathEffectType;
import com.eric.mtd.game.model.actor.deatheffect.BloodSplatter;
import com.eric.mtd.game.model.actor.deatheffect.DeathEffect;
import com.eric.mtd.game.model.actor.deatheffect.VehicleExplosion;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.projectile.AirStrikeBomb;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.support.AirStrike;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.actor.support.LandMine;
import com.eric.mtd.game.model.actor.support.SupplyDrop;
import com.eric.mtd.game.model.actor.support.SupplyDropCrate;
import com.eric.mtd.game.model.actor.support.SupportActor;
import com.eric.mtd.game.service.factory.interfaces.*;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Resources;

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
	private TextureAtlas actorAtlas;
	private MTDAudio audio;
	private ActorGroups actorGroups;
	public ActorFactory(ActorGroups actorGroups, TextureAtlas actorAtlas, MTDAudio audio){
		this.actorAtlas = actorAtlas;
		this.actorGroups = actorGroups;
		this.audio = audio;
		initCombatActorPools(actorGroups);
		
	}
	public void  initCombatActorPools(ActorGroups actorGroups){
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
			TextureRegion rifleRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerRifle(rifleRegion, towerRiflePool, targetGroup, this, this, audio);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, targetGroup, this, this, audio);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerSniper(sniperRegion, towerSniperPool, targetGroup, this, this, audio);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerMachineGun(machineRegion, towerMachinePool, targetGroup, this, this, audio);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, targetGroup, this, this, audio);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			actor = new TowerTank(tankRegion, turretRegion, towerTankPool, targetGroup, this, this);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("TurretMachine");
			TextureRegion bagsRegion = actorAtlas.findRegion("TurretBags");
			actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] rifleRegions = new TextureRegion[3];
			rifleRegions[0] = actorAtlas.findRegion("RifleLeft");
			rifleRegions[1] = actorAtlas.findRegion("RifleRight");
			rifleRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyRifle(rifleRegions, enemyRiflePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] flameThrowerRegions = new TextureRegion[3];
			flameThrowerRegions[0] = actorAtlas.findRegion("RifleLeft");
			flameThrowerRegions[1] = actorAtlas.findRegion("RifleRight");
			flameThrowerRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyFlameThrower(flameThrowerRegions, enemyFlameThrowerPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = actorAtlas.findRegion("Humvee");
			actor = new EnemyHumvee(humveeRegion, enemyHumveePool, this);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] machineRegions = new TextureRegion[3];
			machineRegions[0] = actorAtlas.findRegion("RifleLeft");
			machineRegions[1] = actorAtlas.findRegion("RifleRight");
			machineRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyMachineGun(machineRegions, enemyMachinePool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] rocketLauncherRegions = new TextureRegion[3];
			rocketLauncherRegions[0] = actorAtlas.findRegion("RifleLeft");
			rocketLauncherRegions[1] = actorAtlas.findRegion("RifleRight");
			rocketLauncherRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyRocketLauncher(rocketLauncherRegions, enemyRocketLauncherPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] sniperRegions = new TextureRegion[3];
			sniperRegions[0] = actorAtlas.findRegion("RifleLeft");
			sniperRegions[1] = actorAtlas.findRegion("RifleRight");
			sniperRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemySniper(sniperRegions, enemySniperPool, targetGroup, this, this, audio);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			actor = new EnemyTank(tankRegion, turretRegion, enemyTankPool, targetGroup, this, this);
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
		Array<AtlasRegion> atlasRegions = actorAtlas.findRegions("explosion");
		Explosion explosion = new Explosion(explosionPool, atlasRegions, audio);
		return explosion;

	}

	/**
	 * Create a Flame
	 * 
	 * @return Flame
	 */
	protected Flame createFlameActor() {
		Array<AtlasRegion> atlasRegions = actorAtlas.findRegions("flame");
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
		TextureRegion supplyDropRegion = actorAtlas.findRegion("supply-drop");
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
		TextureRegion supplyDropCrateRegion = actorAtlas.findRegion("supply-drop-crate");
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
			Array<AtlasRegion> atlasRegions = actorAtlas.findRegions("blood-splatter");
			return new BloodSplatter(bloodPool, atlasRegions);
		} else if(type.equals(VehicleExplosion.class)){
			Array<AtlasRegion> atlasRegions = actorAtlas.findRegions("smoke-ring");
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
			textureRegions[0] = actorAtlas.findRegion("apache1");
			textureRegions[1] = actorAtlas.findRegion("apache2");
			textureRegions[2] = actorAtlas.findRegion("apache3");
			return new Apache(apachePool, targetGroup, this, textureRegions, audio);
		} else if(type.equals(AirStrike.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("airstrike");
			return new AirStrike(airStrikePool, targetGroup, this, textureRegion, audio);	
		} else if (type.equals(LandMine.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("landmine");
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