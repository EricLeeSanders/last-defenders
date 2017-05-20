package game.model.actor.combat.tower;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.util.Logger;

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
@PrepareForTest(Logger.class)
public class TowerTest {

    @Before
    public void initTowerTest(){
        PowerMockito.mockStatic(Logger.class);
    }

    /**
     * Tower is killed
     */
    @Test
    public void testTowerDead() {
        Tower tower = TestUtil.createTower("Rifle", false);
        tower.setHasArmor(true);
        tower.takeDamage(100);

        assertTrue(tower.isDead());
        assertFalse(tower.hasArmor());

    }

    /**
     * Tower has armor and takes damage equal to armor
     */
    @Test
    public void testTowerArmor1(){
        Tower tower = TestUtil.createTower("Rifle", false);
        float damageAmount = tower.getHealth() / 2;
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 100f, TestUtil.DELTA);
        assertFalse(tower.hasArmor());
    }

    /**
     * Tower has armor and takes damage < armor
     */
    @Test
    public void testTowerArmor2(){
        Tower tower = TestUtil.createTower("Rifle", false);
        float damageAmount = tower.getHealth() / 4;
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 100f, TestUtil.DELTA);
        assertTrue(tower.hasArmor());
    }

    /**
     * Tower has armor and takes damage > armor
     */
    @Test
    public void testTowerArmor3(){
        Tower tower = TestUtil.createTower("Rifle", false);
        float damageAmount = tower.getHealth();
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 50f, TestUtil.DELTA);
        assertFalse(tower.hasArmor());
    }

    /**
     * Tests that tower attacks target
     */
    @Test
    public void testStateAttackTarget(){
        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", false);

        tower.getTargetGroup().addActor(enemy);

        assertEquals(TowerState.ACTIVE, tower.getState());

        enemy.setPositionCenter(120, 120);
        tower.setPositionCenter(120, 120);

        tower.act(.0001f);
        verify(tower, times(1)).attackTarget(enemy);

    }

    /**
     * Tests that tower switches to dead state
     */
    @Test
    public void testDeadState(){
        Tower tower = TestUtil.createTower("Rifle", true);

        assertEquals(TowerState.ACTIVE, tower.getState());

        tower.takeDamage(tower.getHealth());

        verify(tower, times(1)).deadState();
    }
}
