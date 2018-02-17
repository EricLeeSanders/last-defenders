package game.model.actor.health;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.game.service.factory.HealthFactory.HealthPool;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */
public class HealthBarTest {

    private Batch batchMock = mock(Batch.class);
    private TextureRegionDrawable greenMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable orangeMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable redMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable grayMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable unfilledMock = mock(TextureRegionDrawable.class);
    private HealthPool poolMock = mock(HealthPool.class);

    @Before
    public void initHealthBarTest() {

        Gdx.app = mock(Application.class);
    }

    private HealthBar createHealthBar() {

        setupBarMock(greenMock);
        setupBarMock(orangeMock);
        setupBarMock(redMock);
        setupBarMock(grayMock);
        setupBarMock(unfilledMock);

        return new HealthBar(poolMock, greenMock, orangeMock, redMock, grayMock, unfilledMock);
    }

    private void setupBarMock(TextureRegionDrawable bar){
        TextureRegion textureRegionMock = mock(TextureRegion.class);
        Texture textureMock = mock(Texture.class);

        doReturn(textureRegionMock).when(bar).getRegion();
        doReturn(textureMock).when(textureRegionMock).getTexture();

        textureRegionMock.setRegionWidth(10);
        textureRegionMock.setRegionHeight(10);
        textureRegionMock.setRegionX(1);
        textureRegionMock.setRegionY(1);
    }


    /**
     * Healthbar with green bar is displayed
     */
    @Test
    public void healthBarNoArmorTest1() {

        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", true);
        tower.setPositionCenter(20, 20);

        healthBar.setActor(tower);
        assertFalse(healthBar.isVisible());

        doReturn(.9f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.isVisible());
        verify(batchMock, times(1))
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, never())
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        verify(poolMock, times(1)).free(healthBar);

    }

    /**
     * Healthbar with orange bar is displayed
     */
    @Test
    public void healthBarNoArmorTest2() {

        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", true);
        tower.setPositionCenter(20, 20);

        healthBar.setActor(tower);
        assertFalse(healthBar.isVisible());

        doReturn(.6f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.isVisible());

        verify(batchMock, times(1))
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, never())
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        verify(poolMock, times(1)).free(healthBar);

    }

    /**
     * Healthbar with red bar is displayed
     */
    @Test
    public void healthBarNoArmorTest3() {

        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", true);
        tower.setPositionCenter(20, 20);

        healthBar.setActor(tower);
        assertFalse(healthBar.isVisible());

        doReturn(.3f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.isVisible());

        verify(batchMock, times(1))
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, never())
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        verify(poolMock, times(1)).free(healthBar);

    }

    /**
     * Healthbar with gray bar is displayed
     */
    @Test
    public void healthBarWithArmorTest1() {

        HealthBar healthBar = createHealthBar();
        healthBar = spy(healthBar);
        Tower tower = TestUtil.createTower("Rifle", true);
        tower.setHasArmor(true);
        tower.setPositionCenter(20, 20);

        healthBar.setActor(tower);
        assertFalse(healthBar.isVisible());

        doReturn(.3f).when(tower).getArmorPercent();
        doReturn(.3f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.isVisible());

        verify(batchMock, times(1))
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, never())
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        verify(poolMock, times(1)).free(healthBar);

    }

}
