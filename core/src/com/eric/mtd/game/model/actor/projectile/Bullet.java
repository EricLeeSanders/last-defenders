package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.Logger;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.enemy.Enemy;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.ai.Damage;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;

public class Bullet extends Actor implements Pool.Poolable{
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private GameActor target, shooter;
	public void setAction(GameActor shooter, GameActor target, Vector2 pos, Vector2 size){
		this.target = target;
		this.shooter = shooter;
		this.setPosition(pos.x,pos.y);
		this.setSize(size.x, size.y);
		shooter.getStage();
		if(shooter.getStage() instanceof GameStage){
			((GameStage)shooter.getStage()).getActorGroups().getBulletGroup().addActor(this);
		}
		Vector2 start = shooter.getGunPos();
		Vector2 end = target.getPositionCenter();
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(end.x,end.y);
        moveAction.setDuration(end.dst(start)/350f);
        addAction(moveAction);
	}
    public void draw(Batch batch, float alpha){
    	batch.end();
        Gdx.gl.glClearColor(0, 0, 0, 0); 
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(getBody().x,getBody().y,3);
		shapeRenderer.end();
       /* shapeRenderer.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(target.getCenterPosition().x,target.getCenterPosition().y,((IAoeDamage) actor).getAoeRadius());//TODO:QUESTION:Got to be a better way to do this
        shapeRenderer.end();*/
		batch.begin(); 
    }
    public Rectangle getBody(){
    	return new Rectangle(getX(),getY(),getWidth(),getHeight());
    }
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(target.isDead()){
    		ActorFactory.bulletPool.free(this);
    		this.remove();
    	}
    	else if (this.getActions().size == 0){
    		//if(Logger.DEBUG)System.out.println("Bullet did not reach target");
    		Damage.dealBulletDamage(shooter, target);
    		ActorFactory.bulletPool.free(this);
    		this.remove();
    	}
    	/*else if (CollisionDetection.bulletAndTarget(getBody(), target.getBody())){
    		targetTakeDamage();
    		ActorFactory.bulletPool.free(this);
    		this.remove();
    	}*/
    }
	@Override
	public void reset() {
		if(Logger.DEBUG)System.out.println("freeing bullet");
		this.clear();
		target = null;
		shooter = null;
		setPosition(0,0);
	}

}
