package com.eric.mtd.game.model.actor.combat.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.actor.ai.EnemyAI;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IPassiveEnemy;
import com.eric.mtd.game.service.factory.ActorFactory.CombatActorPool;
import com.eric.mtd.game.service.factory.interfaces.IDeathEffectFactory;
import com.eric.mtd.game.service.factory.interfaces.IProjectileFactory;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;

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
	private List<MoveToAction> actionList = new ArrayList<MoveToAction>();
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

	public Enemy(TextureRegion[] textureRegions, CombatActorPool<CombatActor> pool, Group targetGroup, float[] bodyPoints, Dimension textureSize, Vector2 gunPos,
					float speed, float health, float armor, float attack, float attackSpeed, float range) {
		super(textureRegions[0], pool, targetGroup, bodyPoints, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.textureRegions = textureRegions;
		this.speed = speed;
		multipleTextures = true;
		this.pool = pool;
	}

	public Enemy(TextureRegion textureRegion, CombatActorPool<CombatActor> pool, Group targetGroup, float[] bodyPoints, Dimension textureSize, Vector2 gunPos,
					float speed, float health, float armor, float attack, float attackSpeed, float range) {
		super(textureRegion, pool, targetGroup, bodyPoints, textureSize, gunPos, health, armor, attack, attackSpeed, range);
		this.speed = speed;
		multipleTextures = false;
		this.pool = pool;
	}
	/**
	 * Sets the path for the enemy. Starts off screen.
	 * 
	 * @param path
	 */
	public void setPath(Queue<Vector2> path) {
		Vector2 newWaypoint = new Vector2();
		newWaypoint = path.remove();
		setPositionCenter(newWaypoint);
		setRotation(calculateRotation(path.peek()));
		Vector2 rotatedCoords = getRotatedCoords(this.getPositionCenter().x, getY() + this.getTextureSize().getHeight());
		float newX = this.getPositionCenter().x + (this.getPositionCenter().x - rotatedCoords.x);
		float newY = this.getPositionCenter().y + (this.getPositionCenter().y - rotatedCoords.y);
		this.setPositionCenter(newX, newY); // Start off screen
		Vector2 moveVector = new Vector2();
		Vector2 prevWaypoint = new Vector2();
		while (!path.isEmpty()) {
			prevWaypoint = newWaypoint;
			newWaypoint = (path.remove());
			moveVector.set((newWaypoint.x - (this.getOriginX())), (newWaypoint.y - (this.getOriginY())));
			float moveDistance = (newWaypoint.dst(prevWaypoint) / speed);
			actionList.add(Actions.moveTo(moveVector.x, moveVector.y, moveDistance, Interpolation.linear));
		}
		System.out.println(actionList);
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
		// Find target if delay has expired.
		if (!(this instanceof IPassiveEnemy) && !attacking) {
			if ((findTargetCounter >= findTargetDelay)) {
				findTarget();
			} else {
				findTargetCounter += delta;
			}
		}
		// If the enemy is attacking and the duration to pause for the attack
		// animation has finished, then reset its rotation to the way point
		// it was heading to before it began attacking
		if (attacking) {
			System.out.println(getTarget().getPositionCenter());
			movementDelayCounter += delta;
			attackCounter += delta;
			if (movementDelayCounter >= MOVEMENT_DELAY ) {
				if (actionIndex > 0) {
					setRotation(calculateRotation((actionList.get(actionIndex - 1)).getX() + (this.getOriginX()), (actionList.get(actionIndex - 1)).getY() + (this.getOriginY())));
				}
				movementDelayCounter = 0;
				attacking = false;
				attackCounter = 100; //Ready to attack
				findTargetDelay = random.nextFloat()*2 + 1;
			} else if( attackCounter >= this.getAttackSpeed()) {
				if(this.getTarget() == null || this.getTarget().isDead()){
					attacking = false;
					setRotation(calculateRotation((actionList.get(actionIndex - 1)).getX() + (this.getOriginX()), (actionList.get(actionIndex - 1)).getY() + (this.getOriginY())));
				} else {
					this.setRotation(calculateRotation(super.getTarget().getPositionCenter()));
					this.attackTarget();
				}
				attackCounter = 0;
				
			}
		} else {
			super.act(delta); // Pause to create a delay if attacking
		}

		// Find the next way point when at the end of a way point
		if (!isDead() && !attacking) {
			if (this.getActions().size == 0) {
				if (actionIndex < actionList.size()) {
					setRotation(calculateRotation((actionList.get(actionIndex)).getX() + (this.getOriginX()), (actionList.get(actionIndex)).getY() + (this.getOriginY())));
					this.addAction(actionList.get(actionIndex)); // Set Move TO
					actionIndex++;
				} else {
					if (getStage() instanceof GameStage) {
						((GameStage) getStage()).enemyReachedEnd();
					}
					pool.free(this);
					return;
				}
			}
			changeTextures(delta);

		}
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
		System.out.println("resetting");
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
	}

	/**
	 * Determines the length till the end of the entire path
	 * 
	 * @return boolean - Total Distance till the end
	 */
	public float lengthTillEnd() {
		float totalDistance = 0;
		if (actionIndex > 0) {
			totalDistance = Vector2.dst(this.getX(), this.getY(), actionList.get(actionIndex - 1).getX(), actionList.get(actionIndex - 1).getY());
			for (int i = actionIndex - 1; i < actionList.size() - 1; i++) {
				totalDistance = totalDistance + Vector2.dst(actionList.get(i).getX(), actionList.get(i).getY(), actionList.get(i + 1).getX(), actionList.get(i + 1).getY());
			}
		} else {
			for (int i = 0; i < actionList.size() - 1; i++) {
				totalDistance = totalDistance + Vector2.dst(actionList.get(i).getX(), actionList.get(i).getY(), actionList.get(i + 1).getX(), actionList.get(i + 1).getY());
			}
		}
		return totalDistance;
	}

}
