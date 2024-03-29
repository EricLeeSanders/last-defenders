package com.lastdefenders.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.ai.towerai.ClosestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.FarthestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.LeastHPEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.MostHPEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.StrongestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.WeakestEnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.interfaces.Attacker;

/**
 * Created by Eric on 10/28/2016.
 */

public enum TowerAIType implements TowerAI {

    CLOSEST(new ClosestEnemyAI(), "Closest"),
    FARTHEST(new FarthestEnemyAI(), "Farthest"),
    LEAST_HP(new LeastHPEnemyAI(), "Least HP"),
    MOST_HP(new MostHPEnemyAI(), "Most HP"),
    STRONGEST(new StrongestEnemyAI(), "Strongest"),
    WEAKEST(new WeakestEnemyAI(), "Weakest");

    private final TowerAI ai;
    private final String title;

    TowerAIType(TowerAI ai, String title) {

        this.ai = ai;
        this.title = title;
    }

    @Override
    public Enemy findTarget(Attacker attacker, SnapshotArray<Actor> enemies) {

        return ai.findTarget(attacker, enemies);
    }

    public String getTitle(){
        return title;
    }
}
