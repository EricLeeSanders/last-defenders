package com.lastdefenders.game.ui.presenter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.DebugView;
import com.lastdefenders.game.ui.view.interfaces.IDebugView;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;

/**
 * Created by Eric on 5/29/2017.
 */
public class DebugPresenterTest {

    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);
    private IDebugView debugViewMock = mock(DebugView.class);
    private Resources resourcesMock = ResourcesMock.create();

    @BeforeAll
    public static void initDebugPresenterTest() {

        Gdx.app = mock(Application.class);
    }

    private DebugPresenter createDebugPresenter() {

        return new DebugPresenter(gameUIStateManagerMock, resourcesMock);
    }

    @Test
    public void setViewTest1() {

        DebugPresenter debugPresenter = createDebugPresenter();

        doReturn(GameUIState.DEBUG).when(gameUIStateManagerMock).getState();
        DebugOptions.showTextureBoundaries = true;
        DebugOptions.showFPS = false;

        debugPresenter.setView(debugViewMock);

        verify(debugViewMock, times(1)).setTextureBoundariesChecked(eq(true));
        verify(debugViewMock, times(1)).setFPSChecked(eq(false));
    }

    @Test
    public void showTextureBoundariesPressedTest1() {
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
    public void showFPSPressedTest1() {
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

    public void crashTest1() {

        final DebugPresenter debugPresenter = spy(createDebugPresenter());
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        doThrow(new NullPointerException()).when(debugPresenter).crash();
        assertThrows(NullPointerException.class,
            debugPresenter::crash);
    }

    /**
     * Test with starting state as Debug
     */
    @Test
    public void initialStateTest1() {

        DebugPresenter debugPresenter = createDebugPresenter();

        doReturn(GameUIState.DEBUG).when(gameUIStateManagerMock).getState();

        debugPresenter.setView(debugViewMock);

        verify(debugViewMock, times(1)).debugState();
        verify(debugViewMock, never()).standByState();
    }

}
