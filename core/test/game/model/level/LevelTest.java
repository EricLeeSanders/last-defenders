package game.model.level;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Queue;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.SpawningEnemy;
import com.foxholedefense.game.model.level.wave.impl.DynamicWaveLoader;
import com.foxholedefense.game.model.level.wave.impl.FileWaveLoader;
import com.foxholedefense.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import com.foxholedefense.game.service.factory.HealthFactory;

import org.junit.Before;
import org.junit.Test;


import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 5/26/2017.
 */

public class LevelTest {

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private FileWaveLoader fileWaveLoader = mock(FileWaveLoader.class);
    private DynamicWaveLoader dynamicWaveLoader = mock(DynamicWaveLoader.class);
    private ActorGroups actorGroups = mock(ActorGroups.class);
    private HealthFactory healthFactory = mock(HealthFactory.class);

    @Before
    public void initLevelTest() {
        Gdx.app = mock(Application.class);
    }

    @Test
    public void levelTest1(){
        Level level = new Level(1, actorGroups, healthFactory, fileWaveLoader, dynamicWaveLoader);

        Queue<SpawningEnemy> loadedEnemies = new Queue<SpawningEnemy>();
        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Tank", false);
        Enemy enemy3 = TestUtil.createEnemy("Humvee", false);
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

        doReturn(loadedEnemies).when(fileWaveLoader).loadWave(isA(Integer.class), isA(Integer.class));
        doReturn(loadedEnemies).when(dynamicWaveLoader).loadWave(isA(Integer.class), isA(Integer.class));
        doReturn(new Group()).when(actorGroups).getEnemyGroup();
        HealthBar healthBarMock = mock(HealthBar.class);
        doReturn(healthBarMock).when(healthFactory).loadHealthBar();
        ArmorIcon armorIconMock = mock(ArmorIcon.class);
        doReturn(armorIconMock).when(healthFactory).loadArmorIcon();

        // Calls FileWaveLoader and DynamicWaveLoader
        for(int i = 0; i <= Level.MAX_WAVES + 1; i++){
            level.loadNextWave();
            for(int j = level.getSpawningEnemiesCount()-1; j >= 0; j--){
                level.update(spawnDelay);
                assertEquals(j, level.getSpawningEnemiesCount());
            }
        }

    }
}
