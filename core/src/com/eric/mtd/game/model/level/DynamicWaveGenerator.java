package com.eric.mtd.game.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.enemy.*;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class DynamicWaveGenerator {
	private Random random = new Random();
	private int numOfFlameThrowers;
	private int numOfHumvees;
	private int numOfMachineGuns;
	private int numOfRifles;
	private int numOfRocketLaunchers;
	private int numOfSnipers;
	private int numOfSprinters;
	private int numOfTanks;
	private Map map;
	private ActorGroups actorGroups;
	public DynamicWaveGenerator(Queue<SpawningEnemy> enemies, Map map, ActorGroups actorGroups){
		this.map = map;
		this.actorGroups = actorGroups;
		Enemy enemy;
		for(SpawningEnemy spawningEnemy : enemies){
			enemy = spawningEnemy.getEnemy();
			if(enemy instanceof EnemyFlameThrower){
				numOfFlameThrowers++;
			} else if(enemy instanceof EnemyHumvee){
				numOfHumvees++;
			} else if(enemy instanceof EnemyMachineGun){
				numOfMachineGuns++;
			} else if(enemy instanceof EnemyRifle){
				numOfRifles++;
			} else if(enemy instanceof EnemyRocketLauncher){
				numOfRocketLaunchers++;
			} else if(enemy instanceof EnemySniper){
				numOfSnipers++;
			} else if(enemy instanceof EnemySprinter){
				numOfSprinters++;
			} else if(enemy instanceof EnemyTank){
				numOfTanks++;
			}
		}
		if(Logger.DEBUG) {
			System.out.println("Dynamic Wave Generator Created");
			System.out.println(numOfFlameThrowers + " flame throwers created");
			System.out.println(numOfHumvees + " humvees created");
			System.out.println(numOfMachineGuns + " machine guns created");
			System.out.println(numOfRifles + " rifles created");
			System.out.println(numOfRocketLaunchers + " rocket launchers created");
			System.out.println(numOfSnipers + " snipers created");
			System.out.println(numOfSprinters + " sprinters created");
			System.out.println(numOfTanks + " tanks created");
		}
	}
	
	/**
	 * Generate a wave.
	 * Every wave generate 3 more rifles, or 3 more machine guns, or 2 more snipers.
	 * Every 3 waves generate 2 sprinters or a humvee.
	 * Every 4 waves generate a rocket launcher or a FlameThrower & sniper.
	 * Every 6 waves, generate a Tank.
	 * 
	 * @return
	 */
	public Queue<SpawningEnemy> generateWave(int currentWave){
		int currentGeneratedWave = currentWave - Level.MAX_WAVES;
		if((currentGeneratedWave % 3) == 0) {
			int rnd = random.nextInt(2); // 0 or 1
			if(Logger.DEBUG)System.out.println("% 3 rnd: " + rnd);
			//0 - 2 sprinters
			//1 - 1 humvee
			if(rnd == 0){
				numOfSprinters += 2;
			} else {
				numOfHumvees++;
			}
		}
		
		if((currentGeneratedWave % 4) == 0 ){
			int rnd = random.nextInt(2); // 0 or 1
			if(Logger.DEBUG)System.out.println("% 4 rnd: " + rnd);
			//0 - 1 rocket launcher
			//1 - 1 flame thrower & 1 sniper
			if(rnd == 0){
				numOfRocketLaunchers++;
			} else {
				numOfFlameThrowers++;
				numOfSnipers++;
			}
		}
		if((currentGeneratedWave % 6) == 0 ){
			numOfTanks++;
		}
		
		//Every wave
		int rnd = random.nextInt(3); // 0-2
		if(Logger.DEBUG)System.out.println("% 1 rnd: " + rnd);
		//0 - 3 rifles
		//1 - 3 machine guns
		//2 - 2 snipers
		if(rnd == 0){
			numOfRifles += 3;
		} else if (rnd == 1){
			numOfMachineGuns += 3;
		} else {
			numOfSnipers += 2;
		}
		return createSpawningEnemies();
	}
	private List<Enemy> createEnemies(){
		List<Enemy> enemies = new ArrayList<Enemy>();
		enemies.addAll(createEnemiesByType(numOfRifles,"Rifle"));
		enemies.addAll(createEnemiesByType(numOfFlameThrowers,"FlameThrower"));
		enemies.addAll(createEnemiesByType(numOfHumvees,"Humvee"));
		enemies.addAll(createEnemiesByType(numOfMachineGuns,"MachineGun"));
		enemies.addAll(createEnemiesByType(numOfRocketLaunchers,"RocketLauncher"));
		enemies.addAll(createEnemiesByType(numOfSnipers,"Sniper"));
		enemies.addAll(createEnemiesByType(numOfSprinters,"Sprinter"));
		enemies.addAll(createEnemiesByType(numOfTanks,"Tank"));
		Collections.shuffle(enemies);
		return enemies;
	}
	private List<Enemy> createEnemiesByType(int n, String type) {
		List<Enemy> enemies = new ArrayList<Enemy>();
		int randArmor;
		boolean armor;
		for(int i = 0; i < n; i++){
			randArmor = random.nextInt(3); //0-2
			if(Logger.DEBUG)System.out.println("randArmor: " + randArmor);
			armor = (randArmor == 0) ? true : false;
			if(Logger.DEBUG)System.out.println("Armor: " + armor);
			enemies.add(ActorFactory.loadEnemy(map.getPath(), type, armor, actorGroups.getTowerGroup(), actorGroups.getProjectileGroup()));
		}
		if(Logger.DEBUG)System.out.println(n + " " + type + " created");
		return enemies;
	}
	private Queue<SpawningEnemy> createSpawningEnemies(){
		Queue<SpawningEnemy> spawningEnemies = new LinkedList<SpawningEnemy>();
		List<Enemy> enemies = createEnemies();
		float randDelay;
		for(Enemy enemy : enemies){
			randDelay = random.nextFloat() * 1.5f + 0.25f; // .25 - 1.75
			if(Logger.DEBUG)System.out.println("randDelay: " + randDelay);
			spawningEnemies.add(new SpawningEnemy(enemy, randDelay));
		}
		return spawningEnemies;
	}
}
