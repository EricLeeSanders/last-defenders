package com.eric.mtd.game.model.actor.tower;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Resources;

public class TowerTank extends Tower implements Pool.Poolable,IRPG, IPlatedArmor, IRotatable{
	
	public static float HEALTH = 20;  
	public static float ARMOR = 10;
	public static float ATTACK = 10; 
	public static float ATTACK_SPEED =0.9f;
	public static float RANGE = 80; 
	
	public static int COST = 1500;
	public static int ARMOR_COST = 1200;
	public static int RANGE_INCREASE_COST = 650;
	public static int SPEED_INCREASE_COST = 650;
	public static int ATTACK_INCREASE_COST = 650;
	
	public static float [] BODY ={0,0,0,75,50,75,50,0};
	public static Vector2 GUN_POS = new Vector2(0,57);
	public static Vector2 TEXTURE_BODY_SIZE =  new Vector2(50,76);
	public static Vector2 TEXTURE_TURRET_SIZE = new Vector2(22,120); 
	
	private TextureRegion bodyRegion,turretRegion;
	private ShapeRenderer rangeShape = new ShapeRenderer();
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
	private float bodyRotation;
	
    public TowerTank(TextureRegion bodyRegion ,TextureRegion turretRegion, GameActorPool<GameActor> pool){
    	super(turretRegion,pool,BODY,TEXTURE_TURRET_SIZE,GUN_POS,HEALTH, ARMOR,ATTACK,ATTACK_SPEED,RANGE,COST,ARMOR_COST,RANGE_INCREASE_COST, SPEED_INCREASE_COST,ATTACK_INCREASE_COST);
    	this.bodyRegion = bodyRegion;
    	this.turretRegion = turretRegion;
    }
    @Override
    public void draw(Batch batch, float alpha){
 		if(!isActive()){
 			bodyRotation = getRotation(); //Only rotate the tank body when the tank is not active (when the tank is being placed).
 		}
 		batch.end();
         if(isShowRange()){
             Gdx.gl.glClearColor(0, 0, 0, 0); 
             Gdx.gl.glEnable(GL20.GL_BLEND);
             
             rangeShape.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
             rangeShape.begin(ShapeType.Filled);
             rangeShape.setColor(getRangeColor());
             rangeShape.circle(((Circle)getRangeShape()).x,((Circle)getRangeShape()).y,((Circle)getRangeShape()).radius);
             rangeShape.end();
 			
         }
         
        shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
 		shapeRenderer2.begin(ShapeType.Line);
 		shapeRenderer2.setColor(Color.YELLOW);
 		shapeRenderer2.polygon(getBody().getTransformedVertices());
 		shapeRenderer2.end();
         
        /* shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
 		shapeRenderer2.begin(ShapeType.Filled);
 		shapeRenderer2.setColor(Color.BLACK);
 		shapeRenderer2.circle(getGunPos().x,getGunPos().y,5);
 		shapeRenderer2.end();*/
 		batch.begin(); 
		batch.draw(bodyRegion,this.getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2),this.getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2),TEXTURE_BODY_SIZE.x/2,TEXTURE_BODY_SIZE.y/2, TEXTURE_BODY_SIZE.x,TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
		batch.draw(turretRegion,getX(),getY(),getOriginX(),getOriginY(), TEXTURE_TURRET_SIZE.x,TEXTURE_TURRET_SIZE.y, 1, 1, getRotation());
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
    public void reset() {
    	super.reset();
    	bodyRotation = 0;
    }
	@Override
	public float getAoeRadius() {
		return 75;
	}




}