package com.eric.mtd.game.model.actor.health;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.Resources;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.model.factory.ActorFactory.HealthPool;
import com.eric.mtd.game.stage.GameStage;

public class HealthBar extends Actor implements Pool.Poolable{
	   private GameActor actor = null;
	   private ShapeRenderer backgroundBar = new ShapeRenderer();
	   private ShapeRenderer healthBar = new ShapeRenderer();
	   private ShapeRenderer armorBar = new ShapeRenderer();
	    @Override
	    public void draw(Batch batch, float alpha){
	    	if(actor != null){
	    		float healthPercentage = actor.getHealthPercent();
	    		float armorPercentage = actor.getArmorPercent();
	    		if(((healthPercentage < 100 && !actor.hasArmor()) || (actor.hasArmor() && armorPercentage < 100)) && healthPercentage > 0){ //only show if hit and not dead
		    		float healthBarSize = (((float)(30)*(healthPercentage))/100);
		    		float armorBarSize = (((float)(30)*(armorPercentage))/100);
		    		setPosition(actor.getPositionCenter().x-10, actor.getPositionCenter().y+20);
		    		batch.end();
		    		
		            backgroundBar.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
					backgroundBar.begin(ShapeType.Filled);
					backgroundBar.setColor(Color.RED);
					backgroundBar.rect(getX(),getY(),30,4);
					backgroundBar.end();
					
					healthBar.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
					healthBar.begin(ShapeType.Filled);
					healthBar.setColor(Color.GREEN);
					healthBar.rect(getX(),getY(),healthBarSize,4);
					healthBar.end();
					
					if(actor.hasArmor()){
						armorBar.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
						armorBar.begin(ShapeType.Filled);
						armorBar.setColor(Color.DARK_GRAY);
						armorBar.rect(getX(),getY(),armorBarSize,4);
						armorBar.end();
						
					}
					batch.begin();
	    		}
	    	}
	    }
	    
	    @Override
	    public void act(float delta){

	    	////if(Logger.DEBUG)System.out.println(actor.getParent().toString());
	    	super.act(delta);
	    	if(actor.isDead()){
	    		//if(Logger.DEBUG)System.out.println("Freeing Healthbar");
	    		ActorFactory.healthPool.free(this);
	    	}
	    }
	    public void setActor(GameActor actor, ActorGroups actorGroups){
	    	////if(Logger.DEBUG)System.out.println("Healthbar: Setting actor");
	    	this.actor = actor;
	    	this.setSize(30, 4);
			actorGroups.getHealthBarGroup().addActor(this);
			
	    }

		@Override
		public void reset() {
			////if(Logger.DEBUG)System.out.println("Resetting healthbar");
			this.actor = null;
    		this.remove();
		}
	}
