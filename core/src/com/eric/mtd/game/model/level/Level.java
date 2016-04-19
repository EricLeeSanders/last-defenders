package com.eric.mtd.game.model.level;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class Level implements ILevelStateObserver {
	private float delayCount = 0;
	private float enemyDelay = 0f;
	private Map map;
	private LevelStateManager levelStateManager;
	private int currentWave = 1;
	private Queue<Enemy> enemies;
	private Queue<Float> delays;
	private int intLevel;
	private ActorGroups actorGroups;

	public Level(int level, LevelStateManager levelStateManager, ActorGroups actorGroups) {
		this.intLevel = level;
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.actorGroups = actorGroups;
		map = new Map(level);
	}

	/**
	 * Updated each frame. Spawns enemies when the Level State is
	 * SPAWNING_ENEMIES
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		if (delayCount >= enemyDelay && (levelStateManager.getState().equals(LevelState.SPAWNING_ENEMIES))) {
			if (enemies.isEmpty()) {
				levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
				delayCount = 0;
				enemyDelay = 0;
			} else {
				delayCount = 0;
				actorGroups.getEnemyGroup().addActor(enemies.remove());
				enemyDelay = delays.remove();
				if (Logger.DEBUG)
					System.out.println("Spawning Enemy");
			}
		} else {
			delayCount += delta;
		}
	}

	/**
	 * Loads the wave
	 */
	public void loadWave() {
		JsonValue json = new JsonReader().parse(Gdx.files.internal("game/levels/level" + intLevel + "/waves/wave" + currentWave + ".json"));
		enemies = new LinkedList<Enemy>();
		delays = new LinkedList<Float>();
		JsonValue enemiesJson = json.get("wave");
		for (JsonValue enemyJson : enemiesJson.iterator()) {
			Enemy enemy = ActorFactory.loadEnemy(map.getPath(), enemyJson.getString("enemy"), enemyJson.getBoolean("armor"), actorGroups.getTowerGroup());
			enemies.add(enemy);
			HealthBar healthBar = ActorFactory.loadHealthBar();
			healthBar.setActor(enemy);
			actorGroups.getHealthBarGroup().addActor(healthBar);
			delays.add(enemyJson.getFloat("delay"));

		}
		if (Logger.DEBUG)
			System.out.println("Next Wave loaded");
		currentWave++;
	}

	public int getCurrentWave() {
		return currentWave;
	}

	@Override
	public void changeLevelState(LevelState state) {
		switch (state) {
		case SPAWNING_ENEMIES:
			loadWave();
			break;
		default:
			break;
		}

	}
}
