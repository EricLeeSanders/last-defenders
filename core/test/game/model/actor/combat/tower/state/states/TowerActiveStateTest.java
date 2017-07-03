package game.model.actor.combat.tower.state.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.ai.TowerAIType;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager;
import com.foxholedefense.game.model.actor.combat.tower.state.states.TowerActiveState;
import com.foxholedefense.game.model.actor.interfaces.Targetable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import testutil.TestUtil;


import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Eric on 5/15/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TowerAIType.class})
public class TowerActiveStateTest {

    @Before
    public void initTowerActiveStateTest() {
        Gdx.app = mock(Application.class);
        PowerMockito.mock(TowerAIType.class);
    }

    /**
     * Test that the state attacks an enemy
     */
    @Test
    public void towerActiveStateTest1() {
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        Tower tower = TestUtil.createTower("Rifle", true);

        TowerStateManager stateManagerMock = mock(TowerStateManager.class);
        TowerActiveState towerActiveState = new TowerActiveState(tower, stateManagerMock);

        SnapshotArray<Actor> targetGroupArray = tower.getTargetGroup().getChildren();
        TowerAIType ai = tower.getAI();
        ai = spy(ai);
        doReturn(ai).when(tower).getAI();

        when(ai.findTarget(tower, targetGroupArray)).thenReturn(enemy);
        verify(tower, never()).attackTarget(isA(Targetable.class));

        // Should be ready to attack, but has not attacked yet
        towerActiveState.update(tower.getAttackSpeed());
        verify(tower, never()).attackTarget(isA(Targetable.class));

        // Should attack after this update
        towerActiveState.update(tower.getAttackSpeed() / 4);
        verify(tower, times(1)).attackTarget(isA(Targetable.class));

        // Should be ready to attack after this update, but not attack yet.
        towerActiveState.update(tower.getAttackSpeed() / 4);
        verify(tower, times(1)).attackTarget(isA(Targetable.class));

    }
}
