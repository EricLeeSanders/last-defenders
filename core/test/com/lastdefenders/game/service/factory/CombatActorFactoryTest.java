package com.lastdefenders.game.service.factory;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.TestUtil;

/**
 * Created by Eric on 5/25/2018.
 */

public class CombatActorFactoryTest {

    @Spy private ActorGroups actorGroups = new ActorGroups();
    @Spy private Resources resources = TestUtil.getResources();
    @Mock private LDAudio audio;
    @Mock private EffectFactory effectFactory;
    @Mock private ProjectileFactory projectileFactory;
    @Mock private HealthFactory healthFactory;
    @Mock private Player player;

    @InjectMocks
    private CombatActorFactory combatActorFactory;

    @Before
    public void initCombatActorFactoryTest() {
        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);

        resources.loadActorAtlasRegions();

        Group healthGroup = spy(actorGroups.getHealthGroup());
        doReturn(healthGroup).when(actorGroups).getHealthGroup();
        doNothing().when(healthGroup).addActor(any(Actor.class));
    }

    @Test
    public void loadTowerTest(){

        Tower tower = combatActorFactory.loadTower("Sniper");

        assertNotNull(tower);
    }

    @Test
    public void loadEnemyTest(){

        Enemy enemy = combatActorFactory.loadEnemy("Humvee");

        assertNotNull(enemy);
    }

}
