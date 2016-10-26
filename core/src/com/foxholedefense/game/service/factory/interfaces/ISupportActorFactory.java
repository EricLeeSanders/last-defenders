package com.foxholedefense.game.service.factory.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.support.SupportActor;

public interface ISupportActorFactory {
	public SupportActor loadSupportActor(String type);
}
