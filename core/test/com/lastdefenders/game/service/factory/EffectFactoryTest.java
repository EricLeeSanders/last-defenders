package com.lastdefenders.game.service.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.ResourcesMock;

/**
 * Created by Eric on 5/23/2018.
 */

public class EffectFactoryTest {
    @Spy
    private ActorGroups actorGroups = new ActorGroups();
    @Mock
    private Resources resources = ResourcesMock.create();

    @InjectMocks
    private EffectFactory effectFactory;

    private AutoCloseable closeable;

    @BeforeAll
    public static void initEffectFactoryTest() {

        Gdx.app = mock(Application.class);
    }

    @BeforeEach
    public void startMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    /**
     * Test loading the {@link BloodSplatter} from the {@link EffectFactory}. Add the effect to the
     * group.
     */
    @Test
    public void loadDeathEffectTest1(){

        effectFactory.loadDeathEffect(DeathEffectType.BLOOD,
            true);

        assertEquals(1, actorGroups.getDeathEffectGroup().getChildren().size);
    }
    /**
     * Test loading the {@link BloodSplatter} from the {@link EffectFactory}. Don't add the
     * effect to the group.
     */
    @Test
    public void loadDeathEffectTest2(){

         effectFactory.loadDeathEffect(DeathEffectType.BLOOD,
            false);

        assertEquals(0, actorGroups.getDeathEffectGroup().getChildren().size);
    }
}
