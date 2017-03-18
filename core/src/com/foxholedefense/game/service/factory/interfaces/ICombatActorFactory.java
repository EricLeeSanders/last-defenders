package com.foxholedefense.game.service.factory.interfaces;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;

public interface ICombatActorFactory {
	Tower loadTower(String type);
	Enemy loadEnemy(String type);
}
