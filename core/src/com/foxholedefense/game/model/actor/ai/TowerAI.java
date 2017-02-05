package com.foxholedefense.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ai.towerai.*;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;

/**
 * Created by Eric on 10/28/2016.
 */

public enum TowerAI implements ITowerAI {
    FIRST(new FirstEnemyAI(), 0)
    , LAST(new LastEnemyAI(), 1)
    , LEAST_HP(new LeastHPEnemyAI(), 2)
    , MOST_HP(new MostHPEnemyAI(), 3);

    private ITowerAI ai;
    private int position;

    TowerAI(ITowerAI ai, int position) {
        this.ai = ai;
        this.position = position;
    }
    @Override
    public Enemy findTarget(IAttacker attacker, SnapshotArray<Actor> enemies) {
        return ai.findTarget(attacker, enemies);
    }

    public int getPosition() {
        return position;
    }
}
