package com.eric.mtd.game.service.actorplacement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.support.AirStrike;
import com.eric.mtd.game.model.actor.support.AirStrikeLocation;
import com.eric.mtd.game.model.actor.support.SupplyDrop;
import com.eric.mtd.game.model.actor.support.SupplyDropCrate;
import com.eric.mtd.game.model.actor.support.SupportActor;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.interfaces.ISupplyDropFactory;
import com.eric.mtd.util.ActorUtil;
import com.eric.mtd.util.Logger;

public class SupplyDropPlacement {
	private SupplyDropCrate currentSupplyDropCrate;
	private ISupplyDropFactory actorFactory;
	public SupplyDropPlacement(ISupplyDropFactory actorFactory) {
		this.actorFactory = actorFactory;
	}
	public void createSupplyDrop() {
		currentSupplyDropCrate = actorFactory.loadSupplyDropCrate();
		currentSupplyDropCrate.setPosition(0, 0);
		currentSupplyDropCrate.setActive(false);
		currentSupplyDropCrate.setVisible(false);
	}
	public void setLocation(Vector2 location){
		currentSupplyDropCrate.setVisible(true);
		currentSupplyDropCrate.setShowRange(true);
		currentSupplyDropCrate.setPositionCenter(location);
	}
	public void finishPlacement() {
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
