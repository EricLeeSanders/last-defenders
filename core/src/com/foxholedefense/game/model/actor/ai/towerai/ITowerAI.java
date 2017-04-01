package com.foxholedefense.game.model.actor.ai.towerai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;

/**
 * Created by Eric on 10/28/2016.
 */

public interface ITowerAI {
    Enemy findTarget(IAttacker attacker, SnapshotArray<Actor> enemies);
}
