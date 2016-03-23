package com.eric.mtd.game.model.ai;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.stage.GameStage;

/*
 * Chooses which Enemies to attack
 */
public final class TowerAI {
	public static Enemy findLastEnemy(Tower tower, Group enemyGroup){
		if(enemyGroup.getChildren().size == 0){
			return null;
		}
		float lastEnemyDistance;
		Enemy lastEnemy,platedEnemy;
		SnapshotArray<Actor> enemies;
		enemies = enemyGroup.getChildren();
		lastEnemy = null;
		platedEnemy = null;
		lastEnemyDistance = 0;
		for(Actor enemy : enemies){
			if(enemy instanceof Enemy){
				if(!((Enemy)enemy).isInactive()){
					if(CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(),tower.getRangeShape())){
						if(((Enemy) enemy).lengthTillEnd() > lastEnemyDistance){
							if((!(enemy instanceof IPlatedArmor))||(tower instanceof IRPG)){
								lastEnemy = (Enemy) enemy;
								lastEnemyDistance = ((Enemy)enemy).lengthTillEnd();
							}
							else{
								platedEnemy = (Enemy) enemy; //Attack the plated enemy if there are no eligible enemies
							}
						}
					}
				}
			}
		}
		//If there is no enemy then return plated enemy so the tower has something to attack
		if(lastEnemy == null){
			return platedEnemy;
		}
		else{
			return lastEnemy;
		}
			
	}
	
	public static GameActor findFirstEnemy(Tower tower, Group enemyGroup){
		if(enemyGroup.getChildren().size == 0){
			return null;
		}
		float firstEnemyDistance;
		Enemy firstEnemy, platedEnemy;
		SnapshotArray<Actor> enemies;
		enemies = enemyGroup.getChildren();
		firstEnemy = null;
		platedEnemy = null;
		firstEnemyDistance = Integer.MAX_VALUE;
		for(Actor enemy : enemies){
			if(enemy instanceof Enemy){
				if(!((Enemy)enemy).isInactive()){
					if(CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(),tower.getRangeShape())){
						if(((Enemy) enemy).lengthTillEnd() < firstEnemyDistance){
							if((!(enemy instanceof IPlatedArmor))||(tower instanceof IRPG)){
								firstEnemy = (Enemy) enemy;
								firstEnemyDistance = ((Enemy)enemy).lengthTillEnd();
							}
							else{
								platedEnemy = (Enemy) enemy; //Attack the plated enemy if there are no eligible enemies
							}
						}
					}
				}
			}
		}
		//If there is no enemy then return plated enemy so the tower has something to attack
		if(firstEnemy == null){
			return platedEnemy;
		}
		else{
			return firstEnemy;
		}
	}
	
	//Enemy with lowest health
	public static Enemy findWeakestEnemy(Tower tower, Group enemyGroup){
		if(enemyGroup.getChildren().size == 0){
			return null;
		}
		float weakestEnemyHealth;
		Enemy weakestEnemy, platedEnemy;
		SnapshotArray<Actor> enemies;
		enemies = enemyGroup.getChildren();
		weakestEnemy = null;
		platedEnemy = null;
		weakestEnemyHealth = Integer.MAX_VALUE;
		for(Actor enemy : enemies){
			if(enemy instanceof Enemy){
				if(!((Enemy)enemy).isInactive()){
					if(CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(),tower.getRangeShape())){
						if(((Enemy) enemy).getHealth() < weakestEnemyHealth){
							if((!(enemy instanceof IPlatedArmor))||(tower instanceof IRPG)){
								weakestEnemy = (Enemy) enemy;
								weakestEnemyHealth = ((Enemy)enemy).getHealth();
							}
							else{
								platedEnemy = (Enemy) enemy; //Attack the plated enemy if there are no eligible enemies
							}
						}
					}
				}
			}
		}
		if(weakestEnemy == null){
			return platedEnemy;
		}
		else{
			return weakestEnemy;
		}
			
	}
	
	//Enemy with highest attack
	public static Enemy findStrongestEnemy(Tower tower, Group enemyGroup){
		if(enemyGroup.getChildren().size == 0){
			return null;
		}
		float strongestEnemyAttack;
		Enemy strongestEnemy, platedEnemy;
		SnapshotArray<Actor> enemies;
		enemies = enemyGroup.getChildren();
		strongestEnemy = null;
		platedEnemy = null;
		strongestEnemyAttack = 0;
		for(Actor enemy : enemies){
			if(enemy instanceof Enemy){
				if(!((Enemy)enemy).isInactive()){
					if(CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(),tower.getRangeShape())){
						if(((Enemy) enemy).getAttack() > strongestEnemyAttack){
							if((!(enemy instanceof IPlatedArmor))||(tower instanceof IRPG)){
								strongestEnemy = (Enemy) enemy;
								strongestEnemyAttack = ((Enemy)enemy).getAttack();
							}
							else{
								platedEnemy = (Enemy) enemy; //Attack the plated enemy if there are no eligible enemies
							}
						}
					}
				}
			}
		}
		if(strongestEnemy == null){
			return platedEnemy;
		}
		else{
			return strongestEnemy;
		}
			
	}
	
}
