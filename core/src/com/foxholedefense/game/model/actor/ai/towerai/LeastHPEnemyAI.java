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

public class LeastHPEnemyAI implements ITowerAI {
    @Override
    public Enemy findTarget(IAttacker attacker, SnapshotArray<Actor> enemies) {
        if (enemies.size == 0) {
            return null;
        }
        float lowestEnemyHealth = Integer.MAX_VALUE;
        Enemy lowestHPEnemy = null;
        Enemy platedLowestHPEnemy = null;
        for (Actor actor : enemies) {
            if (actor instanceof Enemy) {
                Enemy enemy = (Enemy)actor;
                if (enemy.isDead() == false) {
                    if (CollisionDetection.targetWithinRange(enemy.getBody(), attacker.getRangeShape())) {
                        if (enemy.getHealth() < lowestEnemyHealth) {
                            if ((enemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
                                lowestHPEnemy = enemy;
                                lowestEnemyHealth = enemy.getHealth();
                            } else {
                                platedLowestHPEnemy = enemy;
                            }
                        }
                    }
                }
            }
        }
        if (lowestHPEnemy == null) {
            return platedLowestHPEnemy;
        } else {
            return lowestHPEnemy;
        }
    }
}
