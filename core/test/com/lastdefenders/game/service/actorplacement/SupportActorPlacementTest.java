package com.lastdefenders.game.service.actorplacement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.ApacheTest;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.LandMineTest;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/28/2017.
 */
public class SupportActorPlacementTest {

    private ActorGroups actorGroups = mock(ActorGroups.class);
    private SupportActorFactory supportActorFactory = mock(SupportActorFactory.class);

    @Before
    public void initSupportActorPlacementTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void successfulApachePlacement() {

        Apache apache = mock(Apache.class);

        SupportActorValidator apacheValidatorMock = mock(SupportActorValidator.class);

        Map<Class<? extends SupportActor>, SupportActorValidator> validatorMap = new HashMap<>();
        validatorMap.put(apache.getClass(), apacheValidatorMock);

        doReturn(ValidationResponseEnum.OK).when(apacheValidatorMock).canCreateSupportActor();

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(supportActorFactory, validatorMap);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        doReturn(apache).when(supportActorFactory).loadSupportActor(eq(apache.getClass()), eq(true));

        supportActorPlacement.createSupportActor(apache.getClass());

        verify(supportActorFactory, times(1)).loadSupportActor(eq(apache.getClass()), eq(true));
        verify(apache, times(1)).initialize();
        // Move apache
        LDVector2 moveCoords = new LDVector2(400, 200);
        supportActorPlacement.setPlacement(moveCoords);

        verify(apache, times(1)).setPlacement(eq(moveCoords));

        // Place apache
        supportActorPlacement.finish();

        verify(apache, times(1)).ready();

    }

    @Test
    public void successfulAirStrikePlacement() {

        AirStrike airStrike = mock(AirStrike.class);

        SupportActorValidator airStrikeValidatorMock = mock(SupportActorValidator.class);

        Map<Class<? extends SupportActor>, SupportActorValidator> validatorMap = new HashMap<>();
        validatorMap.put(airStrike.getClass(), airStrikeValidatorMock);

        doReturn(ValidationResponseEnum.OK).when(airStrikeValidatorMock).canCreateSupportActor();

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(supportActorFactory, validatorMap);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        doReturn(airStrike).when(supportActorFactory).loadSupportActor(eq(airStrike.getClass()), eq(true));

        supportActorPlacement.createSupportActor(airStrike.getClass());

        verify(supportActorFactory, times(1)).loadSupportActor(eq(airStrike.getClass()), eq(true));
        verify(airStrike, times(1)).initialize();

        // set airstrike placements
        LDVector2 firstPlacement = new LDVector2(400, 200);
        supportActorPlacement.setPlacement(firstPlacement);

        LDVector2 secondPlacement = new LDVector2(200, 100);
        supportActorPlacement.setPlacement(secondPlacement);

        LDVector2 thirdPlacement = new LDVector2(100, 50);
        supportActorPlacement.setPlacement(thirdPlacement);

        verify(airStrike, times(3)).setPlacement(isA(LDVector2.class));

        // Place apache
        supportActorPlacement.finish();

        verify(airStrike, times(1)).ready();

    }

    @Test(expected=IllegalStateException.class)
    public void unsuccessfulApachePlacement() {

        Apache apache = mock(Apache.class);

        SupportActorValidator apacheValidatorMock = mock(SupportActorValidator.class);

        Map<Class<? extends SupportActor>, SupportActorValidator> validatorMap = new HashMap<>();
        validatorMap.put(apache.getClass(), apacheValidatorMock);

        doReturn(ValidationResponseEnum.ON_COOLDOWN).when(apacheValidatorMock).canCreateSupportActor();

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(supportActorFactory, validatorMap);


        supportActorPlacement.createSupportActor(apache.getClass());
        verify(apache, never()).initialize();
        verify(apache, never()).ready();
    }
}
