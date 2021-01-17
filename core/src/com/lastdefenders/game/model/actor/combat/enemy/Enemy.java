package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.combat.enemy.event.EnemyEventEnum;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.event.EventObserver;
import com.lastdefenders.game.model.actor.combat.event.EventObserverManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.service.factory.CombatActorFactory.EnemyPool;
import com.lastdefenders.util.action.LDSequenceAction;
import com.lastdefenders.util.action.WaypointAction;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class that represents an Enemy. Enemies are created from the
 * ActorFactory and are pooled. Enemies can attack towers. Enemies can have
 * multiple textures to show movement.
 *
 * @author Eric
 */
public abstract class Enemy extends CombatActor {

    public static final float MOVEMENT_DELAY = 1f; // The delay to wait after a target begins attacking
    private static final float FRAME_DURATION = 0.3f;
    private EventObserverManager<EventObserver<Enemy, EnemyEventEnum>, EnemyEventEnum, Enemy> enemyEventObserverManager = new EventObserverManager<>();
    private float speed;
    private int killReward;
    private float lengthToEnd;
    private boolean lengthToEndCalculated;
    private Animation<TextureRegion> movementAnimation;
    private TextureRegion stationaryTextureRegion;
    private float rotationBeforeAttacking;
    private EnemyPool<? extends Enemy> pool;
    private EnemyStateManager stateManager;
    private GenericGroup<Tower> targetGroup;

    public Enemy(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions,
        Dimension textureSize, EnemyPool<? extends Enemy> pool, GenericGroup<Tower> targetGroup, Vector2 gunPos,
        DeathEffectType deathEffectType, EnemyAttributes attributes) {

        super(stationaryTextureRegion, textureSize, gunPos, deathEffectType, attributes);
        movementAnimation = new Animation<>(FRAME_DURATION, animatedRegions);
        movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
        this.speed = attributes.getSpeed();
        this.stationaryTextureRegion = stationaryTextureRegion;
        this.killReward = attributes.getKillReward();
        this.pool = pool;
        this.targetGroup = targetGroup;
    }

    public void init() {
        super.init();
        stateManager.transition(EnemyStateEnum.SPAWNING);
        lengthToEndCalculated = false;
    }

    /**
     * Sets the path for the enemy. Starts off screen.
     */
    public void setPath(Array<LDVector2> path) {

        if (path == null || path.size <= 1) {
            return;
        }

        //Place the enemy at the start and off screen
        LDVector2 newWaypoint = path.get(0); // start
        setPositionCenter(newWaypoint);

        // face the next waypoint
        setRotation(Math.round(ActorUtil.calculateRotation(path.get(1), getPositionCenter())));

        // The enemy always faces its target (tower or way point) and the top/front of the enemy needs to be off screen.
        // That ensures that the entire body of the enemy is off the screen when spawning.
        // topCenterAfterRotation are the coords of the top/front of the enemy.
        Vector2 centerPos = getPositionCenter();
        LDVector2 topCenterAfterRotation = ActorUtil
            .calculateRotatedCoords(this.getX() + getWidth(), centerPos.y, centerPos.x, centerPos.y,
                Math.toRadians(getRotation()));

        // Reposition the enemy so that it is off the screen
        float newX = this.getPositionCenter().x + (this.getPositionCenter().x - topCenterAfterRotation.x);
        float newY = this.getPositionCenter().y + (this.getPositionCenter().y - topCenterAfterRotation.y);
        topCenterAfterRotation.free();

        this.setPositionCenter(newX, newY); // Start off screen

        //create actions
        LDSequenceAction sequenceAction = UtilPool.getSequenceAction();
        for (int i = 1; i < path.size; i++) {
            Vector2 prevWaypoint = newWaypoint;
            newWaypoint = path.get(i);
            float distance = newWaypoint.dst(prevWaypoint);
            float duration = (distance / speed);
            float rotation = ActorUtil
                .calculateRotation(newWaypoint.x, newWaypoint.y, prevWaypoint.x, prevWaypoint.y);
            WaypointAction waypointAction = createWaypointAction(newWaypoint.x, newWaypoint.y,
                duration, rotation);
            sequenceAction.addAction(waypointAction);
        }

        addAction(sequenceAction);
    }

    private WaypointAction createWaypointAction(float x, float y, float duration, float rotation) {

        return UtilPool.getWaypointAction(x, y, duration, rotation, Interpolation.linear);
    }

    /**
     * Pauses enemy when attacking. Creates new MoveTo Actions to set the next
     * way point for the Enemy. Calls to change textures. Calls to find target
     * when delay expires.
     */
    @Override
    public void act(float delta) {

        lengthToEndCalculated = false;

        if (!isAttacking()) {
            super.act(delta); // Pause to create a delay if attacking
        }

        stateManager.update(delta);
    }

    public void animationStep(float stateTime) {

        setTextureRegion(movementAnimation.getKeyFrame(stateTime, true));
    }


    public void reachedEnd() {

        Logger.info("Enemy " + ID  + ": "  + this.getClass().getSimpleName() + " reached end");
        freeActor();
    }

    public void attack(Targetable target) {

        this.setRotation(
            ActorUtil.calculateRotation(target.getPositionCenter(), getPositionCenter()));
        this.attackTarget(target);
        setTextureRegion(stationaryTextureRegion);
    }

    // TODO move this to state
    public void preAttack() {

        rotationBeforeAttacking = getRotation();
    }

    public void postAttack() {

        setRotation(rotationBeforeAttacking);
    }

    public void deadState() {
        stateManager.transition(EnemyStateEnum.DEAD);
    }


    /**
     * Resets the enemy for pooling
     */
    @Override
    public void reset() {

        super.reset();
        Logger.info("Enemy: " + this.getClass().getSimpleName() + " Resetting");
        this.setRotation(0);
        lengthToEnd = 0;
        rotationBeforeAttacking = 0;
    }

    /**
     * Determines the length till the end of the entire path.
     */
    private void calcLengthToEnd() {
        // The enemy should only have 1 action and it should
        // be a LDSequenceAction;
        if (getActions().size != 1) {
            throw new IllegalStateException("Enemy: " + ID + " has no action");
        }

        LDSequenceAction sequenceAction = (LDSequenceAction) getActions().first();
        Array<Action> waypointActions = sequenceAction.getActions();
        int currentIndex = sequenceAction.getIndex();
        WaypointAction currentWaypoint = (WaypointAction) sequenceAction.getCurrentAction();
        lengthToEndCalculated = true;

        float totalDistance = Vector2
            .dst(this.getPositionCenter().x, this.getPositionCenter().y, currentWaypoint.getX(), currentWaypoint.getY());
        for (int i = currentIndex; i < waypointActions.size - 1; i++) {
            WaypointAction waypoint = (WaypointAction) waypointActions.get(i);
            WaypointAction nextWaypoint = (WaypointAction) waypointActions.get(i + 1);
            totalDistance += Vector2.dst(waypoint.getX(), waypoint.getY()
                , nextWaypoint.getX(), nextWaypoint.getY());
        }

        lengthToEnd = totalDistance;
    }

    boolean isAttacking() {

        return stateManager.getCurrentStateName().equals(EnemyStateEnum.ATTACKING);
    }

    public float getLengthToEnd() {

        if (!lengthToEndCalculated) {
            calcLengthToEnd();
        }

        return lengthToEnd;
    }

    public int getKillReward() {

        return killReward;
    }

    public EnemyStateEnum getState() {

        return stateManager.getCurrentStateName();
    }

    public float getSpeed(){

        return speed;
    }

    public GenericGroup<Tower> getEnemyGroup(){
        return targetGroup;
    }

    @Override
    public void freeActor() {
        pool.free(this);
    }

    public EventObserverManager<EventObserver<Enemy, EnemyEventEnum>, EnemyEventEnum, Enemy>  getEnemyEventObserverManager() {

        return enemyEventObserverManager;
    }

    public void setStateManager(EnemyStateManager stateManager) {

        this.stateManager = stateManager;
    }

    public EnemyStateManager getStateManager(){
        return this.stateManager;
    }

}
