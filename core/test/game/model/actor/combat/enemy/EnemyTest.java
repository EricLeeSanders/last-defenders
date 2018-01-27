package game.model.actor.combat.enemy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.action.LDSequenceAction;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;


/**
 * Created by Eric on 4/23/2017.
 */
public class EnemyTest {

    @Before
    public void initEnemyTest() {

        Gdx.app = mock(Application.class);
    }

    private Array<LDVector2> createWaypoints() {

        Array<LDVector2> path = new Array<>();

        LDVector2 startPoint = new LDVector2(50, 50);
        LDVector2 waypoint1 = new LDVector2(100, 100);
        LDVector2 waypoint2 = new LDVector2(150, 150);
        LDVector2 waypoint3 = new LDVector2(200, 200);
        LDVector2 waypoint4 = new LDVector2(250, 250);

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
    public void testEnemyArmor1() {

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
    public void testEnemyArmor2() {

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
    public void testEnemyArmor3() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        float damageAmount = enemy.getHealth();
        enemy.setHasArmor(true);
        enemy.takeDamage(damageAmount);

        assertEquals(enemy.getHealthPercent(), .5f, TestUtil.DELTA);
        assertFalse(enemy.hasArmor());
    }

    @Test
    public void testWaypointActions1() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        Array<LDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(enemy.getActions().size, 1);

        LDSequenceAction sequenceAction = (LDSequenceAction) enemy.getActions().first();
        // get first waypoint
        MoveToAction moveToAction = (MoveToAction) sequenceAction.getCurrentAction();
        // complete waypoint
        sequenceAction.act(moveToAction.getDuration());

        assertNotEquals(moveToAction, sequenceAction.getCurrentAction());
        assertEquals(path.get(1), enemy.getPositionCenter());
    }

    @Test
    public void testWaypointActions2() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        Array<LDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(1, enemy.getActions().size);

        LDSequenceAction sequenceAction = (LDSequenceAction) enemy.getActions().first();
        // get 3 waypoints and complete;
        for (int i = 0; i < 3; i++) {
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

        Array<LDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(enemy.getActions().size, 1);

        LDSequenceAction sequenceAction = (LDSequenceAction) enemy.getActions().first();
        // get first waypoint
        MoveToAction firstWaypoint = (MoveToAction) sequenceAction.getCurrentAction();
        // complete waypoint
        sequenceAction.act(firstWaypoint.getDuration());

        assertNotEquals(firstWaypoint, sequenceAction.getCurrentAction());

        MoveToAction secondWaypoint = (MoveToAction) sequenceAction.getCurrentAction();
        //complete half of second waypoint
        sequenceAction.act(secondWaypoint.getDuration() / 2);

        //Enemy should be half way from first point to second
        //Create new vector for halfway mark
        LDVector2 halfway = new LDVector2();
        halfway.add(path.get(1));
        halfway.add(path.get(2));
        halfway.sub(halfway.x / 2, halfway.y / 2);
        Array<LDVector2> currentPath = new Array<>();
        currentPath.add(halfway);
        currentPath.addAll(path, 2, path.size - 2);

        float distance = getDistanceOfVectors(currentPath);

        assertEquals(distance, enemy.getLengthToEnd(), TestUtil.DELTA);
    }

    private float getDistanceOfVectors(Array<LDVector2> vectors){

        float distance = 0;
        for(int i = 0; i < vectors.size - 1; i++){
            LDVector2 first = vectors.get(i);
            LDVector2 second = vectors.get(i+1);
            distance += Vector2.dst(first.x, first.y, second.x, second.y);
        }

        return distance;
    }

    @Test
    public void testRunningState() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        enemy.getTargetGroup().addActor(tower);

        Array<LDVector2> path = createWaypoints();
        enemy.setPath(path);

        assertEquals(EnemyState.RUNNING, enemy.getState());
    }

    @Test
    public void testAttackingState() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        enemy.getTargetGroup().addActor(tower);

        Array<LDVector2> path = createWaypoints();
        enemy.setPath(path);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        enemy.setPositionCenter(120, 120);
        tower.setPositionCenter(100, 100);

        enemy.act(2f);
        enemy.act(0.0001f);

        assertEquals(EnemyState.ATTACKING, enemy.getState());
    }

    @Test
    public void testReachedEndState() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        Array<LDVector2> path = createWaypoints();

        enemy.setPath(path);
        assertEquals(1, enemy.getActions().size);

        LDSequenceAction sequenceAction = (LDSequenceAction) enemy.getActions().first();
        for (int i = 0; i < 4; i++) {
            float duration = ((MoveToAction) sequenceAction.getCurrentAction()).getDuration();
            // complete waypoint
            sequenceAction.act(duration);
        }

        enemy.act(10f);
        assertEquals(EnemyState.REACHED_END, enemy.getState());
        enemy.act(0.001f);
        assertEquals(EnemyState.STANDBY, enemy.getState());

        assertEquals(0, enemy.getLengthToEnd(), TestUtil.DELTA);

    }

    /**
     * Tests that enemy switches to dead state
     */
    @Test
    public void testDeadState() {

        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        assertEquals(EnemyState.RUNNING, enemy.getState());

        enemy.takeDamage(enemy.getHealth());

        verify(enemy, times(1)).deadState();
        assertEquals(EnemyState.STANDBY, enemy.getState());
    }
}
