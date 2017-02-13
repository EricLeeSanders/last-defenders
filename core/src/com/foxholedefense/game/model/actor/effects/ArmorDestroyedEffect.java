package com.foxholedefense.game.model.actor.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorDestroyedEffect extends Actor implements Pool.Poolable {

    private static final float DURATION = 2;

    private CombatActor actor = null;
    private Pool<ArmorDestroyedEffect> pool;
    private Animation animation;
    private Label label;
    private float stateTime;

    public ArmorDestroyedEffect(Array<AtlasRegion> regions, Pool<ArmorDestroyedEffect> pool, Label label){
        animation = new Animation(DURATION, regions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        this.pool = pool;
        this.label = label;

        label.setText("ARMOR DESTROYED");
        label.setAlignment(Align.center);
        label.setFontScale(0.3f);
    }

    public Actor initialize(CombatActor actor){
        this.actor = actor;
        System.out.println("Destroy INIT");
        label.setX(ActorUtil.calcXBotLeftFromCenter(actor.getPositionCenter().x, label.getWidth()));
        label.setY(ActorUtil.calcYBotLeftFromCenter(actor.getPositionCenter().y, label.getHeight()));

        this.getParent().addActor(label);

        label.addAction(
                Actions.parallel(
                        Actions.moveTo(label.getX(), label.getY() + 50, DURATION),
                        Actions.fadeOut(DURATION)));

        return this;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TextureRegion region = animation.getKeyFrame(stateTime, false);

        if (actor != null) {
            setPosition(actor.getPositionCenter().x - 22, actor.getPositionCenter().y + 16);
            batch.draw(region, getX(), getY(), 12, 13);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if (actor == null || actor.isDead() || !actor.isActive()){
            pool.free(this);
            return;
        }

        stateTime += delta;
        if (animation.isAnimationFinished(stateTime)) {
            pool.free(this);
        }
    }

    @Override
    public void reset() {
        System.out.println("Reset DESTROY EFECT");
        this.actor = null;
        this.remove();
        stateTime = 0;
        this.clear();
        label.remove();
        label.clear();
        label.getColor().a = 1;
    }

}
