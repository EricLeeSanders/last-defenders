package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.ui.presenter.QuitPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.QuitView;
import com.foxholedefense.game.ui.view.interfaces.IQuitView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;


import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 6/7/2017.
 */

public class QuitPresenterTest {

    private GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    private GameStateManager gameStateManager = mock(GameStateManager.class);
    private ScreenChanger screenChanger = mock(ScreenChanger.class);
    private QuitView quitView = mock(QuitView.class);

    @Before
    public void initQuitPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    public QuitPresenter createQuitPresenter(){

        FHDAudio audioMock = mock(FHDAudio.class);
        return new QuitPresenter(uiStateManager, gameStateManager, screenChanger, audioMock);
    }

    /**
     * Successfully resume the game and unpause
     */
    @Test
    public void resumeTest1(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.QUIT_MENU).when(uiStateManager).getState();
        doReturn(GameState.PLAY).when(gameStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.stateChange(GameUIState.QUIT_MENU);
        quitPresenter.resume();

        verify(uiStateManager, times(1)).setStateReturn();
        verify(quitView, times(1)).quitState();
        verify(gameStateManager, times(1)).setState(eq(GameState.PAUSE));
        verify(gameStateManager, times(1)).setState(eq(GameState.PLAY));
    }

    /**
     * Successfully resume the game and keep paused
     */
    @Test
    public void resumeTest2(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.QUIT_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.stateChange(GameUIState.QUIT_MENU);
        quitPresenter.resume();

        verify(uiStateManager, times(1)).setStateReturn();
        verify(quitView, times(1)).quitState();
        verify(gameStateManager, times(1)).setState(eq(GameState.PAUSE));
        verify(gameStateManager, never()).setState(eq(GameState.PLAY));
    }

    /**
     * Unsuccessfully resume the game
     */
    @Test
    public void resumeTest3(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.resume();

        verify(uiStateManager, never()).setStateReturn();
    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void changeToMainMenuTest1(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.QUIT_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.stateChange(GameUIState.QUIT_MENU);
        quitPresenter.mainMenu();

        verify(screenChanger, times(1)).changeToMenu();
    }

    /**
     * Unsuccessfully change to main menu
     */
    @Test
    public void changeToMainMenuTest2(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(uiStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.mainMenu();

        verify(screenChanger, never()).changeToMenu();
    }

    /**
     * Successfully change to new game
     */
    @Test
    public void changeToNewGameTest1(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.QUIT_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.stateChange(GameUIState.QUIT_MENU);
        quitPresenter.newGame();

        verify(screenChanger, times(1)).changeToLevelSelect();
    }

    /**
     * Unsuccessfully change to new game
     */
    @Test
    public void changeToNewGameTest2(){
        QuitPresenter quitPresenter = createQuitPresenter();
        doReturn(GameUIState.PLACING_TOWER).when(uiStateManager).getState();

        quitPresenter.setView(quitView);
        quitPresenter.newGame();

        verify(screenChanger, never()).changeToLevelSelect();
    }
}
