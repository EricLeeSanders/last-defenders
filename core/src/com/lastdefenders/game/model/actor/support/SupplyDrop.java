package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class SupplyDrop extends GameActor implements Pool.Poolable {

    private static final float SUPPLYDROP_DURATION = 2f;
    private static final Dimension TEXTURE_SIZE = new Dimension(178, 120);

    private boolean active;
    private SupportActorPool<SupplyDrop> pool;
    private SupportActorFactory supportActorFactory;
    private LDAudio audio;

    public SupplyDrop(TextureRegion textureRegion, SupportActorPool<SupplyDrop> pool,
        SupportActorFactory supportActorFactory, LDAudio audio) {

        super(TEXTURE_SIZE);
        this.pool = pool;
        this.supportActorFactory = supportActorFactory;
        this.audio = audio;
        setTextureRegion(textureRegion);
    }

    public void beginSupplyDrop(Vector2 destination) {

        Logger.info("SupplyDrop: Beginning Supply drop");

        setActive(true);

        LDVector2 centerPos = UtilPool.getVector2(-getWidth(), destination.y);
        setPositionCenter(centerPos);
        centerPos.free();

        float moveToX = Resources.VIRTUAL_WIDTH + getWidth();
        float moveToY = destination.y;
        MoveToAction moveToAction = Actions
            .moveTo(moveToX, moveToY, SUPPLYDROP_DURATION, Interpolation.linear);
        moveToAction.setAlignment(Align.center);

        addAction(
            Actions.sequence(
                moveToAction,
                UtilPool.getFreeActorAction(pool)
            )
        );

        float dropDelay =
            SUPPLYDROP_DURATION * ((destination.x - (getWidth() / 4)) / Resources.VIRTUAL_WIDTH);

        Logger.info("DropDelay: " + dropDelay);

        supportActorFactory.loadSupportActor(SupplyDropCrate.class, true).beginDrop(dropDelay, destination).toBack();

        audio.playSound(LDSound.AIRCRAFT_FLYOVER);
    }

    public boolean isActive() {

        return active;
    }

    private void setActive(boolean active) {

        this.active = active;
    }

    @Override
    public void reset() {

        Logger.info("SupplyDrop: Resetting");
        setActive(false);
        this.setPosition(0, 0);
        this.setRotation(0);
        this.clear();
        this.remove();
    }
}
