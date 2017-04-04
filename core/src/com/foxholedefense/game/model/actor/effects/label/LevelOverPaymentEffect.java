package com.foxholedefense.game.model.actor.effects.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.service.factory.EffectFactory.LabelEffectPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Created by Eric on 4/1/2017.
 */

public class LevelOverPaymentEffect extends LabelEffect {

    private static final float SCALE = 0.75f;
    private static final float DURATION = 2f;
    private static final Dimension ICON_SIZE = new Dimension(32, 32);

    private Animation animation;

    public LevelOverPaymentEffect(LabelEffectPool<LevelOverPaymentEffect> pool, Skin skin, Array<AtlasRegion> regions){
        super(pool, DURATION, skin);
        animation = new Animation(0.05f, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        LabelStyle style = new LabelStyle(skin.get(LabelStyle.class));
        style.fontColor = Color.YELLOW;
        setStyle(style);
    }

    public void initialize(int money){
        clearActions();
        setText(String.valueOf(money).toUpperCase());
        setFontScale(SCALE);
        pack();

        float x = ActorUtil.calcXBotLeftFromCenter((Resources.VIRTUAL_WIDTH / 2), getWidth());
        float y = (Resources.VIRTUAL_HEIGHT / 2) + 50;

        setPosition(x, y);

        addAction(Actions.sequence(
                Actions.moveTo(x, y + 100, DURATION),
                Actions.removeActor()));
    }

    @Override
    public void draw(Batch batch, float alpha){
        super.draw(batch, alpha);

        TextureRegion region = animation.getKeyFrame(stateTime, false);
        // Draw the icon
        float x = getX() - ICON_SIZE.getWidth() - 10;
        float y = getY() + 6;

        batch.draw(region, x, y, ICON_SIZE.getWidth(), ICON_SIZE.getHeight());

    }

}
