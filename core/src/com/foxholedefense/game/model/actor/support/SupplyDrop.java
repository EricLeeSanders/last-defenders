package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupplyDropPool;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupplyDropCratePool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

public class SupplyDrop extends GameActor implements Pool.Poolable{
	private static final float SUPPLYDROP_DURATION = 2f;
	private static final Dimension TEXTURE_SIZE = new Dimension(178, 120);
	private boolean active;
	private SupplyDropPool pool;
	private SupplyDropCratePool supplyDropCratePool;

	public SupplyDrop(TextureRegion textureRegion, SupplyDropPool pool, SupplyDropCratePool supplyDropCratePool) {
		super(textureRegion, TEXTURE_SIZE);
		this.pool = pool;
		this.supplyDropCratePool = supplyDropCratePool;
		setTextureRegion(textureRegion);
	}
	
	public void beginSupplyDrop(float x, float y){
		Logger.info("SupplyDrop: Beginning Supply drop");
		active = true;
		FHDVector2 centerPos = UtilPool.getVector2(0-this.getHeight(),y);
		setPositionCenter(centerPos);
		centerPos.free();
		float moveToX = Resources.VIRTUAL_WIDTH+this.getHeight();
		float moveToY = ActorUtil.calcYBotLeftFromCenter(y, getHeight());
		this.addAction(Actions.moveTo(moveToX, moveToY,  SUPPLYDROP_DURATION, Interpolation.linear));
		float dropDelay = SUPPLYDROP_DURATION * ((x - (this.getWidth() / 4))/ Resources.VIRTUAL_WIDTH);
		Logger.info("DropDelay: " + dropDelay);
		supplyDropCratePool.obtain().beginDrop(dropDelay,x, y).toBack();
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
		Logger.info("SupplyDrop: Resetting");
		active = false;
		this.setPosition(0, 0);
		this.setRotation(0);
		this.clear();
		this.remove();
	}
}
