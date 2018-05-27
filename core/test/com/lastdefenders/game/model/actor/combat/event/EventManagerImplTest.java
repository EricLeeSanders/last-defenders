package com.lastdefenders.game.model.actor.combat.event;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.combat.event.EventManagerImpl;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager.CombatActorEventEnum;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class EventManagerImplTest {

    @Before
    public void initEventManagerImplTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void sendEventTest() {

        Tower tower = TestUtil.createTower("Rifle", true);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        ArmorDestroyedEffect armorDestroyedEffectMock = mock(ArmorDestroyedEffect.class);
        doReturn(armorDestroyedEffectMock).when(effectFactoryMock)
            .loadEffect(eq(ArmorDestroyedEffect.class), isA(Boolean.class));

        EventManagerImpl eventManager = new EventManagerImpl(tower, effectFactoryMock);
        eventManager.sendEvent(CombatActorEventEnum.ARMOR_DESTROYED);

        verify(armorDestroyedEffectMock, times(1)).initialize(eq(tower));
    }
}
