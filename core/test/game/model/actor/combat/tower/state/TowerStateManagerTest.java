package game.model.actor.combat.tower.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import util.TestUtil;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 5/14/2017.
 */
public class TowerStateManagerTest {


    @Before
    public void initTowerStateManagerTest(){
        Gdx.app = mock(Application.class);
    }

    @Test
    public void transitionTest(){
        Tower tower = TestUtil.createTower("Rifle", false);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        TowerStateManager stateManager = new TowerStateManager(tower, effectFactoryMock);

        assertEquals(TowerState.STANDBY, stateManager.getCurrentStateName());

        stateManager.transition(TowerState.ACTIVE);
        assertEquals(TowerState.ACTIVE, stateManager.getCurrentStateName());
    }

}
