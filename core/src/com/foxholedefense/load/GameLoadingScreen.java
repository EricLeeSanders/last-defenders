package com.foxholedefense.load;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

public class GameLoadingScreen extends AbstractScreen{
	private Resources resources;
	private FHDAudio audio;
	private ScreenStateManager screenStateManager;
	private Stage stage;
	private static final float MIN_LOAD_TIME = 3.50f;
	private float loadTime = 0;
	public GameLoadingScreen(GameStateManager gameStateManager, ScreenStateManager screenStateManager, Resources resources, FHDAudio audio ) {
		super(gameStateManager);
		this.resources = resources;
		this.screenStateManager = screenStateManager;
		this.audio = audio;
		this.stage = new Stage(new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, new OrthographicCamera()));
		super.addInputProcessor(stage);
	}
	
	@Override
	public void show() {
		super.show();
		resources.loadAtlasSync(Resources.LOAD_ATLAS);
		TextureAtlas atlas = resources.getAtlas(Resources.LOAD_ATLAS);
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
		resources.loadSkin(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		resources.loadAtlas(Resources.MENU_ATLAS);
		resources.loadAtlas(Resources.LEVEL_SELECT_ATLAS);
		audio.load();
		
	}

	public void finishedLoading() {
		resources.initFont();
        screenStateManager.setState(ScreenState.MENU);
		
	}

}
