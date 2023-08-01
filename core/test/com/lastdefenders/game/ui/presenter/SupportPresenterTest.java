package com.lastdefenders.game.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.AirStrikeTest;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.MessageDisplayerImpl;
import com.lastdefenders.game.ui.view.SupportView;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by Eric on 6/10/2017.
 */

public class SupportPresenterTest {

    GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    Player player = mock(Player.class);
    SupportActorPlacement supportActorPlacement = mock(SupportActorPlacement.class);
    SupportView supportView = mock(SupportView.class);
    Viewport gameViewportMock = mock(Viewport.class);

    SupportPresenter createSupportPresenter() {

        LDAudio audio = mock(LDAudio.class);
        MessageDisplayerImpl messageDisplayer = mock(MessageDisplayerImpl.class);

        return new SupportPresenter(uiStateManager, player, audio, supportActorPlacement, messageDisplayer, gameViewportMock);
    }

    @BeforeAll
    public static void initSupportPresenterTest() {

        Gdx.app = mock(Application.class);
    }

    /**
     * Successfully create an airstrike
     */
    @Test
    public void successfullyCreateAirStrike() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(AirStrike.class);

        verify(supportView, times(1)).supportState();
        verify(supportActorPlacement, times(1)).createSupportActor(eq(AirStrike.class));
        verify(uiStateManager, times(1)).setState(eq(GameUIState.PLACING_SUPPORT));

    }

    /**
     * Unsuccessfully create an airstrike - Not enough money
     */
    @Test
    public void airStrikeInsufficientMoney() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.INSUFFICIENT_MONEY).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(AirStrike.class);

        verify(supportView, times(1)).supportState();
        verify(supportActorPlacement, never()).createSupportActor(eq(AirStrike.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully create an airstrike - On Cooldown
     */
    @Test
    public void airStrikeOnCooldown() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.ON_COOLDOWN).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(AirStrike.class);

        verify(supportView, times(1)).supportState();
        verify(supportActorPlacement, never()).createSupportActor(eq(AirStrike.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }


    /**
     * Successfully place air strike
     */
    @Test
    public void successfullyPlaceAirStrike() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, never()).showBtnPlace();
        verify(supportActorPlacement, times(1)).setPlacement(eq(location));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully place air strike - No current air strike
     */
    @Test
    public void placeAirStrikeNoAirStrike() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(false).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, never()).showBtnPlace();
        verify(supportActorPlacement, never()).setPlacement(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully place an air strike - Wrong state
     */
    @Test
    public void placeAirStrikeWrongState() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, never()).showBtnPlace();
        verify(supportActorPlacement, never()).setPlacement(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));


    }

    /**
     * Successfully finish an airstrike
     */
    @Test
    public void finishAirStrikeTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();

        AirStrike airStrike = new AirStrikeTest().createAirStrike();

        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(airStrike).when(supportActorPlacement).getCurrentSupportActor();

        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.finishPlacement();

        verify(player, times(1)).spendMoney(eq(airStrike.getCost()));
        verify(supportActorPlacement, times(1)).finish();
        verify(uiStateManager, times(1)).setStateReturn();

    }

    /**
     * Unsuccessfully finish an airstrike - no current support actor
     */
    @Test
    public void finishAirStrikeNoAirStrike() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(false).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.finishPlacement();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(supportActorPlacement, never()).finish();
        verify(uiStateManager, never()).setStateReturn();

    }

    /**
     * Unsuccessfully finish an Air Strike - Wrong State
     */
    @Test
    public void finishAirStrikeWrongState() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(ValidationResponseEnum.OK).when(supportActorPlacement).canCreateSupport(eq(AirStrike.class));
        doReturn(true).when(supportActorPlacement).supportActorValidState();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.finishPlacement();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(supportActorPlacement, never()).finish();
        verify(uiStateManager, never()).setStateReturn();
    }

}
