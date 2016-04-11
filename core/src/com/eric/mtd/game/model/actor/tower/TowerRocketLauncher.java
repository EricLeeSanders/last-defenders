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
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.AudioUtil.ProjectileSound;

public class TowerRocketLauncher extends Tower implements IRPG{
	
	
	public static float HEALTH = 8;  
	public static float ARMOR = 4;
	public static float ATTACK = 10; 
	public static float ATTACK_SPEED = 1;
	public static float RANGE = 60; 
	
	public static int COST = 800;
	public static int ARMOR_COST = 500;
	public static int RANGE_INCREASE_COST = 450;
	public static int SPEED_INCREASE_COST = 450;
	public static int ATTACK_INCREASE_COST = 450;
	
	public static float [] BODY = {5,22,5,34,26,34,26,22};
	public static Vector2 GUN_POS = new Vector2(4,26);
	public static Vector2 TEXTURE_SIZE =  new Vector2(32,56);
    public TowerRocketLauncher(TextureRegion actorRegion, GameActorPool<GameActor> pool){
    	super(actorRegion,pool,BODY,TEXTURE_SIZE,GUN_POS,HEALTH, ARMOR, ATTACK,ATTACK_SPEED,RANGE,COST,ARMOR_COST,RANGE_INCREASE_COST, SPEED_INCREASE_COST,ATTACK_INCREASE_COST);
    }
	@Override
	public float getAoeRadius() {
		return 50;
	}
	@Override
	public void attackTarget() {
		if(Logger.DEBUG)System.out.println("Attacking target at " +getTarget().getPositionCenter());
		AudioUtil.playProjectileSound(ProjectileSound.ROCKET_LAUNCH);
    	RPG rpg = ActorFactory.loadRPG();
    	rpg.setAction(this, getTarget(),this.getGunPos(),new Vector2(10,10));
		
	}

}