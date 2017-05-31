package game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyRunningState;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.HashMap;
import java.util.Map;

import util.TestUtil;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

/**
 * Created by Eric on 5/15/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnemyAI.class})
public class EnemyRunningStateTest {

    @Before
    public void initEnemyRunningStateTest(){
        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(EnemyAI.class);
    }

    @Test
    public void enemyRunningStateTest1(){
        Enemy enemy = TestUtil.createEnemy("Rifle", true);
        Tower tower = TestUtil.createTower("Rifle", false);

        Array<Action> arrayAction = new Array<Action>();
        arrayAction.add(new SequenceAction());

        doReturn(arrayAction).when(enemy).getActions();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("target", tower);

        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);

        EnemyRunningState runningState = new EnemyRunningState(enemy, stateManagerMock);



        SnapshotArray<Actor> targetGroupArray = enemy.getTargetGroup().getChildren();
        when(EnemyAI.findNearestTower(enemy, targetGroupArray)).thenReturn(tower);
        runningState.update(Enemy.FIND_TARGET_DELAY);
        runningState.update(1f);

        verify(stateManagerMock, times(1)).transition(eq(EnemyState.ATTACKING), isA(Map.class));

    }
}
