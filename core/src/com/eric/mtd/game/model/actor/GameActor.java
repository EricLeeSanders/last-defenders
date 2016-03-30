package com.eric.mtd.game.model.actor;

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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eric.mtd.Logger;
import com.eric.mtd.Resources;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.interfaces.ICollision;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IFlame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.ai.TowerAI;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.model.factory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public abstract class GameActor extends Actor implements Pool.Poolable, Disposable, ICollision{
	private TextureRegion textureRegion;
	private final float RESET_ATTACK_SPEED,RESET_RANGE, MAX_HEALTH, MAX_ARMOR, RESET_ATTACK;
	private int kills;
	private float attackSpeed, range, health, attack, armor;
	private Vector2 textureSize, gunPos;
	private Vector2 positionCenter = new Vector2();
	private float [] bodyPoints;
	private GameActor target; //QUESTION: Contains itself?
	private ShapeRenderer rangeShape = new ShapeRenderer();
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
	private Color rangeColor = new Color(1.0f,0f,0f,0.5f);
	private boolean showRange, hasArmor, dead;
	private GameActorPool<GameActor> pool;
	public GameActor(TextureRegion textureRegion, GameActorPool<GameActor> pool, float [] bodyPoints, Vector2 textureSize, Vector2 gunPos, float health, float armor, float attack, float attackSpeed, float range){
		this.MAX_HEALTH = health;
		this.MAX_ARMOR = armor;
		this.RESET_ATTACK = attack;
		this.RESET_RANGE = range;
		this.RESET_ATTACK_SPEED = attackSpeed;
		this.textureRegion = textureRegion;
		this.health = health;
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.attack = attack;
		this.bodyPoints = bodyPoints;
		this.textureSize = textureSize;
		this.gunPos = gunPos;
		this.range = range;
		this.pool = pool;
    	//QUESTION: do I need to change the x,y bounds if I change the origin?
		this.setOrigin(textureSize.x/2,textureSize.y/2);
		//this.setBounds(this.getX(), this.getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}
	//TODO: Doing rounding because it makes the textures move a bit better.
	public float calculateNewAngle(Vector2 vector){
		double prevAngle = this.getRotation();
    	double angle = MathUtils.atan2(getPositionCenter().x-vector.x,vector.y-getPositionCenter().y); 
		angle = Math.toDegrees(angle);
		double negAngle = (angle-360) % 360;
		double posAngle = (angle+360) % 360;
		double negDistance = Math.abs(prevAngle-negAngle);
		double posDistance = Math.abs(prevAngle - posAngle);
		if(negDistance < posDistance){
			angle = negAngle;
		}
		else{
			angle = posAngle;
		}
		angle = Math.round(angle);
		return (float)angle;
	}
	public float calculateNewAngle(float x, float y){
		double prevAngle = this.getRotation();
    	double angle = MathUtils.atan2(getPositionCenter().x-x,y-getPositionCenter().y); 
		angle = Math.toDegrees(angle);
		double negAngle = (angle-360) % 360;
		double posAngle = (angle+360) % 360;
		double negDistance = Math.abs(prevAngle-negAngle);
		double posDistance = Math.abs(prevAngle - posAngle);
		if(negDistance < posDistance){
			angle = negAngle;
		}
		else{
			angle = posAngle;
		}
		angle = Math.round(angle);
		return (float)angle;
	}
    @Override
    public void reset() {
    	if(Logger.DEBUG)System.out.println("Resetting GameActor");
    	this.setShowRange(false);
    	//this.setDead(false); 
    	health = MAX_HEALTH;
    	armor = MAX_ARMOR;
    	hasArmor = false;
    	attack = RESET_ATTACK;
    	attackSpeed = RESET_ATTACK_SPEED;
    	range = RESET_RANGE;
    	kills = 0;
    	target = null;
    	rangeColor.set(1.0f,0f,0f,0.5f);
    	this.setRotation(0);
    	this.clear();
    	this.remove();
    	//this.clearActions();
    }
    @Override
    public void act(float delta){
    	super.act(delta);
    }
    @Override
    public void draw(Batch batch, float alpha){
        batch.end();
        if(showRange){
            Gdx.gl.glClearColor(0, 0, 0, 0); 
            Gdx.gl.glEnable(GL20.GL_BLEND);
            
            //TODO QUESTION: dispose?
          
            rangeShape.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
            rangeShape.begin(ShapeType.Filled);
            rangeShape.setColor(rangeColor);
            rangeShape.circle(getRangeShape().x,getRangeShape().y,getRangeShape().radius);//TODO:QUESTION:Got to be a better way to do this
            rangeShape.end();
			
        }
        	/*shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
      		shapeRenderer2.begin(ShapeType.Filled);
      		shapeRenderer2.setColor(Color.BLACK);
      		shapeRenderer2.circle(getGunPos().x,getGunPos().y,5);
      		shapeRenderer2.end();*/
       /* if(this instanceof IFlame){
        	Rectangle body = ((IFlame)this).getFlameBody();
        	shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
      		shapeRenderer2.begin(ShapeType.Line);
      		shapeRenderer2.setColor(Color.RED);
      		shapeRenderer2.rect(body.x,body.y,body.width,body.height);
      		shapeRenderer2.end();
        }*/
			batch.begin(); //Question: Not sure why this needs to be here... and the end above...
			batch.draw(textureRegion,getX(),getY(),getOriginX(),getOriginY(), textureSize.x,textureSize.y, 1, 1, getRotation());
        
		//batch.draw(textureRegion,getX(),getY(),getOriginX(),getOriginY(), 100,100, 1, 1, getRotation());
       /* shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
 		shapeRenderer2.begin(ShapeType.Line);
 		shapeRenderer2.setColor(Color.RED);
 		shapeRenderer2.polygon(getBody().getTransformedVertices());
 		shapeRenderer2.end();*/
    }
    public float getHealth(){
    	return health;
    }
    public void takeDamage(float damage){
    	if(hasArmor){
    		if((armor-damage)<0){
    			health = health - (damage-armor);
    			armor = 0;
    		}
    		else{
    			armor = armor - damage;
    		}
    		
    		if(armor <= 0){
    			hasArmor = false;
    		}
    	}
    	else{
    		health = health - damage;
    	}
    	if(health <=0){
    		this.setDead(true);
    	}
 
    }
    public String getAttackType(){
    	return "Nearest";
    }
    public void setTarget(GameActor target){
    	this.target = target;
    }
    @Override
    public Vector2 getPositionCenter(){
    	positionCenter.set(getX()+(textureSize.x/2), getY()+(textureSize.y/2));
    	return positionCenter;
    	//return new Vector2(getX()+(textureSize.x/2), getY()+(textureSize.y/2));
    }
   
    public Vector2 getRotatedCoords(Vector2 coords){
    	//Math stuff here - http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
    	double rotation = Math.toRadians(this.getRotation());
    	float cosa = (float) Math.cos(rotation);
    	float sina = (float) Math.sin(rotation);
    	float newX = ((((coords.x-getPositionCenter().x)*cosa)-((coords.y-getPositionCenter().y)*sina))+getPositionCenter().x);
    	float newY = ((((coords.x-getPositionCenter().x)*sina)+((coords.y-getPositionCenter().y)*cosa))+getPositionCenter().y);
    	return new Vector2(newX,newY);
    }
    public Vector2 getGunPos(){
    	Vector2 pos = new Vector2((getPositionCenter().x+gunPos.x),(getPositionCenter().y+gunPos.y));
    	return getRotatedCoords(pos);
    	
    }
    @Override
    public void setPositionCenter(Vector2 pos){
    	this.setPosition(pos.x-(textureSize.x/2),pos.y-(textureSize.y/2));
    }
    public void setPositionTopMiddle(Vector2 pos){
    	this.setPosition(pos.x-(textureSize.x/2), pos.y-(textureSize.y));
    }
    public Circle getRangeShape(){
    	Vector2 center = getPositionCenter();
    	return new Circle(center.x,center.y, range);
    }
    public float getAttackSpeed(){
    	return attackSpeed;
    }
    public void setAttackSpeed(float attackSpeed){
    	this.attackSpeed = attackSpeed;
    }
    public float getRange(){
    	return range;
    }
    public void setRange(float range){
    	this.range = range;
    }
    public void attackTarget(){
    	if(Logger.DEBUG)System.out.println("Attacking target at " +getTarget().getPositionCenter());
    	if(this instanceof IFlame){
    		Flame flame = ActorFactory.loadFlame();
    		flame.setFlame(this,this.getTarget());
    	}
    	else if(this instanceof IRPG){
        	RPG rpg = ActorFactory.loadRPG();
        	rpg.setAction(this, target,this.getGunPos(),new Vector2(10,10));
    	}
    	else{
	    	Bullet bullet = ActorFactory.loadBullet();
	    	bullet.setAction(this, target,this.getGunPos(),new Vector2(10,10));
    	}
    	
    }
    @Override
    public Polygon getBody(){
    	Polygon poly = new Polygon(bodyPoints);
    	poly.setOrigin((textureSize.x/2), (textureSize.y/2));
    	poly.setRotation(this.getRotation());
    	poly.setPosition(getPositionCenter().x-(textureSize.x/2), getPositionCenter().y-(textureSize.y/2));
    	
    	return poly;
    }
    public void setTexture(TextureRegion textureRegion){
    	this.textureRegion = textureRegion;	
    }
    public void setShowRange(boolean bool){
    	showRange = bool;
    }
    public boolean isShowRange(){
    	return showRange;
    }
    public void setRangeColor(float r, float g, float b, float a){
    	rangeColor.set(r,g,b,a);
    }
    public Color getRangeColor(){
    	return rangeColor;
    }
    public void setDead(boolean dead){
    	this.dead = dead;
    	if(isDead()){
    		pool.free(this);
       	}
    }
	public boolean isDead() {
		return dead;
	}
	public int getNumOfKills(){
		return kills;
	}
	public void giveKill(){
		kills++;
	}
	public float getHealthPercent(){
		return (((float)this.getHealth()/this.getMaxHealth())*100);
	}
	public float getArmorPercent(){
		return (((float)this.armor/this.MAX_ARMOR)*100);
	}
	public float getMaxHealth(){
		return MAX_HEALTH;
	}
	public void resetHealth(){
		health = MAX_HEALTH;
	}
	public float getAttack() {
		return attack;
	}
	public void setAttack(float attack){
		this.attack = attack;
	}
	public boolean hasArmor(){
		return hasArmor;
	}
	public void setHasArmor(boolean hasArmor){
		this.hasArmor = hasArmor;
	}
	public void freeActor(){
		pool.free(this);
	}
    @Override
    public void dispose() {
        ////if(Logger.DEBUG)System.out.println("Actor Disposing");
    }
    public Vector2 getTextureSize(){
    	return textureSize;
    }
    public GameActor getTarget(){
    	return target;
    }
	
}
