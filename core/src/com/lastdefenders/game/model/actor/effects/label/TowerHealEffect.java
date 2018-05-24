package com.lastdefenders.game.model.actor.effects.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.ActorUtil;

/**
 * Created by Eric on 1/6/2017.
 */

public class TowerHealEffect extends LabelEffect {

    public static final float DURATION = 2;
    public static final float Y_END_OFFSET = 50;
    private static final float SCALE = 0.3f;
    private static final String MESSAGE = "+ HEALTH";

    private Tower tower = null;

    public TowerHealEffect(EffectPool<TowerHealEffect> pool, Skin skin, float fontScale) {

        super(pool, DURATION, skin);

        setText(MESSAGE);
        setAlignment(Align.center);
        LabelStyle style = new LabelStyle(getStyle());
        style.fontColor = Color.GREEN;
        setStyle(style);
        setFontScale(SCALE * fontScale);
    }


    public Actor initialize(Tower tower) {

        this.tower = tower;

        float x = ActorUtil.calcBotLeftPointFromCenter(tower.getPositionCenter().x, getWidth());
        float y = ActorUtil.calcBotLeftPointFromCenter(tower.getPositionCenter().y, getHeight());

        setPosition(x, y);

        addAction(
            Actions.parallel(
                Actions.moveTo(getX(), getY() + Y_END_OFFSET, DURATION),
                Actions.fadeOut(DURATION)));

        return this;
    }

    @Override
    public void act(float delta) {

        if (tower == null || tower.isDead() || !tower.isActive()) {
            free();
            return;
        }
        super.act(delta);
    }

    @Override
    public void reset() {

        super.reset();
        tower = null;
        getColor().a = 1;
    }
}
