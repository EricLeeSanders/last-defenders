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
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.UtilPool;

public class Apache extends SupportActor{
	public static final int COST = 2000;
	private static final float ATTACK_SPEED = 0.2f;
	private static final float RANGE = 75f;
	private static final float ATTACK = 5f;
	private static final float MOVE_SPEED = 200f;
	private static final float TIME_ACTIVE_LIMIT = 10f;
	private static final Dimension BULLET_SIZE = new Dimension(6,6);
	private static final Vector2 GUN_POS = UtilPool.getVector2(0,0);
	private static final Dimension TEXTURE_SIZE = new Dimension(30, 30);
	private boolean readyToAttack, exitingStage;
	private float attackCounter, timeActive;
	private IProjectileFactory projectileFactory;
	private FHDAudio audio;
	private ITowerAI ai = new FirstEnemyAI();
	private Animation movementAnimation;
	private float movementAnimationStateTime;

	public Apache(SupportActorPool<Apache> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion stationaryTextureRegion, TextureRegion [] textureRegions, TextureRegion rangeTexture, FHDAudio audio) {
		super(pool, targetGroup, stationaryTextureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK, GUN_POS, COST);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		movementAnimation = new Animation(0.25f, textureRegions);
		movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
		attackCounter = ATTACK_SPEED;
	}
	public void initialize(Vector2 position){
		Logger.info("Apache: initializing");
		FHDVector2 destination = UtilPool.getVector2(position.x - this.getOriginX(), position.y - this.getOriginY());
		FHDVector2 centerPos = UtilPool.getVector2(0-this.getHeight(), Resources.VIRTUAL_HEIGHT/2);
		setPositionCenter(centerPos);
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
		UtilPool.freeObjects(destination, centerPos);
	}
	@Override
	public void reset() {
		Logger.info("Apache: resetting");
		setReadyToAttack(false);
		timeActive = 0;
		attackCounter = ATTACK_SPEED;
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
			attackHandler(delta);
		}
		if(isExitingStage() && this.getActions().size <= 0){
			this.freeActor();
		}
	}

	private void attackHandler(float delta){
		ITargetable target = findTarget();
		attackCounter += delta;
		if(target != null && !target.isDead()){
			setRotation(ActorUtil.calculateRotation(target.getPositionCenter(), getPositionCenter()));
			if (attackCounter >= ATTACK_SPEED) {
				attackCounter = 0;
				attackTarget(target);
			}
		}

		timeActive += delta;
		if(timeActive >= TIME_ACTIVE_LIMIT){
			setReadyToAttack(false);
			setExitingStage(true);
			exitStage();
		}
	}

	private void exitStage(){
		Logger.info("Apache: exiting stage");
		FHDVector2 destination = UtilPool.getVector2(0-this.getWidth() , Resources.VIRTUAL_HEIGHT/2 - this.getHeight()/2);
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
		destination.free();
		setActive(false);
	}
	
	/**
	 * Find a target using TowerAI First Enemy
	 */
	public ITargetable findTarget() {
		return ai.findTarget(this, getTargetGroup().getChildren());

	}

	public void attackTarget(ITargetable target) {
		if(target != null && !target.isDead()){
			audio.playSound(FHDSound.MACHINE_GUN);
			projectileFactory.loadBullet().initialize(this, target, this.getGunPos(), BULLET_SIZE);
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
