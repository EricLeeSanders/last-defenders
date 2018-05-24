package com.lastdefenders.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.UtilPool;
import com.lastdefenders.util.datastructures.Dimension;

public class BloodSplatter extends TextureEffect {

    private static final Dimension TEXTURE_SIZE = new Dimension(48, 48);
    private static final float FADE_OUT_SPEED = 2.0f;

    public BloodSplatter(EffectPool<BloodSplatter> pool, TextureRegion textureRegion) {
        super(pool, TEXTURE_SIZE);
        setTextureRegion(textureRegion);
    }

    @Override
    public void initialize(Vector2 pos){
        super.initialize(pos);
        addAction(Actions.sequence(
            Actions.fadeOut(FADE_OUT_SPEED),
            UtilPool.getFreeActorAction(getPool())));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);
        batch.setColor(color.r, color.g, color.b, 1f);
    }

    @Override
    public void reset() {
        super.reset();
        getColor().a = 1;
    }
}
