package com.foxholedefense.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ai.towerai.FirstEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LastEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LeastHPEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.MostHPEnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.interfaces.Attacker;

/**
 * Created by Eric on 10/28/2016.
 */

public enum TowerAIType implements TowerAI {

    FIRST(new FirstEnemyAI(), 0),
    LAST(new LastEnemyAI(), 1),
    LEAST_HP(new LeastHPEnemyAI(), 2),
    MOST_HP(new MostHPEnemyAI(), 3);

    private TowerAI ai;
    private int position;

    TowerAIType(TowerAI ai, int position) {

        this.ai = ai;
        this.position = position;
    }

    @Override
    public Enemy findTarget(Attacker attacker, SnapshotArray<Actor> enemies) {

        return ai.findTarget(attacker, enemies);
    }

    public TowerAIType getNextTowerAIType() {

        int n = TowerAIType.values().length;
        return TowerAIType.values()[(getPosition() + 1) % n];
    }

    private int getPosition() {

        return position;
    }
}
