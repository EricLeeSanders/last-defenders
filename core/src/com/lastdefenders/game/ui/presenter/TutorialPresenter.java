package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.tutorial.state.TutorialStateManager;
import com.lastdefenders.game.tutorial.state.TutorialStateManager.TutorialState;
import com.lastdefenders.game.tutorial.state.TutorialStateObserver;
import com.lastdefenders.game.ui.view.interfaces.ITutorialView;

/**
 * Created by Eric on 5/6/2018.
 */

public class TutorialPresenter implements TutorialStateObserver{

    private TutorialStateManager tutorialStateManager;
    private ITutorialView view;

    public TutorialPresenter(TutorialStateManager tutorialStateManager){
        this.tutorialStateManager = tutorialStateManager;
        tutorialStateManager.attach(this);
    }

    public void setView(ITutorialView view){
        this.view = view;
    }


    @Override
    public void stateChange(TutorialState state) {
        switch(state){
            case PLACE_FIRST_SOLDIER:
                placeFirstSoldier();
                break;
        }
    }

    private void placeFirstSoldier(){

    }
}
