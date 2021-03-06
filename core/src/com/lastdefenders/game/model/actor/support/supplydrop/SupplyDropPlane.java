package com.lastdefenders.game.model.actor.support.supplydrop;

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
import com.lastdefenders.util.UtilPool;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;

public class SupplyDropPlane extends GameActor {
    static final float SUPPLYDROP_PLANE_DURATION = 2f;
    private static final Dimension TEXTURE_SIZE = new Dimension(206, 131);

    private boolean active;
    private LDAudio audio;

    public SupplyDropPlane(TextureRegion textureRegion, LDAudio audio) {
        super(TEXTURE_SIZE);

        setTextureRegion(textureRegion);
        this.audio = audio;

    }

    public void beginSupplyDrop(Vector2 destination) {

        Logger.info("SupplyDropPlane: Beginning Supply drop");

        setActive(true);
        setVisible(true);

        setPositionCenter(-getWidth(), destination.y);

        LDVector2 endPosition = getEndPositionByDestination(destination);
        MoveToAction moveToAction = Actions
            .moveTo(endPosition.x, endPosition.y, SUPPLYDROP_PLANE_DURATION, Interpolation.linear);
        moveToAction.setAlignment(Align.center);

        addAction(
            Actions.sequence(
                moveToAction,
                new LDOneTimeAction() {
                    @Override
                    public void action() {
                        reset();
                    }
                }
            )
        );


        audio.playSound(LDSound.AIRCRAFT_FLYOVER);
    }

    LDVector2 getEndPositionByDestination(Vector2 destination){
        return UtilPool.getVector2( Resources.VIRTUAL_WIDTH + (getWidth() / 2), destination.y);
    }


    public boolean isActive() {

        return active;
    }

    private void setActive(boolean active) {

        this.active = active;
    }

    public void reset() {

        Logger.info("SupplyDropPlane: Resetting");
        setActive(false);
        this.setPosition(0, 0);
        this.setRotation(0);
        this.clear();
        this.remove();
    }
}
