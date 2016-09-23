package com.eric.mtd.game.model.level;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class Level{
	public static final int MAX_WAVES = 1;
	private float delayCount = 0;
	private float enemyDelay = 0f;
	private Map map;
	private int currentWave = 0;
	private Queue<SpawningEnemy> spawningEnemyQueue;
	private int intLevel;
	private ActorGroups actorGroups;
	private ActorFactory actorFactory;
	private DynamicWaveGenerator waveGenerator;

	public Level(int level, ActorGroups actorGroups, ActorFactory actorFactory, Map map) {
		this.intLevel = level;
		this.actorGroups = actorGroups;
		this.actorFactory = actorFactory;
		this.map = map;
		loadWave();
	}

	/**
	 * Updated each frame. Spawns enemies when the Level State is
	 * SPAWNING_ENEMIES
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		if(spawningEnemyQueue.size() > 0){
			if (delayCount >= enemyDelay ) {
				delayCount = 0;
				SpawningEnemy enemy = spawningEnemyQueue.remove();
				actorGroups.getEnemyGroup().addActor(enemy.getEnemy());
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
				waveGenerator = new DynamicWaveGenerator(spawningEnemyQueue, map, actorGroups, actorFactory);
			}
		}
	}
	
	private void generateWave(){
		spawningEnemyQueue = waveGenerator.generateWave(currentWave);
	}
	private void loadWaveFromJSON(){
		JsonValue json = new JsonReader().parse(Gdx.files.internal("game/levels/level" + intLevel + "/waves/wave" + currentWave + ".json"));
		spawningEnemyQueue = new LinkedList<SpawningEnemy>();
		JsonValue enemiesJson = json.get("wave");
		for (JsonValue enemyJson : enemiesJson.iterator()) {
			Enemy enemy = actorFactory.loadEnemy(map.getPath(), enemyJson.getString("enemy"), enemyJson.getBoolean("armor"), actorGroups.getTowerGroup(), actorGroups.getProjectileGroup());
			float delay = enemyJson.getFloat("delay");
			SpawningEnemy spawningEnemy = new SpawningEnemy(enemy, delay);
			spawningEnemyQueue.add(spawningEnemy);
			HealthBar healthBar = actorFactory.loadHealthBar();
			healthBar.setActor(enemy);
			actorGroups.getHealthBarGroup().addActor(healthBar);

		}
	}
	public int getCurrentWave() {
		return currentWave;
	}
}
