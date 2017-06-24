package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.game.model.actor.ai.TowerAI;
import com.foxholedefense.game.model.actor.ai.towerai.FirstEnemyAI;
import com.foxholedefense.game.model.actor.interfaces.Targetable;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

public class Apache extends SupportActor{

	public static final int COST = 2000;
	private static final float FRAME_DURATION = 0.25f;
	public static final float TIME_ACTIVE_LIMIT = 10f;

	private static final float ATTACK_SPEED = 0.2f;
	private static final float RANGE = 75f;
	private static final float ATTACK = 5f;
	private static final float MOVE_SPEED = 200f;
	private static final Dimension BULLET_SIZE = new Dimension(6,6);
	private static final Vector2 GUN_POS = UtilPool.getVector2(0,0);
	private static final Dimension TEXTURE_SIZE = new Dimension(100, 73);

	private boolean readyToAttack, exitingStage;
	private float attackCounter, timeActive;
	private ProjectileFactory projectileFactory;
	private FHDAudio audio;
	private TowerAI ai = new FirstEnemyAI();
	private Animation<TextureRegion> movementAnimation;
	private float movementAnimationStateTime;

	public Apache(SupportActorPool<Apache> pool, Group targetGroup, ProjectileFactory projectileFactory, TextureRegion stationaryTextureRegion, TextureRegion [] textureRegions, TextureRegion rangeTexture, FHDAudio audio) {
		super(pool, targetGroup, stationaryTextureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK, GUN_POS, COST);
		this.audio = audio;
		this.projectileFactory = projectileFactory;

		movementAnimation = new Animation<>(FRAME_DURATION, textureRegions);
		movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
		attackCounter = ATTACK_SPEED;

	}
	public void initialize(Vector2 placePosition){
		Logger.info("Apache: initializing");
		FHDVector2 centerPos = UtilPool.getVector2(-this.getWidth(), Resources.VIRTUAL_HEIGHT/2);
		setPositionCenter(centerPos);

		FHDVector2 destination = UtilPool.getVector2(placePosition);
		float duration = destination.dst(getPositionCenter()) / MOVE_SPEED;
		MoveToAction moveToAction = Actions.moveTo(destination.x, destination.y, duration, Interpolation.linear);
		moveToAction.setAlignment(Align.center);
		addAction(moveToAction);
		setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));

		UtilPool.freeObjects(centerPos, destination);
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
		//If the Apache has been placed (active) and is done moving to the location
		//then it is ready to attack
		if(isActive() && !isReadyToAttack() && getActions().size == 0){
			setReadyToAttack(true);
		}
		if (isReadyToAttack()) {
			attackHandler(delta);
		}
		if(isExitingStage() && getActions().size <= 0){
			freeActor();
		}
	}

	private void attackHandler(float delta){
		Targetable target = findTarget();
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
			exitStage();
		}
	}

	private void exitStage(){
		Logger.info("Apache: exiting stage");
		exitingStage = true;
		FHDVector2 destination = UtilPool.getVector2(getWidth() , Resources.VIRTUAL_HEIGHT / 2);

		float duration = destination.dst(getPositionCenter()) / MOVE_SPEED;
		MoveToAction moveToAction = Actions.moveTo(destination.x, destination.y, duration, Interpolation.linear);
		moveToAction.setAlignment(Align.center);
		addAction(moveToAction);

		setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));
		destination.free();
		setActive(false);
	}

	/**
	 * Find a target using TowerAIType First Enemy
	 */
	private Targetable findTarget() {
		return ai.findTarget(this, getTargetGroup().getChildren());
	}

	private void attackTarget(Targetable target) {
		if(target != null && !target.isDead()){
			audio.playSound(FHDSound.MACHINE_GUN);
			projectileFactory.loadBullet().initialize(this, target, BULLET_SIZE);
		}

	}

	public boolean isReadyToAttack() {
		return readyToAttack;
	}

	private void setReadyToAttack(boolean readyToAttack) {
		this.readyToAttack = readyToAttack;
	}

	public boolean isExitingStage() {
		return exitingStage;
	}

}
