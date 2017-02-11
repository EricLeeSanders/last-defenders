package com.foxholedefense.game.model.actor.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.ActorUtil;

/**
 * Created by Eric on 1/6/2017.
 */

public class TowerHealEffect extends Label implements Pool.Poolable {

    private static final float DURATION = 2;

    private Tower tower = null;
    private Pool<TowerHealEffect> pool;
    private float stateTime;

    public TowerHealEffect(Pool<TowerHealEffect> pool, Skin skin){

        super("+ HEALTH", skin);
        this.pool = pool;
        this.setSize(24,24);


        this.setAlignment(Align.center);
        Label.LabelStyle style = new Label.LabelStyle(getStyle());
        style.fontColor = Color.GREEN;
        setStyle(style);
        setFontScale(0.3f);
    }


    public Actor initialize(Tower tower){
        this.tower = tower;

        float x = ActorUtil.calcXBotLeftFromCenter(tower.getPositionCenter().x, getWidth());
        float y = ActorUtil.calcYBotLeftFromCenter(tower.getPositionCenter().y, getHeight());

        setPosition(x, y );

        this.addAction(
                Actions.parallel(
                        Actions.moveTo(getX(), getY() + 50, DURATION),
                        Actions.fadeOut(DURATION)));

        return this;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if (stateTime >= DURATION || tower == null || tower.isDead()) {
            pool.free(this);
            return;
        }

        stateTime += delta;
    }

    @Override
    public void reset() {
        this.remove();
        this.tower = null;
        stateTime = 0;
        this.clear();
        getColor().a = 1;
    }

}
