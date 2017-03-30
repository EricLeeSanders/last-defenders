package com.foxholedefense.load;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

public class GameLoadingScreen extends AbstractScreen{
	private Resources resources;
	private FHDAudio audio;
	private IScreenChanger screenChanger;
	private Stage stage;
	private static final float MIN_LOAD_TIME = 3.50f;
	private float loadTime = 0;
	public GameLoadingScreen(GameStateManager gameStateManager, IScreenChanger screenChanger, Resources resources, FHDAudio audio ) {
		super(gameStateManager);
		this.resources = resources;
		this.screenChanger = screenChanger;
		this.audio = audio;
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		super.addInputProcessor(stage);
	}
	
	@Override
	public void show() {
		Logger.info("Game loading screen: show");
		super.show();
		resources.loadAssetSync(Resources.LOAD_ATLAS, TextureAtlas.class);
		TextureAtlas atlas = resources.getAsset(Resources.LOAD_ATLAS, TextureAtlas.class);
		Image image = new Image(atlas.findRegion("img-loading"));
		float x = ActorUtil.calcXBotLeftFromCenter(Resources.VIRTUAL_WIDTH / 2, image.getWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(Resources.VIRTUAL_HEIGHT / 2, image.getHeight());
		image.setPosition(x, y);
		loadTime = 0;
		stage.addActor(image);
		
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
        super.render(delta);
        if (resources.getManager().update() && loadTime >= MIN_LOAD_TIME) { 
            finishedLoading();
        } 
    }
	
	@Override
	public void dispose() {
		Logger.info("Game Loading Screen Dispose");
		super.dispose();
		stage.dispose();
	}
	
	public void load() {
		resources.loadSkin();
		resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
		resources.loadAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);
		audio.load();
		
	}

	public void finishedLoading() {
		resources.initFont();
		screenChanger.changeToMenu();
		
	}

}
