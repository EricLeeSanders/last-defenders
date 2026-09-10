package com.lastdefenders.game.model.level.wave;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.PreloadedWave;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.lastdefenders.levelselect.LevelName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Covers the seeding paths between the file based and dynamically generated waves.
 */
public class HybridWaveLoaderStrategyTest {

    private static final LevelName LEVEL_NAME = LevelName.SERPENTINE_RIVER;
    private static final int SEED_SIZE = 5;

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    private Map map = mock(Map.class);
    private FileWaveLoader fileWaveLoader = mock(FileWaveLoader.class);

    private DynamicWaveLoader dynamicWaveLoader;
    private HybridWaveLoaderStrategy strategy;

    @BeforeEach
    public void setup() {

        // Forces TestUtil to boot the headless application that Logger writes through.
        TestUtil.getResources();

        // A real loader, so the seeding contract between the two is actually exercised.
        dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);
        strategy = new HybridWaveLoaderStrategy(fileWaveLoader, dynamicWaveLoader);

        doAnswer(invocation -> TestUtil.createEnemy(EnemyRifle.class, false))
            .when(combatActorFactory).loadEnemy(anyString(), anyBoolean());

        doAnswer(invocation -> {
            SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
            spawningEnemy.setEnemy(invocation.getArgument(0));
            spawningEnemy.setSpawnDelay(invocation.getArgument(1));
            return spawningEnemy;
        }).when(combatActorFactory).loadSpawningEnemy(isA(Enemy.class), anyFloat());
    }

    private Wave createFileWave(int size) {

        Queue<SpawningEnemy> enemies = new Queue<>();
        for (int i = 0; i < size; i++) {
            SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
            spawningEnemy.setEnemy(TestUtil.createEnemy(EnemyRifle.class, false));
            spawningEnemy.setSpawnDelay(0.1f);
            enemies.addLast(spawningEnemy);
        }

        return new PreloadedWave(enemies);
    }

    /**
     * The normal path: wave 100 seeds the dynamic loader, so wave 101 needs no extra file read.
     */
    @Test
    public void seedsFromTheLastFileWaveWithoutRereadingIt() {

        doAnswer(invocation -> createFileWave(SEED_SIZE)).when(fileWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));

        strategy.loadWave(LEVEL_NAME, Level.FILE_WAVE_LIMIT);
        assertTrue(dynamicWaveLoader.isInitialized());

        Wave dynamicWave = strategy.loadWave(LEVEL_NAME, Level.FILE_WAVE_LIMIT + 1);

        assertTrue(dynamicWave.getRemainingEnemyCount() > 0);

        // Exactly one file read: wave 101 must not fall back to re-reading wave 100.
        verify(fileWaveLoader, times(1)).loadWave(eq(LEVEL_NAME), eq(Level.FILE_WAVE_LIMIT));
    }

    /**
     * An empty last file wave leaves nothing to generate from. Since waves are requested in
     * order, that is the only way to reach a dynamic wave unseeded, and it should fail naming
     * the level and wave rather than surfacing a bare empty-seed argument from the loader.
     */
    @Test
    public void failsClearlyWhenTheSeedWaveFileIsEmpty() {

        doReturn(createFileWave(0)).when(fileWaveLoader)
            .loadWave(LEVEL_NAME, Level.FILE_WAVE_LIMIT);

        // Wave 100 itself is playable-but-empty and defers rather than throwing.
        strategy.loadWave(LEVEL_NAME, Level.FILE_WAVE_LIMIT);
        assertFalse(dynamicWaveLoader.isInitialized());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> strategy.loadWave(LEVEL_NAME, Level.FILE_WAVE_LIMIT + 1));

        assertTrue(exception.getMessage().contains(LEVEL_NAME.toString()),
            "Message should name the level: " + exception.getMessage());
    }
}
