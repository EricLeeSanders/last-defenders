package game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import util.TestUtil;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/29/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EnlistPresenterTest {

    private IEnlistView enlistView = mock(IEnlistView.class);
    private Player playerMock = mock(Player.class);
    private TowerPlacement towerPlacementMock = mock(TowerPlacement.class);
    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);

    @Before
    public void initEnlistPresenterTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    public EnlistPresenter createEnlistPresenter(){

        FHDAudio audioMock = mock(FHDAudio.class);
        IMessageDisplayer messageDisplayerMock = mock(IMessageDisplayer.class);

        EnlistPresenter enlistPresenter = new EnlistPresenter(gameUIStateManagerMock, playerMock, audioMock, towerPlacementMock, messageDisplayerMock);

        return enlistPresenter;
    }

    /**
     * Test creating a tower with the player
     * having enough money.
     */
    @Test
    public void createTowerTest1(){
        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(100000000).when(playerMock).getMoney();
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);

        enlistPresenter.createTower("RocketLauncher");

        verify(gameUIStateManagerMock, times(1)).setState(GameUIState.PLACING_TOWER);

    }

    /**
     * Test creating a tower with the player
     * NOT having enough money.
     */
    @Test
    public void createTowerTest2(){
        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(1).when(playerMock).getMoney();
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);

        enlistPresenter.createTower("RocketLauncher");

        verify(gameUIStateManagerMock, never()).setState(GameUIState.PLACING_TOWER);

    }

    @Test
    public void moveTowerTest1(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.PLACING_TOWER).when(gameUIStateManagerMock).getState();
        enlistPresenter.setView(enlistView);

        Tower tower = TestUtil.createTower("Rifle", false);
        doReturn(tower).when(towerPlacementMock).getCurrentTower();
        doReturn(true).when(towerPlacementMock).isCurrentTower();

        FHDVector2 moveCoords = new FHDVector2(100,100);
        enlistPresenter.moveTower(moveCoords);

        verify(towerPlacementMock, times(1)).moveTower(eq(moveCoords));
        verify(enlistView, times(1)).showBtnPlace();
        verify(enlistView, never()).showBtnRotate();

    }

    @Test
    public void moveTowerTestRotatableTower1(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.PLACING_TOWER).when(gameUIStateManagerMock).getState();
        enlistPresenter.setView(enlistView);

        Tower tower = TestUtil.createTower("Turret", false);
        doReturn(tower).when(towerPlacementMock).getCurrentTower();
        doReturn(true).when(towerPlacementMock).isCurrentTower();

        FHDVector2 moveCoords = new FHDVector2(100,100);
        enlistPresenter.moveTower(moveCoords);

        verify(towerPlacementMock, times(1)).moveTower(eq(moveCoords));
        verify(enlistView, times(1)).showBtnPlace();
        verify(enlistView, times(1)).showBtnRotate();

    }
}
