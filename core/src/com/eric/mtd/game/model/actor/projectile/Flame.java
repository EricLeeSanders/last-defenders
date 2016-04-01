package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IFlame;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.ai.Damage;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Resources;

public class Flame extends Actor implements Pool.Poolable{
	private Animation flameAnimation;
	private TextureRegion currentFlame;
	private float stateTime;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private boolean showFlame;
	private TextureRegion [] flameRegions = new TextureRegion[25];
	private GameActor shooter, target;
	private ShapeRenderer shapeRenderer2 = new ShapeRenderer();
	private boolean firstPass = true;
	private static int attackCount = 0;
	public static float totalDamage;
	Polygon poly = null;
	Polygon targetBodySnap = null;
	private float attackCounter = 0;
	private float attackTick, attackDamage;
	public Flame(){
		TextureAtlas flameAtlas = Resources.getAtlas(Resources.FLAMES_ATLAS);
    	for(int i = 0; i < 25; i++){
    		flameRegions[i] = flameAtlas.findRegion("Flame"+(i+1));
    	}
	}
	public void setFlame(GameActor shooter, GameActor target){
		this.shooter = shooter;
		this.target = target;
		if(shooter.getStage() instanceof GameStage){
			((GameStage)shooter.getStage()).getActorGroups().getFlameGroup().addActor(this);
		}
		stateTime = 0;
		flameAnimation = new Animation(shooter.getAttackSpeed()/25, flameRegions);
    	flameAnimation.setPlayMode(PlayMode.NORMAL);
    
    	attackTick = (shooter.getAttackSpeed()/shooter.getAttack());
    	attackDamage = 1; //Do a little bit of damage each tick
    	//attackDamage = actor.getAttack();
    	attackCount++;
    	//if(Logger.DEBUG)System.out.println("Attack Count: " + attackCount);
	}
	@Override
    public void act(float delta){
    	super.act(delta);
    	//attackTick = (attackTick * delta);
    	stateTime += delta;
    	if(shooter.isDead()){
			this.remove();
			ActorFactory.flamePool.free(this);
    	}
		if(attackCounter >= attackTick){
			attackCounter = 0;
			takeFlameDamage();
		}
		else{
			attackCounter += delta;
		}
    }
    public void draw(Batch batch, float alpha){
        Gdx.gl.glClearColor(0, 0, 0, 0); 
        Gdx.gl.glEnable(GL20.GL_BLEND);
		//stateTime += (Gdx.graphics.getDeltaTime() * MTDGame.gameSpeed );    
		currentFlame = flameAnimation.getKeyFrame(stateTime, false);       	
		this.setOrigin((currentFlame.getRegionWidth()/2),0);
		this.setPosition(shooter.getGunPos().x-(currentFlame.getRegionWidth()/2),shooter.getGunPos().y);
    	setRotation(shooter.getRotation()); 	
    	batch.end();
    		poly = getFlameBody();
    	shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		shapeRenderer2.begin(ShapeType.Line);
		shapeRenderer2.setColor(Color.RED);
		shapeRenderer2.polygon(poly.getTransformedVertices());
		//shapeRenderer2.rect(this.getX(),this.getY(),20,70);
		shapeRenderer2.end();
    	/*shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		shapeRenderer2.begin(ShapeType.Line);
		shapeRenderer2.setColor(Color.RED);
		shapeRenderer2.polygon(targetBodySnap.getTransformedVertices());
		//shapeRenderer2.rect(this.getX(),this.getY(),20,70);
		shapeRenderer2.end();*/
		batch.begin(); //Question: Not sure why this needs to be here... and the end above...
		batch.draw(currentFlame,this.getX(),this.getY(),this.getOriginX(),this.getOriginY(), currentFlame.getRegionWidth(),currentFlame.getRegionHeight(), 1, 1, this.getRotation());
		if(flameAnimation.isAnimationFinished(stateTime)){
			this.remove();
			ActorFactory.flamePool.free(this);
		}
    }

    public Polygon getFlameBody(){
    	Vector2 flameSize = ((IFlame)shooter).getFlameSize();
		float [] bodyPoints = {0,0,0,flameSize.y,flameSize.x,flameSize.y,flameSize.x,0};
		Polygon flameBody = new Polygon(bodyPoints);
		flameBody.setPosition(shooter.getGunPos().x-(flameSize.x/2),shooter.getGunPos().y);
		flameBody.setOrigin((flameSize.x/2),0);
		flameBody.setRotation(this.getRotation());
		
		return flameBody;
    }
    public void takeFlameDamage(){
		Group targetGroup;
		if(shooter instanceof Tower){
			if(this.getStage() instanceof GameStage){
				targetGroup = ((GameStage)this.getStage()).getActorGroups().getEnemyGroup();
			}
			else{
				targetGroup = null;
			}

		}
		else{
			if(this.getStage() instanceof GameStage){
				targetGroup = ((GameStage)this.getStage()).getActorGroups().getTowerGroup();
			}
			else{
				targetGroup = null;
			}
		}
		Damage.dealFlameDamage(shooter, targetGroup, this, attackDamage);
    }
	@Override
	public void reset() {
		//if(Logger.DEBUG)System.out.println("freeing flame");
		this.clear();
		stateTime = 0;
		flameAnimation = null;
		firstPass = true;
		attackCounter = 0;
	}

}
