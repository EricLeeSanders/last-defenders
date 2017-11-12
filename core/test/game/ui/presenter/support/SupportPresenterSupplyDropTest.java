package game.ui.presenter.support;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.model.actor.support.SupplyDropCrate;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Test;

/**
 * Created by Eric on 6/10/2017.
 */

public class SupportPresenterSupplyDropTest extends SupportPresenterTest {

    /**
     * Successfully create a supply drop
     */
    @Test
    public void createSupplyDropTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(SupplyDropCrate.COST).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupplyDrop();

        verify(supportView, times(1)).supportState();
        verify(supplyDropPlacement, times(1)).createSupplyDrop();
        verify(uiStateManager, times(1)).setState(eq(GameUIState.PLACING_SUPPLYDROP));

    }

    /**
     * Unsuccessfully create a supply drop - Not enough money
     */
    @Test
    public void createSupplyDropTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(SupplyDropCrate.COST - 1).when(player).getMoney();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupplyDrop();

        verify(supportView, times(1)).supportState();
        verify(supplyDropPlacement, never()).createSupplyDrop();
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully create a supply drop - Wrong state
     */
    @Test
    public void createSupplyDropTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(SupplyDropCrate.COST).when(player).getMoney();
        doReturn(GameUIState.GAME_OVER).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.createSupplyDrop();

        verify(supportView, times(1)).standByState();
        verify(supplyDropPlacement, never()).createSupplyDrop();
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Successfully move a supply drop
     */
    @Test
    public void moveSupplyDropTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.PLACING_SUPPLYDROP).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupplyDrop(location);

        verify(supportView, times(1)).showBtnPlace();
        verify(supplyDropPlacement, times(1)).setLocation(eq(location));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully move a supply drop - No current supply drop
     */
    @Test
    public void moveSupplyDropTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.PLACING_SUPPLYDROP).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupplyDrop(location);

        verify(supportView, never()).showBtnPlace();
        verify(supplyDropPlacement, never()).setLocation(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Unsuccessfully move a supply drop - Wrong state
     */
    @Test
    public void moveSupplyDropTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.SUPPORT).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        LDVector2 location = new LDVector2(5, 10);
        supportPresenter.moveSupplyDrop(location);

        verify(supportView, never()).showBtnPlace();
        verify(supplyDropPlacement, never()).setLocation(any(LDVector2.class));
        verify(uiStateManager, never()).setState(any(GameUIState.class));

    }

    /**
     * Successfully place a supply drop
     */
    @Test
    public void placeSupplyDropTest1() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.PLACING_SUPPLYDROP).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, times(1)).spendMoney(eq(SupplyDropCrate.COST));
        verify(supplyDropPlacement, times(1)).placeSupplyDrop();
        verify(uiStateManager, times(1)).setStateReturn();

    }

    /**
     * Unsuccessfully place a supply drop - No current supply drop
     */
    @Test
    public void placeSupplyDropTest2() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(false).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.PLACING_SUPPLYDROP).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(eq(SupplyDropCrate.COST));
        verify(supplyDropPlacement, never()).placeSupplyDrop();
        verify(uiStateManager, never()).setStateReturn();

    }

    /**
     * Unsuccessfully place a supply drop - Wrong State
     */
    @Test
    public void placeSupplyDropTest3() {

        SupportPresenter supportPresenter = createSupportPresenter();
        doReturn(true).when(supplyDropPlacement).isCurrentSupplyDropCrate();
        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();

        supportPresenter.setView(supportView);
        supportPresenter.placeActor();

        verify(player, never()).spendMoney(eq(SupplyDropCrate.COST));
        verify(supplyDropPlacement, never()).placeSupplyDrop();
        verify(uiStateManager, never()).setStateReturn();

    }
}
