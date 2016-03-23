package com.eric.mtd.game.model.actor.enemy;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.factory.ActorFactory.GameActorPool;
import com.eric.mtd.game.model.level.Map;

public class EnemyTank extends Enemy implements IPlatedArmor, IRPG{
	
	public static float HEALTH = 20;  
	public static float ARMOR = 10;
	public static float ATTACK = 10; 
	public static float ATTACK_SPEED =0.9f;
	public static float RANGE = 100; 
	public static float SPEED = 45;
	
	public static float [] BODY ={0,0,0,75,50,75,50,0};
	public static Vector2 GUN_POS = new Vector2(0,57);
	public static Vector2 TEXTURE_BODY_SIZE =  new Vector2(50,76);
	public static Vector2 TEXTURE_TURRET_SIZE = new Vector2(22,120); 
	private TextureRegion tankRegion;
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
	private float bodyRotation;
    public EnemyTank(TextureRegion tankRegion,TextureRegion turretRegion, GameActorPool<GameActor> pool){
    	super(turretRegion,pool,BODY,TEXTURE_TURRET_SIZE, GUN_POS,SPEED, HEALTH, ARMOR,ATTACK,ATTACK_SPEED,RANGE);
    	this.tankRegion = tankRegion;
    }
    @Override
    public void draw(Batch batch, float alpha){
    	batch.end();
    	shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
  		shapeRenderer2.begin(ShapeType.Line);
  		shapeRenderer2.setColor(Color.YELLOW);
  		shapeRenderer2.polygon(getBody().getTransformedVertices());
  		shapeRenderer2.end();
  		batch.begin();
  		if(this.getActions().size > 0){
	 		if(this.getActions().get(0) instanceof MoveToAction){
	 			bodyRotation = getRotation(); //Only rotate the tank body when the tank is not active (when the tank is being placed).
	 		}
  		}
    	batch.draw(tankRegion,this.getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2),this.getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2),TEXTURE_BODY_SIZE.x/2,TEXTURE_BODY_SIZE.y/2, TEXTURE_BODY_SIZE.x,TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
    	super.draw(batch, alpha);
    }
    //Need to get rotation of the tankBody, not turret so we need to override
    @Override
    public Polygon getBody(){
    	Polygon poly = new Polygon(BODY);
    	poly.setOrigin((TEXTURE_BODY_SIZE.x/2), (TEXTURE_BODY_SIZE.y/2));
    	poly.setRotation(bodyRotation);
    	poly.setPosition(getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2), getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2));
    	
    	return poly;
    }
	@Override
	public float getAoeRadius() {
		return 75;
	}
}