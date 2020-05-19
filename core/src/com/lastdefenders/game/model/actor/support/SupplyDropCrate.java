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
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UtilPool;
import com.lastdefenders.util.action.FreeActorAction;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;

public class SupplyDropCrate extends GameActor implements Pool.Poolable {

    public static final int COST = 500;
    private static final float SUPPLYDROP_DURATION = 1f;
    private static final float RANGE = 150f;
    private static final Dimension TEXTURE_SIZE = new Dimension(50, 50);

    private Circle rangeCircle = new Circle();
    private boolean active, showRange;
    private SupportActorPool<SupplyDropCrate> pool;
    private EffectFactory effectFactory;
    private Group towerGroup;
    private TextureRegion rangeTexture;

    public SupplyDropCrate(TextureRegion textureRegion, TextureRegion rangeTexture,
        SupportActorPool<SupplyDropCrate> pool, Group towerGroup, EffectFactory effectFactory) {

        super(TEXTURE_SIZE);
        this.pool = pool;
        this.towerGroup = towerGroup;
        this.rangeTexture = rangeTexture;
        this.effectFactory = effectFactory;
        setTextureRegion(textureRegion);

    }

    public SupplyDropCrate beginDrop(float dropDelay, Vector2 destination) {

        Logger.info("SupplyDropCrate: Beginning Crate drop");
        createInitialActions(dropDelay);
        setVisible(false);
        setActive(true);
        setPositionCenter(destination);

        return this;
    }


    /**
     * Creates the initial actions.
     * <br>
     * The initial actions are in the following {@link SequenceAction}:
     * <ol>
     *  <li>{@link DelayAction}</li>
     *  <li>{@link ScaleToAction}</li>
     *  <li>{@link VisibleAction}</li>
     *  <li>{@link LDOneTimeAction} - heal actors</li>
     *  <li>{@link FreeActorAction}</li>
     * </ol>
     *
     * @param dropDelay
     */
    private void createInitialActions(float dropDelay){

        addAction(
            Actions.sequence(
                Actions.delay(dropDelay),
                Actions.visible(true),
                Actions.scaleTo(0.5f, 0.5f, SUPPLYDROP_DURATION, Interpolation.linear),
                new LDOneTimeAction() {
                    @Override
                    public void action() {
                        healActors();
                    }
                },
                UtilPool.getFreeActorAction(pool)
            )
        );

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
                    TowerHealEffect effect = effectFactory.loadEffect(TowerHealEffect.class, true);
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

    public Shape2D getRangeShape() {

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
