package game.model.actor.combat.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.action.FHDSequenceAction;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.IEnemyObserver.EnemyEvent;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.GdxTestRunner;
import util.TestUtil;


/**
 * Created by Eric on 4/23/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, Enemy.class})
public class EnemyTest {

    @Before
    public void initEnemyTest(){
        PowerMockito.mockStatic(Logger.class);
    }

    /**
     * Enemy is killed
     */
    @Test
    public void testEnemyDead() {
        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        enemy.setHasArmor(true);
        enemy.takeDamage(100);

        assertTrue(enemy.isDead());
        assertFalse(enemy.isActive());
        assertFalse(enemy.hasArmor());

    }

    /**
     * Enemy has armor and takes damage equal to armor
     */
    @Test
    public void testEnemyArmor1(){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        float damageAmount = enemy.getHealth() / 2;
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), 100f, TestUtil.DELTA);
        assertFalse(enemy.hasArmor());
    }

    /**
     * Enemy has armor and takes damage < armor
     */
    @Test
    public void testEnemyArmor2(){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        float damageAmount = enemy.getHealth() / 4;
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), 100f, TestUtil.DELTA);
        assertTrue(enemy.hasArmor());
    }

    /**
     * Enemy has armor and takes damage > armor
     */
    @Test
    public void testEnemyArmor3(){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        float damageAmount = enemy.getHealth();
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), 50f, TestUtil.DELTA);
        assertFalse(enemy.hasArmor());
    }

    @Test
    public void testWaypointActions1(){

        Enemy enemy = TestUtil.createEnemy("EnemyRifle");

        Array<FHDVector2> path = new Array<FHDVector2>();

        FHDVector2 startPoint = new FHDVector2();
        startPoint.set(50, 50);
        FHDVector2 waypoint1 = new FHDVector2();
        waypoint1.set(100, 100);
        FHDVector2 waypoint2 = new FHDVector2();
        waypoint2.set(150, 150);
        FHDVector2 waypoint3 = new FHDVector2();
        waypoint3.set(200, 200);
        FHDVector2 waypoint4 = new FHDVector2();
        waypoint4.set(250, 250);

        path.add(startPoint);
        path.add(waypoint1);
        path.add(waypoint2);
        path.add(waypoint3);
        path.add(waypoint4);

        enemy.setPath(path);
        assertEquals(enemy.getActions().size, 1);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        // get first waypoint
        MoveToAction moveToAction = (MoveToAction) sequenceAction.getCurrentAction();
        // complete waypoint
        sequenceAction.act(moveToAction.getDuration());

        assertNotEquals(moveToAction, sequenceAction.getCurrentAction());
        assertEquals(waypoint1, enemy.getPositionCenter());
    }

    @Test
    public void testWaypointActions2(){

        Enemy enemy = TestUtil.createEnemy("EnemyRifle");

        Array<FHDVector2> path = new Array<FHDVector2>();

        FHDVector2 startPoint = new FHDVector2();
        startPoint.set(50, 50);
        FHDVector2 waypoint1 = new FHDVector2();
        waypoint1.set(100, 100);
        FHDVector2 waypoint2 = new FHDVector2();
        waypoint2.set(150, 150);
        FHDVector2 waypoint3 = new FHDVector2();
        waypoint3.set(200, 200);
        FHDVector2 waypoint4 = new FHDVector2();
        waypoint4.set(250, 250);

        path.add(startPoint);
        path.add(waypoint1);
        path.add(waypoint2);
        path.add(waypoint3);
        path.add(waypoint4);

        enemy.setPath(path);
        assertEquals(1, enemy.getActions().size);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        // get 3 waypoints and complete;
        for(int i = 0; i < 3; i++){
            float duration = ((MoveToAction) sequenceAction.getCurrentAction()).getDuration();
            // complete waypoint
            sequenceAction.act(duration);
        }

        assertEquals(3, sequenceAction.getIndex());
        assertEquals(waypoint3, enemy.getPositionCenter());
    }

    @Test
    public void testEnemyReachedEnd(){

        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        enemy.setActive(true);
        Array<FHDVector2> path = new Array<FHDVector2>();

        FHDVector2 startPoint = new FHDVector2();
        startPoint.set(50, 50);
        FHDVector2 waypoint1 = new FHDVector2();
        waypoint1.set(100, 100);
        FHDVector2 waypoint2 = new FHDVector2();
        waypoint2.set(150, 150);
        FHDVector2 waypoint3 = new FHDVector2();
        waypoint3.set(200, 200);
        FHDVector2 waypoint4 = new FHDVector2();
        waypoint4.set(250, 250);

        path.add(startPoint);
        path.add(waypoint1);
        path.add(waypoint2);
        path.add(waypoint3);
        path.add(waypoint4);

        enemy.setPath(path);
        assertEquals(1, enemy.getActions().size);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        for(int i = 0; i < 4; i++){
            float duration = ((MoveToAction) sequenceAction.getCurrentAction()).getDuration();
            // complete waypoint
            sequenceAction.act(duration);
        }

        try{
            enemy.act(1f);
        } catch(Exception e){

        }
        assertEquals(waypoint4, enemy.getPositionCenter());

        try {
            PowerMockito.verifyPrivate(enemy, times(1)).invoke("reachedEnd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEnemyLengthToEnd1() {

        Enemy enemy = TestUtil.createEnemy("EnemyRifle");

        Array<FHDVector2> path = new Array<FHDVector2>();

        FHDVector2 startPoint = new FHDVector2();
        startPoint.set(50, 50);
        FHDVector2 waypoint1 = new FHDVector2();
        waypoint1.set(100, 100);
        FHDVector2 waypoint2 = new FHDVector2();
        waypoint2.set(150, 150);
        FHDVector2 waypoint3 = new FHDVector2();
        waypoint3.set(200, 200);
        FHDVector2 waypoint4 = new FHDVector2();
        waypoint4.set(250, 250);

        path.add(startPoint);
        path.add(waypoint1);
        path.add(waypoint2);
        path.add(waypoint3);
        path.add(waypoint4);

        enemy.setPath(path);
        assertEquals(enemy.getActions().size, 1);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        // get first waypoint
        MoveToAction firstWaypoint = (MoveToAction) sequenceAction.getCurrentAction();
        // complete waypoint
        sequenceAction.act(firstWaypoint.getDuration());

        assertNotEquals(firstWaypoint, sequenceAction.getCurrentAction());

        MoveToAction secondWaypoint = (MoveToAction) sequenceAction.getCurrentAction();
        //complete half of second waypoint
        sequenceAction.act(secondWaypoint.getDuration() / 2);

        //Enemy should be half way from first point to second

        Vector2 halfway = new Vector2(waypoint1);
        halfway.add(waypoint2);

        assertEquals(halfway.x / 2, enemy.getPositionCenter().x, TestUtil.DELTA);
        assertEquals(halfway.y / 2, enemy.getPositionCenter().y, TestUtil.DELTA);

        Vector2 currentWaypoint = new Vector2(secondWaypoint.getX(), secondWaypoint.getY());
        float length = Vector2.dst(enemy.getX(), enemy.getY(), currentWaypoint.x, currentWaypoint.y);
        System.out.println(enemy.getX());
        System.out.println(enemy.getY());
        System.out.println(currentWaypoint.x);
        System.out.println(currentWaypoint.y);
        System.out.println(length);

    }
}
