package com.eric.mtd.game.service.factory.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.support.SupportActor;

public interface ISupportActorFactory {
	public SupportActor loadSupportActor(String type);
}
