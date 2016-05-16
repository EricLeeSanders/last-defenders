package com.eric.mtd.game.service.actorfactory;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.enemy.*;
import com.eric.mtd.game.model.actor.combat.tower.*;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.projectile.AirStrikeBomb;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.support.AirStrike;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.actor.support.LandMine;
import com.eric.mtd.game.model.actor.support.Sandbag;
import com.eric.mtd.game.model.actor.support.SupportActor;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Factory class for obtaining from a pool, various actors
 * 
 * @author Eric
 *
 */
// TODO: Doing a lot of static things here
public class ActorFactory {
	private static CombatActorPool<CombatActor> towerRiflePool = new CombatActorPool<CombatActor>(TowerRifle.class);
	private static CombatActorPool<CombatActor> towerTankPool = new CombatActorPool<CombatActor>(TowerTank.class);
	private static CombatActorPool<CombatActor> towerFlameThrowerPool = new CombatActorPool<CombatActor>(TowerFlameThrower.class);
	private static CombatActorPool<CombatActor> towerTurretPool = new CombatActorPool<CombatActor>(TowerTurret.class);
	private static CombatActorPool<CombatActor> towerSniperPool = new CombatActorPool<CombatActor>(TowerSniper.class);
	private static CombatActorPool<CombatActor> towerMachinePool = new CombatActorPool<CombatActor>(TowerMachineGun.class);
	private static CombatActorPool<CombatActor> towerRocketLauncherPool = new CombatActorPool<CombatActor>(TowerRocketLauncher.class);
	private static CombatActorPool<CombatActor> enemyRiflePool = new CombatActorPool<CombatActor>(EnemyRifle.class);
	private static CombatActorPool<CombatActor> enemyTankPool = new CombatActorPool<CombatActor>(EnemyTank.class);
	private static CombatActorPool<CombatActor> enemyFlameThrowerPool = new CombatActorPool<CombatActor>(EnemyFlameThrower.class);
	private static CombatActorPool<CombatActor> enemyMachinePool = new CombatActorPool<CombatActor>(EnemyMachineGun.class);
	private static CombatActorPool<CombatActor> enemyRocketLauncherPool = new CombatActorPool<CombatActor>(EnemyRocketLauncher.class);
	private static CombatActorPool<CombatActor> enemySniperPool = new CombatActorPool<CombatActor>(EnemySniper.class);
	private static CombatActorPool<CombatActor> enemySprinterPool = new CombatActorPool<CombatActor>(EnemySprinter.class);
	private static CombatActorPool<CombatActor> enemyHumveePool = new CombatActorPool<CombatActor>(EnemyHumvee.class);
	private static HealthPool healthPool = new HealthPool();
	private static BulletPool bulletPool = new BulletPool();
	private static RPGPool rpgPool = new RPGPool();
	private static AirStrikeBombPool airStrikeBombPool = new AirStrikeBombPool();
	private static ExplosionPool explosionPool = new ExplosionPool();
	private static FlamePool flamePool = new FlamePool();
	private static SandbagPool sandbagPool = new SandbagPool();
	private static SupportActorPool<SupportActor> apachePool = new SupportActorPool<SupportActor>(Apache.class);
	private static SupportActorPool<SupportActor> airStrikePool = new SupportActorPool<SupportActor>(AirStrike.class);
	private static SupportActorPool<SupportActor> landMinePool = new SupportActorPool<SupportActor>(LandMine.class);
	private static TextureAtlas actorAtlas = Resources.getAtlas(Resources.ACTOR_ATLAS);
	/**
	 * Obtains a tower from the pool
	 * 
	 * @param pos
	 *            - Position to place the tower
	 * @param type
	 *            - The type of tower
	 * @return Tower
	 */
	public static Tower loadTower(Vector2 pos, String type, Group enemyGroup, Group projectileGroup) {
		Tower tower = null;
		if (type.equals("Rifle")) {
			tower = (Tower) towerRiflePool.obtain();
		} else if (type.equals("Tank")) {
			tower = (Tower) towerTankPool.obtain();
		} else if (type.equals("Turret")) {
			tower = (Tower) towerTurretPool.obtain();
		} else if (type.equals("Sniper")) {
			tower = (Tower) towerSniperPool.obtain();
		} else if (type.equals("Machine")) {
			tower = (Tower) towerMachinePool.obtain();
		} else if (type.equals("RocketLauncher")) {
			tower = (Tower) towerRocketLauncherPool.obtain();
		} else if (type.equals("FlameThrower")) {
			tower = (Tower) towerFlameThrowerPool.obtain();
		}
		tower.setPositionCenter(pos);
		tower.setEnemyGroup(enemyGroup);
		tower.setProjectileGroup(projectileGroup);
		if (Logger.DEBUG)
			System.out.println("Obtained : " + type);
		return tower;
	}

	/**
	 * Obtains an Enemy from the pool
	 * 
	 * @param path
	 *            - The path for the enemy to follow
	 * @param type
	 *            - The type of enemy
	 * @param armor
	 *            - If the enemy has armor
	 * @return Enemy
	 */
	public static Enemy loadEnemy(Queue<Vector2> path, String type, boolean armor, Group towerGroup, Group projectileGroup) {
		Enemy enemy = null;
		if (type.equals("Rifle")) {
			enemy = (Enemy) enemyRiflePool.obtain();
		} else if (type.equals("Tank")) {
			enemy = (Enemy) enemyTankPool.obtain();
		} else if (type.equals("FlameThrower")) {
			enemy = (Enemy) enemyFlameThrowerPool.obtain();
		} else if (type.equals("Machine")) {
			enemy = (Enemy) enemyMachinePool.obtain();
		} else if (type.equals("RocketLauncher")) {
			enemy = (Enemy) enemyRocketLauncherPool.obtain();
		} else if (type.equals("Sniper")) {
			enemy = (Enemy) enemySniperPool.obtain();
		} else if (type.equals("Sprinter")) {
			enemy = (Enemy) enemySprinterPool.obtain();
		} else if (type.equals("Humvee")) {
			enemy = (Enemy) enemyHumveePool.obtain();
		}

		enemy.setPath(new LinkedList<Vector2>(path));
		enemy.setHasArmor(armor);
		enemy.setDead(false);
		enemy.setTowerGroup(towerGroup);
		enemy.setProjectileGroup(projectileGroup);
		if (Logger.DEBUG)
			System.out.println("Obtained : " + type);
		return enemy;
	}

	/**
	 * Obtains a health bar from the pool
	 * 
	 * @return HealthBar
	 */
	public static HealthBar loadHealthBar() {
		HealthBar healthBar = healthPool.obtain();
		if (Logger.DEBUG)
			System.out.println("Obtained healthbar");
		return healthBar;
	}

	/**
	 * Obtains a bullet from the pool
	 * 
	 * @return Bullet
	 */
	public static Bullet loadBullet() {
		Bullet bullet = bulletPool.obtain();
		return bullet;
	}

	/**
	 * Obtains an RPG from the pool
	 * 
	 * @return RPG
	 */
	public static RPG loadRPG() {
		RPG rpg = rpgPool.obtain();
		return rpg;
	}
	
	/**
	 * Obtains an AirStrike Bomb from the pool
	 * 
	 * @return AirStrikeBomb
	 */
	public static AirStrikeBomb loadAirStrikeBomb() {
		AirStrikeBomb airStrikeBomb = airStrikeBombPool.obtain();
		return airStrikeBomb;
	}

	/**
	 * Obtains an Explosion from the pool
	 * 
	 * @return Explosion
	 */
	public static Explosion loadExplosion() {
		Explosion explosion = explosionPool.obtain();
		return explosion;
	}

	/**
	 * Obtains a flame from the pool
	 * 
	 * @return Flame
	 */
	public static Flame loadFlame() {
		Flame flame = flamePool.obtain();
		return flame;
	}

	/**
	 * Obtain a sandbag from the pool
	 * 
	 * @param pos
	 *            - Position to place the sandbag
	 * @return Sandbag
	 */
	public static Sandbag loadSandbag(Vector2 pos) {
		Sandbag sandbag = sandbagPool.obtain();
		if (Logger.DEBUG)
			System.out.println("Sandbag obtained");
		sandbag.setPositionCenter(pos);
		return sandbag;
	}
	
	/**
	 * Obtain a Support Actor from the pool
	 * 
	 * @param pos
	 *            - Position to place the Support Actor
	 * @return Support Actor
	 */
	public static SupportActor loadSupportActor(Vector2 pos, String type, Group enemyGroup) {
		SupportActor supportActor = null;
		if (type.equals("Apache")) {
			supportActor = apachePool.obtain();
		} else if(type.equals("AirStrike")) {
			supportActor = airStrikePool.obtain();
		} else if(type.equals("LandMine")) {
			supportActor = landMinePool.obtain();
		}
		if (Logger.DEBUG)
			System.out.println("Obtained : " + type);
		supportActor.setPositionCenter(pos);
		supportActor.setEnemyGroup(enemyGroup);
		return supportActor;
	}
	
	/**
	 * Create a Game Actor
	 * 
	 * @param type
	 *            - Type of Game Actor
	 * @return CombatActor
	 */
	protected static CombatActor createCombatActor(Class<? extends CombatActor> type) {
		if (Logger.DEBUG)
			System.out.println("Creating new " + type.getSimpleName());
		if (type.equals(TowerRifle.class)) {
			TextureRegion rifleRegion = actorAtlas.findRegion("Rifle");
			return new TowerRifle(rifleRegion, towerRiflePool);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = actorAtlas.findRegion("Rifle");
			return new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = actorAtlas.findRegion("Rifle");
			return new TowerSniper(sniperRegion, towerSniperPool);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("Rifle");
			return new TowerMachineGun(machineRegion, towerMachinePool);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = actorAtlas.findRegion("Rifle");
			return new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			return new TowerTank(tankRegion, turretRegion, towerTankPool);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("TurretMachine");
			TextureRegion bagsRegion = actorAtlas.findRegion("TurretBags");
			return new TowerTurret(bagsRegion, machineRegion, towerTurretPool);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] rifleRegions = new TextureRegion[3];
			rifleRegions[0] = actorAtlas.findRegion("RifleLeft");
			rifleRegions[1] = actorAtlas.findRegion("RifleRight");
			rifleRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemyRifle(rifleRegions, enemyRiflePool);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] flameThrowerRegions = new TextureRegion[3];
			flameThrowerRegions[0] = actorAtlas.findRegion("RifleLeft");
			flameThrowerRegions[1] = actorAtlas.findRegion("RifleRight");
			flameThrowerRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemyFlameThrower(flameThrowerRegions, enemyFlameThrowerPool);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = actorAtlas.findRegion("Humvee");
			return new EnemyHumvee(humveeRegion, enemyHumveePool);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] machineRegions = new TextureRegion[3];
			machineRegions[0] = actorAtlas.findRegion("RifleLeft");
			machineRegions[1] = actorAtlas.findRegion("RifleRight");
			machineRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemyMachineGun(machineRegions, enemyMachinePool);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] rocketLauncherRegions = new TextureRegion[3];
			rocketLauncherRegions[0] = actorAtlas.findRegion("RifleLeft");
			rocketLauncherRegions[1] = actorAtlas.findRegion("RifleRight");
			rocketLauncherRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemyRocketLauncher(rocketLauncherRegions, enemyRocketLauncherPool);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] sniperRegions = new TextureRegion[3];
			sniperRegions[0] = actorAtlas.findRegion("RifleLeft");
			sniperRegions[1] = actorAtlas.findRegion("RifleRight");
			sniperRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemySniper(sniperRegions, enemySniperPool);
		} else if (type.equals(EnemySprinter.class)) {
			TextureRegion[] sprinterRegions = new TextureRegion[3];
			sprinterRegions[0] = actorAtlas.findRegion("RifleLeft");
			sprinterRegions[1] = actorAtlas.findRegion("RifleRight");
			sprinterRegions[2] = actorAtlas.findRegion("Rifle");
			return new EnemySprinter(sprinterRegions, enemySprinterPool);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			return new EnemyTank(tankRegion, turretRegion, enemyTankPool);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

	}

	/**
	 * Create a Health Bar
	 * 
	 * @return Health Bar
	 */
	protected static HealthBar createHealthBarActor() {
		HealthBar healthBar = new HealthBar(healthPool);
		if (Logger.DEBUG)
			System.out.println("Created new healthbar");
		return healthBar;

	}

	/**
	 * Create a Bullet
	 * 
	 * @return Bullet
	 */
	protected static Bullet createBulletActor() {
		Bullet bullet = new Bullet(bulletPool);
		return bullet;

	}

	/**
	 * Create an RPG
	 * 
	 * @return RPG
	 */
	protected static RPG createRPGActor() {
		RPG rpg = new RPG(rpgPool);
		return rpg;

	}
	
	/**
	 * Create an AirStrikeBomb
	 * 
	 * @return AirStrikeBomb
	 */
	protected static AirStrikeBomb createAirStrikeBombActor() {
		AirStrikeBomb airStrikeBomb = new AirStrikeBomb(airStrikeBombPool);
		return airStrikeBomb;

	}

	/**
	 * Create an Explosion
	 * 
	 * @return Explosion
	 */
	protected static Explosion createExplosionActor() {
		Explosion explosion = new Explosion(explosionPool);
		return explosion;

	}

	/**
	 * Create a Flame
	 * 
	 * @return Flame
	 */
	protected static Flame createFlameActor() {
		Flame flame = new Flame(flamePool);
		return flame;

	}

	/**
	 * Create a Sandbag
	 * 
	 * @return Sandbag
	 */
	protected static Sandbag createSandbagActor() {
		Sandbag sandbag = new Sandbag(sandbagPool);
		return sandbag;

	}

	/**
	 * Create an Apache
	 * 
	 * @return Apache
	 */
	protected static SupportActor createSupportActor(Class<? extends SupportActor> type) {
		if (Logger.DEBUG)
			System.out.println("Creating new " + type.getSimpleName());
		
		if (type.equals(Apache.class)) {
			TextureRegion [] textureRegions = new TextureRegion[3];
			textureRegions[0] = actorAtlas.findRegion("apache1");
			textureRegions[1] = actorAtlas.findRegion("apache2");
			textureRegions[2] = actorAtlas.findRegion("apache3");
			return new Apache(apachePool, textureRegions);
		} else if(type.equals(AirStrike.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("airstrike");
			return new AirStrike(airStrikePool, textureRegion);	
		} else if (type.equals(LandMine.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("landmine");
			return new LandMine(landMinePool, textureRegion);	
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

	}
	
	public static class CombatActorPool<T extends CombatActor> extends Pool<CombatActor> {
		private final Class<? extends CombatActor> type;

		public CombatActorPool(Class<? extends CombatActor> type) {
			this.type = type;
		}

		@Override
		protected CombatActor newObject() {
			return createCombatActor(type);
		}

	}
	

	public static class HealthPool extends Pool<HealthBar> {
		@Override
		protected HealthBar newObject() {
			return createHealthBarActor();
		}
	}

	public static class ExplosionPool extends Pool<Explosion> {
		@Override
		protected Explosion newObject() {
			return createExplosionActor();
		}
	}

	public static class BulletPool extends Pool<Bullet> {
		@Override
		protected Bullet newObject() {
			return createBulletActor();
		}
	}

	public static class RPGPool extends Pool<RPG> {
		@Override
		protected RPG newObject() {
			return createRPGActor();
		}
	}

	public static class AirStrikeBombPool extends Pool<AirStrikeBomb> {
		@Override
		protected AirStrikeBomb newObject() {
			return createAirStrikeBombActor();
		}
	}

	public static class FlamePool extends Pool<Flame> {
		@Override
		protected Flame newObject() {
			return createFlameActor();
		}
	}

	public static class SandbagPool extends Pool<Sandbag> {
		@Override
		protected Sandbag newObject() {
			return createSandbagActor();
		}
	}
	
	public static class SupportActorPool<T extends SupportActor> extends Pool<SupportActor> {
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