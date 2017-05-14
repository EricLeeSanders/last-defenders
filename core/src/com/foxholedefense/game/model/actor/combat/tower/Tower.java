package com.foxholedefense.game.model.actor.combat.tower;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ai.TowerAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateManager;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.Logger;

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
	private boolean showRange;
	private TextureRegion rangeRegion, collidingRangeRegion;
	private int kills;
	private boolean towerColliding;
	private StateManager<TowerState, CombatActorState> stateManager;

	public Tower(TextureRegion textureRegion, Dimension textureSize, Pool<CombatActor> pool, Group targetGroup, Vector2 gunPos, TextureRegion rangeRegion, TextureRegion collidingRangeRegion,
				 float health, float armor, float attack, float attackSpeed, float range, int cost, int armorCost, int speedIncreaseCost, int rangeIncreaseCost, int attackIncreaseCost, DeathEffectType deathEffectType) {
		super(textureRegion, textureSize, pool, targetGroup, gunPos, health, armor, attack, attackSpeed, range, deathEffectType);
		this.cost = cost;
		this.armorCost = armorCost;
		this.speedIncreaseCost = speedIncreaseCost;
		this.rangeIncreaseCost = rangeIncreaseCost;
		this.attackIncreaseCost = attackIncreaseCost;
		this.collidingRangeRegion = collidingRangeRegion;
		this.rangeRegion = rangeRegion;
	}

	public void init(){
		stateManager.transition(TowerState.ACTIVE);
		setActive(true);
		setDead(false);
	}

	public void setStateManager(StateManager<TowerState, CombatActorState> stateManager){
		this.stateManager = stateManager;
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
		batch.draw(currentRangeRegion,x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
	}
	/**
	 * Finds targets while active. Always looks for a target.
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		stateManager.update(delta);
	}

	@Override
	public void reset() {
		super.reset();
		Logger.info("Tower: " + this.getClass().getSimpleName() + " Resetting");
		rangeIncreaseEnabled = false;
		speedIncreaseEnabled = false;
		attackIncreaseEnabled = false;
		kills = 0;
		this.setShowRange(false);
		stateManager.transition(TowerState.STANDBY);
	}

	public void deadState(){
		stateManager.transition(TowerState.DYING);
	}

	public void heal() {
		Logger.info("Tower: " + this.getClass().getSimpleName() + " Healing");
		resetHealth();
		resetArmor();
	}

	public void increaseRange() {
		if(!rangeIncreaseEnabled){
			Logger.info("Tower: " + this.getClass().getSimpleName() + " Increasing Range");
			rangeIncreaseEnabled = true;
			this.setRange(this.getRange() + (this.getRange() * TOWER_RANGE_INCREASE_RATE));
		}
	}

	public void increaseSpeed() {
		if(!speedIncreaseEnabled){
			Logger.info("Tower: " + this.getClass().getSimpleName() + " Increasing Speed");
			speedIncreaseEnabled = true;
			this.setAttackSpeed(this.getAttackSpeed() - (this.getAttackSpeed() * TOWER_SPEED_INCREASE_RATE));
		}
	}

	public void increaseAttack() {
		if(!attackIncreaseEnabled){
			Logger.info("Tower: " + this.getClass().getSimpleName() + " Increasing attack");
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
		Logger.info("Tower: " + this.getClass().getSimpleName() + " giving kill");
		kills++;
	}

	public void removeTower() {
		freeActor();
	}

	public TowerAI getAI() {
		return ai;
	}

	public void setAI(TowerAI ai) {
		this.ai = ai;
	}
	public void setTowerColliding(boolean towerColliding){
		this.towerColliding = towerColliding;
	}
	public boolean isTowerColliding(){
		return towerColliding;
	}

	public TowerState getState() {
		return stateManager.getCurrentStateName();
	}
}
