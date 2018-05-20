package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class AirStrike extends SupportActor implements IRocket {

    public static final float AIRSTRIKE_DURATION = 2.5f;
    public static final int COST = 1000;
    public static final float AIRSTRIKE_RADIUS = 60;
    private static final float ATTACK = 10f;
    private static final int MAX_AIRSTRIKES = 3;

    private static final Vector2 GUN_POS = UtilPool.getVector2();
    private static final Dimension ROCKET_SIZE = new Dimension(46, 13);
    private static final Dimension TEXTURE_SIZE = new Dimension(206, 125);

    private Array<AirStrikeLocation> airStrikeLocations = new Array<>();
    private ProjectileFactory projectileFactory;
    private LDAudio audio;

    public AirStrike(SupportActorPool<AirStrike> pool, Group targetGroup,
        ProjectileFactory projectileFactory, TextureRegion textureRegion,
        TextureRegion rangeTexture, LDAudio audio) {

        super(pool, targetGroup, textureRegion, TEXTURE_SIZE, rangeTexture, AIRSTRIKE_RADIUS,
            ATTACK, GUN_POS, COST);
        this.audio = audio;
        this.projectileFactory = projectileFactory;

    }

    public void addLocation(AirStrikeLocation location) {

        airStrikeLocations.add(location);
    }

    public void beginAirStrike() {

        Logger.info("AirStrike: Beginning Air Strike Run");

        setActive(true);
        LDVector2 centerPos = UtilPool.getVector2(-getWidth() / 2, Resources.VIRTUAL_HEIGHT / 2);
        setPositionCenter(centerPos);
        centerPos.free();

        MoveToAction moveAction = Actions
            .moveTo(Resources.VIRTUAL_WIDTH + getWidth(), (Resources.VIRTUAL_HEIGHT / 2),
                AIRSTRIKE_DURATION, Interpolation.linear);
        moveAction.setAlignment(Align.center);
        addAction(moveAction);

        audio.playSound(LDSound.AIRCRAFT_FLYOVER);
        audio.playSound(LDSound.ROCKET_LAUNCH);
        for (AirStrikeLocation location : airStrikeLocations) {
            dropBomb(location);
            location.setShowRange(false);
        }


    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (isActive()) {
            if (getActions().size <= 0) {
                freeActor();
            }
        }
    }

    private void dropBomb(AirStrikeLocation location) {

        projectileFactory.loadRocket()
            .initialize(this, location.getLocation(), ROCKET_SIZE, AIRSTRIKE_RADIUS);


    }

    public boolean isReadyToBegin() {

        return (airStrikeLocations.size >= MAX_AIRSTRIKES);
    }

    @Override
    public void reset() {

        Logger.info("AirStrike: Resetting");
        for (AirStrikeLocation location : airStrikeLocations) {
            location.remove();
            location.clear();
        }
        airStrikeLocations.clear();
        super.reset();
    }
}
