package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.ui.view.PathDisplayer;
import com.lastdefenders.game.ui.view.interfaces.IFirstGameTipView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 5/4/2018.
 */

public class FirstGameTipPresenter {
    private IFirstGameTipView view;
    private HUDPresenter hudPresenter;
    private PathDisplayer pathDisplayer;
    private Resources resources;
    private int tipCounter = 1;


    public FirstGameTipPresenter(HUDPresenter hudPresenter, Resources resources, PathDisplayer pathDisplayer){
        this.hudPresenter = hudPresenter;
        this.resources = resources;
        this.pathDisplayer = pathDisplayer;
    }

    public void setView(IFirstGameTipView view){
        this.view = view;
    }

    public void showNextTip(){

        view.removeTutorialScreens();

        switch(tipCounter){
            case 1:
                enlistTip();
                break;
            case 2:
                supportTip();
                break;
            case 3:
                waveTip();
                break;
            case 4:
                endTips();
                break;
        }

        tipCounter++;
    }

    private void endTips(){
        resources.getUserPreferences().setShowFirstGameTips(false);
        pathDisplayer.init();
    }

    private void enlistTip(){
        Logger.info("TutorialPresenter: enlist tip");
        view.showTutorialScreen(hudPresenter.getBtnEnlist(), "enlist-button");
    }

    private void supportTip(){
        Logger.info("TutorialPresenter: support tip");
        view.showTutorialScreen(hudPresenter.getBtnSupport(), "support-button");
    }

    private void waveTip(){
        Logger.info("TutorialPresenter: wave tip");
        view.showTutorialScreen(hudPresenter.getBtnWave(), "wave-button");
    }
}
