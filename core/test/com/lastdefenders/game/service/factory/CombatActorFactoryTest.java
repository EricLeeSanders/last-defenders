package com.lastdefenders.game.service.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testutil.TestUtil;

/**
 * Created by Eric on 5/25/2018.
 */
@TestInstance(Lifecycle.PER_CLASS)
public class CombatActorFactoryTest {

    //Can't exactly figure out why @Spy annotation won't work here.
    //It must be due to the fact that the spied objects need to be used
    // in the init, but it's not instantiated as a Spy in time if using annotations?
    private ActorGroups actorGroups = spy(new ActorGroups());
    private Resources resources = spy(TestUtil.getResources());
    @Mock private EffectFactory effectFactory;
    @Mock private ProjectileFactory projectileFactory;
    @Mock private HealthFactory healthFactory;
    @Mock private Player player;
    @Mock private HealthBar healthBar;

    @InjectMocks
    private CombatActorFactory combatActorFactory;

    private AutoCloseable closeable;

    @BeforeAll
    public void init(){
        Gdx.app = mock(Application.class);

        resources.loadActorAtlasRegions();

        Group healthGroup = spy(actorGroups.getHealthGroup());
        doReturn(healthGroup).when(actorGroups).getHealthGroup();
        doNothing().when(healthGroup).addActor(any());
    }
    @BeforeEach
    public void startMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void loadTowerTest(){

        Tower tower = combatActorFactory.loadTower("Sniper", false);

        assertNotNull(tower);
    }

    @Test
    public void loadEnemyTest(){

        Enemy enemy = combatActorFactory.loadEnemy("Humvee", false);

        assertNotNull(enemy);
    }

}
