package com.foxholedefense.game.model.level;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;

public class Level{

	public static final int MAX_WAVES = 1;

	private float delayCount = 0;
	private float enemyDelay = 0f;
	private Map map;
	private int currentWave = 0;
	private Queue<SpawningEnemy> spawningEnemyQueue;
	private int intLevel;
	private ActorGroups actorGroups;
	private CombatActorFactory combatActorFactory;
	private HealthFactory healthFactory;
	private DynamicWaveGenerator waveGenerator;

	public Level(int level, ActorGroups actorGroups, CombatActorFactory combatActorFactory, HealthFactory healthFactory, Map map) {
		this.intLevel = level;
		this.actorGroups = actorGroups;
		this.healthFactory = healthFactory;
		this.combatActorFactory = combatActorFactory;
		this.map = map;
	}

	/**
	 * Spwan enemies
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		if(spawningEnemyQueue.size() > 0){
			if (delayCount >= enemyDelay ) {
				Logger.info("Level: Spawning Enemy");
				delayCount = 0;
				SpawningEnemy enemy = spawningEnemyQueue.remove();
				actorGroups.getEnemyGroup().addActor(enemy.getEnemy());
				HealthBar healthBar = healthFactory.loadHealthBar();
				healthBar.setActor(enemy.getEnemy());
				ArmorIcon armorIcon = healthFactory.loadArmorIcon();
				armorIcon.setActor(enemy.getEnemy());
				enemy.getEnemy().init();
				enemyDelay = enemy.getDelay();
			} else {
				delayCount += delta;
			}
		}
	}

	/**
	 * Loads the wave
	 */
	public void loadWave() {

		currentWave++;
		delayCount = 0;
		enemyDelay = 0;
		if(currentWave > MAX_WAVES){
			generateWave();
		}
		else {
			loadWaveFromJSON();
			//If we are on the last wave, then construct
			//The DynamicWaveGenerator
			if(currentWave == MAX_WAVES){
				waveGenerator = new DynamicWaveGenerator(spawningEnemyQueue, map, actorGroups, combatActorFactory);
			}
		}
	}
	
	private void generateWave(){
		spawningEnemyQueue = waveGenerator.generateWave(currentWave);
	}
	private void loadWaveFromJSON(){
		Logger.info("Level: Loading Wave from JSON");
		JsonValue json = new JsonReader().parse(Gdx.files.internal("game/levels/level" + intLevel + "/waves/wave" + currentWave + ".json"));
		spawningEnemyQueue = new LinkedList<SpawningEnemy>();
		JsonValue enemiesJson = json.get("wave");
		Array<FHDVector2> enemyPath = map.getPath();
		for (JsonValue enemyJson : enemiesJson.iterator()) {
			Enemy enemy = combatActorFactory.loadEnemy(enemyJson.getString("enemy"));
			enemy.setPath(enemyPath);
			enemy.setHasArmor(enemyJson.getBoolean("armor"));
			float delay = enemyJson.getFloat("delay");
			SpawningEnemy spawningEnemy = new SpawningEnemy(enemy, delay);
			spawningEnemyQueue.add(spawningEnemy);
		}
	}

	public int getSpawningEnemiesCount(){
		return spawningEnemyQueue.size();
	}
	
	public int getCurrentWave() {
		return currentWave;
	}

}
