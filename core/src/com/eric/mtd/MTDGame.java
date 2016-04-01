package com.eric.mtd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eric.mtd.game.GameScreen;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.levelselect.LevelSelectScreen;
import com.eric.mtd.load.LoadingScreen;
import com.eric.mtd.menu.MenuScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.IScreenStateObserver;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.Resources;

public class MTDGame extends Game implements IScreenStateObserver {
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	private Player player;
	public static float gameSpeed = Resources.NORMAL_SPEED; //This has to be static because animations directly call Gdx.graphics.getDeltaTime()
															
	
	@Override
	public void create () {
		player = new Player();
		screenStateManager = new ScreenStateManager();
		screenStateManager.attach(this);
		gameStateManager = new GameStateManager();
		setScreen(new MenuScreen(screenStateManager));
		
	}
    @Override
    public void dispose() {
    	Resources.dispose();
    	this.getScreen().dispose();
        super.dispose();
    }
	@Override
	public void changeScreenState(ScreenState state) {
    	switch(state){
		case LEVEL_SELECTION:
			this.getScreen().dispose(); //dispose current screen
			this.setScreen(new LevelSelectScreen(screenStateManager));
			break;
		case MENU:
			this.getScreen().dispose(); //dispose current screen
			this.setScreen( new MenuScreen( screenStateManager ) );
			break;
		case LEVEL_1_SELECTED:
			this.getScreen().dispose(); //dispose current screen
			this.setScreen( new GameScreen(1, player, gameStateManager, screenStateManager));
			break;
		case LEVEL_2_SELECTED:
			this.getScreen().dispose(); //dispose current screen
			this.setScreen( new GameScreen(2, player, gameStateManager, screenStateManager));
			break;
		default:
			break;
    	}
		
	}
}
