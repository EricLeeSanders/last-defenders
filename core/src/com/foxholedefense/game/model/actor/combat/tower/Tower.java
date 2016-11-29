package com.foxholedefense.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ai.TowerAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;

/**
 * Represents a Tower
 * 
 * @author Eric
 *
 */
public abstract class Tower extends CombatActor {
	public static final float TOWER_RANGE_INCREASE_RATE = (1/2f);
	public static final float TOWER_SPEED_INCREASE_RATE = 0.33f;
	public static final float TOWER_ATTACK_INCREASE_RATE = 0.33f;
	public static final float TOWER_SELL_RATE = 0.75f;
	private int cost, armorCost, speedIncreaseCost, rangeIncreaseCost, attackIncreaseCost;
	private boolean rangeIncreaseEnabled, speedIncreaseEnabled, attackIncreaseEnabled;
	private TowerAI ai = TowerAI.FIRST;
	private boolean active, showRange; 
	private float attackCounter = 0;
	private TextureRegion rangeRegion, collidingRangeRegion;
	private int kills;
	private Pool<CombatActor> pool;
	private boolean towerColliding;
	public Tower(TextureRegion textureRegion, CombatActorPool<CombatActor> pool, Group targetGroup, Dimension textureSize, Vector2 gunPos, TextureRegion rangeRegion, TextureRegion collidingRangeRegion,
					float health, float armor, float attack, float attackSpeed, float range, int cost, int armorCost, int speedIncreaseCost, int rangeIncreaseCost, int attackIncreaseCost) {
		super(textureRegion, pool, targetGroup, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.cost = cost;
		this.armorCost = armorCost;
		this.speedIncreaseCost = speedIncreaseCost;
		this.rangeIncreaseCost = rangeIncreaseCost;
		this.attackIncreaseCost = attackIncreaseCost;
		this.collidingRangeRegion = collidingRangeRegion;
		this.rangeRegion = rangeRegion;
		this.pool = pool;
	}
	/**
	 * Gets the selling price for the tower. Adds up the upgraded attributes and
	 * their cost and multiplies by a rate.
	 * 
	 * @return
	 */
	public int getSellCost() {
		int networth = cost;
		if(speedIncreaseEnabled){
			networth += speedIncreaseCost;
		}
		if(attackIncreaseEnabled){
			networth += attackIncreaseCost;
		}
		if(rangeIncreaseEnabled){
			networth += rangeIncreaseCost;
		}
		if (hasArmor()) {
			networth = networth + armorCost;
		}
		return (int) (TOWER_SELL_RATE * networth);

	}

	public int getCost() {
		return cost;
	}

	public void setShowRange(boolean showRange) {
		this.showRange = showRange;
	}

	public boolean isShowRange() {
		return showRange;
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if (showRange) {
			drawRange(batch);
		}
		super.draw(batch, alpha);
	}
	protected void drawRange(Batch batch){
		TextureRegion currentRangeRegion = rangeRegion;
		if(isTowerColliding()){
			currentRangeRegion = collidingRangeRegion;
		}
		float width = getRange() * 2;
		float height = getRange() * 2;
		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, width);
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, height);
		batch.draw(currentRangeRegion,x, y, getOriginX(), getOriginY(), width, height, 1, 1, getRotation());
	}
	/**
	 * Finds targets while active. Always looks for a target.
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (isActive()) {
			findTarget();
		}
		if (getTarget() != null) {
			if (getTarget().isDead()) {
				setTarget(null);
			} else {
				setRotation(calculateRotation(getTarget().getPositionCenter()));
				if (attackCounter >= getAttackSpeed()) {
					attackCounter = 0;
					attackTarget();
				} else {
					attackCounter += delta;
				}
			}
		} else { // Make the actor always ready to shoot
			attackCounter += delta;
		}
	}

	@Override
	public void reset() {
		super.reset();
		rangeIncreaseEnabled = false;
		speedIncreaseEnabled = false;
		attackIncreaseEnabled = false;
		kills = 0;
		this.setShowRange(false);
		setActive(false);
	}

	/**
	 * Find a target based on the Target Priority
	 */
	public void findTarget() {
		setTarget(getAI().findTarget(this, getTargetGroup().getChildren()));
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * Used for placing an actor. Need this so that when an actor is
	 * being placed it does not attack enemies, and vice versa.
	 */
	public void setActive(boolean active) {	this.active = active; }

	public void heal() {
		resetHealth();
		resetArmor();
	}

	public void increaseRange() {
		if(!rangeIncreaseEnabled){
			rangeIncreaseEnabled = true;
			this.setRange(this.getRange() + (this.getRange() * TOWER_RANGE_INCREASE_RATE));
		}
	}

	public void increaseSpeed() {
		if(!speedIncreaseEnabled){
			speedIncreaseEnabled = true;
			this.setAttackSpeed(this.getAttackSpeed() - (this.getAttackSpeed() * TOWER_SPEED_INCREASE_RATE));
		}
	}

	public void increaseAttack() {
		if(!attackIncreaseEnabled){
			attackIncreaseEnabled = true;
			this.setAttack(this.getAttack() + (this.getAttack() * TOWER_ATTACK_INCREASE_RATE));
		}
	}

	public boolean hasIncreasedRange() {
		return rangeIncreaseEnabled;
	}

	public boolean hasIncreasedAttack() {
		return attackIncreaseEnabled;
	}

	public boolean hasIncreasedSpeed() {
		return speedIncreaseEnabled;
	}

	public int getAttackIncreaseCost() {
		return attackIncreaseCost;
	}

	public int getArmorCost() {
		return armorCost;
	}

	public int getRangeIncreaseCost() {
		return rangeIncreaseCost;
	}

	public int getSpeedIncreaseCost() {
		return speedIncreaseCost;
	}

	public void sellTower() {
		removeTower();
	}

	public int getNumOfKills() {
		return kills;
	}

	public void giveKill() {
		kills++;
		notifyObservers();
	}

	public void removeTower() {
		pool.free(this);
	}

	public TowerAI getAI() {
		return ai;
	}

	public void setAI(TowerAI ai) {
		this.ai = ai;
		//this.targetPriority = targetPriority;
	}
	public void setTowerColliding(boolean towerColliding){
		this.towerColliding = towerColliding;
	}
	public boolean isTowerColliding(){
		return towerColliding;
	}
}
