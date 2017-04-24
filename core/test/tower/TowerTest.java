package tower;

import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.Logger;

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
        Tower tower = TestUtil.createTower("Rifle");
        tower.setHasArmor(true);
        tower.takeDamage(100);

        assertTrue(tower.isDead());
        assertFalse(tower.isActive());
        assertFalse(tower.hasArmor());

    }

    /**
     * Tower has armor and takes damage equal to armor
     */
    @Test
    public void testTowerArmor1(){
        Tower tower = TestUtil.createTower("Rifle");
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
        Tower tower = TestUtil.createTower("Rifle");
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
        Tower tower = TestUtil.createTower("Rifle");
        float damageAmount = tower.getHealth();
        tower.setHasArmor(true);
        tower.takeDamage(damageAmount);

        assertEquals(tower.getHealthPercent(), 50f, TestUtil.DELTA);
        assertFalse(tower.hasArmor());
    }
}
