package com.eric.mtd.game.model.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

/**
 * A class that holds all of the various Actor Groups that are placed on the
 * game stage.
 * 
 * @author Eric
 *
 */
public class ActorGroups{
	private Group projectileGroup;
	private Group enemyGroup;
	private Group towerGroup;
	private Group healthBarGroup;
	private Group supportGroup;
	private Group landmineGroup;
	
	public ActorGroups(){
		projectileGroup = new Group();
		enemyGroup = new Group();
		towerGroup = new Group();
		healthBarGroup = new Group();
		supportGroup = new Group();
		landmineGroup = new Group();
		
		projectileGroup.setTransform(false);
		enemyGroup.setTransform(false);
		towerGroup.setTransform(false);
		healthBarGroup.setTransform(false);
		supportGroup.setTransform(false);
		landmineGroup.setTransform(false);
	}

	public Group getProjectileGroup() {
		return projectileGroup;
	}

	public void setProjectileGroup(Group projectileGroup) {
		this.projectileGroup = projectileGroup;
	}

	public Group getTowerGroup() {
		return towerGroup;
	}

	public void setTowerGroup(Group towerGroup) {
		this.towerGroup = towerGroup;
	}

	public Group getEnemyGroup() {
		return enemyGroup;
	}

	public void setEnemyGroup(Group enemyGroup) {
		this.enemyGroup = enemyGroup;
	}

	public Group getHealthBarGroup() {
		return healthBarGroup;
	}

	public void setHealthBarGroup(Group healthBarGroup) {
		this.healthBarGroup = healthBarGroup;
	}
	
	public Group getSupportGroup(){
		return supportGroup;
	}
	
	public void setSupportGroup(Group supportGroup){
		this.supportGroup = supportGroup;
	}

	public Group getLandmineGroup() {
		return landmineGroup;
	}

	public void setLandmineGroup(Group landmineGroup) {
		this.landmineGroup = landmineGroup;
	}

}
