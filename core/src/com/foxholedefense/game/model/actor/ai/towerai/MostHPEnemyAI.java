package com.foxholedefense.game.model.actor.ai.towerai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IRpg;

/**
 * Created by Eric on 10/28/2016.
 */

public class MostHPEnemyAI implements ITowerAI {
    @Override
    public Enemy findTarget(IAttacker attacker, SnapshotArray<Actor> enemies) {
        if (enemies.size == 0) {
            return null;
        }
        float highestEnemyHealth = Integer.MIN_VALUE;
        Enemy highestHPEnemy = null;
        Enemy platedHighestHPEnemy = null;
        for (Actor actor : enemies) {
            if (actor instanceof Enemy) {
                Enemy enemy = (Enemy)actor;
                if (enemy.isDead() == false) {
                    if (CollisionDetection.targetWithinRange(enemy.getBody(), attacker.getRangeShape())) {
                        if (enemy.getHealth() > highestEnemyHealth) {
                            if ((enemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
                                highestHPEnemy = enemy;
                                highestEnemyHealth = enemy.getHealth();
                            } else {
                                platedHighestHPEnemy = enemy;
                            }
                        }
                    }
                }
            }
        }
        if (highestHPEnemy == null) {
            return platedHighestHPEnemy;
        } else {
            return highestHPEnemy;
        }
    }
}
