package com.lastdefenders.game.ui.presenter;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.HUDView;
import com.lastdefenders.game.ui.view.interfaces.IHUDView;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by Eric on 5/30/2017.
 */
public class HUDPresenterTest {

    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);
    private LevelStateManager levelStateManagerMock = mock(LevelStateManager.class);
    private GameStateManager gameStateManagerMock = mock(GameStateManager.class);
    private IHUDView hudView = mock(HUDView.class);
    private Player playerMock = mock(Player.class);

    @BeforeAll
    public static void initHUDPresenterTest() {

        Gdx.app = mock(Application.class);
    }

    private HUDPresenter createHUDPresenter() {

        SoundPlayer soundPlayerMock = mock(SoundPlayer.class);

        return new HUDPresenter(gameUIStateManagerMock, levelStateManagerMock, gameStateManagerMock,
            playerMock, soundPlayerMock);
    }

    @Test
    public void stateChangeTest() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.stateChange(GameUIState.GAME_OVER);

        verify(hudView, times(1)).gameOverState();
    }

    /**
     * Successfully pause the game
     */
    @Test
    public void pauseTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();
        doReturn(GameState.PLAY).when(gameStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.pause();

        verify(gameStateManagerMock, times(1)).setState(GameState.PAUSE);
    }

    /**
     * Unsuccessfully pause the game with the GameState PAUSE
     */
    @Test
    public void pauseTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();
        doReturn(GameState.PAUSE).when(gameStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.pause();

        verify(gameStateManagerMock, never()).setState(isA(GameState.class));
    }

    /**
     * Unsuccessfully pause the game with GameUIState != WAVE_IN_PROGRESS
     */
    @Test
    public void pauseTest3() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.pause();

        verify(gameStateManagerMock, never()).setState(isA(GameState.class));
    }

    /**
     * Successfully resume the game
     */
    @Test
    public void resumeTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();
        doReturn(GameState.PAUSE).when(gameStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.resume();

        verify(gameStateManagerMock, times(1)).setState(GameState.PLAY);
    }

    /**
     * Unsuccessfully resume the game with the GameState == PLAY
     */
    @Test
    public void resumeTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();
        doReturn(GameState.PLAY).when(gameStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.resume();

        verify(gameStateManagerMock, never()).setState(isA(GameState.class));
    }

    /**
     * Unsuccessfully resume the game with the GameUIState == Standby
     */
    @Test
    public void resumeTest3() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.resume();

        verify(gameStateManagerMock, never()).setState(isA(GameState.class));
    }

    /**
     * Successfully change ui state to OPTIONS
     */
    @Test
    public void optionsTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.options();

        verify(gameUIStateManagerMock, times(1)).setState(GameUIState.OPTIONS);
    }

    /**
     * Unsuccessfully change ui state to OPTIONS
     */
    @Test
    public void optionsTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.options();

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));
    }

    /**
     * Successfully start wave
     */
    @Test
    public void startWaveTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.startWave();

        verify(gameUIStateManagerMock, times(1)).setState(GameUIState.WAVE_IN_PROGRESS);
        verify(levelStateManagerMock, times(1)).setState(LevelState.WAVE_IN_PROGRESS);
    }

    /**
     * Unsuccessfully start wave
     */
    @Test
    public void startWaveTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.GAME_OVER).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.startWave();

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));
        verify(levelStateManagerMock, never()).setState(isA(LevelState.class));
    }

    /**
     * Successfully change UI state to ENLISTING
     */
    @Test
    public void enlistTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.enlist();

        verify(gameUIStateManagerMock, times(1)).setState(GameUIState.ENLISTING);
    }


    /**
     * Unsuccessfully change UI state to ENLISTING
     */
    @Test
    public void enlistTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.OPTIONS).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.enlist();

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));
    }

    /**
     * Successfully change UI state to SUPPORT
     */
    @Test
    public void addSupportTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.addSupport();

        verify(gameUIStateManagerMock, times(1)).setState(GameUIState.SUPPORT);
    }

    /**
     * Unsuccessfully change UI state to ENLISTING
     */
    @Test
    public void addSupportTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.OPTIONS).when(gameUIStateManagerMock).getState();

        hudPresenter.setView(hudView);
        hudPresenter.addSupport();

        verify(gameUIStateManagerMock, never()).setState(isA(GameUIState.class));
    }

    @Test
    public void playerAttributeTest1() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();
        doReturn(100).when(playerMock).getMoney();
        doReturn(10).when(playerMock).getLives();
        doReturn(20).when(playerMock).getWaveCount();

        hudPresenter.setView(hudView);

        verify(hudView, times(1)).setMoney("100");
        verify(hudView, times(1)).setLives("10");
        verify(hudView, times(1)).setWaveCount("20/20");

    }

    @Test
    public void playerAttributeTest2() {

        HUDPresenter hudPresenter = createHUDPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();
        doReturn(0).when(playerMock).getMoney();
        doReturn(100).when(playerMock).getLives();
        doReturn(22).when(playerMock).getWaveCount();

        hudPresenter.setView(hudView);

        verify(hudView, times(1)).setMoney("0");
        verify(hudView, times(1)).setLives("100");
        verify(hudView, times(1)).setWaveCount("22");

    }
}
