package com.lastdefenders.load;


import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.screen.AbstractScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarStyle;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;

public class LevelLoadingScreen extends AbstractScreen {

    private static final Dimension LOADING_BAR_SIZE = new Dimension(512, 38);
    private static final float MIN_LOAD_TIME = 3f;
    private Resources resources;
    private ScreenChanger screenChanger;
    private Stage stage;
    private float loadTime = 0;
    private LevelName levelName;
    private Label loadingLabel;
    private LDProgressBar progressBar;

    public LevelLoadingScreen(GameStateManager gameStateManager, ScreenChanger screenChanger,
        Resources resources, LevelName levelName) {

        super(gameStateManager);

        this.resources = resources;
        this.screenChanger = screenChanger;
        this.levelName = levelName;
        this.stage = new Stage(
            new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT,
                new OrthographicCamera()));
        super.addInputProcessor(stage);

        createControls();
        createBackListener();
    }

    private void createBackListener() {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                //Don't allow return while loading
                return false;
            }
        };
        super.addInputProcessor(backProcessor);
    }

    private void createControls(){

        Logger.info("Level Loading Screen: createControls");

        resources.loadAssetSync(Resources.LOAD_ATLAS, TextureAtlas.class);
        TextureAtlas atlas = resources.getAsset(Resources.LOAD_ATLAS, TextureAtlas.class);
        loadTime = 0;

        LDProgressBarPadding progressBarPadding = new LDProgressBarPadding(2,2,2,2);
        LDProgressBarStyle progressBarStyle = new LDProgressBarStyle();
        progressBarStyle.frame = new TextureRegionDrawable(atlas.findRegion("progress-bar-frame"));
        progressBarStyle.filled = new TextureRegionDrawable(atlas.findRegion("progress-bar-filled"));
        progressBarStyle.unfilled = new TextureRegionDrawable(atlas.findRegion("progress-bar-unfilled"));
        progressBar = new LDProgressBar(0,1, 0.000001f, progressBarPadding, progressBarStyle);
        progressBar.setSize(LOADING_BAR_SIZE.getWidth(), LOADING_BAR_SIZE.getHeight());
        progressBar.setPosition(stage.getViewport().getWorldWidth() / 2, stage.getViewport().getWorldHeight() / 2, Align.center);

        loadingLabel = new Label("LOADING: 0%", resources.getSkin());
        loadingLabel.setFontScale(0.5f * resources.getFontScale());
        loadingLabel.setAlignment(Align.left);
        loadingLabel.setColor(1f, 1f, 1f, 1f);
        loadingLabel.setPosition((stage.getViewport().getWorldWidth() / 2) + 70, (stage.getViewport().getWorldHeight() / 2) + 2, Align.center);

        Image screen = new Image(atlas.findRegion("level-load-screen"));
        screen.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        screen.setPosition(stage.getViewport().getWorldWidth() / 2, stage.getViewport().getWorldHeight() / 2, Align.center);

        stage.addActor(screen);
        stage.addActor(progressBar);
        stage.addActor(loadingLabel);
        load();

    }

    @Override
    public void renderElements(float delta) {

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void render(float delta) {

        loadTime += delta;

        float percent = loadTime / MIN_LOAD_TIME;
        float realPercent = Interpolation.linear
            .apply(percent, resources.getManager().getProgress(), 0.1f);
        if (realPercent < percent) {
            percent = realPercent;
        }
        if (percent > 1) {
            percent = 1;
        }
        progressBar.setValue(percent);
        loadingLabel.setText("LOADING: " + Math.round(percent * 100) + "%");

        super.render(delta);
        if (resources.getManager().update() && loadTime >= MIN_LOAD_TIME) {
            finishedLoading();
        }
    }

    @Override
    public void dispose() {

        Logger.info("Level Loading Screen Dispose");
        super.dispose();
        resources.unloadAsset(Resources.LEVEL_SELECT_ATLAS);
        resources.unloadAsset(Resources.MENU_ATLAS);
        resources.unloadAsset(Resources.LOAD_ATLAS);
        stage.dispose();
    }

    private void load() {

        resources.loadAsset(Resources.ACTOR_ATLAS, TextureAtlas.class);
        // If we are going to show the Tutorial for the first game, load the atlas
        if(resources.getUserPreferences().getShowTutorialTips()) {
            resources.loadAsset(Resources.TUTORIAL_ATLAS, TextureAtlas.class);
        }
        resources.loadActorAtlasRegions();
        resources.loadMap(levelName);

    }

    private void finishedLoading() {

        screenChanger.changeToLevel(levelName);
    }

}
