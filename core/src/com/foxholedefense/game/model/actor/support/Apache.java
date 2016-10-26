package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.model.actor.ai.TowerSupportAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ICollision;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.service.factory.ActorFactory.BulletPool;
import com.foxholedefense.game.service.factory.ActorFactory.SupportActorPool;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

public class Apache extends SupportActor{
	public static final int COST = 2000;
	private static final float SCALE = 0.5f;
	private static final float ATTACK_SPEED = 0.1f;
	private static final float RANGE = 75f;
	private static final float ATTACK = 5f;
	private static final float MOVE_SPEED = 200f;
	private static final float TIME_ACTIVE_LIMIT = 15f;
	private static final Dimension BULLET_SIZE = new Dimension(10, 10);
	private static final Vector2 GUN_POS = new Vector2(0,0);
	private TextureRegion [] textureRegions;
	private boolean readyToAttack, exitingStage;
	private float textureCounter, attackCounter, timeActive;
	private int textureIndex; // Current texture index
	private CombatActor target;
	private IProjectileFactory projectileFactory;
	private FHDAudio audio;
	public Apache(SupportActorPool<Apache> pool, Group targetGroup, IProjectileFactory projectileFactory, TextureRegion [] textureRegions, FHDAudio audio) {
		super(pool, targetGroup, textureRegions[0], new Dimension(textureRegions[0].getRegionWidth()*SCALE, textureRegions[0].getRegionHeight()*SCALE),
				RANGE, ATTACK, GUN_POS, COST);
		this.textureRegions = textureRegions;
		this.audio = audio;
		this.projectileFactory = projectileFactory;
	}
	public void initialize(Vector2 position){
		Vector2 destination = new Vector2(position.x - (this.getWidth()/2), position.y - (this.getHeight()/2));
		setPositionCenter(new Vector2(0-this.getHeight(), Resources.VIRTUAL_HEIGHT/2));
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(calculateRotation(destination));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
	}
	@Override
	public void reset() {
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
		changeTextures(delta);
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
		Vector2 destination = new Vector2(0-this.getHeight()-(this.getHeight()/2), Resources.VIRTUAL_HEIGHT/2 - this.getWidth()/2);
		float moveDistance = (destination.dst(this.getPositionCenter()) / MOVE_SPEED);
		this.setRotation(calculateRotation(destination));
		addAction(Actions.moveTo(destination.x, destination.y, moveDistance, Interpolation.linear));
		setActive(false);
	}
	
	/**
	 * Find a target using TowerAI First Enemy
	 */
	public void findTarget() {
		setTarget(TowerSupportAI.findFirstEnemy(this, getTargetGroup().getChildren()));

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
	
	/**
	 * Handles the changing of textures
	 * 
	 * @param delta
	 */
	public void changeTextures(float delta) {
		if (textureCounter >= 0.2f) {
			textureCounter = 0;
			textureIndex++;
			super.setTextureRegion(textureRegions[textureIndex % 3]);
		} else {
			textureCounter += delta;
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
