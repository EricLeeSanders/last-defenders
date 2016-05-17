package com.eric.mtd.game.model.actor.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.ICollision;
import com.eric.mtd.game.model.actor.interfaces.ITargetable;
import com.eric.mtd.game.model.actor.interfaces.IVehicle;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

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
	private float[] bodyPoints;
	private ITargetable target;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private Circle rangeCircle = new Circle();
	private Polygon bodyPoly;
	private boolean hasArmor, dead;
	private Pool<CombatActor> pool;
	private List<ICombatActorObserver> observers = new CopyOnWriteArrayList<ICombatActorObserver>();
	private Group projectileGroup, targetGroup;
	public CombatActor(TextureRegion textureRegion,Pool<CombatActor> pool, float[] bodyPoints, Dimension textureSize, Vector2 gunPos, float health, float armor, float attack, float attackSpeed, float range) {
		super(textureRegion, textureSize);
		this.MAX_HEALTH = health;
		this.MAX_ARMOR = armor;
		this.RESET_ATTACK = attack;
		this.RESET_RANGE = range;
		this.RESET_ATTACK_SPEED = attackSpeed;
		this.health = health;
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.attack = attack;
		this.bodyPoints = bodyPoints;
		this.bodyPoly = new Polygon(bodyPoints);
		this.gunPos = gunPos;
		this.range = range;
		this.pool = pool;
	}
	public void detach(ICombatActorObserver observer){
		System.out.println(observers.remove(observer));
	}
	public void attach(ICombatActorObserver observer){
		observers.add(observer);
	}
	protected void notifyObservers(){
		for(ICombatActorObserver observer: observers){
			observer.notifty();
		}
	}
	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("Resetting GameActor");
		health = MAX_HEALTH;
		armor = MAX_ARMOR;
		hasArmor = false;
		attack = RESET_ATTACK;
		attackSpeed = RESET_ATTACK_SPEED;
		range = RESET_RANGE;
		target = null;
		this.setRotation(0);
		this.clear();
		this.remove();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float alpha) {
		if(Logger.DEBUG && debugBody != null){
			batch.end();
			debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			debugBody.begin(ShapeType.Line);
			debugBody.setColor(Color.YELLOW);
			debugBody.polygon(getBody().getTransformedVertices());
			debugBody.end();
			batch.begin();
		}

		super.draw(batch, alpha);
	}

	public float getHealth() {
		return health;
	}

	public void takeDamage(float damage) {
		if (hasArmor) {
			if ((armor - damage) < 0) {
				health = health - (damage - armor);
				armor = 0;
			} else {
				armor = armor - damage;
			}

			if (armor <= 0) {
				hasArmor = false;
			}
		} else {
			health = health - damage;
		}
		if (health <= 0) {
			this.setDead(true);
		}

	}

	public String getAttackType() {
		return "Nearest";
	}

	public void setTarget(CombatActor target) {
		this.target = target;
	}

	public Vector2 getGunPos() {
		return getRotatedCoords((getPositionCenter().x + gunPos.x), (getPositionCenter().y + gunPos.y));

	}
	@Override
	public Shape2D getRangeShape() {
		rangeCircle.set(getPositionCenter().x, getPositionCenter().y, range);
		return rangeCircle;
		//return;// new Circle(getPositionCenter().x, getPositionCenter().y, range);
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

	public abstract void attackTarget();
	
	@Override
	public Polygon getBody() {
		
		bodyPoly.setOrigin((getTextureSize().getWidth() / 2), (getTextureSize().getHeight() / 2));
		bodyPoly.setRotation(this.getRotation());
		bodyPoly.setPosition(getPositionCenter().x - (getTextureSize().getWidth() / 2), getPositionCenter().y - (getTextureSize().getHeight() / 2));

		return bodyPoly;
	}


	public void setDead(boolean dead) {
		this.dead = dead;
		if (isDead()) {
			if (this instanceof IVehicle) {
				AudioUtil.playVehicleExplosion();
			}
			pool.free(this);
		}
		notifyObservers();
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
		this.hasArmor = hasArmor;
	}
	public void setProjectileGroup(Group projectileGroup){
		this.projectileGroup = projectileGroup;
	}
	public Group getProjectileGroup(){
		return projectileGroup;
	}
	public ITargetable getTarget() {
		return target;
	}
	
	public void freeActor(){
		pool.free(this);
	}
	public Group getTargetGroup() {
		return targetGroup;
	}
	public void setTargetGroup(Group targetGroup) {
		this.targetGroup = targetGroup;
	}
	
	

}
