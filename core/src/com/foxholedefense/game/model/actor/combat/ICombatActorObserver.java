package com.foxholedefense.game.model.actor.combat;
/**
 * Interface for observing a tower
 * @author Eric
 *
 */
public interface ICombatActorObserver {
	void notifyCombatActor(CombatActor actor, CombatActorEvent event);

	enum CombatActorEvent {
		DEAD, HEALED, ARMOR_BROKEN, OTHER
	}
}
