package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.interfaces.IDebugView;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugPresenter implements GameUIStateObserver {
    private IDebugView view;
    private GameUIStateManager uiStateManager;
    private GameStateManager gameStateManager;

    public DebugPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager){
        this.uiStateManager = uiStateManager;
        this.gameStateManager = gameStateManager;
        uiStateManager.attach(this);
    }

    public void setView(IDebugView view) {
        this.view = view;
        initView();
    }


    private void initView(){
        Logger.info("Debug Presenter: initializing view");
        stateChange(uiStateManager.getState());
        view.setFPSChecked(DebugOptions.showFPS);
        view.setTextureBoundariesChecked(DebugOptions.showTextureBoundaries);
        Logger.info("Debug Presenter: view initialized");
    }


    public void resumeGame(){
        Logger.info("Debug Presenter: resume game");
        gameStateManager.setState(GameStateManager.GameState.PLAY);
        uiStateManager.setStateReturn();
    }

    public void showTextureBoundariesPressed(){
        DebugOptions.showTextureBoundaries= !DebugOptions.showTextureBoundaries;
    }

    public void showFPSListener(){
        DebugOptions.showFPS = !DebugOptions.showFPS;
        view.showFPS(DebugOptions.showFPS);
    }

    public void crash(){
        Object o = null;
        o.toString();
    }

    @Override
    public void stateChange(GameUIState state) {

        switch(state){
            case DEBUG:
                view.debugState();
                break;
            default:
                view.standByState();
                break;
        }
    }
}
