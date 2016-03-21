package com.eric.mtd.model.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.helper.CollisionDetection;
import com.eric.mtd.model.actor.enemy.Enemy;
import com.eric.mtd.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.model.actor.tower.Tower;
import com.eric.mtd.model.stage.GameStage;

public class EnemyAI {
	public static Tower findNearestTower(Enemy enemy, Group towerGroup){
		if(towerGroup.getChildren().size == 0){
			return null;
		}
		float firstTowerDistance;
		Tower firstTower;
		SnapshotArray<Actor> towers;
		towers = towerGroup.getChildren();
		firstTower = null;
		firstTowerDistance = Integer.MAX_VALUE;
		
		for(Actor tower : towers){
			if(tower instanceof Tower){
				if(!((Tower)tower).isInactive() && ((Tower)tower).isActive()){
					if(CollisionDetection.targetWithinRange(((Tower) tower).getBody(),enemy.getRangeShape())){
						if(((Tower) tower).getPositionCenter().dst(enemy.getPositionCenter()) < firstTowerDistance){
							if(!(tower instanceof IPlatedArmor)){
								firstTower = (Tower) tower;
								firstTowerDistance = ((Tower) tower).getPositionCenter().dst(enemy.getPositionCenter());
							}
						}
					}
				}
			}
		}
		return firstTower;
	}
}