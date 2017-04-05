package com.foxholedefense.game.model.actor.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ICollision;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver.CombatActorEvent;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents both a Tower and Enemy.
 * 
 * @author Eric
 *
 */
public abstract class CombatActor extends GameActor implements Pool.Poolable, ICollision, IAttacker, ITargetable {
	private final float RESET_ATTACK_SPEED, RESET_RANGE, MAX_HEALTH, MAX_ARMOR, RESET_ATTACK;
	private float attackSpeed, range, health, attack, armor;
	private Vector2 gunPos;
	private Vector2 rotatedGunPos = UtilPool.getVector2();
	private Circle rangeCircle = new Circle();
	private boolean hasArmor, dead, active;
	private Pool<CombatActor> pool;
	private SnapshotArray<ICombatActorObserver> observers = new SnapshotArray<ICombatActorObserver>();
	private DeathEffectType deathEffectType;
	private Group targetGroup;

	public CombatActor(TextureRegion textureRegion, Dimension textureSize, Pool<CombatActor> pool, Group targetGroup, Vector2 gunPos,
						float health, float armor, float attack, float attackSpeed, float range, DeathEffectType deathEffectType) {

		super(textureSize);
		this.MAX_HEALTH = health;
		this.MAX_ARMOR = armor;
		this.RESET_ATTACK = attack;
		this.RESET_RANGE = range;
		this.RESET_ATTACK_SPEED = attackSpeed;
		this.health = health;
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.attack = attack;
		this.gunPos = gunPos;
		this.range = range;
		this.pool = pool;
		this.targetGroup = targetGroup;
		this.deathEffectType = deathEffectType;
		setTextureRegion(textureRegion);
		
		
	}
	public void detachCombatActor(ICombatActorObserver observer){
		Logger.info("Combat Actor Detach: " + observer.getClass().getName());
		observers.removeValue(observer, false);
	}

	public void attachAllCombatActor(Array<ICombatActorObserver> observers){
		this.observers.addAll(observers);
	}

	public void attachCombatActor(ICombatActorObserver observer){
		Logger.info("Combat Actor Attach: " + observer.getClass().getName());
		observers.add(observer);
	}
	protected void notifyObserversCombatActor(CombatActorEvent event){
		Logger.info("Combat Actor: Notify Observers");
		Object[] objects = observers.begin();
		for(int i = observers.size - 1; i >= 0; i--){
			ICombatActorObserver observer = (ICombatActorObserver) objects[i];
			Logger.info("Combat Actor Notifying: " + observer.getClass().getName());
			observer.notifyCombatActor(this, event);
		}
		observers.end();
	}
	@Override
	public void reset() {
		Logger.info("Combat Actor: " + this.getClass().getSimpleName() + " Resetting");
		health = MAX_HEALTH;
		armor = MAX_ARMOR;
		hasArmor = false;
		attack = RESET_ATTACK;
		attackSpeed = RESET_ATTACK_SPEED;
		range = RESET_RANGE;
		this.setRotation(0);
		this.clear();
		this.remove();
		setActive(false);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float alpha) {

		super.draw(batch, alpha);

		if(DebugOptions.showTextureBoundaries){
			drawDebugBody(batch);
		}
	}

	private void drawDebugBody(Batch batch){
		batch.end();
		ShapeRenderer debugBody = Resources.getShapeRenderer();
		debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		debugBody.begin(ShapeType.Line);
		debugBody.setColor(Color.RED);
		Shape2D body = getBody();
		if(body instanceof Polygon) {
			Polygon polyBody = (Polygon) body;
			debugBody.polygon(polyBody.getTransformedVertices());
		} else if (body instanceof Circle) {
			Circle circleBody = (Circle) body;
			debugBody.circle(circleBody.x, circleBody.y, circleBody.radius);
		}
		debugBody.end();
		batch.begin();
	}

	public float getHealth() {
		return health;
	}

	public void takeDamage(float damage) {
		if (hasArmor()) {
			if ((armor - damage) <= 0) {
				health = health - (damage - armor);
				setHasArmor(false);
			} else {
				armor = armor - damage;
			}

		} else {
			health = health - damage;
		}
		if (health <= 0) {
			this.setDead(true);
		}

	}

	public Vector2 getGunPos() {
		Vector2 centerPosition = getPositionCenter();
		FHDVector2 rotatedCoords = ActorUtil.getRotatedCoords((getPositionCenter().x + gunPos.x), (getPositionCenter().y + gunPos.y), centerPosition.x, centerPosition.y, Math.toRadians(getRotation()));
		rotatedGunPos.set(rotatedCoords.x, rotatedCoords.y);
		rotatedCoords.free();
		return rotatedGunPos;
	}
	@Override
	public Shape2D getRangeShape() {
		rangeCircle.set(getPositionCenter().x, getPositionCenter().y, range);
		return rangeCircle;
	}

	public float getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public abstract void attackTarget(ITargetable target);
	
	public DeathEffectType getDeathEffectType(){
		return deathEffectType;
	}

	public abstract Shape2D getBody();

	public void setDead(boolean dead) {
		this.dead = dead;
		if (isDead()) {
			Logger.info("Combat Actor: " + this.getClass().getSimpleName() + " Dead");
			notifyObserversCombatActor(CombatActorEvent.DEAD);
			pool.free(this);
		}
	}

	public boolean isDead() {
		return dead;
	}

	public float getHealthPercent() {
		return ((this.getHealth() / this.getMaxHealth()) * 100);
	}

	public float getArmorPercent() {
		return ((this.armor / this.MAX_ARMOR) * 100);
	}

	public float getMaxHealth() {
		return MAX_HEALTH;
	}

	public void resetHealth() {
		health = MAX_HEALTH;
	}
	public void resetArmor() {
		if(hasArmor()){
			armor = MAX_ARMOR;
		}
	}
	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public boolean hasArmor() {
		return hasArmor;
	}

	public void setHasArmor(boolean hasArmor) {
		if(hasArmor() && !hasArmor) {
			armor = 0;
			notifyObserversCombatActor(CombatActorEvent.ARMOR_BROKEN);
		}
		resetArmor();
		this.hasArmor = hasArmor;
	}

	public void freeActor(){
		pool.free(this);
	}
	
	public Group getTargetGroup(){
		return targetGroup;
	}

	/**
	 * Combat actor is an active actor on the stage.
	 * It can be targeted, and attacked.
	 * @return
     */
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {	this.active = active; }

}
