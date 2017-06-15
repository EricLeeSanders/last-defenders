package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.ui.presenter.EnlistPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IEnlistView;
import com.foxholedefense.game.ui.view.interfaces.MessageDisplayer;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import testutil.TestUtil;


import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/29/2017.
 */
@RunWith(DataProviderRunner.class)
public class EnlistPresenterTest {

    private IEnlistView enlistView = mock(IEnlistView.class);
    private Player playerMock = mock(Player.class);
    private TowerPlacement towerPlacementMock = mock(TowerPlacement.class);
    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);

    @Before
    public void initEnlistPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    public EnlistPresenter createEnlistPresenter(){

        FHDAudio audioMock = mock(FHDAudio.class);
        MessageDisplayer messageDisplayerMock = mock(MessageDisplayer.class);

        EnlistPresenter enlistPresenter = new EnlistPresenter(gameUIStateManagerMock, playerMock, audioMock, towerPlacementMock, messageDisplayerMock);

        return enlistPresenter;
    }

    @DataProvider
    public static Object[][] filteredGameUIStateEnums() {

        Object[][] gameUIStateEnums = new Object[GameUIState.values().length-2][1];

        int count = 0;
        for(GameUIState state : GameUIState.values()){
            if(state == GameUIState.ENLISTING || state == GameUIState.PLACING_TOWER){
                continue;
            }
            gameUIStateEnums[count][0] = state;
            count++;
        }

        return gameUIStateEnums;
    }

    /**
     * State change to Enlisting
     */
    @Test
    public void stateChangeTest1(){
        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);
        enlistPresenter.stateChange(GameUIState.ENLISTING);

        verify(enlistView, times(1)).standByState();
        verify(enlistView, times(1)).enlistingState();
    }

    /**
     * State change to Placing Tower
     */
    @Test
    public void stateChangeTest2(){
        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);
        enlistPresenter.stateChange(GameUIState.PLACING_TOWER);

        verify(enlistView, times(1)).standByState();
        verify(enlistView, times(1)).placingTowerState();
    }

    /**
     * Test with starting state as Standby and change to all other states excluding
     * Enlisting and Placing Tower
     */
    // Bit of an overkill... But I wanted to use a dataprovider at least once
    @Test
    @UseDataProvider( "filteredGameUIStateEnums" )
    public void stateChangeTest3(GameUIState state){
        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);
        enlistPresenter.stateChange(state);

        verify(enlistView, times(2)).standByState();
        verify(towerPlacementMock, times(2)).removeCurrentTower(eq(true));
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

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));

    }

    /**
     * Test creating a tower without being in ENLISTING state
     */
    @Test
    public void createTowerTest3(){
        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(1).when(playerMock).getMoney();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        enlistPresenter.setView(enlistView);

        enlistPresenter.createTower("RocketLauncher");

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));

    }

    /**
     * Test placing a tower
     */
    @Test
    public void placeTowerTest1(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();
        Tower towerMock = mock(Tower.class);
        doReturn(GameUIState.PLACING_TOWER).when(gameUIStateManagerMock).getState();
        doReturn(true).when(towerPlacementMock).isCurrentTower();
        doReturn(true).when(towerPlacementMock).placeTower();
        doReturn(towerMock).when(towerPlacementMock).getCurrentTower();
        doReturn(1000).when(towerMock).getCost();

        enlistPresenter.setView(enlistView);
        enlistPresenter.placeTower();

        verify(gameUIStateManagerMock, times(1)).setStateReturn();
        verify(playerMock, times(1)).spendMoney(eq(1000));

    }

    /**
     * Unsuccessfully place tower, state != PLACING_TOWER
     */
    @Test
    public void placeTowerTest2(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(GameUIState.LEVEL_COMPLETED).when(gameUIStateManagerMock).getState();
        doReturn(true).when(towerPlacementMock).isCurrentTower();

        enlistPresenter.setView(enlistView);
        enlistPresenter.placeTower();

        verify(towerPlacementMock, never()).placeTower();


    }

    /**
     * Unsuccessfully place tower, no current tower
     */
    @Test
    public void placeTowerTest3(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(GameUIState.PLACING_TOWER).when(gameUIStateManagerMock).getState();
        doReturn(false).when(towerPlacementMock).isCurrentTower();
        doReturn(false).when(towerPlacementMock).placeTower();

        enlistPresenter.setView(enlistView);
        enlistPresenter.placeTower();

        verify(towerPlacementMock, never()).placeTower();


    }


    /**
     * Unsuccessfully place tower, method return false
     */
    @Test
    public void placeTowerTest4(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();
        doReturn(GameUIState.PLACING_TOWER).when(gameUIStateManagerMock).getState();
        doReturn(true).when(towerPlacementMock).isCurrentTower();

        enlistPresenter.setView(enlistView);
        enlistPresenter.placeTower();

        verify(gameUIStateManagerMock, never()).setStateReturn();


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


    /**
     * Unsuccessfully move the tower
     */
    @Test
    public void moveTowerTest2(){

        EnlistPresenter enlistPresenter = createEnlistPresenter();

        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();
        enlistPresenter.setView(enlistView);

        Tower tower = TestUtil.createTower("Rifle", false);
        doReturn(tower).when(towerPlacementMock).getCurrentTower();
        doReturn(true).when(towerPlacementMock).isCurrentTower();

        FHDVector2 moveCoords = new FHDVector2(100,100);
        enlistPresenter.moveTower(moveCoords);

        verify(towerPlacementMock, never()).moveTower(eq(moveCoords));
        verify(enlistView, never()).showBtnPlace();
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
