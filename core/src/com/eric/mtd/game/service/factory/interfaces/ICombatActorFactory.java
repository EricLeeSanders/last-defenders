package com.eric.mtd.game.service.factory.interfaces;

import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.combat.tower.Tower;

public interface ICombatActorFactory {
	public Tower loadTower(String type);
	public Enemy loadEnemy(String type);
}
