package com.lastdefenders.game.model.actor.combat.tower.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/14/2017.
 */
public class TowerStateManagerTest {

    @Test
    public void transitionTest() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);

        assertEquals(TowerStateEnum.ACTIVE, tower.getStateManager().getCurrentStateName());

        tower.getStateManager().transition(TowerStateEnum.WAVE_END);
        assertEquals(TowerStateEnum.WAVE_END, tower.getStateManager().getCurrentStateName());
    }

}
