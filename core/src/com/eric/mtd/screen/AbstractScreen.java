package com.eric.mtd.screen;

import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;


public abstract class AbstractScreen implements Screen {
	private TextureAtlas atlas;
	private TextureAtlas skinAtlas;
    private Skin skin;
    private Table table;
    private Viewport viewport;
    //public static int WORLD_WIDTH;
    //public static int WORLD_HEIGHT;
    private TextField textX, textY;
    private OrthographicCamera camera;
    private InputMultiplexer imp;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
	public AbstractScreen(){
       // WORLD_WIDTH = Gdx.graphics.getHeight();
        //WORLD_HEIGHT = Gdx.graphics.getWidth();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(Resources.SCREEN_WIDTH,Resources.SCREEN_HEIGHT, camera);
	    imp = new InputMultiplexer();
	}
	public abstract void renderElements(float delta);
	/*public void setStage(Stage stage){
	    this.stage = stage;
	    stage.setViewport(viewport);
	    if(Logger.DEBUG)System.out.println("AbstractScreen) I was called :" +Gdx.graphics.getWidth());
	    imp.addProcessor(stage);
	}
	public Stage getStage(){
		return stage;
	}*/
	@Override
	public void render(float delta) {
		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		camera.update(); //Question should this be here?
		renderElements(delta);
		//if(Logger.DEBUG)System.out.println(Gdx.graphics.getFramesPerSecond());
		/*if(stage!=null){
			stage.act( delta );
			stage.draw();
		}*/
	}


	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
		if(Logger.DEBUG)System.out.println("changed to " + width+ ", " + height);
		camera.setToOrtho(false, width, height);
	    camera.update();
		viewport.update(width, height, true); //Changes  viewport
		if(Logger.DEBUG)System.out.println("viewport to " + viewport.getScreenWidth()+ ", " + viewport.getScreenHeight());	
	    BigInteger b1 = BigInteger.valueOf(viewport.getScreenWidth());
	    BigInteger b2 = BigInteger.valueOf(viewport.getScreenHeight());
	    BigInteger gcd = b1.gcd(b2);
		if(Logger.DEBUG)System.out.println(b1.divide(gcd) + "/" + b2.divide(gcd));
		if(Logger.DEBUG)System.out.println("World: " + viewport.getWorldWidth() + ", " + viewport.getWorldHeight());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(imp);
		if(Logger.DEBUG)System.out.println("Processer Set");
	}

	@Override
	public void dispose() {
		if(Logger.DEBUG)System.out.println("abstractscreen disposing");

	}
	public Camera getCamera(){
		return camera;
	}
	public Viewport getViewport(){
		return viewport;
	}
	public InputMultiplexer getInputMultiplexer(){
		return imp;
	}
	protected void addInputProcessor(InputProcessor ip){
		imp.addProcessor(ip);
	}


}
