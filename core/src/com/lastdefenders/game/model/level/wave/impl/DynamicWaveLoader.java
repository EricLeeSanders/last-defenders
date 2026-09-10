package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.Wave;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Dynamically generates waves based on a seed wave pattern.
 * Uses linear difficulty scaling instead of exponential growth.
 *
 * Difficulty increases by 10% per wave past the seed wave. That difficulty budget is split
 * evenly between the number of enemies and how fast they spawn, so a wave gets both bigger
 * and denser rather than merely longer. Scaling only the enemy count would leave every wave
 * spawning at the seed wave's rate and simply run longer.
 *
 * For example, with a seed wave of 100 enemies at a 0.1s spawn delay:
 * - Wave 101: 110% difficulty -> 105 enemies at 0.095s
 * - Wave 110: 200% difficulty -> 141 enemies at 0.071s
 * - Wave 200: 1100% difficulty -> 332 enemies at the 1/30s spawn floor
 *
 * Wave length is capped at {@link #MAX_WAVE_DURATION_SECONDS}. Because enemy strength itself
 * never scales here, difficulty stops climbing once both that cap and
 * {@link #MIN_SPAWN_DELAY} bind: there is no spare room in either the wave length or the
 * spawn rate. Raising difficulty past that point needs the enemies themselves to get harder.
 *
 * Enemies are created on demand as they spawn rather than all at once, so a high wave
 * does not stall the game loop or grow the enemy pool by thousands of actors.
 *
 * @author Eric
 */
public class DynamicWaveLoader extends AbstractWaveLoader {

    private static final float DIFFICULTY_INCREASE_PER_WAVE = 0.10f; // 10% increase per wave

    /**
     * Enemies spawn no faster than this, so that difficulty scaling cannot drive the spawn
     * delay towards zero and put an unbounded number of enemies on screen at once.
     *
     * Level spawns at most one enemy per frame and restarts its delay counter after each
     * spawn, so a requested delay is rounded up to a whole frame. A 1/30s floor is the
     * shortest delay that costs what it asks for at both 30fps (one frame) and 60fps (two),
     * which keeps MAX_WAVE_DURATION_SECONDS honest. A smaller floor would be silently rounded
     * up and make every capped wave run longer than the cap allows.
     */
    private static final float MIN_SPAWN_DELAY = 1f / 30f;

    /**
     * A generated wave never takes longer than this to spawn, however high the wave number.
     *
     * Without a cap, wave length climbs again once MIN_SPAWN_DELAY binds and there is no
     * spare spawn rate left to absorb the growing enemy count.
     */
    public static final float MAX_WAVE_DURATION_SECONDS = 210f; // 3.5 minutes

    private Array<SpawningEnemySnapshot> seedWavePattern;
    private int seedWaveNumber;

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map) {
        super(combatActorFactory, map);
    }

    @Override
    public Wave loadWave(LevelName levelName, int wave) {
        Logger.info("DynamicWaveLoader: Generating Wave " + wave);

        if (!isInitialized()) {
            throw new IllegalStateException(
                "DynamicWaveLoader: Must be initialized with a seed wave first. Call initializeFromSeedWave().");
        }

        // Difficulty scales off the wave the seed was taken from. Waves at or below the seed
        // fall back to the seed difficulty rather than generating an empty wave.
        int wavesAboveSeed = Math.max(0, wave - seedWaveNumber);
        float difficultyMultiplier = 1.0f + (wavesAboveSeed * DIFFICULTY_INCREASE_PER_WAVE);

        // Split the difficulty budget evenly between wave size and spawn rate.
        float countMultiplier = (float) Math.sqrt(difficultyMultiplier);
        float spawnDelayMultiplier = 1.0f / countMultiplier;

        int baseEnemyCount = seedWavePattern.size;
        int scaledEnemyCount = Math.max(1, Math.round(baseEnemyCount * countMultiplier));

        // Hold the wave to a bounded length. Difficulty is expressed as spawn density, so
        // trimming the tail keeps the pressure while stopping the wave from dragging on.
        int maxEnemyCount = maxEnemyCountWithin(MAX_WAVE_DURATION_SECONDS, spawnDelayMultiplier);
        int enemyCount = Math.min(scaledEnemyCount, maxEnemyCount);

        Logger.info("DynamicWaveLoader: Base enemies: " + baseEnemyCount +
            ", Scaled enemies: " + enemyCount +
            " (difficulty multiplier: " + String.format("%.2f", difficultyMultiplier) + "x" +
            ", spawn delay multiplier: " + String.format("%.2f", spawnDelayMultiplier) + "x)");

        if (enemyCount < scaledEnemyCount) {
            Logger.info("DynamicWaveLoader: Wave " + wave + " capped from " + scaledEnemyCount
                + " to " + enemyCount + " enemies to stay within "
                + String.format("%.0f", MAX_WAVE_DURATION_SECONDS) + "s");
        }

        return new GeneratedWave(seedWavePattern, enemyCount, spawnDelayMultiplier);
    }

    /**
     * Initializes the dynamic wave loader with a seed wave pattern.
     * This pattern will be used as the basis for all dynamically generated waves.
     * Must be called before loadWave() can be used.
     *
     * @param seedWave The wave to use as a pattern for dynamic generation
     * @param seedWaveNumber The wave number the seed was taken from. Difficulty scaling is
     * measured from this wave, so the loader does not need to know where the file waves end.
     */
    public void initializeFromSeedWave(Queue<SpawningEnemy> seedWave, int seedWaveNumber) {
        Logger.info("DynamicWaveLoader: Initializing with seed wave " + seedWaveNumber
            + " of " + seedWave.size + " enemies");

        if (seedWave.size == 0) {
            throw new IllegalArgumentException("DynamicWaveLoader: Seed wave cannot be empty.");
        }

        Array<SpawningEnemySnapshot> seedSnapshot = new Array<>();

        // Create snapshots of the seed wave pattern
        for (SpawningEnemy spawningEnemy : seedWave) {
            SpawningEnemySnapshot snapshot = new SpawningEnemySnapshot(spawningEnemy);
            seedSnapshot.add(snapshot);
        }

        this.seedWavePattern = seedSnapshot;
        this.seedWaveNumber = seedWaveNumber;
    }

    public boolean isInitialized() {

        return seedWavePattern != null && seedWavePattern.size > 0;
    }

    /**
     * The most enemies that can be spawned within a time budget, whatever order they come in.
     *
     * Two things stop a simple budget/averageDelay from being right. MIN_SPAWN_DELAY applies
     * per enemy, so on a mixed-delay seed only some entries hit the floor and clamping the
     * mean understates the real average. And a wave that ends part way through a cycle takes
     * a random subset of the pattern, which can be weighted towards the slower enemies.
     *
     * Whole cycles cost exactly one cycle each. The leftover is charged at the slowest delays
     * in the pattern, so the bound holds for every possible shuffle rather than on average.
     */
    private int maxEnemyCountWithin(float budgetSeconds, float spawnDelayMultiplier) {

        float[] delays = new float[seedWavePattern.size];
        // Accumulated in double: summing thousands of floats drifts by enough to let one
        // extra enemy through, and MIN_SPAWN_DELAY itself rounds up in float.
        double cycleDuration = 0d;

        for (int i = 0; i < seedWavePattern.size; i++) {
            delays[i] = Math.max(MIN_SPAWN_DELAY,
                seedWavePattern.get(i).getSpawnDelay() * spawnDelayMultiplier);
            cycleDuration += delays[i];
        }

        Arrays.sort(delays);

        int count = 0;
        double remainingBudget = budgetSeconds;

        if (cycleDuration > 0d) {
            int wholeCycles = (int) (remainingBudget / cycleDuration);
            count += wholeCycles * delays.length;
            remainingBudget -= wholeCycles * cycleDuration;
        }

        // Charge the partial cycle at the slowest delays first, worst case for the budget.
        for (int i = delays.length - 1; i >= 0 && remainingBudget >= delays[i]; i--) {
            remainingBudget -= delays[i];
            count++;
        }

        return Math.max(1, count);
    }

    /**
     * A wave generated from the seed pattern. Enemies are created as they are requested so
     * that the whole wave is never resident at once.
     *
     * The pattern is reshuffled at the start of every cycle through it, otherwise a wave that
     * is several times the size of the seed would replay the same ordering back to back.
     */
    private class GeneratedWave implements Wave {

        private final Array<SpawningEnemySnapshot> pattern;
        private final float spawnDelayMultiplier;
        private int remaining;
        private int indexInCycle;

        private GeneratedWave(Array<SpawningEnemySnapshot> seedWavePattern, int enemyCount,
            float spawnDelayMultiplier) {

            this.pattern = new Array<>(seedWavePattern);
            this.remaining = enemyCount;
            this.spawnDelayMultiplier = spawnDelayMultiplier;
            this.indexInCycle = 0;

            this.pattern.shuffle();
        }

        @Override
        public boolean hasNextEnemy() {

            return remaining > 0;
        }

        @Override
        public SpawningEnemy nextEnemy() {

            if (!hasNextEnemy()) {
                // Matches what PreloadedWave propagates from Queue.removeFirst().
                throw new NoSuchElementException("GeneratedWave: No enemies left to spawn.");
            }

            if (indexInCycle >= pattern.size) {
                pattern.shuffle();
                indexInCycle = 0;
            }

            SpawningEnemySnapshot snapshot = pattern.get(indexInCycle);
            indexInCycle++;
            remaining--;

            float spawnDelay = Math.max(MIN_SPAWN_DELAY,
                snapshot.getSpawnDelay() * spawnDelayMultiplier);

            return loadSpawningEnemy(snapshot.getName(), snapshot.hasArmor(), spawnDelay);
        }

        @Override
        public int getRemainingEnemyCount() {

            return remaining;
        }
    }

    /**
     * Creates a Snapshot of a SpawningEnemy. This is important because the SpawningEnemy is reset after each wave.
     */
    private static class SpawningEnemySnapshot {

        private final String name;
        private final float spawnDelay;
        private final boolean armor;

        SpawningEnemySnapshot(SpawningEnemy spawningEnemy) {

            this.name = spawningEnemy.getEnemy().getClass().getSimpleName().split("Enemy")[1];
            this.spawnDelay = spawningEnemy.getSpawnDelay();
            this.armor = spawningEnemy.getEnemy().hasArmor();
        }

        String getName() {

            return name;
        }

        float getSpawnDelay() {

            return spawnDelay;
        }

        boolean hasArmor() {

            return armor;
        }
    }
}
