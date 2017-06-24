package game.ui.presenter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.ui.presenter.DebugPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.DebugView;
import com.foxholedefense.game.ui.view.interfaces.IDebugView;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.DebugOptions;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/29/2017.
 */
public class DebugPresenterTest {

    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);
    private GameStateManager gameStateManager = mock(GameStateManager.class);
    private IDebugView debugViewMock = mock(DebugView.class);

    @Before
    public void initDebugPresenterTest() {
        Gdx.app = mock(Application.class);
    }

    private DebugPresenter createDebugPresenter(){

        return new DebugPresenter(gameUIStateManagerMock, gameStateManager);
    }

    @Test
    public void setViewTest1(){
        DebugPresenter debugPresenter = createDebugPresenter();

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();
        DebugOptions.showTextureBoundaries = true;
        DebugOptions.showFPS = false;

        debugPresenter.setView(debugViewMock);

        verify(debugViewMock, times(1)).setTextureBoundariesChecked(eq(true));
        verify(debugViewMock, times(1)).setFPSChecked(eq(false));
    }

    @Test
    public void showTextureBoundariesPressedTest1(){
        // Rest static variable
        DebugOptions.showTextureBoundaries = false;

        DebugPresenter debugPresenter = createDebugPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        assertFalse(DebugOptions.showTextureBoundaries);

        debugPresenter.showTextureBoundariesPressed();

        assertTrue(DebugOptions.showTextureBoundaries);

        debugPresenter.showTextureBoundariesPressed();

        assertFalse(DebugOptions.showTextureBoundaries);

    }

    @Test
    public void showFPSPressedTest1(){
        // Rest static variable
        DebugOptions.showFPS = false;

        DebugPresenter debugPresenter = createDebugPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        assertFalse(DebugOptions.showFPS);

        debugPresenter.showFPSPressed();

        assertTrue(DebugOptions.showFPS);

        debugPresenter.showFPSPressed();

        assertFalse(DebugOptions.showFPS);
    }

    @Test(expected=NullPointerException.class)
    public void crashTest1(){
        DebugPresenter debugPresenter = createDebugPresenter();
        debugPresenter = spy(debugPresenter);
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        doThrow(new NullPointerException()).when(debugPresenter).crash();

        debugPresenter.crash();
    }

    /**
     * Test with starting state as Debug
     */
    @Test
    public void initialStateTest1(){
        DebugPresenter debugPresenter = createDebugPresenter();

        doReturn(GameUIState.DEBUG).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        verify(debugViewMock, times(1)).debugState();
        verify(debugViewMock, never()).standByState();
    }

}
