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

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("Tank", false);
        Enemy enemy4 = TestUtil.createEnemy("Humvee", false);

        SpawningEnemy spawningEnemy1 = new SpawningEnemy(spawningEnemyPool);
        SpawningEnemy spawningEnemy2 = new SpawningEnemy(spawningEnemyPool);
        SpawningEnemy spawningEnemy3 = new SpawningEnemy(spawningEnemyPool);
        SpawningEnemy spawningEnemy4 = new SpawningEnemy(spawningEnemyPool);
        spawningEnemy1.setEnemy(enemy1);
        spawningEnemy2.setEnemy(enemy2);
        spawningEnemy3.setEnemy(enemy3);
        spawningEnemy4.setEnemy(enemy4);

        Queue<SpawningEnemy> initSpawningEnemyQueue = new Queue<>();
        initSpawningEnemyQueue.addFirst(spawningEnemy1);
        initSpawningEnemyQueue.addFirst(spawningEnemy2);
        initSpawningEnemyQueue.addFirst(spawningEnemy3);
        initSpawningEnemyQueue.addFirst(spawningEnemy4);

        doReturn(new Array<LDVector2>()).when(map).getPath();
        doReturn(TestUtil.createEnemy("Rifle", false)).when(combatActorFactory)
            .loadEnemy(isA(String.class));

        DynamicWaveLoader dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);
        dynamicWaveLoader.initDynamicWaveLoader(initSpawningEnemyQueue);

        Queue<SpawningEnemy> spawningEnemies = dynamicWaveLoader.loadWave(LevelName.SERPENTINE_RIVER, 5);

        assertTrue(spawningEnemies.size > 4);

    }
}
