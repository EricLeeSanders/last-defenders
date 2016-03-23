package com.eric.mtd.game.model.factory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.*;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.perks.Sandbag;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.tower.*;
//Question: Doing a lot of static things here
public class ActorFactory {
	private static GameActorPool<GameActor> towerRiflePool = new GameActorPool<GameActor>(TowerRifle.class);
	private static GameActorPool<GameActor> towerTankPool = new GameActorPool<GameActor>(TowerTank.class);
	private static GameActorPool<GameActor> towerFlameThrowerPool = new GameActorPool<GameActor>(TowerFlameThrower.class);
	private static GameActorPool<GameActor> towerTurretPool = new GameActorPool<GameActor>(TowerTurret.class);
	private static GameActorPool<GameActor> towerSniperPool = new GameActorPool<GameActor>(TowerSniper.class);
	private static GameActorPool<GameActor> towerMachinePool = new GameActorPool<GameActor>(TowerMachine.class);
	private static GameActorPool<GameActor> towerRocketLauncherPool = new GameActorPool<GameActor>(TowerRocketLauncher.class);
	private static GameActorPool<GameActor> enemyRiflePool = new GameActorPool<GameActor>(EnemyRifle.class);
	private static GameActorPool<GameActor> enemyTankPool = new GameActorPool<GameActor>(EnemyTank.class);
	private static GameActorPool<GameActor> enemyFlameThrowerPool = new GameActorPool<GameActor>(EnemyFlameThrower.class);
	private static GameActorPool<GameActor> enemyMachinePool = new GameActorPool<GameActor>(EnemyMachine.class);
	private static GameActorPool<GameActor> enemyRocketLauncherPool = new GameActorPool<GameActor>(EnemyRocketLauncher.class);
	private static GameActorPool<GameActor> enemySniperPool = new GameActorPool<GameActor>(EnemySniper.class);
	private static GameActorPool<GameActor> enemySprinterPool = new GameActorPool<GameActor>(EnemySprinter.class);
	private static GameActorPool<GameActor> enemyHumveePool = new GameActorPool<GameActor>(EnemyHumvee.class);
	public static HealthPool healthPool = new HealthPool();
	public static BulletPool bulletPool = new BulletPool();
	public static RPGPool rpgPool = new RPGPool();
	public static ExplosionPool explosionPool = new ExplosionPool();
	public static FlamePool flamePool = new FlamePool();
	public static SandbagPool sandbagPool = new SandbagPool();
	public static Tower loadTower(Vector2 pos, String type){
		Tower tower = null;
		if(type.equals("Rifle")){
			tower =  (Tower) towerRiflePool.obtain();
		}
		else if(type.equals("Tank")){
			tower =  (Tower) towerTankPool.obtain();
		}
		else if(type.equals("Turret")){
			tower =  (Tower) towerTurretPool.obtain();
		}
		else if(type.equals("Sniper")){
			tower =  (Tower) towerSniperPool.obtain();
		}
		else if(type.equals("Machine")){
			tower =  (Tower) towerMachinePool.obtain();
		}
		else if(type.equals("RocketLauncher")){
			tower =  (Tower) towerRocketLauncherPool.obtain();
		}
		else if(type.equals("FlameThrower")){
			tower =  (Tower) towerFlameThrowerPool.obtain();
		}
		tower.setPositionCenter(pos);


		//GameStage.towerGroup.addActor(tower);
      return tower;
	}
	public static Enemy loadEnemy(Queue<Vector2> path, String type, boolean armor){
		Enemy enemy = null;
		if(type.equals("Rifle")){
			enemy = (Enemy) enemyRiflePool.obtain();
		}
		else if(type.equals("Tank")){
			enemy = (Enemy) enemyTankPool.obtain();
		}
		else if(type.equals("FlameThrower")){
			enemy = (Enemy) enemyFlameThrowerPool.obtain();
		}
		else if(type.equals("Machine")){
			enemy = (Enemy) enemyMachinePool.obtain();
		}
		else if(type.equals("RocketLauncher")){
			enemy = (Enemy) enemyRocketLauncherPool.obtain();
		}
		else if(type.equals("Sniper")){
			enemy = (Enemy) enemySniperPool.obtain();
		}
		else if(type.equals("Sprinter")){
			enemy = (Enemy) enemySprinterPool.obtain();
		}
		else if(type.equals("Humvee")){
			enemy = (Enemy) enemyHumveePool.obtain();
		}
		
		enemy.setPath(new LinkedList<Vector2>(path));
		enemy.setHasArmor(armor);
		
		//GameStage.enemyGroup.addActor(enemy);
      return enemy;
	}
	public static HealthBar loadHealthBar(){
		HealthBar healthBar = healthPool.obtain();
		//GameStage.healthBarGroup.addActor(healthBar);
		return healthBar;
	}
	public static Bullet loadBullet(){
		Bullet bullet = bulletPool.obtain();
		//GameStage.bulletGroup.addActor(bullet);
		return bullet;
	}
	public static RPG loadRPG(){
		RPG rpg = rpgPool.obtain();
		//GameStage.bulletGroup.addActor(bullet);
		return rpg;
	}
	public static Explosion loadExplosion(){
		Explosion explosion = explosionPool.obtain();
		//if(Logger.DEBUG)System.out.println("Explosion obtained");
		//GameStage.explosionGroup.addActor(explosion);
		return explosion;
	}
	public static Flame loadFlame(){
		Flame flame = flamePool.obtain();
		////if(Logger.DEBUG)System.out.println("Flame obtained");
		//GameStage.flameGroup.addActor(flame);
		return flame;
	}
	public static Sandbag loadSandbag(Vector2 coords){
		Sandbag sandbag = sandbagPool.obtain();
		//if(Logger.DEBUG)System.out.println("Sandbag obtained");
		sandbag.setPositionCenter(coords);
		return sandbag;
	}
	protected static GameActor createGameActor(Class<? extends GameActor> type){
		TextureAtlas towerAtlas = Resources.getAtlas(Resources.TOWER_ATLAS);
		TextureAtlas enemyAtlas = Resources.getAtlas(Resources.ENEMY_ATLAS);
      GameActor actorType = null;
      if (type.equals(TowerRifle.class)) {
  		TextureRegion rifleRegion = towerAtlas.findRegion("Rifle");
  		actorType = new TowerRifle(rifleRegion, towerRiflePool);
      } 
      else if (type.equals(TowerFlameThrower.class)) {
  		TextureRegion flameThrowerRegion = towerAtlas.findRegion("Rifle");
  		actorType = new TowerFlameThrower(flameThrowerRegion, towerFlameThrowerPool);
      } 
      else if (type.equals(TowerSniper.class)) {
  		TextureRegion sniperRegion = towerAtlas.findRegion("Rifle");
  		actorType = new TowerSniper(sniperRegion, towerSniperPool);
      } 
      else if (type.equals(TowerMachine.class)) {
  		TextureRegion machineRegion = towerAtlas.findRegion("Rifle");
  		actorType = new TowerMachine(machineRegion, towerMachinePool);
      } 
      else if (type.equals(TowerRocketLauncher.class)) {
  		TextureRegion rocketLauncherRegion = towerAtlas.findRegion("Rifle");
  		actorType = new TowerRocketLauncher(rocketLauncherRegion, towerRocketLauncherPool);
      } 
      else if (type.equals(TowerTank.class)) {
  		TextureRegion tankRegion = towerAtlas.findRegion("Tank");
  		TextureRegion turretRegion = towerAtlas.findRegion("TankTurret");
  		actorType = new TowerTank(tankRegion,turretRegion, towerTankPool);
      } 
      else if (type.equals(TowerTurret.class)) {
  		TextureRegion machineRegion = towerAtlas.findRegion("TurretMachine");
  		TextureRegion bagsRegion = towerAtlas.findRegion("TurretBags");
  		actorType = new TowerTurret(bagsRegion,machineRegion, towerTurretPool);
      } 
      else if (type.equals(EnemyRifle.class)) {
  		TextureRegion [] rifleRegions = new TextureRegion[3];
  		rifleRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		rifleRegions[1] = enemyAtlas.findRegion("RifleRight");
  		rifleRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemyRifle(rifleRegions,enemyRiflePool);
      } 
      else if (type.equals(EnemyFlameThrower.class)) {
  		TextureRegion [] flameThrowerRegions = new TextureRegion[3];
  		flameThrowerRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		flameThrowerRegions[1] = enemyAtlas.findRegion("RifleRight");
  		flameThrowerRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemyFlameThrower(flameThrowerRegions,enemyFlameThrowerPool);
      } 
      else if (type.equals(EnemyHumvee.class)) {
  		TextureRegion humveeRegion = enemyAtlas.findRegion("Humvee");
          actorType = new EnemyHumvee(humveeRegion,enemyHumveePool);
      } 
      else if (type.equals(EnemyMachine.class)) {
  		TextureRegion [] machineRegions = new TextureRegion[3];
  		machineRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		machineRegions[1] = enemyAtlas.findRegion("RifleRight");
  		machineRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemyMachine(machineRegions,enemyMachinePool);
      } 
      else if (type.equals(EnemyRocketLauncher.class)) {
  		TextureRegion [] rocketLauncherRegions = new TextureRegion[3];
  		rocketLauncherRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		rocketLauncherRegions[1] = enemyAtlas.findRegion("RifleRight");
  		rocketLauncherRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemyRocketLauncher(rocketLauncherRegions,enemyRocketLauncherPool);
      } 
      else if (type.equals(EnemySniper.class)) {
  		TextureRegion [] sniperRegions = new TextureRegion[3];
  		sniperRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		sniperRegions[1] = enemyAtlas.findRegion("RifleRight");
  		sniperRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemySniper(sniperRegions,enemySniperPool);
      } 
      else if (type.equals(EnemySprinter.class)) {
  		TextureRegion [] sprinterRegions = new TextureRegion[3];
  		sprinterRegions[0] = enemyAtlas.findRegion("RifleLeft");
  		sprinterRegions[1] = enemyAtlas.findRegion("RifleRight");
  		sprinterRegions[2] = enemyAtlas.findRegion("Rifle");
          actorType = new EnemySprinter(sprinterRegions,enemySprinterPool);
      } 
      else if (type.equals(EnemyTank.class)) {
  		TextureRegion tankRegion = enemyAtlas.findRegion("Tank");
  		TextureRegion turretRegion = enemyAtlas.findRegion("TankTurret");
          actorType = new EnemyTank(tankRegion, turretRegion,enemyTankPool);
      } 
      else {
          throw new NullPointerException("Actor factory couldn't create: " + type.getSimpleName());
      }
      //if(Logger.DEBUG)System.out.println("Created new " + actorType);
		return actorType;
		
	}
	protected static HealthBar createHealthBarActor(){
		HealthBar healthBar = new HealthBar();
      //if(Logger.DEBUG)System.out.println("Created new healthbar");
		return healthBar;
		
	}
	protected static Bullet createBulletActor(){
		Bullet bullet = new Bullet();
      //if(Logger.DEBUG)System.out.println("Created new Bullet");
		return bullet;
		
	}
	protected static RPG createRPGActor(){
		RPG rpg = new RPG();
      //if(Logger.DEBUG)System.out.println("Created new RPG");
		return rpg;
		
	}
	protected static Explosion createExplosionActor(){
		Explosion explosion = new Explosion();
      //if(Logger.DEBUG)System.out.println("Created new Explosion");
		return explosion;
		
	}
	protected static Flame createFlameActor(){
		Flame flame = new Flame();
      //if(Logger.DEBUG)System.out.println("Created new Flame");
		return flame;
		
	}
	protected static Sandbag createSandbagActor(){
		Sandbag sandbag = new Sandbag(sandbagPool);
      //if(Logger.DEBUG)System.out.println("Created new Sandbag");
		return sandbag;
		
	}
	public static class GameActorPool<T extends GameActor> extends Pool<GameActor> {
		private final Class<? extends GameActor> type;
		public GameActorPool(Class<? extends GameActor>type){
			this.type = type;
		}

		@Override
		protected GameActor newObject() {
			return createGameActor(type);
		}

	}
	public static class HealthPool extends Pool<HealthBar>{
		@Override
		protected HealthBar newObject() {
			return (HealthBar) createHealthBarActor();
		}
	}
	public static class ExplosionPool extends Pool<Explosion>{
		@Override
		protected Explosion newObject() {
			return (Explosion) createExplosionActor();
		}
	}
	public static class BulletPool extends Pool<Bullet>{
		@Override
		protected Bullet newObject() {
			return (Bullet) createBulletActor();
		}
	}
	public static class RPGPool extends Pool<RPG>{
		@Override
		protected RPG newObject() {
			return (RPG) createRPGActor();
		}
	}
	public static class FlamePool extends Pool<Flame>{
		@Override
		protected Flame newObject() {
			return (Flame) createFlameActor();
		}
	}
	public static class SandbagPool extends Pool<Sandbag>{
		@Override
		protected Sandbag newObject() {
			return (Sandbag) createSandbagActor();
		}
	}
}