package com.lastdefenders.game.service.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.health.ArmorIcon;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2018.
 */

public class HealthFactoryTest {
    @Spy
    private ActorGroups actorGroups = new ActorGroups();
    @Mock
    private Resources resources = TestUtil.createResourcesMock();

    @InjectMocks
    private HealthFactory healthFactory;

    @Before
    public void initHealthFactoryTest() {

        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test loading the {@link ArmorIcon} from the {@link HealthFactory}.
     */
    @Test
    public void loadArmorIconTest1(){

        ArmorIcon armorIcon = healthFactory.loadArmorIcon();

        assertNotNull(armorIcon);
        assertEquals(1, actorGroups.getHealthGroup().getChildren().size);
    }
    /**
     * Test loading the {@link HealthBar} from the {@link HealthFactory}.
     */
    @Test
    public void loadHealthBarTest2(){

        HealthBar healthBar = healthFactory.loadHealthBar();

        assertNotNull(healthBar);
        assertEquals(1, actorGroups.getHealthGroup().getChildren().size);
    }
}
