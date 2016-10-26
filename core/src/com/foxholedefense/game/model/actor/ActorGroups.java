package com.foxholedefense.game.model.actor;

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
	private final Group projectileGroup;
	private final Group enemyGroup;
	private final Group towerGroup;
	private final Group healthBarGroup;
	private final Group supportGroup;
	private final Group landmineGroup;
	private final Group deathEffectGroup;
	
	public ActorGroups(){
		projectileGroup = new Group();
		enemyGroup = new Group();
		towerGroup = new Group();
		healthBarGroup = new Group();
		supportGroup = new Group();
		landmineGroup = new Group();
		deathEffectGroup = new Group();
		
		projectileGroup.setTransform(false);
		enemyGroup.setTransform(false);
		towerGroup.setTransform(false);
		healthBarGroup.setTransform(false);
		supportGroup.setTransform(false);
		landmineGroup.setTransform(false);
		deathEffectGroup.setTransform(false);
	}

	public Group getProjectileGroup() {
		return projectileGroup;
	}

	public Group getTowerGroup() {
		return towerGroup;
	}

	public Group getEnemyGroup() {
		return enemyGroup;
	}

	public Group getHealthBarGroup() {
		return healthBarGroup;
	}

	public Group getSupportGroup(){
		return supportGroup;
	}
	
	public Group getLandmineGroup() {
		return landmineGroup;
	}

	public Group getDeathEffectGroup() {
		return deathEffectGroup;
	}


}
