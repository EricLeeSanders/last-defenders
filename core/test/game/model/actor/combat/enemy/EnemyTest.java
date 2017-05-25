package game.model.actor.combat.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.action.FHDSequenceAction;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;


/**
 * Created by Eric on 4/23/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EnemyTest {

    @Before
    public void initEnemyTest(){
        PowerMockito.mockStatic(Logger.class);
    }

    private Array<FHDVector2> createWaypoints(){

        Array<FHDVector2> path = new Array<FHDVector2>();

        FHDVector2 startPoint = new FHDVector2(50, 50);
        FHDVector2 waypoint1 = new FHDVector2(100, 100);
        FHDVector2 waypoint2 = new FHDVector2(150, 150);
        FHDVector2 waypoint3 = new FHDVector2(200, 200);
        FHDVector2 waypoint4 = new FHDVector2(250, 250);

        path.add(startPoint);
        path.add(waypoint1);
        path.add(waypoint2);
        path.add(waypoint3);
        path.add(waypoint4);

        return path;
    }

    /**
     * Enemy is killed
     */
    @Test
    public void testEnemyDead() {
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        enemy.setHasArmor(true);
        enemy.takeDamage(100);

        assertTrue(enemy.isDead());
        assertFalse(enemy.hasArmor());

    }

    /**
     * Enemy has armor and takes damage equal to armor
     */
    @Test
    public void testEnemyArmor1(){
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        float damageAmount = enemy.getHealth() / 2;
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), 1f, TestUtil.DELTA);
        assertFalse(enemy.hasArmor());
    }

    /**
     * Enemy has armor and takes damage < armor
     */
    @Test
    public void testEnemyArmor2(){
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        float damageAmount = enemy.getHealth() / 4;
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), 1, TestUtil.DELTA);
        assertTrue(enemy.hasArmor());
    }

    /**
     * Enemy has armor and takes damage > armor
     */
    @Test
    public void testEnemyArmor3(){
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        float damageAmount = enemy.getHealth();
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), .5f, TestUtil.DELTA);
        assertFalse(enemy.hasArmor());
    }

    @Test
    public void testWaypointActions1(){

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        Array<FHDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(enemy.getActions().size, 1);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        // get first waypoint
        MoveToAction moveToAction = (MoveToAction) sequenceAction.getCurrentAction();
        // complete waypoint
        sequenceAction.act(moveToAction.getDuration());

        assertNotEquals(moveToAction, sequenceAction.getCurrentAction());
        assertEquals(path.get(1), enemy.getPositionCenter());
    }

    @Test
    public void testWaypointActions2(){

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        Array<FHDVector2> path = createWaypoints();

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
        assertEquals(path.get(3), enemy.getPositionCenter());
    }

    @Test
    public void testEnemyLengthToEnd1() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        Array<FHDVector2> path = createWaypoints();

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

        Vector2 halfway = new Vector2(path.get(1));
        halfway.add(path.get(2));

        assertEquals(halfway.x / 2, enemy.getPositionCenter().x, TestUtil.DELTA);
        assertEquals(halfway.y / 2, enemy.getPositionCenter().y, TestUtil.DELTA);

        Vector2 currentWaypoint = new Vector2(secondWaypoint.getX(), secondWaypoint.getY());
        float lengthToEnd = Vector2.dst(enemy.getPositionCenter().x, enemy.getPositionCenter().y, currentWaypoint.x, currentWaypoint.y);

        float x1 = enemy.getPositionCenter().x;
        float y1 = enemy.getPositionCenter().y;
        float x2 = currentWaypoint.x;
        float y2 = currentWaypoint.y;

        double distance = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
        assertEquals(distance, lengthToEnd, 0.00001f);
    }

    @Test
    public void testRunningState() {
        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        enemy.getTargetGroup().addActor(tower);

        Array<FHDVector2> path = createWaypoints();
        enemy.setPath(path);


        assertEquals(EnemyState.RUNNING, enemy.getState());
    }

    @Test
    public void testAttackingState(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        enemy.getTargetGroup().addActor(tower);

        Array<FHDVector2> path = createWaypoints();
        enemy.setPath(path);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        enemy.setPositionCenter(120, 120);
        tower.setPositionCenter(100, 100);

        enemy.act(2f);
        enemy.act(0.0001f);

        assertEquals(EnemyState.ATTACKING, enemy.getState());
    }

    @Test
    public void testReachedEndState(){

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        Array<FHDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(1, enemy.getActions().size);

        FHDSequenceAction sequenceAction = (FHDSequenceAction) enemy.getActions().first();
        for(int i = 0; i < 4; i++){
            float duration = ((MoveToAction) sequenceAction.getCurrentAction()).getDuration();
            // complete waypoint
            sequenceAction.act(duration);
        }

        enemy.act(10f);
        assertEquals(EnemyState.REACHED_END, enemy.getState());
        enemy.act(0.001f);
        assertEquals(EnemyState.STANDBY, enemy.getState());

    }

    /**
     * Tests that enemy switches to dead state
     */
    @Test
    public void testDeadState(){
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        enemy.takeDamage(enemy.getHealth());

        verify(enemy, times(1)).deadState();
        assertEquals(EnemyState.STANDBY, enemy.getState());
    }
}
