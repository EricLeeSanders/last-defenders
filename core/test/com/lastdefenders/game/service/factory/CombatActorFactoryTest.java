package com.lastdefenders.game.service.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory;
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
    @Mock private Resources resources = TestUtil.createResourcesMock();
    @Mock private LDAudio audio;
    @Mock private EffectFactory effectFactory;
    @Mock private ProjectileFactory projectileFactory;
    @Mock private Player player;

    @InjectMocks
    private CombatActorFactory combatActorFactory;

    @Before
    public void initCombatActorFactoryTest() {
        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
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
