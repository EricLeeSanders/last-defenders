package com.eric.mtd.game.model.actor.tower;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.mtd.Resources;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.factory.ActorFactory.GameActorPool;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.stage.GameStage;

public class TowerMachine extends Tower{
	
	public static final float HEALTH = 8;  
	public static final float ARMOR = 4;
	public static final float ATTACK = 1; 
	public static final float ATTACK_SPEED = 0.2f;
	public static final float RANGE = 30; 
	
	public static final int COST = 200;
	public static final int ARMOR_COST = 200;
	public static final int RANGE_INCREASE_COST = 100;
	public static final int SPEED_INCREASE_COST = 100;
	public static final int ATTACK_INCREASE_COST = 100;
	public static final float [] BODY = {5,22,5,34,26,34,26,22};
	public static final Vector2 GUN_POS = new Vector2(4,26);
	public static final Vector2 TEXTURE_SIZE =  new Vector2(32,56);
    public TowerMachine(TextureRegion actorRegion, GameActorPool<GameActor> pool){
    	super(actorRegion,pool,BODY,TEXTURE_SIZE,GUN_POS,HEALTH, ARMOR, ATTACK,ATTACK_SPEED,RANGE,COST,ARMOR_COST,RANGE_INCREASE_COST, SPEED_INCREASE_COST,ATTACK_INCREASE_COST);
    }

}