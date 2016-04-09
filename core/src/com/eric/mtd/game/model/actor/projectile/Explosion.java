package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.ai.Damage;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class Explosion extends Actor implements Pool.Poolable{
	private Animation explosionAnimation;
	private TextureRegion currentExplosion;
	private float stateTime;
	private boolean showExplosion;
	private TextureRegion [] explosionRegions = new TextureRegion[16];
	private GameActor shooter, target;
	public Explosion(){
		TextureAtlas explosionAtlas = Resources.getAtlas(Resources.EXPLOSION_ATLAS);
    	for(int i = 0; i < 16; i++){
    		explosionRegions[i] = explosionAtlas.findRegion("Explosion"+(i+1));
    	}
  
	}
	public void setExplosion(GameActor shooter, GameActor target){
		if(Logger.DEBUG)System.out.println("Setting RPG");
		this.shooter = shooter;
		this.target = target;
		if(shooter.getStage() instanceof GameStage){
			((GameStage)shooter.getStage()).getActorGroups().getExplosionGroup().addActor(this);
		}
		stateTime = 0;
		explosionAnimation = new Animation(0.05f, explosionRegions);
    	explosionAnimation.setPlayMode(PlayMode.NORMAL);
		this.setPosition(target.getPositionCenter().x,target.getPositionCenter().y);
		takeDamage();
	}
    public void draw(Batch batch, float alpha){
        Gdx.gl.glClearColor(0, 0, 0, 0); 
        Gdx.gl.glEnable(GL20.GL_BLEND);
		stateTime += (Gdx.graphics.getDeltaTime() * MTDGame.gameSpeed ); 
		currentExplosion = explosionAnimation.getKeyFrame(stateTime, false);
		
		batch.draw(currentExplosion,this.getX()-(currentExplosion.getRegionWidth()/2),this.getY()-(currentExplosion.getRegionHeight()/2));
		if(explosionAnimation.isAnimationFinished(stateTime)){
			this.remove();
			ActorFactory.explosionPool.free(this);
		}
    }
    public void takeDamage(){
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
		Damage.dealExplosionDamage(shooter, target, targetGroup);
    }
	@Override
	public void reset() {
		if(Logger.DEBUG)System.out.println("freeing explosion");
		this.clear();
		explosionAnimation = null;
	}

}
