package com.lastdefenders.game.model.actor.health;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class ArmorIconTest {

    private Batch batchMock = mock(Batch.class);

    @Before
    public void initArmorIconTest() {

        Gdx.app = mock(Application.class);
    }

    private ArmorIcon createArmorIcon(CombatActor actor) {

        TextureRegion iconMock = mock(TextureRegion.class);
        doReturn(20).when(iconMock).getRegionWidth();
        doReturn(20).when(iconMock).getRegionHeight();

        @SuppressWarnings("unchecked")
        ArmorIcon armorIcon = new ArmorIcon(iconMock, actor);

        return armorIcon;
    }

    /**
     * Test that the ArmorIcon is placed in the correct position
     * when the actor takes damage and that it is freed when the
     * actor is killed.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void armorIconTest1() {


        Tower tower = TestUtil.createTower(TowerRifle.class, false);
        tower.setHasArmor(true);
        tower.setPositionCenter(20, 20);

        ArmorIcon armorIcon = createArmorIcon(tower);

        armorIcon.draw(batchMock, 1);

        assertEquals(tower.getPositionCenter().y + ArmorIcon.Y_OFFSET, armorIcon.getY(),
            TestUtil.DELTA);
        assertEquals(tower.getPositionCenter().x + ArmorIcon.X_OFFSET, armorIcon.getX(),
            TestUtil.DELTA);

        tower.takeDamage(1);
        tower.setPositionCenter(50, 50);
        armorIcon.draw(batchMock, 1);

        assertEquals(tower.getPositionCenter().y + ArmorIcon.Y_OFFSET, armorIcon.getY(),
            TestUtil.DELTA);
        assertEquals(tower.getPositionCenter().x + ArmorIcon.X_HEALTH_BAR_DISPLAYING_OFFSET,
            armorIcon.getX(), TestUtil.DELTA);


    }
}
