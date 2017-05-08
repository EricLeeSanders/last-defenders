package com.foxholedefense.game.model.actor.combat.enemy;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.action.FHDSequenceAction;
import com.foxholedefense.action.WaypointAction;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * An abstract class that represents an Enemy. Enemies are created from the
 * ActorFactory and are pooled. Enemies can attack towers. Enemies can have
 * multiple textures to show movement.
 *
 * @author Eric
 *
 */
public abstract class Enemy extends CombatActor {
	public static final float MOVEMENT_DELAY = 1f; // The delay to wait after a target begins attacking
	public static final float FIND_TARGET_DELAY = 2f;
	private static final float FRAME_DURATION = 0.3f;
	private Random random = new Random();
	private Pool<CombatActor> pool;
	private float speed;
	private int killReward;
	private float lengthToEnd;
	private boolean lengthToEndCalculated;
	private Animation movementAnimation;
	private TextureRegion stationaryTextureRegion;
	private float rotationBeforeAttacking;
	private SnapshotArray<IEnemyObserver> observers = new SnapshotArray<IEnemyObserver>();

	private final EnemyStateManager stateManager = new EnemyStateManager(this);

	public Enemy(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, Dimension textureSize, Pool<CombatActor> pool, Group targetGroup, Vector2 gunPos,
				 float speed, float health, float armor, float attack, float attackSpeed, float range, int killReward, DeathEffectType deathEffectType) {
		super(stationaryTextureRegion, textureSize, pool, targetGroup, gunPos, health, armor, attack, attackSpeed, range, deathEffectType);
		movementAnimation = new Animation(FRAME_DURATION, animatedRegions);
		movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
		this.speed = speed;
		this.pool = pool;
		this.stationaryTextureRegion = stationaryTextureRegion;
		this.killReward = killReward;
	}

	public void detachEnemy(IEnemyObserver observer){
		Logger.info("Enemy Detach: " + observer.getClass().getName());
		observers.removeValue(observer, false);
	}

	public void attachAllEnemy(Array<IEnemyObserver> observers){
		this.observers.addAll(observers);
	}

	public void attachEnemy(IEnemyObserver observer){
		Logger.info("Enemy Actor Attach: " + observer.getClass().getName());
		observers.add(observer);
	}

	protected void notifyObserversEnemy(IEnemyObserver.EnemyEvent event){
		Logger.info("Enemy Actor: Notify Observers");
		Object[] objects = observers.begin();
		for(int i = observers.size - 1; i >= 0; i--){
			IEnemyObserver observer = (IEnemyObserver) objects[i];
			Logger.info("Enemy Actor Notifying: " + observer.getClass().getName());
			observer.notifyEnemy(this, event);
		}
		observers.end();
	}

	public void init(){
		stateManager.setCurrentState(EnemyState.RUNNING);
	}
	/**
	 * Sets the path for the enemy. Starts off screen.
	 *
	 * @param path
	 */
	public void setPath(Array<FHDVector2> path) {
		if(path == null | path.size <= 1){
			return;
		}

		//Place the enemy at the start and off screen
		FHDVector2 newWaypoint = path.get(0); // start
		setPositionCenter(newWaypoint);

		// face the next waypoint
		setRotation(Math.round(ActorUtil.calculateRotation(path.get(1), getPositionCenter())));

		// The enemy always faces its target (tower or way point) and the top/front of the enemy needs to be off screen.
		// That ensures that the entire body of the enemy is off the screen when spawning.
		// rotatedCoords are the coords of the top/front of the enemy.
		Vector2 centerPos = getPositionCenter();
		FHDVector2 rotatedCoords = ActorUtil.getRotatedCoords(this.getX() + getWidth(), centerPos.y, centerPos.x,  centerPos.y, Math.toRadians(getRotation()));

		// Reposition the enemy so that it is off the screen
		float newX = this.getPositionCenter().x + (this.getPositionCenter().x - rotatedCoords.x);
		float newY = this.getPositionCenter().y + (this.getPositionCenter().y - rotatedCoords.y);

		rotatedCoords.free();

		this.setPositionCenter(newX, newY); // Start off screen

		//create actions
		FHDSequenceAction sequenceAction = UtilPool.getSequenceAction();
		FHDVector2 moveVector = UtilPool.getVector2();
		for (int i = 1; i < path.size; i++) {
			Vector2 prevWaypoint = newWaypoint;
			newWaypoint = path.get(i);
			moveVector.set((newWaypoint.x - (this.getOriginX())), (newWaypoint.y - (this.getOriginY())));
			float distance = newWaypoint.dst(prevWaypoint);
			float duration = (distance / speed);
			float rotation = ActorUtil.calculateRotation(newWaypoint.x, newWaypoint.y, prevWaypoint.x, prevWaypoint.y);
			WaypointAction waypointAction = createWaypointAction(moveVector.x, moveVector.y, duration, rotation);
			sequenceAction.addAction(waypointAction);
		}

		addAction(sequenceAction);

		moveVector.free();
	}

	private WaypointAction createWaypointAction(float x, float y, float duration, float rotation){
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

	public void animationStep(float stateTime){
		setTextureRegion(movementAnimation.getKeyFrame(stateTime, true));
	}


	public void reachedEnd(){
		Logger.info("Enemy: " + this.getClass().getSimpleName() + " reached end");
		notifyObserversEnemy(IEnemyObserver.EnemyEvent.REACHED_END);
		pool.free(this);
	}

	public void attack(ITargetable target){
		this.setRotation(ActorUtil.calculateRotation(target.getPositionCenter(), getPositionCenter()));
		this.attackTarget(target);
		setTextureRegion(stationaryTextureRegion);
	}

	public void preAttack(){
		rotationBeforeAttacking = getRotation();
	}

	public void postAttack(){
		setRotation(rotationBeforeAttacking);
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
	 *
	 * @return boolean - Total Distance till the end
	 */
	private void calcLengthToEnd() {
		// The enemy should only have 1 action and it should
		// be a FHDSequenceAction;
		if(getActions().size <= 0){
			return;
		}

		FHDSequenceAction sequenceAction = (FHDSequenceAction) getActions().first();
		Array<Action> waypointActions = sequenceAction.getActions();
		int currentIndex = sequenceAction.getIndex();
		WaypointAction currentWaypoint = (WaypointAction) sequenceAction.getCurrentAction();
		lengthToEndCalculated = true;
		float totalDistance = 0;

		totalDistance = Vector2.dst(this.getX(), this.getY(), currentWaypoint.getX(), currentWaypoint.getY());
		for (int i = currentIndex; i < waypointActions.size - 1; i++) {
			WaypointAction waypoint = (WaypointAction) waypointActions.get(i);
			WaypointAction nextWaypoint = (WaypointAction) waypointActions.get(i + 1);
			totalDistance += Vector2.dst(waypoint.getX(), waypoint.getY()
					, nextWaypoint.getX(),nextWaypoint.getY());
		}

		lengthToEnd = totalDistance;

	}

	public boolean isAttacking(){
		return stateManager.getCurrentStateName().equals(EnemyState.ATTACKING);
	}

	public float getLengthToEnd() {
		if(!lengthToEndCalculated) {
			calcLengthToEnd();
		}
		return lengthToEnd;
	}

	public int getKillReward(){
		return killReward;
	}

}
