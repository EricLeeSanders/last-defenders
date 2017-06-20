package game.model.actor.health;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.HealthBar;

import org.junit.Before;
import org.junit.Test;


import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */
public class HealthBarTest {

    private Batch batchMock = mock(Batch.class);
    private TextureRegion backgroundBarMock = mock(TextureRegion.class);
    private TextureRegion healthBarMock = mock(TextureRegion.class);
    private TextureRegion armorBarMock = mock(TextureRegion.class);
    @SuppressWarnings("rawtypes")
    private Pool poolMock = mock(Pool.class);

    @Before
    public void initHealthBarTest() {
        Gdx.app = mock(Application.class);
    }

    private HealthBar createHealthBar(){

        HealthBar healthBar = new HealthBar(poolMock, backgroundBarMock, healthBarMock, armorBarMock);

        return healthBar;
    }

    /**
     * Healthbar is displayed with the correct size when the tower takes damage
     * and the Healthbar is freed when the tower is killed. The tower does not have armor
     */
    @Test
    public void healthBarNoArmorTest1(){
        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", false);
        tower.setPositionCenter(20,20);

        healthBar.setActor(tower);
        healthBar.draw(batchMock, 1);

        verify(healthBar, never()).draw(eq(batchMock), eq(1));

        tower.takeDamage(1);
        healthBar.draw(batchMock, 1);

        assertEquals(tower.getPositionCenter().x + HealthBar.X_OFFSET, healthBar.getX(), TestUtil.DELTA);
        assertEquals(tower.getPositionCenter().y + HealthBar.Y_OFFSET, healthBar.getY(), TestUtil.DELTA);

        //Check the healthBar texture size
        float x = healthBar.getX();
        float y = healthBar.getY();
        float healthBarSize = HealthBar.MAX_BAR_WIDTH * tower.getHealthPercent();
        verify(batchMock, times(1)).draw(eq(healthBarMock), eq(x), eq(y), eq(healthBarSize), eq(HealthBar.BAR_HEIGHT));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        verify(poolMock, times(1)).free(healthBar);
        verify(batchMock, never()).draw(eq(armorBarMock), isA(Float.class), isA(Float.class), isA(Float.class), isA(Float.class));

    }


    /**
     * Healthbar.ArmorBar is displayed with the correct size when the tower takes damage
     * The tower has armor
     */
    @Test
    public void healthBarWithArmorTest1(){
        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", false);
        tower.setHasArmor(true);
        tower.setPositionCenter(20,20);

        healthBar.setActor(tower);
        healthBar.draw(batchMock, 1);

        verify(healthBar, never()).draw(eq(batchMock), eq(1));

        tower.takeDamage(1);
        healthBar.draw(batchMock, 1);

        //Check the armor texture size
        float x = healthBar.getX();
        float y = healthBar.getY();
        float armorBarSize = HealthBar.MAX_BAR_WIDTH * tower.getArmorPercent();
        verify(batchMock, times(1)).draw(eq(armorBarMock), eq(x), eq(y), eq(armorBarSize), eq(HealthBar.BAR_HEIGHT));
        //Make sure health bar is full size
        verify(batchMock, times(1)).draw(eq(healthBarMock), eq(x), eq(y), eq(HealthBar.MAX_BAR_WIDTH), eq(HealthBar.BAR_HEIGHT));

        // remove armor and verify that the bars are not drawn again
        tower.setHasArmor(false);
        healthBar.draw(batchMock, 1);
        verify(batchMock, times(3)).draw(isA(TextureRegion.class), isA(Float.class), isA(Float.class), isA(Float.class), isA(Float.class));




    }

}
