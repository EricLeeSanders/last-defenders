package com.lastdefenders.game.model.actor.effects.texture.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.UtilPool;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 4/2/2017.
 */

public class EnemyCoinEffect extends AnimationEffect {

    private static final Dimension TEXTURE_SIZE = new Dimension(12, 12);
    private static final float FRAME_DURATION = 0.05f;
    private static final float DURATION = 2f;

    public EnemyCoinEffect(EffectPool<EnemyCoinEffect> pool, Array<AtlasRegion> regions) {

        super(pool, regions, TEXTURE_SIZE, PlayMode.LOOP, FRAME_DURATION);
    }

    public void initialize(Vector2 pos) {

        this.setPositionCenter(pos);

        addAction(Actions.sequence(
            Actions.moveTo(getX(), getY() + 25, DURATION),
            UtilPool.getFreeActorAction(getPool())));
    }
}
