package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.ui.presenter.GameOverPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.GameOverView;
import com.foxholedefense.game.ui.view.interfaces.IGameOverView;
import com.foxholedefense.screen.ScreenChanger;
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
 * Created by Eric on 5/30/2017.
 */
public class GameOverPresenterTest {

    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);
    private ScreenChanger screenChangerMock = mock(ScreenChanger.class);
    private Player playerMock = mock(Player.class);
    private IGameOverView gameOverView = mock(GameOverView.class);

    @Before
    public void initGameOverPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    private GameOverPresenter createGameOverPresenter(){
        FHDAudio audioMock = mock(FHDAudio.class);

        return new GameOverPresenter(gameUIStateManagerMock, screenChangerMock, playerMock, audioMock);
    }

    /**
     * State change to Game Over
     */
    @Test
    public void stateChangeTest1(){

        GameOverPresenter gameOverPresenter = createGameOverPresenter();

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();
        doReturn(10).when(playerMock).getWavesCompleted();

        gameOverPresenter.setView(gameOverView);
        gameOverPresenter.stateChange(GameUIState.GAME_OVER);

        verify(gameOverView, times(1)).standByState();
        verify(gameOverView, times(1)).gameOverState();
        verify(gameOverView, times(1)).setWavesCompleted(eq("10"));

    }

    /**
     * Successfully change to new game state
     */
    @Test
    public void newGameTest1(){

        GameOverPresenter gameOverPresenter = createGameOverPresenter();
        doReturn(GameUIState.GAME_OVER).when(gameUIStateManagerMock).getState();

        gameOverPresenter.setView(gameOverView);
        gameOverPresenter.newGame();

        verify(screenChangerMock, times(1)).changeToLevelSelect();
    }

    /**
     * Unsuccessfully change to new game state
     */
    @Test
    public void newGameTest2(){

        GameOverPresenter gameOverPresenter = createGameOverPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        gameOverPresenter.setView(gameOverView);
        gameOverPresenter.newGame();

        verify(screenChangerMock, never()).changeToLevelSelect();
    }

    /**
     * Successfully change to main menu
     */
    @Test
    public void mainMenuTest1(){
        GameOverPresenter gameOverPresenter = createGameOverPresenter();
        doReturn(GameUIState.GAME_OVER).when(gameUIStateManagerMock).getState();

        gameOverPresenter.setView(gameOverView);
        gameOverPresenter.mainMenu();

        verify(screenChangerMock, times(1)).changeToMenu();
    }

    /**
     * Unsuccessfully change to main menu
     */
    @Test
    public void mainMenuTest2(){
        GameOverPresenter gameOverPresenter = createGameOverPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        gameOverPresenter.setView(gameOverView);
        gameOverPresenter.mainMenu();

        verify(screenChangerMock, never()).changeToMenu();
    }

}
