package com.foxholedefense.game.model.actor.ai.towerai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IRocket;

/**
 * Created by Eric on 10/28/2016.
 */

public class LeastHPEnemyAI extends AbstractTowerAI {

    @Override
    protected boolean swap(Enemy currentEnemy, Enemy replacingEnemy) {
        return replacingEnemy.getHealth() < currentEnemy.getHealth();
    }
}
