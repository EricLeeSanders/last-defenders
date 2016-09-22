package com.eric.mtd.game.service.actorfactory;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.ActorGroups;
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
import com.eric.mtd.game.model.actor.support.SupportActor;
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
public class ActorFactory {
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
	private CombatActorPool<CombatActor> enemySprinterPool;
	private CombatActorPool<CombatActor> enemyHumveePool;
	private HealthPool healthPool = new HealthPool();
	private BulletPool bulletPool = new BulletPool();
	private RPGPool rpgPool = new RPGPool();
	private AirStrikeBombPool airStrikeBombPool = new AirStrikeBombPool();
	private ExplosionPool explosionPool = new ExplosionPool();
	private FlamePool flamePool = new FlamePool();
	private SupportActorPool<Apache> apachePool = new SupportActorPool<Apache>(Apache.class);
	private SupportActorPool<AirStrike> airStrikePool = new SupportActorPool<AirStrike>(AirStrike.class);
	private SupportActorPool<LandMine> landMinePool = new SupportActorPool<LandMine>(LandMine.class);
	private TextureAtlas actorAtlas;
	private MTDAudio audio;
	public ActorFactory(ActorGroups actorGroups, TextureAtlas actorAtlas, MTDAudio audio){
		this.actorAtlas = actorAtlas;
		this.audio = audio;
		initPools(actorGroups);
		
	}
	public void  initPools(ActorGroups actorGroups){
		towerRiflePool = new CombatActorPool<CombatActor>(TowerRifle.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerTankPool = new CombatActorPool<CombatActor>(TowerTank.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerFlameThrowerPool = new CombatActorPool<CombatActor>(TowerFlameThrower.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerTurretPool = new CombatActorPool<CombatActor>(TowerTurret.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerSniperPool = new CombatActorPool<CombatActor>(TowerSniper.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerMachinePool = new CombatActorPool<CombatActor>(TowerMachineGun.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		towerRocketLauncherPool = new CombatActorPool<CombatActor>(TowerRocketLauncher.class, actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		enemyRiflePool = new CombatActorPool<CombatActor>(EnemyRifle.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemyTankPool = new CombatActorPool<CombatActor>(EnemyTank.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemyFlameThrowerPool = new CombatActorPool<CombatActor>(EnemyFlameThrower.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemyMachinePool = new CombatActorPool<CombatActor>(EnemyMachineGun.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemyRocketLauncherPool = new CombatActorPool<CombatActor>(EnemyRocketLauncher.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemySniperPool = new CombatActorPool<CombatActor>(EnemySniper.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemySprinterPool = new CombatActorPool<CombatActor>(EnemySprinter.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
		enemyHumveePool = new CombatActorPool<CombatActor>(EnemyHumvee.class, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
	}
	/**
	 * Obtains a tower from the pool
	 * 
	 * @param pos
	 *            - Position to place the tower
	 * @param type
	 *            - The type of tower
	 * @return Tower
	 */
	public Tower loadTower(Vector2 pos, String type, Group enemyGroup, Group projectileGroup) {
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
		tower.setPositionCenter(pos);
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
	public Enemy loadEnemy(Queue<Vector2> path, String type, boolean armor, Group towerGroup, Group projectileGroup) {
		Enemy enemy = null;
		if (type.equals("Rifle")) {
			enemy = (Enemy) enemyRiflePool.obtain();
		} else if (type.equals("Tank")) {
			enemy = (Enemy) enemyTankPool.obtain();
		} else if (type.equals("FlameThrower")) {
			enemy = (Enemy) enemyFlameThrowerPool.obtain();
		} else if (type.equals("MachineGun")) {
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
		return enemy;
	}

	/**
	 * Obtains a health bar from the pool
	 * 
	 * @return HealthBar
	 */
	public HealthBar loadHealthBar() {
		HealthBar healthBar = healthPool.obtain();
		return healthBar;
	}

	/**
	 * Obtains a bullet from the pool
	 * 
	 * @return Bullet
	 */
	public Bullet loadBullet() {
		Bullet bullet = bulletPool.obtain();
		return bullet;
	}

	/**
	 * Obtains an RPG from the pool
	 * 
	 * @return RPG
	 */
	public RPG loadRPG() {
		RPG rpg = rpgPool.obtain();
		return rpg;
	}
	
	/**
	 * Obtains an AirStrike Bomb from the pool
	 * 
	 * @return AirStrikeBomb
	 */
	public AirStrikeBomb loadAirStrikeBomb() {
		AirStrikeBomb airStrikeBomb = airStrikeBombPool.obtain();
		return airStrikeBomb;
	}

	/**
	 * Obtains an Explosion from the pool
	 * 
	 * @return Explosion
	 */
	public Explosion loadExplosion() {
		Explosion explosion = explosionPool.obtain();
		return explosion;
	}

	/**
	 * Obtains a flame from the pool
	 * 
	 * @return Flame
	 */
	public Flame loadFlame() {
		Flame flame = flamePool.obtain();
		return flame;
	}

	/**
	 * Obtain a Support Actor from the pool
	 * 
	 * @param pos
	 *            - Position to place the Support Actor
	 * @return Support Actor
	 */
	public SupportActor loadSupportActor(Vector2 pos, String type, Group enemyGroup, Group projectileGroup) {
		SupportActor supportActor = null;
		if (type.equals("Apache")) {
			supportActor = apachePool.obtain();
		} else if(type.equals("AirStrike")) {
			supportActor = airStrikePool.obtain();
		} else if(type.equals("LandMine")) {
			supportActor = landMinePool.obtain();
		}
		supportActor.setPositionCenter(pos);
		supportActor.setEnemyGroup(enemyGroup);
		supportActor.setProjectileGroup(projectileGroup);
		return supportActor;
	}
	
	/**
	 * Create a Game Actor
	 * 
	 * @param type - Type of Game Actor
	 * @return CombatActor
	 */
	protected CombatActor createCombatActor(Class<? extends CombatActor> type, Group targetGroup, Group projectileGroup) {
		CombatActor actor = null;
		if (type.equals(TowerRifle.class)) {
			TextureRegion rifleRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerRifle(rifleRegion, towerRiflePool, bulletPool, audio);
		} else if (type.equals(TowerFlameThrower.class)) {
			TextureRegion flameThrowerRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool, flamePool, audio);
		} else if (type.equals(TowerSniper.class)) {
			TextureRegion sniperRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerSniper(sniperRegion, towerSniperPool, bulletPool, audio);
		} else if (type.equals(TowerMachineGun.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerMachineGun(machineRegion, towerMachinePool, bulletPool, audio);
		} else if (type.equals(TowerRocketLauncher.class)) {
			TextureRegion rocketLauncherRegion = actorAtlas.findRegion("Rifle");
			actor = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool, rpgPool, audio);
		} else if (type.equals(TowerTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			actor = new TowerTank(tankRegion, turretRegion, towerTankPool, rpgPool);
		} else if (type.equals(TowerTurret.class)) {
			TextureRegion machineRegion = actorAtlas.findRegion("TurretMachine");
			TextureRegion bagsRegion = actorAtlas.findRegion("TurretBags");
			actor = new TowerTurret(bagsRegion, machineRegion, towerTurretPool, bulletPool, audio);
		} else if (type.equals(EnemyRifle.class)) {
			TextureRegion[] rifleRegions = new TextureRegion[3];
			rifleRegions[0] = actorAtlas.findRegion("RifleLeft");
			rifleRegions[1] = actorAtlas.findRegion("RifleRight");
			rifleRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyRifle(rifleRegions, enemyRiflePool, bulletPool, audio);
		} else if (type.equals(EnemyFlameThrower.class)) {
			TextureRegion[] flameThrowerRegions = new TextureRegion[3];
			flameThrowerRegions[0] = actorAtlas.findRegion("RifleLeft");
			flameThrowerRegions[1] = actorAtlas.findRegion("RifleRight");
			flameThrowerRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyFlameThrower(flameThrowerRegions, enemyFlameThrowerPool, flamePool, audio);
		} else if (type.equals(EnemyHumvee.class)) {
			TextureRegion humveeRegion = actorAtlas.findRegion("Humvee");
			actor = new EnemyHumvee(humveeRegion, enemyHumveePool);
		} else if (type.equals(EnemyMachineGun.class)) {
			TextureRegion[] machineRegions = new TextureRegion[3];
			machineRegions[0] = actorAtlas.findRegion("RifleLeft");
			machineRegions[1] = actorAtlas.findRegion("RifleRight");
			machineRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyMachineGun(machineRegions, enemyMachinePool, bulletPool, audio);
		} else if (type.equals(EnemyRocketLauncher.class)) {
			TextureRegion[] rocketLauncherRegions = new TextureRegion[3];
			rocketLauncherRegions[0] = actorAtlas.findRegion("RifleLeft");
			rocketLauncherRegions[1] = actorAtlas.findRegion("RifleRight");
			rocketLauncherRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemyRocketLauncher(rocketLauncherRegions, enemyRocketLauncherPool, rpgPool, audio);
		} else if (type.equals(EnemySniper.class)) {
			TextureRegion[] sniperRegions = new TextureRegion[3];
			sniperRegions[0] = actorAtlas.findRegion("RifleLeft");
			sniperRegions[1] = actorAtlas.findRegion("RifleRight");
			sniperRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemySniper(sniperRegions, enemySniperPool, bulletPool, audio);
		} else if (type.equals(EnemySprinter.class)) {
			TextureRegion[] sprinterRegions = new TextureRegion[3];
			sprinterRegions[0] = actorAtlas.findRegion("RifleLeft");
			sprinterRegions[1] = actorAtlas.findRegion("RifleRight");
			sprinterRegions[2] = actorAtlas.findRegion("Rifle");
			actor = new EnemySprinter(sprinterRegions, enemySprinterPool);
		} else if (type.equals(EnemyTank.class)) {
			TextureRegion tankRegion = actorAtlas.findRegion("Tank");
			TextureRegion turretRegion = actorAtlas.findRegion("TankTurret");
			actor = new EnemyTank(tankRegion, turretRegion, enemyTankPool, rpgPool);
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}
		actor.setTargetGroup(targetGroup);
		actor.setProjectileGroup(projectileGroup);
		return actor;
	}

	/**
	 * Create a Health Bar
	 * 
	 * @return Health Bar
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
		Explosion explosion = new Explosion(explosionPool, actorAtlas, audio);
		return explosion;

	}

	/**
	 * Create a Flame
	 * 
	 * @return Flame
	 */
	protected Flame createFlameActor() {
		Flame flame = new Flame(flamePool, actorAtlas);
		return flame;

	}


	/**
	 * Create an Apache
	 * 
	 * @return Apache
	 */
	protected SupportActor createSupportActor(Class<? extends SupportActor> type) {
		
		if (type.equals(Apache.class)) {
			TextureRegion [] textureRegions = new TextureRegion[3];
			textureRegions[0] = actorAtlas.findRegion("apache1");
			textureRegions[1] = actorAtlas.findRegion("apache2");
			textureRegions[2] = actorAtlas.findRegion("apache3");
			return new Apache(apachePool, bulletPool, textureRegions, audio);
		} else if(type.equals(AirStrike.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("airstrike");
			return new AirStrike(airStrikePool, airStrikeBombPool, textureRegion, audio);	
		} else if (type.equals(LandMine.class)){
			TextureRegion textureRegion = actorAtlas.findRegion("landmine");
			return new LandMine(landMinePool,explosionPool, textureRegion);	
		} else {
			throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
		}

	}
	
	public class CombatActorPool<T extends CombatActor> extends Pool<CombatActor> {
		private final Class<? extends CombatActor> type;
		private final Group targetGroup;
		private final Group projectileGroup;
		public CombatActorPool(Class<? extends CombatActor> type, Group targetGroup, Group projectileGroup){
			this.type = type;
			this.targetGroup = targetGroup;
			this.projectileGroup = projectileGroup;
		}

		@Override
		protected CombatActor newObject() {
			return createCombatActor(type, targetGroup, projectileGroup);
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