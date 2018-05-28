package com.lastdefenders.game.ui.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.lastdefenders.game.ui.presenter.OptionsPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.OptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.TestUtil;

/**
 * Created by Eric on 6/5/2017.
 */

public class OptionsPresenterTest {

    @Mock private GameUIStateManager uiStateManager;
    @Mock private ScreenChanger screenChanger;
    @Mock private UserPreferences userPreferences;
    @Mock private LDAudio audio;
    @Mock private OptionsView view;
    @Spy private Resources resources = TestUtil.createResourcesMock();

    @InjectMocks private OptionsPresenter optionsPresenter;

    @Before
    public void initOptionsPresenterTest() {

        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void initViewTest1() {

        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();
        doReturn(true).when(audio).isMusicEnabled();
        doReturn(false).when(audio).isSoundEnabled();
        doReturn(userPreferences).when(resources).getUserPreferences();
        doReturn(true).when(userPreferences).getShowTowerRanges();
        optionsPresenter.setView(view);

        verify(view, times(1)).setBtnMusicOn(eq(true));
        verify(view, times(1)).setBtnSoundOn(eq(false));
        verify(view, times(1)).setBtnShowRangesOn(eq(true));

    }

    @Test
    public void closeTest1() {

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        doReturn(userPreferences).when(resources).getUserPreferences();
        optionsPresenter.setView(view);

        optionsPresenter.closeOptions();

        verify(uiStateManager, times(1)).setStateReturn();
    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void mainMenuTest1() {

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        doReturn(userPreferences).when(resources).getUserPreferences();
        optionsPresenter.setView(view);

        optionsPresenter.mainMenu();

        verify(screenChanger, times(1)).changeToMenu();
    }

    /**
     * Unsuccessfully change to main menu
     */
    @Test
    public void mainMenuTest2() {

        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();
        doReturn(userPreferences).when(resources).getUserPreferences();
        optionsPresenter.setView(view);

        optionsPresenter.mainMenu();

        verify(screenChanger, never()).changeToMenu();
    }

    /**
     * Successfully change to new game
     */
    @Test
    public void newGameTest1() {

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        doReturn(userPreferences).when(resources).getUserPreferences();
        optionsPresenter.setView(view);

        optionsPresenter.newGame();

        verify(screenChanger, times(1)).changeToLevelSelect();
    }

    /**
     * Unsuccessfully change to new game
     */
    @Test
    public void newGameTest2() {

        doReturn(GameUIState.DEBUG).when(uiStateManager).getState();
        doReturn(userPreferences).when(resources).getUserPreferences();
        optionsPresenter.setView(view);

        optionsPresenter.newGame();

        verify(screenChanger, never()).changeToLevelSelect();
    }
}
