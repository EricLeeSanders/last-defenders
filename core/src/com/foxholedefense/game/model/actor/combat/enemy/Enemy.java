package com.foxholedefense.game.model.actor.combat.enemy;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.interfaces.IPassiveEnemy;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.util.Dimension;

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
													// attacking
	private Random random = new Random();
	private float findTargetDelay = 2f;
	private SnapshotArray<MoveToAction> actionList = new SnapshotArray<MoveToAction>();
	private Pool<CombatActor> pool;
	private int actionIndex = 0; // Current index in the actionList
	private float speed; // number of pixels it moves in a second
	private float textureCounter; // Used to animate textures
	private float findTargetCounter = 0;
	private float attackCounter = 100; //Ready to attack
	private int textureIndex; // Current texture index
	private boolean multipleTextures, attacking;
	private TextureRegion[] textureRegions;
	private float movementDelayCounter;
	private Vector2 currentWaypoint = new Vector2();
	private float lengthToEnd;
	private boolean lengthToEndCalculated;
	public Enemy(TextureRegion[] textureRegions, CombatActorPool<CombatActor> pool, Group targetGroup, Dimension textureSize, Vector2 gunPos,
					float speed, float health, float armor, float attack, float attackSpeed, float range) {
		super(textureRegions[0], pool, targetGroup, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.textureRegions = textureRegions;
		this.speed = speed;
		multipleTextures = true;
		this.pool = pool;
	}

	public Enemy(TextureRegion textureRegion, CombatActorPool<CombatActor> pool, Group targetGroup, Dimension textureSize, Vector2 gunPos,
					float speed, float health, float armor, float attack, float attackSpeed, float range) {
		super(textureRegion, pool, targetGroup, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.speed = speed;
		multipleTextures = false;
		this.pool = pool;
	}

	/**
	 * Sets the path for the enemy. Starts off screen.
	 * 
	 * @param path
	 */
	public void setPath(Array<Vector2> path) {
		if(path == null | path.size <= 1){
			return;
		}

		//position
		Vector2 newWaypoint = path.get(0);
		setPositionCenter(newWaypoint);
		setRotation(calculateRotation(path.get(1)));
		Vector2 rotatedCoords = getRotatedCoords(this.getPositionCenter().x, getY() + this.getTextureSize().getHeight());
		float newX = this.getPositionCenter().x + (this.getPositionCenter().x - rotatedCoords.x);
		float newY = this.getPositionCenter().y + (this.getPositionCenter().y - rotatedCoords.y);
		this.setPositionCenter(newX, newY); // Start off screen

		//create MoveTo actions
		Vector2 moveVector = new Vector2();
		for (int i = 1; i < path.size; i++) {
			Vector2 prevWaypoint = newWaypoint;
			newWaypoint = path.get(i);
			moveVector.set((newWaypoint.x - (this.getOriginX())), (newWaypoint.y - (this.getOriginY())));
			float distance = newWaypoint.dst(prevWaypoint);
			float duration = (distance / speed);
			actionList.add(Actions.moveTo(moveVector.x, moveVector.y, duration, Interpolation.linear));
		}

		moveToNextWaypoint();
	}

	/**
	 * Finds a tower to attack.
	 */
	public void findTarget() {
		this.setTarget(EnemyAI.findNearestTower(this, getTargetGroup().getChildren()));
		if (getTarget() != null) {
			this.setRotation(calculateRotation(super.getTarget().getPositionCenter()));
			findTargetCounter = 0;
			attackCounter = 100; //Ready to attack
			attacking = true;
		}
	}

	/**
	 * Pauses enemy when attacking. Creates new MoveTo Actions to set the next
	 * way point for the Enemy. Calls to change textures. Calls to find target
	 * when delay expires.
	 */
	@Override
	public void act(float delta) {
		//System.out.println(getLengthToEnd());
		lengthToEndCalculated = false;
		// Find target if delay has expired.
		if (!(this instanceof IPassiveEnemy) && !attacking) {
			findTargetHandler(delta);
		}
		// If the enemy is attacking and the duration to pause for the attack
		// animation has finished, then reset its rotation to the way point
		// it was heading to before it began attacking
		if (attacking) {
			attackHandler(delta);
		} else {
			super.act(delta); // Pause to create a delay if attacking
		}

		// Find the next way point when at the end of a way point
		if (!isDead() && !attacking) {
			changeTextures(delta);
			nextWaypointHandler();
		}
	}

	private void findTargetHandler(float delta){
		if ((findTargetCounter >= findTargetDelay)) {
			findTarget();
		} else {
			findTargetCounter += delta;
		}
	}

	private void moveToNextWaypoint(){
		setRotation(calculateRotation((actionList.get(actionIndex)).getX() + (this.getOriginX()), (actionList.get(actionIndex)).getY() + (this.getOriginY())));
		this.addAction(actionList.get(actionIndex)); // Set Move TO
	}

	private void nextWaypointHandler(){
		if (this.getActions().size == 0) {
			actionIndex++;
			if (actionIndex < actionList.size) {
				moveToNextWaypoint();
			} else {
				if (getStage() instanceof GameStage) {
					((GameStage) getStage()).enemyReachedEnd();
				}
				pool.free(this);
			}
		}
	}

	private void attackHandler(float delta){
		movementDelayCounter += delta;
		attackCounter += delta;
		if (movementDelayCounter >= MOVEMENT_DELAY ){
			finishAttacking();
		} else if(this.getTarget() == null || this.getTarget().isDead()){
			findTargetHandler(delta);
		} else if( attackCounter >= this.getAttackSpeed()) {
			this.setRotation(calculateRotation(super.getTarget().getPositionCenter()));
			this.attackTarget();
			attackCounter = 0;
		}
	}

	private void finishAttacking(){
		setRotation(calculateRotation((actionList.get(actionIndex)).getX() + (this.getOriginX()), (actionList.get(actionIndex)).getY() + (this.getOriginY())));
		movementDelayCounter = 0;
		attacking = false;
		attackCounter = 100; //Ready to attack
		findTargetDelay = random.nextFloat()*2 + 1;
	}
	/**
	 * Handles the changing of textures
	 * 
	 * @param delta
	 */
	public void changeTextures(float delta) {
		if (multipleTextures) {
			if (!attacking) {
				if (textureCounter >= 0.3f) {
					textureCounter = 0;
					textureIndex++;
					setTextureRegion(textureRegions[textureIndex % 2]);
				} else {
					textureCounter += delta;
				}
			} else {
				setTextureRegion(textureRegions[2]); // Stationary when attacking
			}
		}
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
		this.setRotation(0);
		attacking = false;
		textureIndex = 0;
		textureCounter = 0;
		findTargetCounter = 0;
		actionIndex = 0;
		actionList.clear();
		findTargetDelay = 2f;
		attackCounter = 100; //Ready to attack
		lengthToEnd = 0;
	}

	/**
	 * Determines the length till the end of the entire path
	 * 
	 * @return boolean - Total Distance till the end
	 */
	public void calcLengthToEnd() {
		lengthToEndCalculated = true;
		float totalDistance = 0;
		totalDistance = Vector2.dst(this.getX(), this.getY(), actionList.get(actionIndex).getX(), actionList.get(actionIndex).getY());
		for (int i = actionIndex; i < actionList.size - 1; i++) {
			totalDistance += Vector2.dst(actionList.get(i).getX(), actionList.get(i).getY()
					, actionList.get(i + 1).getX(), actionList.get(i + 1).getY());
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
