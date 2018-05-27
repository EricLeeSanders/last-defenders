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
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class CombatSupportActor extends GameActor implements Pool.Poolable, Attacker {

    private SupportActorPool<? extends Actor> pool;
    private float range, attack;
    private Vector2 gunPos;
    private Vector2 rotatedGunPos = UtilPool.getVector2();
    private boolean active;
    private int cost;
    private Group getTargetGroup;
    private boolean showRange;
    private TextureRegion rangeTexture;
    private Circle rangeShape;

    CombatSupportActor(SupportActorPool<? extends Actor> pool, Group targetGroup, TextureRegion textureRegion,
        Dimension textureSize, TextureRegion rangeTexture, float range, float attack,
        Vector2 gunPos, int cost) {

        super(textureSize);
        this.pool = pool;
        this.range = range;
        this.attack = attack;
        this.gunPos = gunPos;
        this.cost = cost;
        this.getTargetGroup = targetGroup;
        this.rangeTexture = rangeTexture;
        rangeShape = new Circle(getPositionCenter().x, getPositionCenter().y, range);
        setTextureRegion(textureRegion);
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if (isShowRange()) {
            drawRange(batch);
        }
        super.draw(batch, alpha);
    }

    private void drawRange(Batch batch) {

        float width = range * 2;
        float height = range * 2;
        float x = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, width);
        float y = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, height);
        batch.draw(rangeTexture, x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
    }

    public Group getTargetGroup() {

        return getTargetGroup;
    }

    public void freeActor() {

        pool.free(this);
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }

    public int getCost() {

        return cost;
    }

    public boolean isShowRange() {

        return showRange;
    }

    public void setShowRange(boolean bool) {

        showRange = bool;
    }

    @Override
    public Shape2D getRangeShape() {

        rangeShape.setPosition(getPositionCenter().x, getPositionCenter().y);
        return rangeShape;
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

    public SupportActorPool<? extends Actor> getPool(){
        return pool;
    }

    @Override
    public void reset() {

        Logger.info("SupportActor: Resetting");
        this.setActive(false);
        this.setPosition(0, 0);
        this.setRotation(0);
        this.clear();
        this.remove();
    }
}
