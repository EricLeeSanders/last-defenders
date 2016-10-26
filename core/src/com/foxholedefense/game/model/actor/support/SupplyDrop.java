package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.service.factory.ActorFactory.SupplyDropPool;
import com.foxholedefense.game.service.factory.interfaces.ISupplyDropFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

public class SupplyDrop extends GameActor implements Pool.Poolable{
	private static final float SUPPLYDROP_DURATION = 2f;
	private boolean active;
	private SupplyDropPool pool;
	private ISupplyDropFactory supplyDropFactory;
	public SupplyDrop(TextureRegion textureRegion, SupplyDropPool pool, ISupplyDropFactory supplyDropFactory) {
		super(textureRegion, new Dimension(textureRegion.getRegionWidth(), textureRegion.getRegionHeight()));
		this.pool = pool;
		this.supplyDropFactory = supplyDropFactory;
	}
	
	public void beginSupplyDrop(Vector2 dropLocation){
		Logger.info("Beginning Supply drop");
		active = true;
		this.setRotation(-90);
		setPositionCenter(new Vector2(0-this.getHeight(), dropLocation.y));
		float moveToX = Resources.VIRTUAL_WIDTH+this.getHeight();
		float moveToY = ActorUtil.calcYBotLeftFromCenter(dropLocation.y, getHeight());
		this.addAction(Actions.moveTo(moveToX, moveToY,  SUPPLYDROP_DURATION, Interpolation.linear));
		float dropDelay = SUPPLYDROP_DURATION * ((dropLocation.x - (this.getWidth() / 4))/ Resources.VIRTUAL_WIDTH);
		Logger.info("DropDelay: " + dropDelay);
		supplyDropFactory.loadSupplyDropCrate().beginDrop(dropDelay, dropLocation).toBack();
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(active){
			if(this.getActions().size <= 0){
				pool.free(this);
			}
		}
	}

	public void setActive(boolean active){
		this.active = active;
	}
	
	public boolean isActive(){
		return active;
	}
	
	@Override
	public void reset() {
		active = false;
		this.setPosition(0, 0);
		this.setRotation(0);
		this.clear();
		this.remove();
	}
}
