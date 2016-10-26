package com.foxholedefense.game.model.level;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;

public class SpawningEnemy {
	private Enemy enemy;
	private float delay;
	
	public SpawningEnemy(){
		
	}
	
	public SpawningEnemy(Enemy enemy, float delay){
		this.setEnemy(enemy);
		this.setDelay(delay);
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public float getDelay() {
		return delay;
	}

	public void setDelay(float delay) {
		this.delay = delay;
	}
}
