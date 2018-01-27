package com.lastdefenders.game.model.actor.effects.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.service.factory.EffectFactory.LabelEffectPool;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 4/1/2017.
 */

public class WaveOverCoinEffect extends LabelEffect {

    public static final float DURATION = 2;
    public static final float Y_END_OFFSET = 100;
    public static final float Y_BEGIN_OFFSET = 50;
    private static final float SCALE = 0.75f;
    private static final Dimension ICON_SIZE = new Dimension(32, 32);
    private float fontScale;

    private Animation<TextureRegion> animation;

    public WaveOverCoinEffect(LabelEffectPool<WaveOverCoinEffect> pool, Skin skin,
        Array<AtlasRegion> regions, float fontScale) {

        super(pool, DURATION, skin);
        animation = new Animation<TextureRegion>(0.05f, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        setFontScale(SCALE * fontScale);
        LabelStyle style = new LabelStyle(skin.get(LabelStyle.class));
        style.fontColor = Color.YELLOW;
        setStyle(style);
    }

    public void initialize(int money) {

        clearActions();
        setText(String.valueOf(money).toUpperCase());
        pack();

        float x = getStage().getViewport().getWorldWidth() / 2;
        float y = (getStage().getViewport().getWorldHeight() / 2) + Y_BEGIN_OFFSET;

        setPosition(x, y, Align.center);

        addAction(Actions.sequence(
            Actions.moveToAligned(x, y + Y_END_OFFSET, Align.center, DURATION),
            Actions.removeActor()));
    }

    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        TextureRegion region = animation.getKeyFrame(stateTime, false);
        // Draw the icon
        float x = getX() - ICON_SIZE.getWidth() - 10;
        float y = getY() + 6;

        batch.draw(region, x, y, ICON_SIZE.getWidth(), ICON_SIZE.getHeight());

    }
}
