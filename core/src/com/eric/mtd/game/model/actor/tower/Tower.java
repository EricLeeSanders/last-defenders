package com.eric.mtd.game.model.actor.tower;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.IGameActorObserver;
import com.eric.mtd.game.model.actor.ai.TowerAI;
import com.eric.mtd.game.model.actor.ai.TowerTargetPriority;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;

/**
 * Represents a Tower
 * 
 * @author Eric
 *
 */
public abstract class Tower extends GameActor {
	public static final int TOWER_RANGE_LEVEL_MAX = 2;
	public static final int TOWER_ATTACK_SPEED_LEVEL_MAX = 2;
	public static final int TOWER_ATTACK_LEVEL_MAX = 2;
	public static final float TOWER_RANGE_INCREASE_RATE = 0.333333f;
	public static final float TOWER_SPEED_INCREASE_RATE = 0.25f;
	public static final float TOWER_ATTACK_INCREASE_RATE = 0.25f;
	public static final float TOWER_SELL_RATE = 0.75f;
	private int cost, armorCost, speedIncreaseCost, rangeIncreaseCost, attackIncreaseCost, rangeLevel, speedLevel,
			attackLevel;
	private TowerTargetPriority targetPriority = TowerTargetPriority.FIRST;
	private boolean active = false; // Used for placing a tower
	private GameActorPool<GameActor> pool;
	private float attackCounter = 0;
	private Group enemyTargetGroup;
	private int kills;
	public Tower(TextureRegion textureRegion, GameActorPool<GameActor> pool, float[] bodyPoints, Vector2 textureSize, Vector2 gunPos, float health, float armor, float attack, float attackSpeed, float range, int cost, int armorCost, int speedIncreaseCost, int rangeIncreaseCost, int attackIncreaseCost) {
		super(textureRegion, pool, bodyPoints, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.pool = pool;
		this.cost = cost;
		this.armorCost = armorCost;
		this.speedIncreaseCost = speedIncreaseCost;
		this.rangeIncreaseCost = rangeIncreaseCost;
		this.attackIncreaseCost = attackIncreaseCost;
		rangeLevel = 0;
		speedLevel = 0;
		attackLevel = 0;
	}

	/**
	 * Sets the Enemy Group
	 */
	public void setEnemyGroup(Group enemyGroup) {
		this.enemyTargetGroup = enemyGroup;
	}

	/**
	 * Gets the selling price for the tower. Adds up the upgraded attributes and
	 * their cost and multiplies by a rate.
	 * 
	 * @return
	 */
	public int getSellCost() {
		float networth = (cost + (speedLevel * speedIncreaseCost) + (rangeLevel * rangeIncreaseCost) + (attackLevel * attackIncreaseCost));
		if (hasArmor()) {
			networth = networth + armorCost;
		}
		return (int) (TOWER_SELL_RATE * networth);

	}

	public int getCost() {
		return cost;
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
		System.out.println("Resetting Tower");
		rangeLevel = 0;
		speedLevel = 0;
		attackLevel = 0;
		kills = 0;
		setActive(false);
	}

	/**
	 * Find a target based on the Target Priority
	 */
	public void findTarget() {
		switch (getTargetPriority()) {
		case FIRST:
			setTarget(TowerAI.findFirstEnemy(this, enemyTargetGroup.getChildren()));
			break;
		case LAST:
			setTarget(TowerAI.findLastEnemy(this, enemyTargetGroup.getChildren()));
			break;
		case WEAKEST:
			setTarget(TowerAI.findLeastHPEnemy(this, enemyTargetGroup.getChildren()));
			break;
		case STRONGEST:
			setTarget(TowerAI.findMostHPEnemy(this, enemyTargetGroup.getChildren()));
			break;
		}
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * Used mainly for placing an actor. Need this so that when an actor is
	 * being placed it does not attack enemies, and vice versa.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public void heal() {
		resetHealth();
		resetArmor();
	}

	public void increaseRange() {
		if (this.getRangeLevel() < TOWER_RANGE_LEVEL_MAX) {
			this.increaseRangeLevel();
			this.setRange(this.getRange() + (this.getRange() * TOWER_RANGE_INCREASE_RATE));
		}
	}

	public void increaseSpeed() {
		this.increaseSpeedLevel();
		this.setAttackSpeed(this.getAttackSpeed() + (this.getAttackSpeed() * TOWER_SPEED_INCREASE_RATE));
	}

	public void increaseAttack() {
		this.increaseAttackLevel();
		this.setAttack(this.getAttack() + (this.getAttack() * TOWER_ATTACK_INCREASE_RATE));
	}

	public void increaseRangeLevel() {
		rangeLevel++;
	}

	public int getRangeLevel() {
		return rangeLevel;
	}

	public void increaseAttackLevel() {
		attackLevel++;
	}

	public void increaseSpeedLevel() {
		speedLevel++;
	}

	public int getAttackLevel() {
		return attackLevel;
	}

	public int getSpeedLevel() {
		return speedLevel;
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

	public TowerTargetPriority getTargetPriority() {
		return targetPriority;
	}

	public void setTargetPriority(TowerTargetPriority targetPriority) {
		this.targetPriority = targetPriority;
	}

}
