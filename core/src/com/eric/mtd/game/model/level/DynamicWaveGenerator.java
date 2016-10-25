package com.eric.mtd.game.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.enemy.*;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.util.Logger;

public class DynamicWaveGenerator {
	private Random random = new Random();
	private java.util.Map<Class<? extends Enemy>, Integer> towerMap = new HashMap<Class<? extends Enemy>, Integer>();
	private Map map;
	private ActorGroups actorGroups;
	private ActorFactory actorFactory;
	public DynamicWaveGenerator(Queue<SpawningEnemy> enemies, Map map, ActorGroups actorGroups, ActorFactory actorFactory){
		this.map = map;
		this.actorGroups = actorGroups;
		this.actorFactory = actorFactory;
		for(SpawningEnemy spawningEnemy : enemies){
			incrementTowerMapCount(spawningEnemy.getEnemy().getClass(), 1);
		}
	}
	
	/**
	 * Generate a wave.
	 * Every wave generate 3 more rifles, or 3 more machine guns, or 2 more snipers.
	 * Every 3 waves generate a humvee.
	 * Every 4 waves generate a rocket launcher or a FlameThrower & sniper.
	 * Every 6 waves, generate a Tank.
	 * 
	 * @return
	 */
	public Queue<SpawningEnemy> generateWave(int currentWave){
		int currentGeneratedWave = currentWave - Level.MAX_WAVES;
		if((currentGeneratedWave % 3) == 0) {
			//Add humvee
			incrementTowerMapCount(EnemyHumvee.class, 1);
		}
		
		if((currentGeneratedWave % 4) == 0 ){
			int rnd = random.nextInt(2); // 0 or 1
			//0 - 1 rocket launcher
			//1 - 1 flame thrower & 1 sniper
			if(rnd == 0){
				incrementTowerMapCount(EnemyRocketLauncher.class, 1);
			} else {
				incrementTowerMapCount(EnemyFlameThrower.class, 1);
				incrementTowerMapCount(EnemySniper.class, 1);
			}
		}
		if((currentGeneratedWave % 6) == 0 ){
			//Add tank
			incrementTowerMapCount(EnemyTank.class, 2);
		}
		
		//Every wave
		int rnd = random.nextInt(3); // 0-2
		//0 - 3 rifles
		//1 - 3 machine guns
		//2 - 2 snipers
		if(rnd == 0){
			incrementTowerMapCount(EnemyRifle.class, 3);
		} else if (rnd == 1){
			incrementTowerMapCount(EnemyMachineGun.class, 3);
		} else {
			incrementTowerMapCount(EnemySniper.class, 2);
		}
		return createSpawningEnemies();
	}
	
	private void incrementTowerMapCount(Class<? extends Enemy> enemyClass, int amount){
		Integer count = towerMap.get(enemyClass);
		if(count == null){
			count = 0;
		}
		count += amount;
		towerMap.put(enemyClass, count);
	}
	
	private SnapshotArray<Enemy> createEnemies(){
		SnapshotArray<Enemy> enemies = new SnapshotArray<Enemy>();
		Iterator<Entry<Class<? extends Enemy>, Integer>> iter = towerMap.entrySet().iterator();
		while(iter.hasNext()){
	        java.util.Map.Entry<Class<? extends Enemy>, Integer> entry = iter.next();
	        enemies.addAll(createEnemiesByType(entry.getValue(), entry.getKey()));
		}
		shuffle(enemies);
		return enemies;
	}
	private SnapshotArray<Enemy> createEnemiesByType(int n, Class<? extends Enemy> enemyClass) {
		SnapshotArray<Enemy> enemies = new SnapshotArray<Enemy>();
		Queue<Vector2> enemyPath = map.getPath();
		for(int i = 0; i < n; i++){
			String type = enemyClass.getSimpleName();
			int randArmor = random.nextInt(3); //0-2
			boolean armor = (randArmor == 0) ? true : false;
			Enemy enemy = actorFactory.loadEnemy(type);
			enemy.setPath(new LinkedList<Vector2>(enemyPath));
			enemy.setHasArmor(armor);
			HealthBar healthBar = actorFactory.loadHealthBar();
			healthBar.setActor(enemy);
			enemies.add(enemy);
		}
		return enemies;
	}
	private Queue<SpawningEnemy> createSpawningEnemies(){
		Queue<SpawningEnemy> spawningEnemies = new LinkedList<SpawningEnemy>();
		SnapshotArray<Enemy> enemies = createEnemies();
		float randDelay;
		for(Enemy enemy : enemies){
			randDelay = random.nextFloat() * 1.5f + 0.25f; // .25 - 1.75
			spawningEnemies.add(new SpawningEnemy(enemy, randDelay));
		}
		return spawningEnemies;
	}
	
	public void shuffle(SnapshotArray<Enemy> enemies) {
		int n = enemies.size;
		for (int i = 0; i < n; i++) {
			int rand = i + (int) (Math.random() * (n - i)); // between i and n-1
			swap(enemies, i, rand);
		}
	}

	private void swap(SnapshotArray<Enemy> enemies, int i, int j) {
		enemies.swap(i, j);
	}
}
