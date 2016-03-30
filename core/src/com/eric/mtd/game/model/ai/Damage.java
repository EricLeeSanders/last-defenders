package com.eric.mtd.game.model.ai;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.Logger;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.Explosion;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.factory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;

public class Damage {
	
	public static void dealBulletDamage(GameActor shooter, GameActor target){
		if((!(target instanceof IPlatedArmor))||(shooter instanceof IRPG)){
			target.takeDamage(shooter.getAttack());
	    	if(target.isDead()){
	    		if(Logger.DEBUG)System.out.println("Bullet giving kill");
	    		shooter.giveKill();
	    	}
		}
	}
	public static void dealFlameDamage(GameActor shooter, Group targetGroup, Flame flame, float attackDamage){
		int flameDamage = 0;
		for(Actor flameTarget : targetGroup.getChildren()){
			Polygon targetBody = ((GameActor) flameTarget).getBody();
			if(CollisionDetection.polygonAndPolygon(targetBody, flame.getFlameBody())){
				float damage = (attackDamage);
				flame.totalDamage += damage;
				flameDamage += damage;
				//if(Logger.DEBUG)System.out.println("Doing " + damage + " damage");
				((GameActor)flameTarget).takeDamage(damage);
				//if(Logger.DEBUG)System.out.println("Actors new health:"+((GameActor)flameTarget).getHealth());
		    	if(((GameActor)flameTarget).isDead()){
		    		if(Logger.DEBUG)System.out.println("Flame giving kill");
		    		shooter.giveKill();
		    	}
			}
		}
		//if(Logger.DEBUG)System.out.println("Flame Damage: " + flameDamage);
		flameDamage = 0;
	}
	public static void dealExplosionDamage(GameActor shooter, GameActor target, Group targetGroup){
		//dealBulletDamage(actor, target);
		float radius = ((IRPG) shooter).getAoeRadius();
		for(Actor aoeTarget : targetGroup.getChildren()){
	    	if(((GameActor)aoeTarget).isDead()==false){
				if(aoeTarget.equals(target)){
					if(Logger.DEBUG)System.out.println("aoeTarget == target");
				}
				else{
	    			Circle aoeRadius = new Circle(target.getPositionCenter().x,target.getPositionCenter().y,radius);
					float distance = target.getPositionCenter().dst(((GameActor)aoeTarget).getPositionCenter());
					if(Logger.DEBUG)System.out.println("AOE Actor distance: " + distance + " aoe radius: " + aoeRadius.radius);
					if(distance<=aoeRadius.radius){
	    				float damagePercent = (100/(distance/10)); //Divide by 10 to give the AOE a bit more damage
	    				float damage = (shooter.getAttack()*((float)damagePercent/100));
	    				if(Logger.DEBUG)System.out.println("Doing " + damagePercent + "% of damage for " + damage + " damage");
	    				((GameActor)aoeTarget).takeDamage(damage);
	    				if(Logger.DEBUG)System.out.println("Actors new health:"+((GameActor)aoeTarget).getHealth());
	    		    	if(((GameActor)aoeTarget).isDead()){
	    		    		if(Logger.DEBUG)System.out.println("Explosion giving kill");
	    		    		shooter.giveKill();
	    		    	}
	    			}
				}
	    	}
		}
	}

}
