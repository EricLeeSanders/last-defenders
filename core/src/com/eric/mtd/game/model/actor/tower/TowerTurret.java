package com.eric.mtd.game.model.actor.tower;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Resources;

public class TowerTurret extends Tower implements IRotatable{
	

	
	public static float HEALTH = 14;  
	public static float ARMOR = 10;
	public static float ATTACK = 3; 
	public static float ATTACK_SPEED = .2f;
	public static float RANGE = 50; 
	
	public static int COST = 1300;
	public static int ARMOR_COST = 900;
	public static int RANGE_INCREASE_COST = 500;
	public static int SPEED_INCREASE_COST = 500;
	public static int ATTACK_INCREASE_COST = 500;

	//public static float [] BODY = {40,0,19,4,5,19,0,30,0,47,4,57,12,66,24,73,40,74,56,73,68,66,76,57,80,47,80,30,75,19,61,4};
	public static float [] BODY = {30,0,13,3,3,14,0,23,0,35,3,43,11,51,16,55,30,56,46,54,56,43,60,35,60,23,56,14,49,5};
	public static float [] RANGE_COORDS = {30,28,-10,170,70,170};
	public static Vector2 GUN_POS = new Vector2(4,26);
	public static Vector2 TEXTURE_BODY_SIZE =  new Vector2(60,56);
	public static Vector2 TEXTURE_TURRET_SIZE = new Vector2(32,56); 
	
	private TextureRegion bodyRegion,turretRegion;
	private ShapeRenderer rangeShape = new ShapeRenderer();
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
	private float bodyRotation;
	private float [] rangeTransformedVertices;
    public TowerTurret(TextureRegion bodyRegion,TextureRegion turretRegion, GameActorPool<GameActor> pool){
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
             rangeTransformedVertices = ((Polygon)getRangeShape()).getTransformedVertices();
             rangeShape.triangle(rangeTransformedVertices[0],rangeTransformedVertices[1],rangeTransformedVertices[2],rangeTransformedVertices[3],rangeTransformedVertices[4],rangeTransformedVertices[5]);
             rangeShape.end();
         }
         
        shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
 		shapeRenderer2.begin(ShapeType.Line);
 		shapeRenderer2.setColor(Color.YELLOW);
 		shapeRenderer2.polygon(getBody().getTransformedVertices());
 		shapeRenderer2.end();
         
         /*shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
 		shapeRenderer2.begin(ShapeType.Filled);
 		shapeRenderer2.setColor(Color.BLACK);
 		shapeRenderer2.circle(getGunPos().x,getGunPos().y,5);
 		shapeRenderer2.end();*/
 		batch.begin(); 
		batch.draw(bodyRegion,this.getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2),this.getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2),TEXTURE_BODY_SIZE.x/2,TEXTURE_BODY_SIZE.y/2, TEXTURE_BODY_SIZE.x,TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
		batch.draw(turretRegion,getX(),getY(),getOriginX(),getOriginY(), TEXTURE_TURRET_SIZE.x,TEXTURE_TURRET_SIZE.y, 1, 1, getRotation());

    }
    @Override
    public Polygon getBody(){
    	Polygon poly = new Polygon(BODY);
    	poly.setOrigin((TEXTURE_BODY_SIZE.x/2), (TEXTURE_BODY_SIZE.y/2));
    	poly.setRotation(bodyRotation);
    	poly.setPosition(getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2), getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2));
    	
    	return poly;
    }

    @Override
    public Shape2D getRangeShape(){
    	Polygon poly = new Polygon(RANGE_COORDS);
    	poly.setOrigin((TEXTURE_BODY_SIZE.x/2), (TEXTURE_BODY_SIZE.y/2));
    	poly.setRotation(bodyRotation);
    	poly.setPosition(getPositionCenter().x-(TEXTURE_BODY_SIZE.x/2), getPositionCenter().y-(TEXTURE_BODY_SIZE.y/2));
    	return poly;
    }

}