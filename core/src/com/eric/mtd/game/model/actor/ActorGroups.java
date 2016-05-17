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
	private Group projectileGroup = new Group();
	private Group enemyGroup = new Group();
	private Group towerGroup = new Group();
	private Group healthBarGroup = new Group();
	private Group sandbagGroup = new Group();
	private Group supportGroup = new Group();
	private Group landmineGroup = new Group();

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

	public Group getSandbagGroup() {
		return sandbagGroup;
	}

	public void setSandbagGroup(Group sandbagGroup) {
		this.sandbagGroup = sandbagGroup;
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
