package com.lastdefenders.game.model.actor.ai.towerai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.ai.TowerAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.interfaces.IRocket;

/**
 * Created by Eric on 4/24/2017.
 */

public abstract class AbstractTowerAI implements TowerAI {

    @Override
    public Enemy findTarget(Attacker attacker, SnapshotArray<Actor> enemies) {

        if (enemies.size == 0) {
            return null;
        }

        Enemy returnEnemy = null;
        Enemy platedReturnEnemy = null;
        for (Actor actor : enemies) {
            if (actor instanceof Enemy) {
                Enemy enemy = (Enemy) actor;
                if (!enemy.isDead() && enemy.isActive()) {
                    if (CollisionDetection
                        .shapesIntersect(enemy.getBody(), attacker.getRangeShape())) {
                        if (returnEnemy == null || swap(returnEnemy, enemy)) {
                            if (canAttackTarget(attacker, enemy)) {
                                returnEnemy = enemy;
                            } else {
                                /* This target must be a plated enemy (i.e., tank).
                                   Attack the plated enemy if there are no
                                   Other enemies to attack
                                 */
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

    private boolean canAttackTarget(Attacker attacker, Enemy target){
        return !(target instanceof PlatedArmor) || (attacker instanceof IRocket);
    }

    protected abstract boolean swap(Enemy currentEnemy, Enemy replacingEnemy);
}
