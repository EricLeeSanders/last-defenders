package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class AirStrike extends CombatSupportActor implements IRocket {

    public static final float AIRSTRIKE_RADIUS = 60;
    public static final float AIRSTRIKE_DURATION = 2.5f;
    public static final int COST = 1000;
    public static final float COOLDOWN_TIME = 30;
    private static final float ATTACK = 10f;
    public static final int MAX_AIRSTRIKES = 3;

    private static final Vector2 GUN_POS = UtilPool.getVector2();
    private static final Dimension ROCKET_SIZE = new Dimension(46, 13);
    private static final Dimension TEXTURE_SIZE = new Dimension(206, 131);

    private Array<AirStrikeLocation> airStrikeLocations = new Array<>();
    private ProjectileFactory projectileFactory;
    private SoundPlayer soundPlayer;
    private int airStrikeLocationCounter = 0;


    public AirStrike(SupportActorPool<AirStrike> pool, EnemyGroup enemyGroup,
        ProjectileFactory projectileFactory, TextureRegion textureRegion,
        TextureRegion rangeTexture, Array<AirStrikeLocation> airStrikeLocations, SoundPlayer soundPlayer) {

        super(pool, enemyGroup, textureRegion, TEXTURE_SIZE, rangeTexture, AIRSTRIKE_RADIUS,
            ATTACK, GUN_POS);
        this.soundPlayer = soundPlayer;
        this.projectileFactory = projectileFactory;
        this.airStrikeLocations = airStrikeLocations;
    }

    @Override
    public boolean setPlacement(LDVector2 coords){
        if(isReadyToBegin()){
            return true;
        }
        AirStrikeLocation location = airStrikeLocations.get(airStrikeLocationCounter);
        getParent().addActor(location);
        location.initialize(coords);
        airStrikeLocationCounter++;
        return isReadyToBegin();
    }

    @Override
    public void ready() {

        Logger.info("AirStrike: Beginning Air Strike Run");

        setActive(true);
        LDVector2 centerPos = UtilPool.getVector2(-getWidth() / 2, Resources.VIRTUAL_HEIGHT / 2);
        setPositionCenter(centerPos);
        centerPos.free();

        MoveToAction moveAction = Actions
            .moveTo(Resources.VIRTUAL_WIDTH + getWidth(), (Resources.VIRTUAL_HEIGHT / 2),
                AIRSTRIKE_DURATION, Interpolation.linear);
        moveAction.setAlignment(Align.center);

        setVisible(true);

        addAction(
            Actions.sequence(
                moveAction,
                new LDOneTimeAction() {
                    @Override
                    public void action() {
                        freeActor();
                    }
                }
            )
        );

        soundPlayer.play(LDSound.Type.AIRCRAFT_FLYOVER);
        soundPlayer.play(LDSound.Type.ROCKET_LAUNCH);
        for (AirStrikeLocation location : airStrikeLocations) {
            dropBomb(location);
        }


    }

    private void dropBomb(AirStrikeLocation location) {
        location.setShowRange(false);
        projectileFactory.loadProjectile(Rocket.class)
            .initialize(this, location.getPositionCenter(), ROCKET_SIZE, AIRSTRIKE_RADIUS);


    }

    public boolean isReadyToBegin() {

        return (airStrikeLocationCounter == MAX_AIRSTRIKES);
    }

    @Override
    public void reset() {

        Logger.info("AirStrike: Resetting");
        airStrikeLocationCounter = 0;
        for (AirStrikeLocation location : airStrikeLocations) {
            location.remove();
            location.clear();
        }
        super.reset();
    }

    @Override
    public int getCost() {

        return COST;
    }


    public static class AirStrikeLocation extends GameActor {

        private boolean showRange = false;
        private TextureRegion rangeTexture;

        public AirStrikeLocation(TextureRegion rangeTexture) {
            this.rangeTexture = rangeTexture;
        }

        public void initialize(LDVector2 location) {

            this.setPositionCenter(location);
            showRange = true;
        }

        @Override
        public void draw(Batch batch, float alpha) {

            if (isShowRange()) {
                drawRange(batch);
            }
            super.draw(batch, alpha);
        }

        private void drawRange(Batch batch) {

            float width = AIRSTRIKE_RADIUS * 2;
            float height = AIRSTRIKE_RADIUS * 2;
            float x = ActorUtil.calcBotLeftPointFromCenter(getX(), width);
            float y = ActorUtil.calcBotLeftPointFromCenter(getY(), height);
            batch.draw(rangeTexture, x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
        }

        private boolean isShowRange() {

            return showRange;
        }

        public void setShowRange(boolean showRange) {

            this.showRange = showRange;
        }

        @Override
        public void clear() {
            showRange = false;
        }
    }
}
