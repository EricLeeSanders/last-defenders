package game.ui.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.ui.presenter.PausePresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.PauseView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Eric on 6/7/2017.
 */

public class PausePresenterTest {

    private GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    private GameStateManager gameStateManager = mock(GameStateManager.class);
    private ScreenChanger screenChanger = mock(ScreenChanger.class);
    private PauseView pauseView = mock(PauseView.class);

    @Before
    public void initPausePresenterTest() {

        Gdx.app = mock(Application.class);
    }

    private PausePresenter createPausePresenter() {

        FHDAudio audioMock = mock(FHDAudio.class);
        return new PausePresenter(uiStateManager, gameStateManager, screenChanger, audioMock);
    }

    /**
     * Successfully resume the game and unpause
     */
    @Test
    public void resumeTest1() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.PAUSE_MENU).when(uiStateManager).getState();
        doReturn(GameState.PLAY).when(gameStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.stateChange(GameUIState.PAUSE_MENU);
        pausePresenter.resume();

        verify(uiStateManager, times(1)).setStateReturn();
        verify(pauseView, times(1)).pauseState();
        verify(gameStateManager, times(1)).setState(eq(GameState.PAUSE));
        verify(gameStateManager, times(1)).setState(eq(GameState.PLAY));
    }

    /**
     * Successfully resume the game and keep paused
     */
    @Test
    public void resumeTest2() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.PAUSE_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.stateChange(GameUIState.PAUSE_MENU);
        pausePresenter.resume();

        verify(uiStateManager, times(1)).setStateReturn();
        verify(pauseView, times(1)).pauseState();
        verify(gameStateManager, times(1)).setState(eq(GameState.PAUSE));
        verify(gameStateManager, never()).setState(eq(GameState.PLAY));
    }

    /**
     * Unsuccessfully resume the game
     */
    @Test
    public void resumeTest3() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.resume();

        verify(uiStateManager, never()).setStateReturn();
    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void changeToMainMenuTest1() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.PAUSE_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.stateChange(GameUIState.PAUSE_MENU);
        pausePresenter.mainMenu();

        verify(screenChanger, times(1)).changeToMenu();
    }

    /**
     * Unsuccessfully change to main menu
     */
    @Test
    public void changeToMainMenuTest2() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(uiStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.mainMenu();

        verify(screenChanger, never()).changeToMenu();
    }

    /**
     * Successfully change to new game
     */
    @Test
    public void changeToNewGameTest1() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.PAUSE_MENU).when(uiStateManager).getState();
        doReturn(GameState.PAUSE).when(gameStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.stateChange(GameUIState.PAUSE_MENU);
        pausePresenter.newGame();

        verify(screenChanger, times(1)).changeToLevelSelect();
    }

    /**
     * Unsuccessfully change to new game
     */
    @Test
    public void changeToNewGameTest2() {

        PausePresenter pausePresenter = createPausePresenter();
        doReturn(GameUIState.PLACING_TOWER).when(uiStateManager).getState();

        pausePresenter.setView(pauseView);
        pausePresenter.newGame();

        verify(screenChanger, never()).changeToLevelSelect();
    }
}
