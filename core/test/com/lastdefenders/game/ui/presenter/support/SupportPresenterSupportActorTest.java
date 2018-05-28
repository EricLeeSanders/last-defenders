package com.lastdefenders.game.ui.presenter.support;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Test;

/**
 * Created by Eric on 6/11/2017.
 */

public class SupportPresenterSupportActorTest extends SupportPresenterTest {

    /**
     * Successfully create a support actor
     */
    @Test
    public void createSupportActorTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(Apache.COST).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(Apache.class);

        verify(supportView, times(1)).supportState();
        verify(supportActorPlacement, times(1)).createSupportActor(eq(Apache.class));
        verify(uiStateManager, times(1)).setState(eq(GameUIState.PLACING_SUPPORT));

    }

    /**
     * Unsuccessfully create a supportActor - Not enough money
     */
    @Test
    public void createSupportActorTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(Apache.COST - 1).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(Apache.class);

        verify(supportView, times(1)).supportState();
        verify(supportActorPlacement, never()).createSupportActor(eq(Apache.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully create a supportActor - Wrong state
     */
    @Test
    public void createSupportActorTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(LandMine.COST).when(player).getMoney();
        doReturn(GameUIState.LEVEL_COMPLETED).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupportActor(LandMine.class);

        verify(supportView, times(1)).standByState();
        verify(supportActorPlacement, never()).createSupportActor(eq(LandMine.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Successfully move a support actor
     */
    @Test
    public void moveSupportActorTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, times(1)).showBtnPlace();
        verify(supportActorPlacement, times(1)).moveSupportActor(eq(location));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully move a support actor - No current support actor
     */
    @Test
    public void moveSupportActorTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, never()).showBtnPlace();
        verify(supportActorPlacement, never()).moveSupportActor(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully move a support actor - Wrong state
     */
    @Test
    public void moveSupportActorTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupportActor(location);

        verify(supportView, never()).showBtnPlace();
        verify(supportActorPlacement, never()).moveSupportActor(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Successfully place a support actor
     */
    @Test
    public void placeSupportActorTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        Apache apache = mock(Apache.class);
        doReturn(Apache.COST).when(apache).getCost();
        doReturn(apache).when(supportActorPlacement).getCurrentSupportActor();
        doReturn(true).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, times(1)).spendMoney(eq(Apache.COST));
        verify(supportActorPlacement, times(1)).placeSupportActor();
        verify(uiStateManager, times(1)).setStateReturn();

    }

    /**
     * Unsuccessfully place a support actor - No current support actor
     */
    @Test
    public void placeSupportActorTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.PLACING_SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(supportActorPlacement, never()).placeSupportActor();
        verify(uiStateManager, never()).setStateReturn();

    }

    /**
     * Unsuccessfully place a Support Actor - Wrong State
     */
    @Test
    public void placeSupportActorTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supportActorPlacement).isCurrentSupportActor();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(supportActorPlacement, never()).placeSupportActor();
        verify(uiStateManager, never()).setStateReturn();

    }
}
