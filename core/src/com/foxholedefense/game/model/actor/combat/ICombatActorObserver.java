package com.foxholedefense.game.model.actor.combat;
/**
 * Interface for observing a tower
 * @author Eric
 *
 */
public interface ICombatActorObserver {
	public void notifyCombatActor(CombatActor actor, CombatActorEvent event);

	public enum CombatActorEvent {
		DEAD, HEALED, ARMOR_BROKEN, OTHER
	}
}
