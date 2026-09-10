package com.lastdefenders.game.model.level.wave;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.lastdefenders.levelselect.LevelName;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/26/2017.
 */

public class DynamicWaveLoaderTest {

    private static final LevelName LEVEL_NAME = LevelName.SERPENTINE_RIVER;
    private static final int SEED_WAVE_NUMBER = 100;
    private static final float SEED_SPAWN_DELAY = 0.1f;

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    private Map map = mock(Map.class);
    private DynamicWaveLoader dynamicWaveLoader;

    @BeforeEach
    public void setup() {

        // Forces TestUtil to boot the headless application that Logger writes through.
        TestUtil.getResources();

        dynamicWaveLoader = new DynamicWaveLoader(combatActorFactory, map);

        doAnswer(invocation -> TestUtil.createEnemy(enemyClass(invocation.getArgument(0)), false))
            .when(combatActorFactory).loadEnemy(anyString(), anyBoolean());

        doAnswer(invocation -> {
            SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
            spawningEnemy.setEnemy(invocation.getArgument(0));
            spawningEnemy.setSpawnDelay(invocation.getArgument(1));
            return spawningEnemy;
        }).when(combatActorFactory).loadSpawningEnemy(isA(Enemy.class), anyFloat());
    }

    private Class<? extends Enemy> enemyClass(String type) {

        switch (type) {
            case "Rifle":
                return EnemyRifle.class;
            case "Tank":
                return EnemyTank.class;
            case "Humvee":
                return EnemyHumvee.class;
            default:
                throw new IllegalArgumentException(type + " is not mapped in this test");
        }
    }

    private Queue<SpawningEnemy> createSeedWave(int size) {

        Queue<SpawningEnemy> seedWave = new Queue<>();
        Class<? extends Enemy>[] types = new Class[]{EnemyRifle.class, EnemyTank.class,
            EnemyHumvee.class};

        for (int i = 0; i < size; i++) {
            SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
            spawningEnemy.setEnemy(TestUtil.createEnemy(types[i % types.length], false));
            spawningEnemy.setSpawnDelay(SEED_SPAWN_DELAY);
            seedWave.addLast(spawningEnemy);
        }

        return seedWave;
    }

    private List<SpawningEnemy> drain(Wave wave) {

        List<SpawningEnemy> spawned = new ArrayList<>();
        while (wave.hasNextEnemy()) {
            spawned.add(wave.nextEnemy());
        }

        return spawned;
    }

    @Test
    public void throwsWhenNotSeeded() {

        assertFalse(dynamicWaveLoader.isInitialized());
        assertThrows(IllegalStateException.class,
            () -> dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 1));
    }

    @Test
    public void rejectsEmptySeedWave() {

        assertThrows(IllegalArgumentException.class,
            () -> dynamicWaveLoader.initializeFromSeedWave(new Queue<>(), SEED_WAVE_NUMBER));
    }

    /**
     * The wave immediately after the seed should be very close to the seed in size, and each
     * wave after that should be at least as large.
     *
     * Growth is deliberately sub-linear in the difficulty multiplier, since the rest of the
     * budget goes into spawn rate. The guard against runaway sizes matters because the
     * original generator doubled the wave on every step, reaching 1024x the seed by wave 110.
     */
    @Test
    public void scalesWaveSizeGraduallyAboveTheSeedWave() {

        int seedSize = 100;
        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(seedSize), SEED_WAVE_NUMBER);

        int previousCount = seedSize;
        for (int wave = SEED_WAVE_NUMBER + 1; wave <= SEED_WAVE_NUMBER + 20; wave++) {
            int count = dynamicWaveLoader.loadWave(LEVEL_NAME, wave).getRemainingEnemyCount();

            assertTrue(count >= previousCount,
                "Wave " + wave + " (" + count + ") should not be smaller than the previous wave");
            assertTrue(count < seedSize * 3,
                "Wave " + wave + " (" + count + ") grew far faster than expected");

            previousCount = count;
        }
    }

    /**
     * Difficulty has to show up as spawn pressure, not just a longer wave. A wave well above
     * the seed should spawn its enemies faster than the seed did.
     */
    @Test
    public void increasesSpawnRateAsWavesProgress() {

        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(50), SEED_WAVE_NUMBER);

        List<SpawningEnemy> earlyWave = drain(dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 1));
        List<SpawningEnemy> lateWave = drain(dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 100));

        assertTrue(lateWave.get(0).getSpawnDelay() < earlyWave.get(0).getSpawnDelay(),
            "Later waves should spawn enemies faster");
        assertTrue(lateWave.size() > earlyWave.size(),
            "Later waves should also contain more enemies");
    }

    /**
     * A wave larger than the seed cycles through the pattern more than once. Each cycle is
     * reshuffled so the wave does not audibly repeat the same ordering back to back.
     */
    @Test
    public void reshufflesEachCycleThroughTheSeedPattern() {

        int seedSize = 30;
        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(seedSize), SEED_WAVE_NUMBER);

        // Far enough above the seed to require several cycles through the pattern.
        List<SpawningEnemy> spawned = drain(dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 200));

        assertTrue(spawned.size() > seedSize * 2, "Test needs a wave spanning multiple cycles");

        StringBuilder firstCycle = new StringBuilder();
        StringBuilder secondCycle = new StringBuilder();
        for (int i = 0; i < seedSize; i++) {
            firstCycle.append(spawned.get(i).getEnemy().getClass().getSimpleName()).append(",");
            secondCycle.append(spawned.get(seedSize + i).getEnemy().getClass().getSimpleName()).append(",");
        }

        assertNotEquals(firstCycle.toString(), secondCycle.toString(),
            "Consecutive cycles through the seed pattern should be reshuffled");
    }

    /**
     * The loader scales from the wave its seed came from, so a wave at or below the seed falls
     * back to the seed difficulty instead of generating an empty wave. An empty wave would
     * complete instantly and hand out the wave-over reward for free.
     */
    @Test
    public void neverGeneratesAnEmptyWave() {

        int seedSize = 40;
        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(seedSize), SEED_WAVE_NUMBER);

        for (int wave : new int[]{1, 50, SEED_WAVE_NUMBER - 10, SEED_WAVE_NUMBER, SEED_WAVE_NUMBER + 1}) {
            Wave generated = dynamicWaveLoader.loadWave(LEVEL_NAME, wave);

            assertTrue(generated.getRemainingEnemyCount() > 0,
                "Wave " + wave + " generated no enemies");
            assertTrue(generated.hasNextEnemy());
        }

        assertEquals(seedSize, dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER)
            .getRemainingEnemyCount());
    }

    /**
     * Sums the actual spawn delays of a wave, which is how long it takes Level to spawn it.
     */
    private double durationOf(Wave wave) {

        // Summed in double so the check measures the cap, not float accumulation drift.
        double duration = 0d;
        for (SpawningEnemy spawningEnemy : drain(wave)) {
            duration += spawningEnemy.getSpawnDelay();
        }

        return duration;
    }

    /**
     * A wave must never drag on, however high the wave number gets. Once the spawn delay
     * floor binds there is no spare rate left to absorb a growing enemy count, so without a
     * cap the wave simply gets longer again.
     */
    @Test
    public void keepsWaveLengthBoundedAtVeryHighWaves() {

        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(100), SEED_WAVE_NUMBER);

        for (int wave : new int[]{SEED_WAVE_NUMBER + 1, 340, 1000, 5000, 20000}) {
            double duration = durationOf(dynamicWaveLoader.loadWave(LEVEL_NAME, wave));

            assertTrue(duration <= DynamicWaveLoader.MAX_WAVE_DURATION_SECONDS,
                "Wave " + wave + " takes " + duration + "s to spawn");
        }
    }

    /**
     * The spawn delay floor is applied per enemy, so on a seed with mixed delays only some
     * entries hit it. Deriving the cap from the scaled-and-clamped mean of the whole pattern
     * is what keeps it exact; clamping the mean instead understates the real average and lets
     * the wave overrun. Authored waves already mix delays, so this is reachable.
     */
    @Test
    public void keepsWaveLengthBoundedForSeedsWithMixedSpawnDelays() {

        Queue<SpawningEnemy> seedWave = new Queue<>();
        for (int i = 0; i < 1000; i++) {
            SpawningEnemy spawningEnemy = new SpawningEnemy(spawningEnemyPool);
            spawningEnemy.setEnemy(TestUtil.createEnemy(EnemyRifle.class, false));
            // Half well below the floor once scaled, half well above it.
            spawningEnemy.setSpawnDelay(i % 2 == 0 ? 0.01f : 0.2f);
            seedWave.addLast(spawningEnemy);
        }

        dynamicWaveLoader.initializeFromSeedWave(seedWave, SEED_WAVE_NUMBER);

        for (int wave : new int[]{SEED_WAVE_NUMBER + 1, 200, 1000}) {
            double duration = durationOf(dynamicWaveLoader.loadWave(LEVEL_NAME, wave));

            assertTrue(duration <= DynamicWaveLoader.MAX_WAVE_DURATION_SECONDS,
                "Wave " + wave + " takes " + duration + "s to spawn");
        }
    }

    /**
     * WHISPERING_THICKET seeds from a 3222 enemy wave, which already runs well past the cap
     * at the seed spawn rate. Its generated waves have to be trimmed to fit.
     */
    @Test
    public void capsWavesSeededFromAnAlreadyOverlongWave() {

        int seedSize = 3222;
        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(seedSize), SEED_WAVE_NUMBER);

        Wave wave = dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 1);
        int enemyCount = wave.getRemainingEnemyCount();

        assertTrue(enemyCount < seedSize,
            "Expected the wave to be trimmed below the seed size, got " + enemyCount);
        assertTrue(durationOf(wave) <= DynamicWaveLoader.MAX_WAVE_DURATION_SECONDS);
    }

    /**
     * Trimming for length must not make a wave less dense than the seed. The wave gets
     * shorter, not easier.
     */
    @Test
    public void cappedWavesAreStillAtLeastAsDenseAsTheSeed() {

        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(3222), SEED_WAVE_NUMBER);

        List<SpawningEnemy> spawned = drain(dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 1));

        assertTrue(spawned.get(0).getSpawnDelay() <= SEED_SPAWN_DELAY,
            "Capped waves should spawn at least as fast as the seed");
    }

    /**
     * Enemies are created as they spawn. Building the whole wave up front stalled the game
     * loop and grew the enemy pool by thousands of actors on high waves.
     */
    @Test
    public void createsEnemiesLazily() {

        dynamicWaveLoader.initializeFromSeedWave(createSeedWave(50), SEED_WAVE_NUMBER);

        Wave wave = dynamicWaveLoader.loadWave(LEVEL_NAME, SEED_WAVE_NUMBER + 100);
        int totalEnemies = wave.getRemainingEnemyCount();

        // Nothing has been built yet, even though the wave knows how big it is.
        assertTrue(totalEnemies > 0);
        verify(combatActorFactory, never()).loadEnemy(anyString(), anyBoolean());

        wave.nextEnemy();
        assertEquals(totalEnemies - 1, wave.getRemainingEnemyCount());
        verify(combatActorFactory, times(1)).loadEnemy(anyString(), anyBoolean());

        drain(wave);
        assertFalse(wave.hasNextEnemy());
        assertEquals(0, wave.getRemainingEnemyCount());
        assertThrows(NoSuchElementException.class, wave::nextEnemy);
    }
}
