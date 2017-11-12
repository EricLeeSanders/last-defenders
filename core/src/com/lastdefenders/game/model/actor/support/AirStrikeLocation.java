package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lastdefenders.game.service.factory.SupportActorFactory.AirStrikeLocationPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.datastructures.pool.LDVector2;

public class AirStrikeLocation extends Actor {

    private LDVector2 location;
    private float radius;
    private boolean showRange = true;
    private TextureRegion rangeTexture;
    private AirStrikeLocationPool pool;

    public AirStrikeLocation(AirStrikeLocationPool pool, TextureRegion rangeTexture) {

        this.pool = pool;
        this.rangeTexture = rangeTexture;
    }

    public void initialize(LDVector2 location, float radius) {

        this.location = location;
        this.radius = radius;
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if (isShowRange()) {
            drawRange(batch);
        }
        super.draw(batch, alpha);
    }

    private void drawRange(Batch batch) {

        float width = radius * 2;
        float height = radius * 2;
        float x = ActorUtil.calcBotLeftPointFromCenter(location.x, width);
        float y = ActorUtil.calcBotLeftPointFromCenter(location.y, height);
        batch.draw(rangeTexture, x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
    }

    public LDVector2 getLocation() {

        return location;
    }

    private boolean isShowRange() {

        return showRange;
    }

    public void setShowRange(boolean showRange) {

        this.showRange = showRange;
    }

    @Override
    public void clear() {

        location.free();
        pool.free(this);
        showRange = true;
    }
}
