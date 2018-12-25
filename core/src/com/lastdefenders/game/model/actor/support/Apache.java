package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.ai.TowerAI;
import com.lastdefenders.game.model.actor.ai.towerai.ClosestEnemyAI;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.action.LDOneTimeAction;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

public class Apache extends CombatSupportActor {

    public static final int COST = 2000;
    public static final float TIME_ACTIVE_LIMIT = 10f;
    private static final float FRAME_DURATION = 0.05f;
    private static final float ATTACK_SPEED = 0.2f;
    private static final float RANGE = 75f;
    private static final float ATTACK = 5f;
    private static final float MOVE_SPEED = 200f;
    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(0, 0);
    private static final Dimension TEXTURE_SIZE = new Dimension(150, 116);

    private boolean readyToAttack, exitingStage;
    private float attackCounter, timeActive;
    private ProjectileFactory projectileFactory;
    private LDAudio audio;
    private TowerAI ai = new ClosestEnemyAI();
    private Animation<TextureRegion> movementAnimation;
    private float movementAnimationStateTime;

    public Apache(SupportActorPool<Apache> pool, Group targetGroup,
        ProjectileFactory projectileFactory, TextureRegion stationaryTextureRegion,
        TextureRegion[] textureRegions, TextureRegion rangeTexture, LDAudio audio) {

        super(pool, targetGroup, stationaryTextureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK,
            GUN_POS, COST);
        this.audio = audio;
        this.projectileFactory = projectileFactory;

        movementAnimation = new Animation<>(FRAME_DURATION, textureRegions);
        movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
        attackCounter = ATTACK_SPEED;

    }

    public void initialize(Vector2 placePosition) {

        Logger.info("Apache: initializing");
        LDVector2 centerPos = UtilPool.getVector2(-this.getWidth(), Resources.VIRTUAL_HEIGHT / 2);
        setPositionCenter(centerPos);

        LDVector2 destination = UtilPool.getVector2(placePosition);
        setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));

        createInitialActions(destination);

        UtilPool.freeObjects(centerPos, destination);

        audio.playSound(LDSound.HELICOPTER_HOVER);
    }

    /**
     * Creates the initial actions.
     * <br>
     * The initial actions are in the following {@link SequenceAction}:
     * <ol>
     *  <li>{@link MoveToAction} - Move to the destination</li>
     *  <li>{@link LDOneTimeAction} - set ready to attack</li>
     *  <li>{@link DelayAction} - Delay for {@value TIME_ACTIVE_LIMIT} seconds</li>
     *  <li>{@link LDOneTimeAction} - exit stage</li>
     * </ol>
     *
     * @param destination - the destination to move to
     */
    private void createInitialActions(LDVector2 destination){

        SequenceAction sequenceAction = Actions.sequence();

        float duration = destination.dst(getPositionCenter()) / MOVE_SPEED;
        MoveToAction moveToAction = Actions
            .moveTo(destination.x, destination.y, duration, Interpolation.linear);
        moveToAction.setAlignment(Align.center);

        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(new LDOneTimeAction(){
            @Override
            public void action() {
                setReadyToAttack(true);
            }
        });
        sequenceAction.addAction(Actions.delay(TIME_ACTIVE_LIMIT));
        sequenceAction.addAction(new LDOneTimeAction() {
            @Override
            public void action() {
                setReadyToAttack(false);
                exitStage();
            }
        });

        addAction(sequenceAction);
    }

    @Override
    public void reset() {

        Logger.info("Apache: resetting");
        setReadyToAttack(false);
        timeActive = 0;
        attackCounter = ATTACK_SPEED;
        exitingStage = false;
        super.reset();
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        movementAnimationStateTime += delta;
        setTextureRegion(movementAnimation.getKeyFrame(movementAnimationStateTime, true));

        if (isReadyToAttack()) {
            attackHandler(delta);
        }
    }

    private void attackHandler(float delta) {

        Targetable target = findTarget();
        attackCounter += delta;
        if (target != null && !target.isDead()) {
            setRotation(
                ActorUtil.calculateRotation(target.getPositionCenter(), getPositionCenter()));
            if (attackCounter >= ATTACK_SPEED) {
                attackCounter = 0;
                attackTarget(target);
            }
        }
    }

    private void exitStage() {

        Logger.info("Apache: exiting stage");
        exitingStage = true;
        LDVector2 destination = UtilPool.getVector2(-getWidth(), Resources.VIRTUAL_HEIGHT / 2);

        float duration = destination.dst(getPositionCenter()) / MOVE_SPEED;
        MoveToAction moveToAction = Actions
            .moveTo(destination.x, destination.y, duration, Interpolation.linear);
        moveToAction.setAlignment(Align.center);

        addAction(
            Actions.sequence(
                moveToAction,
                UtilPool.getFreeActorAction(getPool())
            ));

        setRotation(ActorUtil.calculateRotation(destination, getPositionCenter()));
        destination.free();
        setActive(false);

        audio.playSound(LDSound.HELICOPTER_HOVER);
    }

    /**
     * Find a target using TowerAIType First Enemy
     */
    private Targetable findTarget() {

        return ai.findTarget(this, getTargetGroup().getChildren());
    }

    private void attackTarget(Targetable target) {

        if (target != null && !target.isDead()) {
            audio.playSound(LDSound.MACHINE_GUN);
            projectileFactory.loadProjectile(Bullet.class).initialize(this, target, BULLET_SIZE);
        }

    }

    public boolean isReadyToAttack() {

        return readyToAttack;
    }

    private void setReadyToAttack(boolean readyToAttack) {

        this.readyToAttack = readyToAttack;
    }
}
