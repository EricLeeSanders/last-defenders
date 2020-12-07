package com.lastdefenders.game.model.actor.groups;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;


public class TowerGroup extends GenericGroup<Tower> {

    public SnapshotArray<Tower> getCastedChildren(){

        SnapshotArray<Tower> towers = new SnapshotArray<>();
        for(Actor actor : getChildren()){
            Tower tower = (Tower)actor;
            towers.add(tower);
        }

        return towers;
    }
}
