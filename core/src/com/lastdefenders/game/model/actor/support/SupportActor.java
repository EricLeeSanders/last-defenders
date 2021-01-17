package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

public class SupportActor extends GameActor implements Pool.Poolable {
    private boolean showRange;
    private boolean active;
    private float range;
    private TextureRegion rangeTexture;
    private Circle rangeShape;
    private SupportActorPool<? extends SupportActor> pool;

    public SupportActor(TextureRegion rangeTexture, TextureRegion textureRegion,
        Dimension textureSize, SupportActorPool<? extends SupportActor> pool, float range) {

        super(textureSize);
        this.range = range;
        this.rangeTexture = rangeTexture;
        this.pool = pool;

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

    public boolean isShowRange() {

        return showRange;
    }

    public void setShowRange(boolean bool) {

        showRange = bool;
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }

    public Circle getRangeShape() {

        rangeShape.setPosition(getPositionCenter().x, getPositionCenter().y);
        return rangeShape;
    }


    public void freeActor() {
        pool.free(this);
    }

    @Override
    public void reset() {

        Logger.info("SupportActor: Resetting");
        setActive(false);
        setShowRange(false);
        setVisible(false);
        this.setPosition(0, 0);
        this.setRotation(0);
        this.clear();
        this.remove();
    }

}
