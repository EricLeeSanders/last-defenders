package com.eric.mtd.game.model.actor.tower;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.ai.TowerAI;
import com.eric.mtd.game.model.ai.TowerTargetPriority;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.util.Resources;
public abstract class Tower extends GameActor implements Pool.Poolable{
    public static final int TOWER_RANGE_LEVEL_MAX = 2;
    public static final int TOWER_ATTACK_SPEED_LEVEL_MAX = 2;
    public static final int TOWER_ATTACK_LEVEL_MAX = 2;
    public static final float TOWER_RANGE_INCREASE_RATE = 0.333333f;
    public static final float TOWER_SPEED_INCREASE_RATE = 0.25f;
    public static final float TOWER_ATTACK_INCREASE_RATE = 0.25f;
    public static final float TOWER_SELL_RATE = 0.75f;
	private Rectangle bodyRectangle;
	private TextureRegion textureRegion;
	private int cost, armorCost, speedIncreaseCost, rangeIncreaseCost, attackIncreaseCost, rangeLevel, speedLevel, attackLevel;
	private String targetPriority ="FIRST";
	private boolean active = false;
	private GameActorPool<GameActor> pool;
	private float attackCounter = 0;
	public Tower(TextureRegion textureRegion,GameActorPool<GameActor> pool, float [] bodyPoints,Vector2 textureSize, Vector2 gunPos,float health, float armor, float attack, float attackSpeed, float range, int cost,
				int armorCost, int speedIncreaseCost, int rangeIncreaseCost, int attackIncreaseCost){
		super(textureRegion,pool, bodyPoints,textureSize, gunPos,health,armor,attack, attackSpeed, range);
		this.pool = pool;
    	this.textureRegion = textureRegion;
    	this.cost = cost;
    	this.armorCost = armorCost;
    	this.speedIncreaseCost = speedIncreaseCost;
    	this.rangeIncreaseCost = rangeIncreaseCost;
    	this.attackIncreaseCost = attackIncreaseCost;
    	rangeLevel = 0;
    	speedLevel = 0;
    	attackLevel = 0;
	}
	public int getSellCost(){
		float networth = (cost + (speedLevel * speedIncreaseCost) + (rangeLevel * rangeIncreaseCost) + (attackLevel * attackIncreaseCost));
		if(hasArmor()){
			networth = networth + armorCost;
		}
		return (int) (TOWER_SELL_RATE * networth);

	}
	public int getCost(){
		return cost;
	}
	 @Override
	 public void act(float delta){
		super.act(delta);
		if(isActive()){
			findTarget();
		}
		if(super.getTarget() != null){
    		if(super.getTarget().isDead()){
    			this.setTarget(null);
    		}
    		else{
	    		this.setRotation(calculateNewAngle(super.getTarget().getPositionCenter()));
				if(attackCounter >= super.getAttackSpeed()){
					attackCounter = 0;
					attackTarget();
				}
				else{
					attackCounter += delta;
				}
	    	}
    	}
    	else{ //Make the actor always ready to shoot
    		attackCounter += delta;
    	}
	}
    @Override
    public void reset() {
    	super.reset();
    	rangeLevel = 0;
    	speedLevel = 0;
    	attackLevel = 0;
    	setActive(false);
    }
	public void findTarget(){
		TowerTargetPriority tp = TowerTargetPriority.valueOf(this.getTargetPriority());
		GameActor target = null;
    	Group enemyTargetGroup;
    	//TODO: Question: Don't really like having to do it this way. Maybe a better way?
    	if(this.getStage() instanceof GameStage){
    		enemyTargetGroup = ((GameStage)this.getStage()).getActorGroups().getEnemyGroup();
    	}
    	else{
    		enemyTargetGroup = null;
    	}
		switch (tp){
			case FIRST:
				target = TowerAI.findFirstEnemy(this, enemyTargetGroup);
				break;
			case LAST:
				target = TowerAI.findLastEnemy(this, enemyTargetGroup);
				break;
			case WEAKEST:
				target = TowerAI.findWeakestEnemy(this, enemyTargetGroup);
				break;
			case STRONGEST:
				target = TowerAI.findStrongestEnemy(this, enemyTargetGroup);
		}
		setTarget(target);
	}	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public void heal(){
		this.resetHealth();
	}
	public void increaseRange(){
		if(this.getRangeLevel() < this.TOWER_RANGE_LEVEL_MAX){
			this.increaseRangeLevel();
			this.setRange(this.getRange() + (this.getRange()*TOWER_RANGE_INCREASE_RATE));
		}
	}
	public void increaseSpeed(){
		this.increaseSpeedLevel();
		this.setAttackSpeed(this.getAttackSpeed() + (this.getAttackSpeed()*TOWER_SPEED_INCREASE_RATE));
	}
	public void increaseAttack(){
		this.increaseAttackLevel();
		this.setAttack(this.getAttack() + (this.getAttack()*TOWER_ATTACK_INCREASE_RATE));
	}
    public void increaseRangeLevel(){
    	rangeLevel++;
    }
    public int getRangeLevel(){
    	return rangeLevel;
    }
    public void increaseAttackLevel(){
    	attackLevel++;
    }
    public void increaseSpeedLevel(){
    	speedLevel++;
    }
    public int getAttackLevel(){
    	return attackLevel;
    }
    public int getSpeedLevel(){
    	return speedLevel;
    }
	public int getAttackIncreaseCost(){
		return attackIncreaseCost;
	}
	public int getArmorCost(){
		return armorCost;
	}
	public int getRangeIncreaseCost(){
		return rangeIncreaseCost;
	}
	public int getSpeedIncreaseCost(){
		return speedIncreaseCost;
	}
	public void sellTower(){
		removeTower();
	}
	public void removeTower(){
    	pool.free(this);
		this.remove();
	}
	public String getTargetPriority(){
		return targetPriority;
	}
	public void setTargetPriority(String targetPriority){
		this.targetPriority = targetPriority;
	}

}
