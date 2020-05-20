package com.lastdefenders.game.model.actor.groups;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;

/**
 * A class that holds all of the various Actor Groups that are placed on the
 * game stage.
 *
 * @author Eric
 */
public class ActorGroups {

    private final Group projectileGroup = new Group();
    private final EnemyGroup enemyGroup = new EnemyGroup();
    private final TowerGroup towerGroup = new TowerGroup();
    private final Group healthBarGroup = new Group();
    private final Group supportGroup = new Group();
    private final Group landmineGroup = new Group();
    private final Group deathEffectGroup = new Group();
    private final Group effectGroup = new Group();

    public ActorGroups() {

        projectileGroup.setTransform(false);
        enemyGroup.setTransform(false);
        towerGroup.setTransform(false);
        healthBarGroup.setTransform(false);
        supportGroup.setTransform(false);
        landmineGroup.setTransform(false);
        deathEffectGroup.setTransform(false);
        effectGroup.setTransform(false);
    }

    public Group getProjectileGroup() {

        return projectileGroup;
    }

    public TowerGroup getTowerGroup() {

        return towerGroup;
    }

    public EnemyGroup getEnemyGroup() {

        return enemyGroup;
    }

    public Group getHealthGroup() {

        return healthBarGroup;
    }

    public Group getSupportGroup() {

        return supportGroup;
    }

    public Group getLandmineGroup() {

        return landmineGroup;
    }

    public Group getDeathEffectGroup() {

        return deathEffectGroup;
    }

    public Group getEffectGroup() {

        return effectGroup;
    }

}
