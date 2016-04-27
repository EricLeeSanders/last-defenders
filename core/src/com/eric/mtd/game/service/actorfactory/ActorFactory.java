package com.eric.mtd.game.service.actorfactory;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.*;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.actor.support.Sandbag;
import com.eric.mtd.game.model.actor.tower.*;
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
	private static GameActorPool<GameActor> towerRiflePool = new GameActorPool<GameActor>(TowerRifle.class);
	private static GameActorPool<GameActor> towerTankPool = new GameActorPool<GameActor>(TowerTank.class);
	private static GameActorPool<GameActor> towerFlameThrowerPool = new GameActorPool<GameActor>(TowerFlameThrower.class);
	private static GameActorPool<GameActor> towerTurretPool = new GameActorPool<GameActor>(TowerTurret.class);
	private static GameActorPool<GameActor> towerSniperPool = new GameActorPool<GameActor>(TowerSniper.class);
	private static GameActorPool<GameActor> towerMachinePool = new GameActorPool<GameActor>(TowerMachineGun.class);
	private static GameActorPool<GameActor> towerRocketLauncherPool = new GameActorPool<GameActor>(TowerRocketLauncher.class);
	private static GameActorPool<GameActor> enemyRiflePool = new GameActorPool<GameActor>(EnemyRifle.class);
	private static GameActorPool<GameActor> enemyTankPool = new GameActorPool<GameActor>(EnemyTank.class);
	private static GameActorPool<GameActor> enemyFlameThrowerPool = new GameActorPool<GameActor>(EnemyFlameThrower.class);
	private static GameActorPool<GameActor> enemyMachinePool = new GameActorPool<GameActor>(EnemyMachineGun.class);
	private static GameActorPool<GameActor> enemyRocketLauncherPool = new GameActorPool<GameActor>(EnemyRocketLauncher.class);
	private static GameActorPool<GameActor> enemySniperPool = new GameActorPool<GameActor>(EnemySniper.class);
	private static GameActorPool<GameActor> enemySprinterPool = new GameActorPool<GameActor>(EnemySprinter.class);
	private static GameActorPool<GameActor> enemyHumveePool = new GameActorPool<GameActor>(EnemyHumvee.class);
	private static HealthPool healthPool = new HealthPool();
	private static BulletPool bulletPool = new BulletPool();
	private static RPGPool rpgPool = new RPGPool();
	private static ExplosionPool explosionPool = new ExplosionPool();
	private static FlamePool flamePool = new FlamePool();
	private static SandbagPool sandbagPool = new SandbagPool();
	private static ApachePool apachePool = new ApachePool();

	/**
	 * Obtains a tower from the pool
	 * 
	 * @param pos
	 *            - Position to place the tower
	 * @param type
	 *            - The type of tower
	 * @return Tower
	 */
	public static Tower loadTower(Vector2 pos, String type, Group enemyGroup) {
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
	public static Enemy loadEnemy(Queue<Vector2> path, String type, boolean armor, Group towerGroup) {
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
	 * Obtain an Apache from the pool
	 * 
	 * @param pos
	 *            - Position to place the Apache
	 * @return Apache
	 */
	public static Apache loadApache(Vector2 pos) {
		Apache apache = apachePool.obtain();
		if (Logger.DEBUG)
			System.out.println("Apache obtained");
		apache.setPositionCenter(pos);
		return apache;
	}
	
	/**
	 * Create a Game Actor
	 * 
	 * @param type
	 *            - Type of Game Actor
	 * @return GameActor
	 */
	protected static GameActor createGameActor(Class<? extends GameActor> type) {
		TextureAtlas towerAtlas = Resources.getAtlas(Resources.TOWER_ATLAS);
		TextureAtlas enemyAtlas = Resources.getAtlas(Resources.ENEMY_ATLAS);
		GameActor actorType = null;
		if (type.equals(TowerRifle.class)) {
			TextureRegion rifleRegion = towerAtlas.findRegion("Rifle");
			actorType = new TowerRifle(rifleRegion, towerRiflePool);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = towerAtlas.findRegion("Rifle");
			actorType = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = towerAtlas.findRegion("Rifle");
			actorType = new TowerSniper(sniperRegion, towerSniperPool);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = towerAtlas.findRegion("Rifle");
			actorType = new TowerMachineGun(machineRegion, towerMachinePool);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = towerAtlas.findRegion("Rifle");
			actorType = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = towerAtlas.findRegion("Tank");
			TextureRegion turretRegion = towerAtlas.findRegion("TankTurret");
			actorType = new TowerTank(tankRegion, turretRegion, towerTankPool);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = towerAtlas.findRegion("TurretMachine");
			TextureRegion bagsRegion = towerAtlas.findRegion("TurretBags");
			actorType = new TowerTurret(bagsRegion, machineRegion, towerTurretPool);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] rifleRegions = new TextureRegion[3];
			rifleRegions[0] = enemyAtlas.findRegion("RifleLeft");
			rifleRegions[1] = enemyAtlas.findRegion("RifleRight");
			rifleRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemyRifle(rifleRegions, enemyRiflePool);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] flameThrowerRegions = new TextureRegion[3];
			flameThrowerRegions[0] = enemyAtlas.findRegion("RifleLeft");
			flameThrowerRegions[1] = enemyAtlas.findRegion("RifleRight");
			flameThrowerRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemyFlameThrower(flameThrowerRegions, enemyFlameThrowerPool);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = enemyAtlas.findRegion("Humvee");
			actorType = new EnemyHumvee(humveeRegion, enemyHumveePool);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] machineRegions = new TextureRegion[3];
			machineRegions[0] = enemyAtlas.findRegion("RifleLeft");
			machineRegions[1] = enemyAtlas.findRegion("RifleRight");
			machineRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemyMachineGun(machineRegions, enemyMachinePool);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] rocketLauncherRegions = new TextureRegion[3];
			rocketLauncherRegions[0] = enemyAtlas.findRegion("RifleLeft");
			rocketLauncherRegions[1] = enemyAtlas.findRegion("RifleRight");
			rocketLauncherRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemyRocketLauncher(rocketLauncherRegions, enemyRocketLauncherPool);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] sniperRegions = new TextureRegion[3];
			sniperRegions[0] = enemyAtlas.findRegion("RifleLeft");
			sniperRegions[1] = enemyAtlas.findRegion("RifleRight");
			sniperRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemySniper(sniperRegions, enemySniperPool);
		} else if (type.equals(EnemySprinter.class)) {
			TextureRegion[] sprinterRegions = new TextureRegion[3];
			sprinterRegions[0] = enemyAtlas.findRegion("RifleLeft");
			sprinterRegions[1] = enemyAtlas.findRegion("RifleRight");
			sprinterRegions[2] = enemyAtlas.findRegion("Rifle");
			actorType = new EnemySprinter(sprinterRegions, enemySprinterPool);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = enemyAtlas.findRegion("Tank");
			TextureRegion turretRegion = enemyAtlas.findRegion("TankTurret");
			actorType = new EnemyTank(tankRegion, turretRegion, enemyTankPool);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}
		if (Logger.DEBUG)
			System.out.println("Created new " + actorType);
		return actorType;

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
	protected static Apache createApacheActor() {
		Apache apache = new Apache(apachePool);
		return apache;

	}
	
	public static class GameActorPool<T extends GameActor> extends Pool<GameActor> {
		private final Class<? extends GameActor> type;

		public GameActorPool(Class<? extends GameActor> type) {
			this.type = type;
		}

		@Override
		protected GameActor newObject() {
			return createGameActor(type);
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
	
	public static class ApachePool extends Pool<Apache> {
		@Override
		protected Apache newObject() {
			return createApacheActor();
		}
	}
}