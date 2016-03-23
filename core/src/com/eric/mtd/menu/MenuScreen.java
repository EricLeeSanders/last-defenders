package com.eric.mtd.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.MTDGame;
import com.eric.mtd.Resources;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

public class MenuScreen extends AbstractScreen
{
	private ScreenStateManager screenStateManager;
	private Stage stage;
	public MenuScreen(ScreenStateManager screenStateManager)	
	{
	    Resources.loadGraphics();
	    this.screenStateManager = screenStateManager;
	    this.stage = new Stage();
		stage.setViewport(getViewport());
		super.addInputProcessor(stage);
	}

    @Override
    public void show()
    {
    	super.show();
        // retrieve the default table actor
        Table table =  new Table();
        table.setFillParent( true );
        TextureAtlas menuAtlas = Resources.getAtlas(Resources.MENU_ATLAS);
        
        Image imgTitle = new Image(menuAtlas.findRegion("title"));
        table.add( imgTitle ).spaceBottom( 35 );
        table.row();
        
        // register the button "start game"
        Image btnStartGame = new Image(menuAtlas.findRegion("start") );
        btnStartGame.addListener(new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                screenStateManager.setState(ScreenState.LEVEL_SELECTION);
            }
        } );
        table.add( btnStartGame ).spaceBottom( 10 );
        table.row();

        // register the button "options"
        Image btnOptions = new Image(menuAtlas.findRegion("options") );
        btnOptions.addListener( new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                //game.setScreen( new StartGameScreen( game ) );
            }
        } );
        table.add( btnOptions ).spaceBottom( 10 );
        table.row();

        // register the button "high scores"
        Image btnHighScores = new Image(menuAtlas.findRegion("highscores") );
        btnHighScores.addListener( new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                //game.setScreen( new StartGameScreen( game ) );
            }
        } );
        table.add( btnHighScores );
        stage.addActor(table);
    }

	@Override
	public void renderElements(float delta) {
		stage.act( delta );
		stage.draw();
		
	}
	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();

	}
}