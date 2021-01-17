package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public abstract class CombatSupportActor extends SupportActor implements Attacker {

    private SupportActorPool<? extends Actor> pool;
    private float attack;
    private Vector2 gunPos;
    private Vector2 rotatedGunPos = UtilPool.getVector2();
    private EnemyGroup enemyGroup;

    CombatSupportActor(SupportActorPool<? extends SupportActor> pool, EnemyGroup enemyGroup, TextureRegion textureRegion,
        Dimension textureSize, TextureRegion rangeTexture, float range, float attack,
        Vector2 gunPos) {

        super(rangeTexture, textureRegion, textureSize, pool, range);
        this.pool = pool;
        this.attack = attack;
        this.gunPos = gunPos;
        this.enemyGroup = enemyGroup;
    }

    public EnemyGroup getEnemyGroup() {

        return enemyGroup;
    }

    @Override
    public Circle getRangeShape() {
        return super.getRangeShape();
    }

    @Override
    public Vector2 getGunPos() {

        Vector2 centerPos = getPositionCenter();
        LDVector2 rotatedCoords = ActorUtil
            .calculateRotatedCoords((getPositionCenter().x + gunPos.x),
                (getPositionCenter().y + gunPos.y), centerPos.x, centerPos.y,
                Math.toRadians(getRotation()));
        rotatedGunPos.set(rotatedCoords.x, rotatedCoords.y);
        rotatedCoords.free();
        return rotatedGunPos;
    }

    @Override
    public float getAttack() {

        return attack;
    }

    public abstract int getCost();
}
