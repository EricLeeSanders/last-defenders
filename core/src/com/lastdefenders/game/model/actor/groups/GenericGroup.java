package com.lastdefenders.game.model.actor.groups;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Have to create this because libgdx has no interest in making Groups generic: https://github.com/libgdx/libgdx/pull/4107
 */
public abstract class GenericGroup<T extends Actor> extends Group {
    public abstract SnapshotArray<T> getCastedChildren();
}
