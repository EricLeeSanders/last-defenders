package com.lastdefenders.game.model.actor.combat.tower.state;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/14/2017.
 */
public class TowerStateManagerTest {


    @Before
    public void initTowerStateManagerTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void transitionTest() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);

        assertEquals(TowerStateEnum.ACTIVE, tower.getStateManager().getCurrentStateName());

        tower.getStateManager().transition(TowerStateEnum.WAVE_END);
        assertEquals(TowerStateEnum.WAVE_END, tower.getStateManager().getCurrentStateName());
    }

}
