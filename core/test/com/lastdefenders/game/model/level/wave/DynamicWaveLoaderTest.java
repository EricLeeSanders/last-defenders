package com.lastdefenders.game.model.level.wave;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/26/2017.
 */

public class DynamicWaveLoaderTest {

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    private Map map = mock(Map.class);

    @Before
    public void initDynamicWaveLoaderTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void dynamicWaveLoaderTest1() {


    }
}
