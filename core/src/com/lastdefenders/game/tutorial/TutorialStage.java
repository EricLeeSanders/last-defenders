package com.lastdefenders.game.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.tutorial.state.TutorialStateManager;
import com.lastdefenders.game.tutorial.state.TutorialStateManager.TutorialState;
import com.lastdefenders.game.ui.presenter.TutorialPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.view.TutorialView;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 5/6/2018.
 */

public class TutorialStage extends GameStage {

    private TutorialStateManager tutorialStateManager;
    private Resources resources;

    public TutorialStage(LevelName levelName, Player player, ActorGroups actorGroups, LDAudio audio,
        LevelStateManager levelStateManager, GameUIStateManager uiStateManager,
        Viewport viewport, Resources resources, SpriteBatch spriteBatch) {

        super(levelName, player, actorGroups, audio, levelStateManager, uiStateManager, viewport,
            resources, spriteBatch);

        this.resources = resources;
    }

    @Override
    public void init(){
        super.init();
        tutorialStateManager = new TutorialStateManager();
        createUI();
        beginTutorial();
    }

    private void createUI(){

        TutorialPresenter tutorialPresenter = new TutorialPresenter(tutorialStateManager);
        TutorialView tutorialView = new TutorialView(tutorialPresenter, resources.getAsset(Resources.TUTORIAL_ATLAS, TextureAtlas.class));
        tutorialPresenter.setView(tutorialView);
    }

    private void beginTutorial(){
        tutorialStateManager.setState(TutorialState.PLACE_FIRST_SOLDIER);
    }
}
