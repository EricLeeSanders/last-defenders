//TODO: Right now this handles the GameInputProcessor, I might want to create a different screen for that...
package com.eric.mtd.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.eric.mtd.MTDGame;
import com.eric.mtd.helper.Logger;
import com.eric.mtd.helper.Resources;
import com.eric.mtd.model.Player;
import com.eric.mtd.model.actor.ActorGroups;
import com.eric.mtd.model.level.state.LevelStateManager;
import com.eric.mtd.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.model.placement.TowerPlacement;
import com.eric.mtd.model.stage.GameStage;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.ui.controller.EnlistController;
import com.eric.mtd.ui.controller.HUDController;
import com.eric.mtd.ui.controller.InspectController;
import com.eric.mtd.ui.controller.PerksController;
import com.eric.mtd.ui.controller.interfaces.IEnlistController;
import com.eric.mtd.ui.controller.interfaces.IHUDController;
import com.eric.mtd.ui.controller.interfaces.IInspectController;
import com.eric.mtd.ui.controller.interfaces.IPerksController;
import com.eric.mtd.ui.stage.UIStage;
import com.eric.mtd.ui.state.UIStateManager;
import com.eric.mtd.ui.view.EnlistGroup;
import com.eric.mtd.ui.view.HUDGroup;
import com.eric.mtd.ui.view.InspectGroup;
import com.eric.mtd.ui.view.PerksGroup;

public class GameScreen extends AbstractScreen {
	private TextField framesField;
	private GameStage gameStage;
	private UIStage uiStage;
	private Player player;
	private GameStateManager gameStateManager;

    public GameScreen(int intLevel, Player player, GameStateManager gameStateManager, ScreenStateManager screenStateManager){
    	this.player = player;
    	//TODO: Question: Don't really like creating my actor groups, and state managers here.
    	ActorGroups actorGroups = new ActorGroups();
    	UIStateManager uiStateManager = new UIStateManager();
    	LevelStateManager levelStateManager = new LevelStateManager();
    	this.gameStateManager = gameStateManager;
	    gameStage = new GameStage(intLevel, player, actorGroups, levelStateManager, uiStateManager);
	    gameStage.setViewport(getViewport());
	    uiStage = new UIStage(intLevel, player, actorGroups, uiStateManager, levelStateManager, gameStateManager, screenStateManager);
	    uiStage.setViewport(getViewport());
	    super.addInputProcessor(uiStage);
	    super.addInputProcessor(uiStage.getEnlistGroup());
	    super.addInputProcessor(uiStage.getInspectGroup());
		super.show();
		
	    
	}
    public void createFramesField(){
    	framesField = new TextField( "0", Resources.getSkin(Resources.SKIN_JSON) );
    	framesField.setSize(40, 40);
    	framesField.setPosition(0, 0);
    	uiStage.addActor(framesField);
    }

	@Override
	public void show() {
		//if(Logger.DEBUG)System.out.println("Processer Set!GS");
		createFramesField();
	}
	@Override
	public void render(float delta) {
		/*if(delta < 1/30f){
			try {
				if(Logger.DEBUG)System.out.println("sleeping");
				Thread.sleep((long)((1/30f)-delta));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		delta = (1/30f);*/
		////if(Logger.DEBUG)System.out.println("Screen Delta" + delta);
		delta = delta * MTDGame.gameSpeed;
		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		////if(Logger.DEBUG)System.out.println(Gdx.graphics.getDeltaTime());
		getCamera().update();
		framesField.setText(Integer.valueOf(Gdx.graphics.getFramesPerSecond()).toString());
		renderElements(delta);

	}
	@Override
	public void renderElements(float delta) {
		if(gameStateManager.getState().equals(GameState.PLAY)){
			gameStage.act( delta );
		}
		gameStage.draw();
		
		uiStage.act( delta );
		uiStage.draw();
		
	}
	
	@Override
	public void dispose(){
		if(Logger.DEBUG)System.out.println("Game Screen Dispose");
		gameStage.dispose();
		uiStage.dispose();
	}


}
