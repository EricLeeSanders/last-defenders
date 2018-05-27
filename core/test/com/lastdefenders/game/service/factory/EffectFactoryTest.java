package com.lastdefenders.game.service.factory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.VehicleExplosion;
import com.lastdefenders.game.service.factory.EffectFactory;
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

public class EffectFactoryTest {
    @Spy
    private ActorGroups actorGroups = new ActorGroups();
    @Mock
    private Resources resources = TestUtil.createResourcesMock();

    @InjectMocks
    private EffectFactory effectFactory;

    @Before
    public void initEffectFactoryTest() {

        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test loading the {@link BloodSplatter} from the {@link EffectFactory}. Add the effect to the
     * group.
     */
    @Test
    public void loadDeathEffectTest1(){

        BloodSplatter bloodSplatter = effectFactory.loadDeathEffect(DeathEffectType.BLOOD,
            true);

        assertEquals(1, actorGroups.getDeathEffectGroup().getChildren().size);
    }
    /**
     * Test loading the {@link BloodSplatter} from the {@link EffectFactory}. Don't add the
     * effect to the group.
     */
    @Test
    public void loadDeathEffectTest2(){

        BloodSplatter bloodSplatter = effectFactory.loadDeathEffect(DeathEffectType.BLOOD,
            false);

        assertEquals(0, actorGroups.getDeathEffectGroup().getChildren().size);
    }
}
