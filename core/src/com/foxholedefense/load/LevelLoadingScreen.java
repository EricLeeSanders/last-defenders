package com.foxholedefense.load;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

public class LevelLoadingScreen extends AbstractScreen{
	private Resources resources;
	private IScreenChanger screenChanger;
	private Stage stage;
	private static final float MIN_LOAD_TIME = 3f;
	private float loadTime = 0;
	private int level;
	private Image loadingBarBg;
	private Label loadingLabel;
	private float endPos = 528;
	private float startPos = 55;

	public LevelLoadingScreen(GameStateManager gameStateManager, IScreenChanger screenChanger, Resources resources, int level ) {
		super(gameStateManager);
		this.resources = resources;
		this.screenChanger = screenChanger;
		this.level = level;
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		super.addInputProcessor(stage);
	}
	
	@Override
	public void show() {
		Logger.info("Level loading screen: show");
		super.show();
		loadTime = 0;
		

		resources.loadAssetSync(Resources.LOAD_ATLAS, TextureAtlas.class);
		TextureAtlas atlas = resources.getAsset(Resources.LOAD_ATLAS, TextureAtlas.class);
		
		Image loadingBar = new Image(atlas.findRegion("level-load-bar"));
		loadingBar.setSize(endPos, 45);
		loadingBar.setPosition(startPos, ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, loadingBar.getHeight()) + 6);
		
		
		loadingBarBg = new Image(atlas.findRegion("level-load-bar-bg"));
		loadingBarBg.setSize(endPos, loadingBar.getHeight());
		loadingBarBg.setPosition(startPos, ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, loadingBar.getHeight()) + 6);

		
		loadingLabel = new Label("LOADING: 0%", resources.getSkin());
		loadingLabel.setFontScale(0.75f);
		loadingLabel.setAlignment(Align.left);
		loadingLabel.setColor(1f, 1f, 1f, 1f);
		float lblX = ActorUtil.calcXBotLeftFromCenter(Resources.VIRTUAL_WIDTH / 2, loadingLabel.getWidth()) + 30;
		float lblY = ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, loadingBar.getHeight()) + 2;
		loadingLabel.setPosition(lblX, lblY);
		
		
		Image screen = new Image(atlas.findRegion("level-load-screen"));
		screen.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
		float screenX = ActorUtil.calcXBotLeftFromCenter(Resources.VIRTUAL_WIDTH / 2, screen.getWidth());
		float screenY = ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, screen.getHeight());
		screen.setPosition(screenX, screenY);
		
		stage.addActor(loadingBar);
		stage.addActor(loadingBarBg);
		stage.addActor(screen);
		stage.addActor(loadingLabel);
		load();
	}
	
	@Override
	public void resize(int width, int height) {
	    super.resize(width, height);
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
        float realPercent = Interpolation.linear.apply(percent, resources.getManager().getProgress(), 0.1f);
        if(realPercent < percent){
        	percent = realPercent;
        }
		if (percent > 1) {
			percent = 1;
		}
        loadingLabel.setText("LOADING: " + Math.round(percent*100) + "%");
        float startX = startPos + endPos * percent;
        loadingBarBg.setX(startX);
        loadingBarBg.setWidth(endPos - endPos * percent);
    	
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
	
	public void load() {
		resources.loadAsset(Resources.ACTOR_ATLAS, TextureAtlas.class);
		resources.loadMap(level);
		
	}

	public void finishedLoading() {
		screenChanger.changeToLevel(level);
	}

}
