package com.lastdefenders.game.ui.presenter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.OptionsView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.MusicPlayer;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.ResourcesMock;

/**
 * Created by Eric on 6/5/2017.
 */

public class OptionsPresenterTest {

    @Mock private GameUIStateManager uiStateManager;
    @Mock private ScreenChanger screenChanger;
    @Mock private UserPreferences userPreferences;
    @Mock private AudioManager audio;
    @Mock private OptionsView view;
    @Mock private StoreManager storeManager;
    @Spy private Resources resources = ResourcesMock.create();

    @InjectMocks private OptionsPresenter optionsPresenter;

    private AutoCloseable closeable;

    @BeforeAll
    public static void initOptionsPresenterTest() {

        Gdx.app = mock(Application.class);
        Gdx.audio = mock(Audio.class);
        Gdx.files = mock(Files.class);

    }

    @BeforeEach
    public void startMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        initView();
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    private void initView(){
        SoundPlayer soundPlayerMock = mock(SoundPlayer.class);
        MusicPlayer musicPlayerMock = mock(MusicPlayer.class);

        doReturn(soundPlayerMock).when(audio).getSoundPlayer();
        doReturn(musicPlayerMock).when(audio).getMusicPlayer();

        doReturn(GameUIState.STANDBY).when(uiStateManager).getState();
        doReturn(true).when(musicPlayerMock).isEnabled();
        doReturn(false).when(soundPlayerMock).isEnabled();
        doReturn(userPreferences).when(resources).getUserPreferences();
        doReturn(true).when(userPreferences).getShowTowerRanges();
        optionsPresenter.setView(view);
    }

    @Test
    public void initViewTest1() {

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

    @Test
    public void handlePurchaseTest(){

        optionsPresenter.handlePurchase(PurchasableItem.NO_ADS);

        verify(view, times(1)).adRemovalPurchased(true);
        verify(view, never()).adRemovalPurchased(false);
    }

    @Test
    public void removeAdsTest(){

        doReturn(true).when(storeManager).isAdsRemovalPurchasable();

        optionsPresenter.removeAds();

        verify(storeManager, times(1)).purchaseItem(PurchasableItem.NO_ADS);

    }

    @Test
    public void removeAdsInvalidTest(){

        doReturn(false).when(storeManager).isAdsRemovalPurchasable();

        assertThrows(IllegalStateException.class,
            optionsPresenter::removeAds);

        verify(storeManager, never()).purchaseItem(PurchasableItem.NO_ADS);
    }


}
