package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.support.SupportActor;

public interface ISupportActorFactory {
	SupportActor loadSupportActor(String type);
}
