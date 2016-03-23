package com.eric.mtd.game.model.actor.enemy;


import java.util.ArrayList;
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
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.factory.ActorFactory.GameActorPool;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.stage.GameStage;

public class EnemyRocketLauncher extends Enemy implements IRPG{
	   
	public static float HEALTH = 8;  
	public static float ARMOR = 4;
	public static float ATTACK = 9; 
	public static float ATTACK_SPEED = 1;
	public static float RANGE = 60; 
	public static float SPEED = 55f;
	
	public static float [] BODY = {5,22,5,34,26,34,26,22};
	public static Vector2 GUN_POS = new Vector2(4,26);
	public static Vector2 TEXTURE_SIZE =  new Vector2(32,56);
    public EnemyRocketLauncher(TextureRegion [] actorRegions, GameActorPool<GameActor> pool){
    	super(actorRegions,pool,BODY,TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR,ATTACK,ATTACK_SPEED,RANGE);
    }
	@Override
	public float getAoeRadius() {
		return 50;
	}
}