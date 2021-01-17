package com.lastdefenders.game.model.actor.support.supplydrop;

import static com.lastdefenders.game.model.actor.support.supplydrop.SupplyDropPlane.SUPPLYDROP_PLANE_DURATION;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.model.actor.groups.TowerGroup;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class SupplyDrop extends SupportActor implements Pool.Poolable {
    public static final int COST = 500;
    private static final float SUPPLYDROP_CRATE_DURATION = 1f;
    private static final float RANGE = 150f;
    private static final Dimension TEXTURE_SIZE = new Dimension(50, 50);
    private SupplyDropPlane plane;
    private Circle rangeCircle = new Circle();
    private SupportActorPool<SupplyDrop> pool;
    private EffectFactory effectFactory;
    private TowerGroup towerGroup;

    public SupplyDrop(TextureRegion crateTextureRegion, TextureRegion rangeTexture,
        SupportActorPool<SupplyDrop> pool, TowerGroup towerGroup, EffectFactory effectFactory, SupplyDropPlane plane) {

        super(rangeTexture, crateTextureRegion, TEXTURE_SIZE, pool, RANGE);
        this.pool = pool;
        this.towerGroup = towerGroup;
        this.effectFactory = effectFactory;
        this.plane = plane;

    }

    public void beginSupplyDrop(Vector2 destination) {

        Logger.info("SupplyDrop: Beginning Supply drop");
        setActive(true);
        setVisible(false);
        setPositionCenter(destination);
        this.getParent().addActor(plane);
        plane.beginSupplyDrop(destination);

        float dropDelay = calculateDropDelay(destination);

        addAction(
            Actions.sequence(
                Actions.delay(dropDelay),
                Actions.visible(true),
                Actions.scaleTo(0.5f, 0.5f, SUPPLYDROP_CRATE_DURATION, Interpolation.linear),
                new LDOneTimeAction() {
                    @Override
                    public void action() {
                        healActors();
                    }
                },
                new LDOneTimeAction() {
                    @Override
                    public void action() {
                        freeActor();
                    }
                }
            )
        );
    }

    private void healActors() {

        Logger.info("SupplyDrop: Healing actors");
        for (Tower tower : towerGroup.getCastedChildren()) {
            if (CollisionDetection.shapesIntersect(tower.getBody(), getRangeShape())) {
                tower.heal();
                tower.resetArmor();
                TowerHealEffect effect = effectFactory.loadEffect(TowerHealEffect.class, true);
                effect.initialize(tower);

            }
        }
    }

    float calculateDropDelay(Vector2 destination) {

        LDVector2 planeEndPos = plane.getEndPositionByDestination(destination);
        float dropDelay = SUPPLYDROP_PLANE_DURATION * ((destination.x + getWidth()) / (planeEndPos.x
            + getWidth()));
        planeEndPos.free();

        return dropDelay;
    }

    @Override
    public void reset() {
        Logger.info("SupplyDrop: Resetting");
        this.setScale(1);
        super.reset();
    }
}
