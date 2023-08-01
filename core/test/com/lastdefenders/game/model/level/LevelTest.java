package com.lastdefenders.game.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.lastdefenders.levelselect.LevelName;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/26/2017.
 */

public class LevelTest {

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private FileWaveLoader fileWaveLoader = mock(FileWaveLoader.class);
    private DynamicWaveLoader dynamicWaveLoader = mock(DynamicWaveLoader.class);
    private ActorGroups actorGroups = mock(ActorGroups.class);

    @Test
    public void levelTest1() {

        LevelName levelName = LevelName.SERPENTINE_RIVER;

        Level level = new Level(levelName, actorGroups, fileWaveLoader, dynamicWaveLoader);

        Queue<SpawningEnemy> loadedEnemies = new Queue<>();
        Enemy enemy1 = TestUtil.createEnemy(EnemyRifle.class, false);
        Enemy enemy2 = TestUtil.createEnemy(EnemyTank.class, false);
        Enemy enemy3 = TestUtil.createEnemy(EnemyHumvee.class, false);
        float spawnDelay = 0.5f;
        SpawningEnemy spawningEnemy1 = new SpawningEnemy(spawningEnemyPool);
        SpawningEnemy spawningEnemy2 = new SpawningEnemy(spawningEnemyPool);
        SpawningEnemy spawningEnemy3 = new SpawningEnemy(spawningEnemyPool);
        spawningEnemy1.setEnemy(enemy1);
        spawningEnemy1.setSpawnDelay(spawnDelay);
        spawningEnemy2.setEnemy(enemy2);
        spawningEnemy2.setSpawnDelay(spawnDelay);
        spawningEnemy3.setEnemy(enemy3);
        spawningEnemy3.setSpawnDelay(spawnDelay);
        loadedEnemies.addFirst(spawningEnemy1);
        loadedEnemies.addFirst(spawningEnemy2);
        loadedEnemies.addFirst(spawningEnemy3);

        doReturn(loadedEnemies).when(fileWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));
        doReturn(loadedEnemies).when(dynamicWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));
        doReturn(new EnemyGroup()).when(actorGroups).getEnemyGroup();


        // Calls FileWaveLoader and DynamicWaveLoader
        for (int i = 0; i <= Level.FILE_WAVE_LIMIT + 1; i++) {
            level.loadNextWave();
            for (int j = level.getSpawningEnemiesCount() - 1; j >= 0; j--) {
                level.update(spawnDelay);
                assertEquals(j, level.getSpawningEnemiesCount());
            }
        }

        verify(dynamicWaveLoader, times(1)).loadCurrentWaveQueue(any(Queue.class));
        verify(dynamicWaveLoader, times(1)).loadWave(levelName, Level.FILE_WAVE_LIMIT + 1);
        verify(fileWaveLoader, times(Level.FILE_WAVE_LIMIT)).loadWave(eq(levelName), anyInt());
    }
}
