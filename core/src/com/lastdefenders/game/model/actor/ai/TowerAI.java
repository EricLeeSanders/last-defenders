package com.lastdefenders.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.interfaces.Attacker;

/**
 * Created by Eric on 10/28/2016.
 */

public interface TowerAI {

    Enemy findTarget(Attacker attacker, SnapshotArray<Actor> enemies);
}
