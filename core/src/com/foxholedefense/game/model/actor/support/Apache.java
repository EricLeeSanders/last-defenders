package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.foxholedefense.game.model.actor.ai.towerai.ITowerAI;
import com.foxholedefense.game.model.actor.ai.towerai.FirstEnemyAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.service.factory.ActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.UtilPool;

public class Apache extends SupportActor{
	public static final int COST = 2000;
	private static final float ATTACK_SPEED = 0.1f;
	private static final float RANGE = 75f;
	private static final float ATTACK = 5f;
	private static final float MOVE_SPEED = 200f;
	private static final float TIME_ACTIVE_LIMIT = 1f;
	private static final Dimension BULLET_SIZE = new Dimension(6,6);
	private static final Vector2 GUN_POS = UtilPool.getVector2(0,0);
	private TextureRegion [] textureRegions;
	private boolean readyToAttack, exitingStage;
	private float textureCounter, attackCounter, timeActive;
	private int textureIndex; // Current texture index
	private CombatActor target;
	private IProjectileFactory projectileFactory;
	private FHDAudio audio;
	private ITowerAI ai = new FirstEnemyAI();
	private Animation movementAnimation;
	private float movementAnimationStateTime;

	public Apache(SupportActorPool<Apache> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion stationaryTextureRegion, TextureRegion [] textureRegions, TextureRegion rangeTexture, FHDAudio audio) {
		super(pool, targetGroup, stationaryTextureRegion, rangeTexture,	RANGE, ATTACK, GUN_POS, COST);
		this.textureRegions = textureRegions;
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		movementAnimation = new Animation(0.25f, textureRegions);
		movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
	}
	public void initialize(Vector2 position){
		Logger.info("Apache: initializing");
		FHDVector2 destination = UtilPool.getVector2(position.x - this.getOriginX(), position.y - this.getOriginY());
		FHDVector2 centerPos = UtilPool.getVector2(0-this.getHeight(), Resources.VIRTUAL_HEIGHT/2);
		setPositionCenter(centerPos);
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(calculateRotation(destination));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
		UtilPool.freeObjects(destination, centerPos);
	}
	@Override
	public void reset() {
		Logger.info("Apache: resetting");
		setReadyToAttack(false);
		timeActive = 0;
		attackCounter = 0;
		textureCounter = 0;
		textureIndex = 0;
		exitingStage = false;
		super.reset();
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		movementAnimationStateTime += delta;
		setTextureRegion(movementAnimation.getKeyFrame(movementAnimationStateTime, true));
		//If the Apache has been placed (active) but is done moving to the location
		//then it is ready to attack
		if(isActive() && !isReadyToAttack() && this.getActions().size <= 0){
			setReadyToAttack(true);
		}
		if (isReadyToAttack()) {
			findTarget();
			timeActive += delta;
			if(timeActive >= TIME_ACTIVE_LIMIT){
				setReadyToAttack(false);
				setExitingStage(true);
				exitStage();
				setTarget(null);
			}
		}
		if (getTarget() != null) {
			if (getTarget().isDead()) {
				setTarget(null);
			} else {
				setRotation(calculateRotation(getTarget().getPositionCenter()));
				if (attackCounter >= ATTACK_SPEED) {
					attackCounter = 0;
					attackTarget();
				} else {
					attackCounter += delta;
				}
			}
		} else { // Make the actor always ready to shoot
			attackCounter += delta;
		} 
		if(isExitingStage() && this.getActions().size <= 0){
			this.freeActor();
		}
	}
	private void exitStage(){
		Logger.info("Apache: exiting stage");
		FHDVector2 destination = UtilPool.getVector2(0-this.getWidth() , Resources.VIRTUAL_HEIGHT/2 - this.getHeight()/2);
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(calculateRotation(destination));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
		destination.free();
		setActive(false);
	}
	
	/**
	 * Find a target using TowerAI First Enemy
	 */
	public void findTarget() {
		setTarget(ai.findTarget(this, getTargetGroup().getChildren()));

	}
	public void setTarget(CombatActor target) {
		this.target = target;
	}
	public CombatActor getTarget() {
		return target;
	}
	
	public void attackTarget() {
		if(getTarget() != null){
			audio.playSound(FHDSound.MACHINE_GUN);
			projectileFactory.loadBullet().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE);
		}

	}

	public boolean isReadyToAttack() {
		return readyToAttack;
	}

	public void setReadyToAttack(boolean readyToAttack) {
		this.readyToAttack = readyToAttack;
	}
	public boolean isExitingStage() {
		return exitingStage;
	}
	public void setExitingStage(boolean exitingStage) {
		this.exitingStage = exitingStage;
	}	

}
