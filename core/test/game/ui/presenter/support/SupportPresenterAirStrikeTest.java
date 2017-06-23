package game.ui.presenter.support;

import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.ui.presenter.SupportPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 6/10/2017.
 */

public class SupportPresenterAirStrikeTest extends SupportPresenterTest {

    /**
     * Successfully create an airstrike
     */
    @Test
    public void createAirStrikeTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(AirStrike.COST).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createAirStrike();

        verify(supportView, times(1)).supportState();
        verify(airStrikePlacement, times(1)).createAirStrike();
        verify(uiStateManager, times(1)).setState(eq(GameUIState.PLACING_AIRSTRIKE));

    }

    /**
     * Unsuccessfully create an airstrike - Not enough money
     */
    @Test
    public void createAirStrikeTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(AirStrike.COST - 1).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createAirStrike();

        verify(supportView, times(1)).supportState();
        verify(airStrikePlacement, never()).createAirStrike();
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully create an airstrike - Wrong state
     */
    @Test
    public void createAirStrikeTest3() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(AirStrike.COST).when(player).getMoney();
        doReturn(GameUIState.GAME_OVER).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createAirStrike();

        verify(supportView, times(1)).standByState();
        verify(airStrikePlacement, never()).createAirStrike();
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Successfully place air strike
     */
    @Test
    public void placeAirStrikeTest1() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(false).when(airStrikePlacement).isReadyToBegin();
        doReturn(GameUIState.PLACING_AIRSTRIKE).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        FHDVector2 location = new FHDVector2(5, 10);
        supportPresenter.placeAirStrikeLocation(location);

        verify(supportView, never()).showBtnPlace();
        verify(airStrikePlacement, times(1)).addLocation(eq(location));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully place air strike - No current air strike
     */
    @Test
    public void placeAirStrikeTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(GameUIState.PLACING_AIRSTRIKE).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        FHDVector2 location = new FHDVector2(5, 10);
        supportPresenter.placeAirStrikeLocation(location);

        verify(supportView, never()).showBtnPlace();
        verify(airStrikePlacement, never()).addLocation(any(FHDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully place an air strike - Wrong state
     */
    @Test
    public void placeAirStrikeTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(false).when(airStrikePlacement).isReadyToBegin();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        FHDVector2 location = new FHDVector2(5, 10);
        supportPresenter.placeAirStrikeLocation(location);

        verify(supportView, never()).showBtnPlace();
        verify(airStrikePlacement, never()).addLocation(any(FHDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));


    }

    /**
     * Successfully finish an airstrike
     */
    @Test
    public void finishAirStrikeTest1() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(true).when(airStrikePlacement).isReadyToBegin();
        doReturn(GameUIState.PLACING_AIRSTRIKE).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, times(1)).spendMoney(eq(AirStrike.COST));
        verify(airStrikePlacement, times(1)).finishCurrentAirStrike();
        verify(uiStateManager, times(1)).setStateReturn();

    }

    /**
     * Unsuccessfully finish an airstrike - No current supply drop
     */
    @Test
    public void finishAirStrikeTest2() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(GameUIState.PLACING_AIRSTRIKE).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(airStrikePlacement, never()).finishCurrentAirStrike();
        verify(uiStateManager, never()).setStateReturn();

    }

    /**
     * Unsuccessfully finish an Air Strike - Not ready to begin
     */
    @Test
    public void finishAirStrikeTest3() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(false).when(airStrikePlacement).isReadyToBegin();
        doReturn(GameUIState.PLACING_AIRSTRIKE).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(airStrikePlacement, never()).finishCurrentAirStrike();
        verify(uiStateManager, never()).setStateReturn();

    }

    /**
     * Unsuccessfully finish an Air Strike - Wrong State
     */
    @Test
    public void finishAirStrikeTest4() {
        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(airStrikePlacement).isCurrentAirStrike();
        doReturn(false).when(airStrikePlacement).isReadyToBegin();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(any(Integer.class));
        verify(airStrikePlacement, never()).finishCurrentAirStrike();
        verify(uiStateManager, never()).setStateReturn();
    }
}
