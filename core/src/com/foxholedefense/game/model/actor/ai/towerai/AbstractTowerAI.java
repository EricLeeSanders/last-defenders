package com.foxholedefense.game.model.actor.ai.towerai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.ai.TowerAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.health.interfaces.PlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IRocket;
/**
 * Created by Eric on 4/24/2017.
 */

public abstract class AbstractTowerAI implements TowerAI {
    @Override
    public Enemy findTarget(IAttacker attacker, SnapshotArray<Actor> enemies) {
        if (enemies.size == 0) {
            return null;
        }
        Enemy returnEnemy = null;
        Enemy platedReturnEnemy = null;
        for (Actor actor : enemies) {
            if (actor instanceof Enemy) {
                Enemy enemy = (Enemy)actor;
                if (!enemy.isDead()) {
                    if (CollisionDetection.shapesIntersect(enemy.getBody(), attacker.getRangeShape())) {
                        if (returnEnemy == null || swap(returnEnemy, enemy)) {
                            if (!(enemy instanceof PlatedArmor) || (attacker instanceof IRocket)) {
                                returnEnemy = enemy;
                            } else {
                                // Attack the plated enemy if there are no
                                // Other enemies to attack
                                platedReturnEnemy = enemy;
                            }
                        }
                    }
                }
            }
        }
        // If there is no enemy then return plated enemy so the tower has
        // something to attack
        if (returnEnemy == null) {
            return platedReturnEnemy;
        } else {
            return returnEnemy;
        }

    }

    protected abstract boolean swap(Enemy currentEnemy, Enemy replacingEnemy);
}
