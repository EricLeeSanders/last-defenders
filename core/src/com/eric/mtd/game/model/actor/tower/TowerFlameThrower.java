package com.eric.mtd.game.model.actor.tower;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IFlame;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.model.factory.ActorFactory.GameActorPool;
import com.eric.mtd.game.model.level.Map;

public class TowerFlameThrower extends Tower implements IFlame{

	public static float HEALTH = 8; 
	public static float ARMOR = 3;
	public static float ATTACK = 5; 
	public static float ATTACK_SPEED = 1f;
	public static float RANGE = 90; 
	
	public static int COST = 600;
	public static int ARMOR_COST = 5665;
	public static int RANGE_INCREASE_COST = 450;
	public static int SPEED_INCREASE_COST = 450;
	public static int ATTACK_INCREASE_COST = 450;
	
	public static float [] BODY = {5,22,5,34,26,34,26,22};
	public static Vector2 GUN_POS = new Vector2(4,26);
	public static Vector2 TEXTURE_SIZE =  new Vector2(32,56);
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
    public TowerFlameThrower(TextureRegion actorRegion, GameActorPool<GameActor> pool){
    	super(actorRegion,pool,BODY,TEXTURE_SIZE,GUN_POS,HEALTH, ARMOR, ATTACK,ATTACK_SPEED,RANGE,COST,ARMOR_COST,RANGE_INCREASE_COST, SPEED_INCREASE_COST,ATTACK_INCREASE_COST);
    }
   /* @Override
    public void draw(Batch batch, float alpha){
    	
    	Rectangle body = this.getFlameBody();
    	shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		shapeRenderer2.begin(ShapeType.Line);
		shapeRenderer2.setColor(Color.RED);
		shapeRenderer2.rect(body.x,body.y,body.width,body.height);
		shapeRenderer2.end();
		super.draw(batch, alpha);
    }*/
    
	@Override
	public Vector2 getFlameSize() {
		Vector2 size = new Vector2(20,62);
		return size;
	}

}