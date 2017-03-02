package com.foxholedefense.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.factory.interfaces.ISupplyDropFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;

public class SupplyDropPlacement {
	private SupplyDropCrate currentSupplyDropCrate;
	private ISupplyDropFactory actorFactory;
	public SupplyDropPlacement(ISupplyDropFactory actorFactory) {
		this.actorFactory = actorFactory;
	}
	public void createSupplyDrop() {
		Logger.info("SupplyDropPlacement: creating supply drop");
		currentSupplyDropCrate = actorFactory.loadSupplyDropCrate();
		currentSupplyDropCrate.setPosition(0, 0);
		currentSupplyDropCrate.setActive(false);
		currentSupplyDropCrate.setVisible(false);
	}
	public void setLocation(Vector2 location){
		Logger.info("SupplyDropPlacement: creating location");
		currentSupplyDropCrate.setVisible(true);
		currentSupplyDropCrate.setShowRange(true);
		currentSupplyDropCrate.setPositionCenter(location);
	}
	public void finishPlacement() {
		Logger.info("SupplyDropPlacement: finishing placement");
		if (isCurrentSupplyDropCrate()) {
			float x = ActorUtil.calcXCenterFromBotLeft(currentSupplyDropCrate.getX(), currentSupplyDropCrate.getWidth());
			float y = ActorUtil.calcYCenterFromBotLeft(currentSupplyDropCrate.getY(), currentSupplyDropCrate.getHeight());
			actorFactory.loadSupplyDrop().beginSupplyDrop(new Vector2(x,y));
			currentSupplyDropCrate.setShowRange(false);
			currentSupplyDropCrate.freeActor();
			currentSupplyDropCrate = null;
		}
	}
	
	public void removeCurrentSupplyDropCrate(){
		Logger.info("SupplyDropPlacement: remove supply drop");
		if (isCurrentSupplyDropCrate()) {
			currentSupplyDropCrate.freeActor();
			currentSupplyDropCrate = null;
		}
	}

	public boolean isCurrentSupplyDropCrate() {
		return (currentSupplyDropCrate != null);
	}

	public SupplyDropCrate getCurrentSupplyDropCrate() {
		return currentSupplyDropCrate;
	}
}
