package com.lastdefenders.game.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
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
import com.lastdefenders.game.model.level.wave.HybridWaveLoaderStrategy;
import com.lastdefenders.game.model.level.wave.Wave;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.PreloadedWave;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.lastdefenders.levelselect.LevelName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/26/2017.
 */

public class LevelTest {

    private static final float SPAWN_DELAY = 0.5f;
    private static final int ENEMIES_PER_WAVE = 3;

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private FileWaveLoader fileWaveLoader = mock(FileWaveLoader.class);
    private DynamicWaveLoader dynamicWaveLoader = mock(DynamicWaveLoader.class);
    private ActorGroups actorGroups = mock(ActorGroups.class);

    @BeforeEach
    public void setup() {

        // Forces TestUtil to boot the headless application that Logger writes through.
        TestUtil.getResources();
    }

    /**
     * Each call must hand back a new wave. Level drains the wave as it spawns, so reusing one
     * instance across waves would leave every wave after the first empty and make the
     * assertions below pass regardless of what the loaders actually did.
     */
    private Wave createWave() {

        Queue<SpawningEnemy> loadedEnemies = new Queue<>();
        loadedEnemies.addLast(createSpawningEnemy(EnemyRifle.class));
        loadedEnemies.addLast(createSpawningEnemy(EnemyTank.class));
        loadedEnemies.addLast(createSpawningEnemy(EnemyHumvee.class));

        return new PreloadedWave(loadedEnemies);
    }

    private SpawningEnemy createSpawningEnemy(Class<? extends Enemy> enemyClass) {

        Enemy enemy = TestUtil.createEnemy(enemyClass, false);
        SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
        spawningEnemy.setEnemy(enemy);
        spawningEnemy.setSpawnDelay(SPAWN_DELAY);

        return spawningEnemy;
    }

    @Test
    public void levelTest1() {

        LevelName levelName = LevelName.SERPENTINE_RIVER;

        HybridWaveLoaderStrategy waveLoaderStrategy = new HybridWaveLoaderStrategy(fileWaveLoader, dynamicWaveLoader);
        Level level = new Level(levelName, actorGroups, waveLoaderStrategy);

        doAnswer(invocation -> createWave()).when(fileWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));
        doAnswer(invocation -> createWave()).when(dynamicWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));
        // The real loader reports this once seeded; the mock has to be told.
        doReturn(true).when(dynamicWaveLoader).isInitialized();
        doReturn(new EnemyGroup()).when(actorGroups).getEnemyGroup();

        // Calls FileWaveLoader and DynamicWaveLoader through HybridWaveLoaderStrategy
        for (int i = 0; i <= Level.FILE_WAVE_LIMIT; i++) {
            level.loadNextWave();
            assertEquals(ENEMIES_PER_WAVE, level.getSpawningEnemiesCount());
            for (int j = level.getSpawningEnemiesCount() - 1; j >= 0; j--) {
                level.update(SPAWN_DELAY);
                assertEquals(j, level.getSpawningEnemiesCount());
            }
        }

        // Verify FileWaveLoader was called for waves 1-100
        verify(fileWaveLoader, times(Level.FILE_WAVE_LIMIT)).loadWave(eq(levelName), anyInt());

        // Verify DynamicWaveLoader was called for wave 101
        verify(dynamicWaveLoader, times(1)).loadWave(levelName, Level.FILE_WAVE_LIMIT + 1);
    }

    /**
     * The dynamic loader is seeded once, from the last file wave, with the full wave still
     * intact at the moment of the call.
     *
     * The wave is drained as it would be in a real game, so this fails if the loaders ever go
     * back to handing out a single shared queue: the seed would then be the drained leftovers
     * of wave 1 rather than a full wave 100.
     */
    @Test
    public void seedsDynamicLoaderFromLastFileWave() {

        LevelName levelName = LevelName.SERPENTINE_RIVER;

        HybridWaveLoaderStrategy waveLoaderStrategy = new HybridWaveLoaderStrategy(fileWaveLoader, dynamicWaveLoader);
        Level level = new Level(levelName, actorGroups, waveLoaderStrategy);

        doAnswer(invocation -> createWave()).when(fileWaveLoader)
            .loadWave(isA(LevelName.class), isA(Integer.class));
        doReturn(true).when(dynamicWaveLoader).isInitialized();
        doReturn(new EnemyGroup()).when(actorGroups).getEnemyGroup();

        // The captured queue is live and keeps draining, so record the size at call time.
        int[] seedSizeAtCall = {-1};
        int[] seedWaveNumberAtCall = {-1};
        doAnswer(invocation -> {
            Queue<SpawningEnemy> seed = invocation.getArgument(0);
            seedSizeAtCall[0] = seed.size;
            seedWaveNumberAtCall[0] = invocation.getArgument(1);
            return null;
        }).when(dynamicWaveLoader).initializeFromSeedWave(isA(Queue.class), isA(Integer.class));

        for (int i = 0; i < Level.FILE_WAVE_LIMIT; i++) {
            level.loadNextWave();
            while (level.getSpawningEnemiesCount() > 0) {
                level.update(SPAWN_DELAY);
            }
        }

        verify(dynamicWaveLoader, times(1))
            .initializeFromSeedWave(isA(Queue.class), isA(Integer.class));

        assertEquals(ENEMIES_PER_WAVE, seedSizeAtCall[0]);
        assertEquals(Level.FILE_WAVE_LIMIT, seedWaveNumberAtCall[0]);
    }
}
