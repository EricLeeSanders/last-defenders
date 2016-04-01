package com.eric.mtd.game.model.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;

public class Level implements ILevelStateObserver{
	private float delayCount = 0;
	private float enemyDelay = 0f;
	private Map map;
	private LevelStateManager levelStateManager;
	private int currentWave = 1;
	private final int startingLives = 20;
	private final int startingMoney = 1000;
	private Queue<Enemy> enemies;
	private Queue<Float> delays;
	private int numOfWaves = 3;
	private int level;
	public static float totalSpawningDelta;
	private ActorGroups actorGroups;
	public Level(int level, LevelStateManager levelStateManager, ActorGroups actorGroups){
		this.level = level;
		this.levelStateManager = levelStateManager;
		this.actorGroups = actorGroups;
		map = new Map(level);
	}
	public void update(float delta) {
		if(delayCount >= enemyDelay && (levelStateManager.getState().equals(LevelState.SPAWNING_ENEMIES))){
			if(enemies.isEmpty()){
				levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
				delayCount = 0;
				enemyDelay = 0;
			}
			else{
				////if(Logger.DEBUG)System.out.println("DelayCount: " + delayCount);
				delayCount = 0;
				actorGroups.getEnemyGroup().addActor(enemies.remove());
				enemyDelay = delays.remove(); //Question: Cause lag? Should I use linked list and just iterate?
				////if(Logger.DEBUG)System.out.println("EnemyDelay: " + enemyDelay);
			}
		}
		else{
			delayCount += delta;
		}
		/*if(levelStateManager.getState().equals(LevelState.SPAWNING_ENEMIES)){
			totalSpawningDelta += delta;
		}*/
	}
	public int getLives(){
		return startingLives;
	}
	public int getMoney(){
		return startingMoney;
	}
	public void loadNextWave(){
		JsonValue json = new JsonReader().parse( Gdx.files.internal("levels/level"+level+"/waves/wave"+currentWave+".json"));
		enemies = new LinkedList<Enemy>();   //Question will the old list of enemies still persist? Do I  need to set Enemies to null first?
		delays = new LinkedList<Float>();
		JsonValue enemiesJson = json.get("wave");
		for (JsonValue enemyJson : enemiesJson.iterator()) // iterator() returns a list of children
		{
			Enemy enemy = ActorFactory.loadEnemy(map.getPath(),enemyJson.getString("enemy"),enemyJson.getBoolean("armor"));
			enemies.add(enemy);
			HealthBar healthBar = ActorFactory.loadHealthBar();
			healthBar.setActor(enemy, actorGroups);
			
			delays.add(enemyJson.getFloat("delay"));
		
		}
		if(Logger.DEBUG) System.out.println("Next Wave loaded");		
		currentWave++;
	}
	public int getCurrentWave(){
		return currentWave;
	}
	public int getNumOfWaves(){
		return numOfWaves;
	}
	@Override
	public void changeLevelState(LevelState state) {
		// TODO Auto-generated method stub
		
	}
}
