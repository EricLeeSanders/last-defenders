package com.eric.mtd.game.model.actor.enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.ai.EnemyAI;
import com.eric.mtd.game.service.actorfactory.ActionFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.badlogic.gdx.utils.Pool;

public abstract class Enemy extends GameActor implements Pool.Poolable{
	private GameActorPool<GameActor> pool; 
	//private Queue<Vector2> path;
	private List<MoveToAction> actionList = new ArrayList<MoveToAction>(); //Best to use a List instead of a Queue for optimization with method LengthTillEnd
	private int actionIndex = 0;
	//private Vector2 currentWaypoint; //used to get distance to next vector
	private float speed; //number of pixels it moves in a second
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	//private Color rangeColor = new Color(1.0f,0f,0f,0.5f);
	private float textureCounter;
	float findTargetDelay = 3;
	float findTargetCounter = 0;
	private int texturePosCount;
	private boolean hasMultipleTextures, attacking;
	private TextureRegion [] textureRegions; 
	private static int enemyCount;
	private int myCount;
	public static float totalDifference;
	private Vector2 moveVector = new Vector2(); //class variable for optimization
	private float moveDistance, totalDistance, delayDuration, delayCounter;
	public Enemy(TextureRegion [] textureRegions,GameActorPool<GameActor> pool, float [] bodyPoints, Vector2 textureSize, Vector2 gunPos, float speed, float health, float armor, float attack, float attackSpeed, float range){
		super(textureRegions[0],pool,bodyPoints,textureSize, gunPos, health,armor,attack,attackSpeed,range);
		this.textureRegions = textureRegions;
		this.pool = pool;
		this.speed = speed;
    	hasMultipleTextures = true; 
    	enemyCount++;
    	this.myCount = enemyCount;
    	//super.setShowRange(true);
	}
	
	public Enemy(TextureRegion textureRegion,GameActorPool<GameActor> pool, float [] bodyPoints, Vector2 textureSize, Vector2 gunPos, float speed, float health, float armor, float attack,float attackSpeed,float range){
		super(textureRegion,pool,bodyPoints,textureSize,gunPos,health,armor,attack,attackSpeed,range);
		this.speed = speed;
		this.pool = pool;
    	hasMultipleTextures = false;
    	enemyCount++;
    	this.myCount = enemyCount;
    	//super.setShowRange(true);
	}
	
	//Set the Enemies Path. Start off the screen
	//TODO: Needs testing on different maps when the starting point is not on the left side of the screen.
	public void setPath(Queue<Vector2> path){
		//this.path = path;
		//currentWaypoint = path.remove();

       //	actionQueue.add(createMoveToAction(0));
      Vector2 prevWaypoint = new Vector2();
      Vector2 newWaypoint = new Vector2();
      newWaypoint = path.remove();
		setPositionCenter(newWaypoint);
		if(Logger.DEBUG)System.out.println("peek" + path.peek());
		if(Logger.DEBUG)System.out.println("cal roate: " + calculateNewAngle(path.peek()));
		setRotation(calculateNewAngle(path.peek()));

		Vector2 rotatedCoords = getRotatedCoords(new Vector2(this.getPositionCenter().x,getY()+this.getTextureSize().y));
		float newX = this.getPositionCenter().x + (this.getPositionCenter().x - rotatedCoords.x);
		float newY = this.getPositionCenter().y + (this.getPositionCenter().y - rotatedCoords.y);
		this.setPositionCenter(new Vector2(newX,newY));
		//newWaypoint = getPositionCenter();
		if(Logger.DEBUG)System.out.println("Rotated Coords : " + rotatedCoords);
		if(Logger.DEBUG)System.out.println("Rotated Coords : " + rotatedCoords);
		if(Logger.DEBUG)System.out.println("x: " + newX + " y: " + newY);
       while(!path.isEmpty()){
    	   prevWaypoint = newWaypoint;
    	   newWaypoint = (path.remove());
    	   moveVector.set((newWaypoint.x-(this.getOriginX())), (newWaypoint.y-(this.getOriginY())));
    	   moveDistance = (newWaypoint.dst(prevWaypoint)/speed);
    	  // if(Logger.DEBUG)System.out.println("prev waypoint: " + prevWaypoint);
    	   //if(Logger.DEBUG)System.out.println("new waypoint: " + newWaypoint);
    	   //if(Logger.DEBUG)System.out.println("move distance: " + moveDistance);

           actionList.add(Actions.moveTo(moveVector.x,moveVector.y, moveDistance, Interpolation.linear)); //Question: Need to do this interpolation
       }
	}
    public void findTarget(){
    	////if(Logger.DEBUG)System.out.println("Enemy: Trying to find a target");
    	Group towerTargetGroup;
    	//TODO: Question: Don't really like having to do it this way. Maybe a better way?
    	if(this.getStage() instanceof GameStage){
    		towerTargetGroup = ((GameStage)this.getStage()).getActorGroups().getTowerGroup();
    	}
    	else{
    		towerTargetGroup = null;
    	}
    	this.setTarget(EnemyAI.findNearestTower(this, towerTargetGroup));
    	if(getTarget() !=null){
    		////if(Logger.DEBUG)System.out.println("Enemy: Found a target");
			findTargetCounter = 0;
    		this.setRotation(calculateNewAngle(super.getTarget().getPositionCenter()));
	    	//DelayAction delayAction = (DelayAction) ActionFactory.loadAction("DelayAction");
	    	//delayAction.setDuration(0.5f);
	    
	    	this.attackTarget();
	    	attacking = true;
	        ////if(Logger.DEBUG)System.out.println("Creating delayAction");
	    	delayDuration = 0.5f/MTDGame.gameSpeed;
	    
	        ////if(Logger.DEBUG)System.out.println("restarting move action");
	    	//actionQueue.add(createMoveToAction(0));
    	}
    	else{
    		////if(Logger.DEBUG)System.out.println("Could not find a target, continue moving");
    	}
    	
    }
    @Override
    public void act(float delta){
    	////if(Logger.DEBUG)System.out.println("Enemy Delta " + delta);
    	////if(Logger.DEBUG)System.out.println(this.myCount + " pos: " +this.getPositionCenter());
    	//if(this.getActions().size > 0){
    		////if(Logger.DEBUG)System.out.println("Delta: " + delta  + " duration: " + ((MoveToAction)this.getActions().get(0)).getDuration() +
    			//	" time: " + ((MoveToAction)this.getActions().get(0)).getTime());
    		/*if((delta + ((MoveToAction)this.getActions().get(0)).getTime()) > ((MoveToAction)this.getActions().get(0)).getDuration() ){
    			float diff = (delta + ((MoveToAction)this.getActions().get(0)).getTime()) - ((MoveToAction)this.getActions().get(0)).getDuration();
    			////if(Logger.DEBUG)System.out.println("myCount " + myCount + " difference: " + diff);
    			totalDifference += diff;
    		}*/
    	//}
    	if(attacking){
    		delayCounter+=delta;
    		if(delayCounter > delayDuration){
    			if(actionIndex > 0){
					setRotation(calculateNewAngle((actionList.get(actionIndex-1)).getX() + (this.getOriginX()), //Reset Rotation
							(actionList.get(actionIndex-1)).getY() + (this.getOriginY())));
					if(Logger.DEBUG)System.out.println("changing");
    			}
    			attacking = false;
    		}
    	}
    	else{
    		super.act(delta);
    	}
 
    	if(!isDead() && !attacking){
    		if(this.getActions().size == 0){
    			if(actionIndex < actionList.size()){
    				//if(Logger.DEBUG)System.out.println("getting new waypoint");
    					/*if(Logger.DEBUG)System.out.println("peek: " + ((MoveToAction)actionQueue.peek()).getX() + ", " + ((MoveToAction)actionQueue.peek()).getY());
    					if(Logger.DEBUG)System.out.println("rotation: " + calculateNewAngle(((MoveToAction)actionQueue.peek()).getX(),((MoveToAction)actionQueue.peek()).getY()));
    					if(Logger.DEBUG)System.out.println("cal roate: " + calculateNewAngle(((MoveToAction)actionQueue.peek()).getX(),((MoveToAction)actionQueue.peek()).getY()));*/
					setRotation(calculateNewAngle((actionList.get(actionIndex)).getX() + (this.getOriginX()),
								(actionList.get(actionIndex)).getY() + (this.getOriginY())));
    					
    				this.addAction(actionList.get(actionIndex)); //Set Move TO
    				actionIndex++;
    			}
    			else{
        			if(getStage() instanceof GameStage){
        				((GameStage)getStage()).enemyReachedEnd();
        				pool.free(this);
        				return;
        			}
    			}
    		}
    		else{
    			//if(Logger.DEBUG)System.out.println(this.getActions().get(0).getDuration())
    		}
    		//lengthTillEnd();
    		changeTextures(delta);
			if(findTargetCounter >= findTargetDelay){
				//findTarget();
			}
			else{
				findTargetCounter += delta;
			}
    	}
    }
    public void changeTextures(float delta){
    	if(hasMultipleTextures){ //Question: Probably could do this with an Animation
    		if(!attacking){
				if(textureCounter >= 0.3f){
					textureCounter = 0;
					texturePosCount++;
					super.setTexture(textureRegions[texturePosCount%2]);
				}
				else{
					textureCounter += delta;
				}
    		}
    		else{
    			super.setTexture(textureRegions[2]);
    		}
    	}
    }
    @Override
    public void reset() {
    	super.reset();
    	//if(Logger.DEBUG)System.out.println("Resetting Enemy");
    	this.setRotation(0);
    	attacking = false;
    	//path = null;
    	texturePosCount = 0;
    	textureCounter = 0;
    	findTargetCounter = 0;
    	totalDistance = 0;
    	actionIndex = 0;
    	actionList.clear();
    }
    public float lengthTillEnd(){
    	totalDistance = 0;
    	if(actionIndex > 0){
    		totalDistance = Vector2.dst(this.getX(),this.getY(),actionList.get(actionIndex-1).getX(), actionList.get(actionIndex-1).getY());
	    	for(int i = actionIndex-1; i < actionList.size()-1; i++){
	    		totalDistance = totalDistance
	    				+ Vector2.dst(actionList.get(i).getX(), actionList.get(i).getY()
	    								,actionList.get(i+1).getX(), actionList.get(i+1).getY());
	    	}
    	}
    	else{
	    	for(int i = 0; i < actionList.size()-1; i++){
	    		totalDistance = totalDistance
	    				+ Vector2.dst(actionList.get(i).getX(), actionList.get(i).getY()
	    								,actionList.get(i+1).getX(), actionList.get(i+1).getY());
	    	}
    	}
    	//if(Logger.DEBUG)System.out.println("index: " + actionIndex + " total: " + totalDistance);
    	return totalDistance;
    }



}
