package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.foxholedefense.game.ui.presenter.OptionsPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.OptionsView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UserPreferences;


import org.junit.Before;
import org.junit.Test;


import util.TestUtil;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 6/5/2017.
 */

public class OptionsPresenterTest {

    private GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    private ScreenChanger screenChanger = mock(ScreenChanger.class);
    private UserPreferences userPreferences = mock(UserPreferences.class);
    private Preferences preferences = mock(Preferences.class);
    private FHDAudio audio = mock(FHDAudio.class);
    private OptionsView view = mock(OptionsView.class);

    @Before
    public void initOptionsPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    public OptionsPresenter createOptionsPresenter(){
        Resources resources = TestUtil.createResourcesMock();

        doReturn(userPreferences).when(resources).getUserPreferences();
        doReturn(preferences).when(userPreferences).getPreferences();

        return new OptionsPresenter(uiStateManager, screenChanger, resources, audio);

    }

    @Test
    public void initViewTest1(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();
        doReturn(true).when(audio).isMusicEnabled();
        doReturn(false).when(audio).isSoundEnabled();
        doReturn(true).when(preferences).getBoolean(eq("showRanges"), eq(false));
        optionsPresenter.setView(view);

        verify(view, times(1)).setBtnMusicOn(eq(true));
        verify(view, times(1)).setBtnSoundOn(eq(false));
        verify(view, times(1)).setBtnShowRangesOn(eq(true));

    }

    @Test
    public void closeTest1(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        optionsPresenter.setView(view);

        optionsPresenter.closeOptions();

        verify(uiStateManager, times(1)).setStateReturn();
    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void mainMenuTest1(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        optionsPresenter.setView(view);

        optionsPresenter.mainMenu();

        verify(screenChanger, times(1)).changeToMenu();
    }

    /**
     * Unsuccessfully change to main menu
     */
    @Test
    public void mainMenuTest2(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();
        optionsPresenter.setView(view);

        optionsPresenter.mainMenu();

        verify(screenChanger, never()).changeToMenu();
    }

    /**
     * Successfully change to new game
     */
    @Test
    public void newGameTest1(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.OPTIONS).when(uiStateManager).getState();
        optionsPresenter.setView(view);

        optionsPresenter.newGame();

        verify(screenChanger, times(1)).changeToLevelSelect();
    }

    /**
     * Unsuccessfully change to new game
     */
    @Test
    public void newGameTest2(){

        OptionsPresenter optionsPresenter = createOptionsPresenter();

        doReturn(GameUIState.DEBUG).when(uiStateManager).getState();
        optionsPresenter.setView(view);

        optionsPresenter.newGame();

        verify(screenChanger, never()).changeToLevelSelect();
    }
}