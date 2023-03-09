package com.lastdefenders.game.ui.presenter;

import static com.lastdefenders.util.UserPreferences.AD_REMOVAL_PURCHASED;

import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IDebugView;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugPresenter implements GameUIStateObserver {

    private IDebugView view;
    private GameUIStateManager uiStateManager;
    private Resources resources;

    public DebugPresenter(GameUIStateManager uiStateManager, Resources resources) {

        this.uiStateManager = uiStateManager;
        this.resources = resources;
        uiStateManager.attach(this);
    }

    public void setView(IDebugView view) {

        this.view = view;
        initView();
    }


    private void initView() {

        Logger.info("Debug Presenter: initializing view");
        stateChange(uiStateManager.getState());
    }


    public void resumeGame() {

        Logger.info("Debug Presenter: resume game");
        uiStateManager.setStateReturn();
    }

    public void showTextureBoundariesPressed() {

        DebugOptions.showTextureBoundaries = !DebugOptions.showTextureBoundaries;
    }

    public void showTutorialPressed() {
        resources.getUserPreferences().setShowTutorialTips(!resources.getUserPreferences().getShowTutorialTips());
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

    public void removeAdPreferences(){
        resources.getUserPreferences().getPreferences().remove(AD_REMOVAL_PURCHASED);
    }

    private void debugState(){
        view.debugState();
        view.setFPSChecked(DebugOptions.showFPS);
        view.setTextureBoundariesChecked(DebugOptions.showTextureBoundaries);
        view.setTutorialChecked(resources.getUserPreferences().getShowTutorialTips());
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case DEBUG:
                debugState();
                break;
            default:
                view.standByState();
                break;
        }
    }
}
