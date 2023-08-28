package com.lastdefenders.game.model.level.wave;

import static org.mockito.Mockito.mock;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.CombatActorFactory.SpawningEnemyPool;
import org.junit.jupiter.api.Test;

/**
 * Created by Eric on 5/26/2017.
 */

public class DynamicWaveLoaderTest {

    private SpawningEnemyPool spawningEnemyPool = mock(SpawningEnemyPool.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    private Map map = mock(Map.class);

    @Test
    public void dynamicWaveLoaderTest1() {


    }
}
