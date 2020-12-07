package com.lastdefenders.game.model.actor.groups;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;

public class EnemyGroup extends GenericGroup<Enemy> {

    public SnapshotArray<Enemy> getCastedChildren(){

        SnapshotArray<Enemy> enemies = new SnapshotArray<>();
        for(Actor actor : getChildren()){
            Enemy enemy = (Enemy)actor;
            enemies.add(enemy);
        }
        return enemies;
    }
}
