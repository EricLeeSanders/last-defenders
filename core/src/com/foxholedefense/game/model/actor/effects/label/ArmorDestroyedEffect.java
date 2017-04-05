package com.foxholedefense.game.model.actor.effects.label;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.service.factory.EffectFactory.LabelEffectPool;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorDestroyedEffect extends LabelEffect {

    private static final float DURATION = 2;
    private static final float SCALE = 0.35f;
    private static final String MESSAGE = "ARMOR DESTROYED";

    private CombatActor actor = null;
    private Animation animation;

    public ArmorDestroyedEffect(Array<AtlasRegion> regions, LabelEffectPool<ArmorDestroyedEffect> pool, Skin skin){
        super(pool, DURATION, skin);
        animation = new Animation(DURATION, regions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setText(MESSAGE);
        setAlignment(Align.center);
        setFontScale(SCALE);

    }

    public Actor initialize(CombatActor actor){
        this.actor = actor;
        setX(ActorUtil.calcXBotLeftFromCenter(actor.getPositionCenter().x, getWidth()));
        setY(ActorUtil.calcYBotLeftFromCenter(actor.getPositionCenter().y, getHeight()));

        addAction(
                Actions.parallel(
                        Actions.moveTo(getX(), getY() + 50, DURATION),
                        Actions.fadeOut(DURATION)));

        return this;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
        TextureRegion region = animation.getKeyFrame(stateTime, false);

        if (actor != null) {
            setPosition(actor.getPositionCenter().x - 22, actor.getPositionCenter().y + 16);
            batch.draw(region, getX(), getY(), 12, 13);
        }
    }

    @Override
    public void act(float delta){
        if (actor == null || actor.isDead() || !actor.isActive()){
            free();
            return;
        }
        super.act(delta);
    }

    @Override
    public void reset() {
        super.reset();
        this.actor = null;
        getColor().a = 1;
    }

}
