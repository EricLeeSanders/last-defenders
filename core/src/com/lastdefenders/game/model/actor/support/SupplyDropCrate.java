package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupplyDropCratePool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

public class SupplyDropCrate extends GameActor implements Pool.Poolable {

    public static final int COST = 1000;
    private static final float SUPPLYDROP_DURATION = 1f;
    private static final float RANGE = 150f;
    private static final Dimension TEXTURE_SIZE = new Dimension(50, 50);

    private Circle rangeCircle = new Circle();
    private boolean active, showRange;
    private SupplyDropCratePool pool;
    private EffectFactory effectFactory;
    private Group towerGroup;
    private TextureRegion rangeTexture;

    public SupplyDropCrate(TextureRegion textureRegion, TextureRegion rangeTexture,
        SupplyDropCratePool pool, Group towerGroup, EffectFactory effectFactory) {

        super(TEXTURE_SIZE);
        this.pool = pool;
        this.towerGroup = towerGroup;
        this.rangeTexture = rangeTexture;
        this.effectFactory = effectFactory;
        setTextureRegion(textureRegion);

    }

    public SupplyDropCrate beginDrop(float dropDelay, Vector2 destination) {

        Logger.info("SupplyDropCrate: Beginning Crate drop");

        setVisible(false);
        setActive(true);
        setPositionCenter(destination);

        ScaleToAction scaleToAction = Actions
            .scaleTo(0.5f, 0.5f, SUPPLYDROP_DURATION, Interpolation.linear);
        DelayAction scaleDelayAction = Actions.delay(dropDelay, scaleToAction);
        addAction(scaleDelayAction);

        VisibleAction visibleAction = Actions.visible(true);
        DelayAction visibleDelayAction = Actions.delay(dropDelay, visibleAction);
        addAction(visibleDelayAction);

        return this;
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (isActive()) {
            if (this.getActions().size <= 0) {
                healActors();
                pool.free(this);
            }
        }
    }


    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);
        if (isShowRange()) {
            float width = RANGE * 2;
            float height = RANGE * 2;
            float x = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, width);
            float y = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, height);
            batch.draw(rangeTexture, x, y, getOriginX(), getOriginY(), width, height, 1, 1,
                getRotation());
        }
    }

    private void healActors() {

        Logger.info("Healing actors");
        for (Actor actor : towerGroup.getChildren()) {
            if (actor instanceof Tower) {
                Tower tower = (Tower) actor;
                if (CollisionDetection.shapesIntersect(tower.getBody(), getRangeShape())) {
                    tower.heal();
                    tower.resetArmor();
                    TowerHealEffect effect = effectFactory.loadLabelEffect(TowerHealEffect.class);
                    effect.initialize(tower);

                }
            }
        }
    }

    public void freeActor() {

        pool.free(this);
    }

    public boolean isShowRange() {

        return showRange;
    }

    public void setShowRange(boolean showRange) {

        this.showRange = showRange;
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }

    private Shape2D getRangeShape() {

        rangeCircle.set(getPositionCenter().x, getPositionCenter().y, RANGE);
        return rangeCircle;
    }

    @Override
    public void reset() {

        Logger.info("SupplyDropCrate: Resetting");
        setActive(false);
        setShowRange(false);
        this.setPosition(0, 0);
        this.setRotation(0);
        this.clear();
        this.remove();
        this.setScale(1);
    }
}