package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.ui.presenter.LevelCompletedPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.LevelCompletedView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.util.FHDAudio;

import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 6/5/2017.
 */

public class LevelCompletedPresenterTest {

    private GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    private ScreenChanger screenChanger = mock(ScreenChanger.class);
    private LevelCompletedView view = mock(LevelCompletedView.class);

    @Before
    public void initLevelCompletedPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    private LevelCompletedPresenter createLevelCompletedPresenter() {

        FHDAudio audio = mock(FHDAudio.class);

        return new LevelCompletedPresenter(uiStateManager, screenChanger, audio);

    }

    @Test
    public void stateChangeTest1() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();

        presenter.setView(view);
        presenter.stateChange(GameUIState.LEVEL_COMPLETED);

        verify(view, times(1)).levelCompletedState();
    }

    /**
     * Successfully change to level select
     */
    @Test
    public void levelSelectTest1() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.LEVEL_COMPLETED).when(uiStateManager).getState();

        presenter.levelSelect();

        verify(screenChanger, times(1)).changeToLevelSelect();

    }

    /**
     * Unsuccessfully change to level select because the state is not == LEVEL_COMPLETED
     */
    @Test
    public void levelSelectTest2() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();

        presenter.levelSelect();

        verify(screenChanger, never()).changeToLevelSelect();

    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void mainMenuTest1() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.LEVEL_COMPLETED).when(uiStateManager).getState();

        presenter.mainMenu();

        verify(screenChanger, times(1)).changeToMenu();

    }

    /**
     * Unsuccessfully change to main menu because the state is not == LEVEL_COMPLETED
     */
    @Test
    public void mainMenuTest2() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();

        presenter.mainMenu();

        verify(screenChanger, never()).changeToMenu();

    }

    /**
     * Successfully change to continue level
     */
    @Test
    public void continueLevelTest1() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.LEVEL_COMPLETED).when(uiStateManager).getState();

        presenter.continueLevel();

        verify(uiStateManager, times(1)).setState(GameUIState.STANDBY);

    }

    /**
     * Unsuccessfully change to continue level because the state is not == LEVEL_COMPLETED
     */
    @Test
    public void continueLevelTest2() {

        LevelCompletedPresenter presenter = createLevelCompletedPresenter();
        doReturn(GameUIState.GAME_OVER).when(uiStateManager).getState();

        presenter.continueLevel();

        verify(uiStateManager, never()).setState(GameUIState.STANDBY);

    }
}
