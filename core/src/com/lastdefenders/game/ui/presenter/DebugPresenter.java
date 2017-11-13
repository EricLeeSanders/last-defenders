package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IDebugView;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugPresenter implements GameUIStateObserver {

    private IDebugView view;
    private GameUIStateManager uiStateManager;
    private GameStateManager gameStateManager;

    public DebugPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager) {

        this.uiStateManager = uiStateManager;
        this.gameStateManager = gameStateManager;
        uiStateManager.attach(this);
    }

    public void setView(IDebugView view) {

        this.view = view;
        initView();
    }


    private void initView() {

        Logger.info("Debug Presenter: initializing view");
        stateChange(uiStateManager.getState());
        view.setFPSChecked(DebugOptions.showFPS);
        view.setTextureBoundariesChecked(DebugOptions.showTextureBoundaries);
    }


    public void resumeGame() {

        Logger.info("Debug Presenter: resume game");
        gameStateManager.setState(GameStateManager.GameState.PLAY);
        uiStateManager.setStateReturn();
    }

    public void showTextureBoundariesPressed() {

        DebugOptions.showTextureBoundaries = !DebugOptions.showTextureBoundaries;
    }

    public void showFPSPressed() {

        DebugOptions.showFPS = !DebugOptions.showFPS;
        view.showFPS(DebugOptions.showFPS);
    }

    public void crash() {

        Logger.info("Debug Presenter: crashing game");
        Object o = null;
        o.toString();
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case DEBUG:
                view.debugState();
                break;
            default:
                view.standByState();
                break;
        }
    }
}
