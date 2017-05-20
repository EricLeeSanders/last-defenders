package game.model.actor.combat.event;

import com.foxholedefense.game.model.actor.combat.event.EventManagerImpl;
import com.foxholedefense.game.model.actor.combat.event.interfaces.EventManager.CombatActorEventEnum;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EventManagerImplTest {

    @Before
    public void initEventManagerImplTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void sendEventTest(){
        Tower tower = TestUtil.createTower("Rifle", true);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        ArmorDestroyedEffect armorDestroyedEffectMock = mock(ArmorDestroyedEffect.class);
        doReturn(armorDestroyedEffectMock).when(effectFactoryMock).loadLabelEffect(eq(ArmorDestroyedEffect.class));

        EventManagerImpl eventManager = new EventManagerImpl(tower, effectFactoryMock);
        eventManager.sendEvent(CombatActorEventEnum.ARMOR_DESTROYED);

        verify(armorDestroyedEffectMock, times(1)).initialize(eq(tower));
    }

}
