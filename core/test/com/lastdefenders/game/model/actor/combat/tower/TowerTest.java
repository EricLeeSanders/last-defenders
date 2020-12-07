package com.lastdefenders.game.model.actor.combat.tower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;


/**
 * Created by Eric on 4/23/2017.
 */
public class TowerTest {

    @Before
    public void initTowerTest() {

        Gdx.app = mock(Application.class);
    }

    /**
     * Tower is killed
     */
    @Test
    public void testTowerDead() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);
        tower.setHasArmor(true);
        tower.takeDamage(100);

        assertTrue(tower.isDead());
        assertFalse(tower.hasArmor());

    }

    /**
     * Tower has armor and takes damage equal to armor
     */
    @Test
    public void testTowerArmor1() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);
        float damageAmount = tower.getHealth() / 2;
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 1, TestUtil.DELTA);
        assertFalse(tower.hasArmor());
    }

    /**
     * Tower has armor and takes damage < armor
     */
    @Test
    public void testTowerArmor2() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);
        float damageAmount = tower.getHealth() / 4;
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 1, TestUtil.DELTA);
        assertTrue(tower.hasArmor());
    }

    /**
     * Tower has armor and takes damage > armor
     */
    @Test
    public void testTowerArmor3() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);
        float damageAmount = tower.getHealth();
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), .5f, TestUtil.DELTA);
        assertFalse(tower.hasArmor());
    }

    /**
     * Tests that tower attacks target
     */
    @Test
    public void testStateAttackTarget() {

        Tower tower = TestUtil.createTower(TowerRifle.class, true);
        Enemy enemy = TestUtil.createRunningEnemy(EnemyRifle.class, false);

        tower.getTargetGroup().addActor(enemy);

        assertEquals(TowerStateEnum.ACTIVE, tower.getState());

        enemy.setPositionCenter(120, 120);
        tower.setPositionCenter(120, 120);

        tower.act(.0001f);
        verify(tower, times(1)).attackTarget(enemy);

    }

    /**
     * Tests that tower switches to dead state
     */
    @Test
    public void testDeadState() {

        Tower tower = TestUtil.createTower(TowerRifle.class, true);

        assertEquals(TowerStateEnum.ACTIVE, tower.getState());

        tower.takeDamage(tower.getHealth());

        verify(tower, times(1)).deadState();
    }
}
