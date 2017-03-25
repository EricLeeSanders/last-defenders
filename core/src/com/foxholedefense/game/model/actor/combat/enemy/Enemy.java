package com.foxholedefense.game.model.actor.combat.enemy;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.action.FHDSequenceAction;
import com.foxholedefense.action.WaypointAction;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.interfaces.IPassiveEnemy;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.UtilPool;

/**
 * An abstract class that represents an Enemy. Enemies are created from the
 * ActorFactory and are pooled. Enemies can attack towers. Enemies can have
 * multiple textures to show movement.
 *
 * @author Eric
 *
 */
public abstract class Enemy extends CombatActor {
	private static final float MOVEMENT_DELAY = 1f; // The delay to wait after
	private Random random = new Random();
	private float findTargetDelay = 2f;
	private Pool<CombatActor> pool;
	private float speed; // number of pixels it moves in a second
	private float findTargetCounter = 0;
	private float attackCounter = 100; //Ready to attack
	private boolean attacking;
	private float movementDelayCounter;
	private float lengthToEnd;
	private boolean lengthToEndCalculated;
	private Animation movementAnimation;
	private float movementAnimationStateTime;
	private TextureRegion stationaryTextureRegion;
	private float rotationBeforeAttacking;
	private SnapshotArray<IEnemyObserver> observers = new SnapshotArray<IEnemyObserver>();

	public Enemy(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, Dimension textureSize, CombatActorPool<CombatActor> pool, Group targetGroup, Vector2 gunPos,
				 float speed, float health, float armor, float attack, float attackSpeed, float range) {
		super(stationaryTextureRegion, textureSize, pool, targetGroup, gunPos, health, armor, attack, attackSpeed, range);
		movementAnimation = new Animation(0.3f, animatedRegions);
		movementAnimation.setPlayMode(Animation.PlayMode.LOOP);
		this.speed = speed;
		this.pool = pool;
		this.stationaryTextureRegion = stationaryTextureRegion;
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
	 * Finds a tower to attack.
	 */
	private ITargetable findTarget() {
		return EnemyAI.findNearestTower(this, getTargetGroup().getChildren());
	}

	/**
	 * Pauses enemy when attacking. Creates new MoveTo Actions to set the next
	 * way point for the Enemy. Calls to change textures. Calls to find target
	 * when delay expires.
	 */
	@Override
	public void act(float delta) {

		lengthToEndCalculated = false;

		attackHandler(delta);
		if (!attacking) {
			super.act(delta); // Pause to create a delay if attacking
			movementAnimationStateTime += delta;
		}

		// Find the next way point when at the end of a way point
		if (!isDead() && !attacking) {
			setTextureRegion(movementAnimation.getKeyFrame(movementAnimationStateTime, true));
			hasEnemyReachedEnd();
		}
	}

	private boolean isReadyToFindTarget(){
		return findTargetCounter >= findTargetDelay;
	}

	private void hasEnemyReachedEnd(){
		if (this.getActions().size == 0
				&& !isDead()
				&& isActive()) {
			reachedEnd();
		}
	}

	private void reachedEnd(){
		Logger.info("Enemy: " + this.getClass().getSimpleName() + " reached end");
		notifyObserversEnemy(IEnemyObserver.EnemyEvent.REACHED_END);
		pool.free(this);
	}

	private void attackHandler(float delta){
		if(attacking) {
			movementDelayCounter += delta;
		}
		attackCounter += delta;
		if (isFinishedAttacking()){
			finishAttacking();
		} else if( canAttack()) {
			if (!(this instanceof IPassiveEnemy) && !attacking) {
				if(isReadyToFindTarget()){
					ITargetable target = findTarget();
					if(target != null && !target.isDead()) {
						rotationBeforeAttacking = getRotation();
						this.setRotation(ActorUtil.calculateRotation(target.getPositionCenter(), getPositionCenter()));
						this.attackTarget(target);
						attacking = true;
						setTextureRegion(stationaryTextureRegion);
						attackCounter = 0;
						findTargetCounter = 0;
					}
				} else {
					findTargetCounter += delta;
				}
			}
		}
	}

	private boolean isFinishedAttacking(){
		return attacking && movementDelayCounter >= MOVEMENT_DELAY;
	}

	private boolean canAttack(){
		return attackCounter >= this.getAttackSpeed();
	}

	private void finishAttacking(){
		setRotation(rotationBeforeAttacking);
		movementDelayCounter = 0;
		attacking = false;
		findTargetDelay = random.nextFloat()*2 + 1;
	}

	public boolean isAttacking() {
		return attacking;
	}

	/**
	 * Resets the enemy for pooling
	 */
	@Override
	public void reset() {
		super.reset();
		Logger.info("Enemy: " + this.getClass().getSimpleName() + " Resetting");
		this.setRotation(0);
		attacking = false;
		findTargetCounter = 0;
		findTargetDelay = 2f;
		attackCounter = 100; //Ready to attack
		lengthToEnd = 0;
		movementAnimationStateTime = 0;
		rotationBeforeAttacking = 0;
		movementDelayCounter = 0;
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

	public float getLengthToEnd() {
		if(!lengthToEndCalculated) {
			calcLengthToEnd();
		}
		return lengthToEnd;
	}

}
