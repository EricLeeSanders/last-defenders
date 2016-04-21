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
	private Group bulletGroup = new Group();
	private Group enemyGroup = new Group();
	private Group towerGroup = new Group();
	private Group healthBarGroup = new Group();
	private Group flameGroup = new Group();
	private Group explosionGroup = new Group();
	private Group sandbagGroup = new Group();

	public Group getBulletGroup() {
		return bulletGroup;
	}

	public void setBulletGroup(Group bulletGroup) {
		this.bulletGroup = bulletGroup;
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

	public Group getFlameGroup() {
		return flameGroup;
	}

	public void setFlameGroup(Group flameGroup) {
		this.flameGroup = flameGroup;
	}

	public Group getExplosionGroup() {
		return explosionGroup;
	}

	public void setExplosionGroup(Group explosionGroup) {
		this.explosionGroup = explosionGroup;
	}

	public Group getSandbagGroup() {
		return sandbagGroup;
	}

	public void setSandbagGroup(Group sandbagGroup) {
		this.sandbagGroup = sandbagGroup;
	}

}
