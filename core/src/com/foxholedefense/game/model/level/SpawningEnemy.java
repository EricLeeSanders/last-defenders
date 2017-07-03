package com.foxholedefense.game.model.level;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.service.factory.CombatActorFactory.SpawningEnemyPool;

public class SpawningEnemy implements Poolable {

    private SpawningEnemyPool spawningEnemyPool;

    private Enemy enemy;
    private float spawnDelay;

    public SpawningEnemy(SpawningEnemyPool spawningEnemyPool) {
        this.spawningEnemyPool = spawningEnemyPool;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public float getSpawnDelay() {
        return spawnDelay;
    }

    public void setSpawnDelay(float spawnDelay) {
        this.spawnDelay = spawnDelay;
    }

    public void free() {
        spawningEnemyPool.free(this);
    }

    @Override
    public void reset() {
        enemy = null;
        spawnDelay = 0;
    }
}
