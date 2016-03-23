package com.eric.mtd.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Resources;
import com.eric.mtd.game.model.factory.ActionFactory;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

public class MenuScreen extends AbstractScreen
{
	private static final float PLAY_MOVE_SPEED = 1f;
	private ScreenStateManager screenStateManager;
	private MenuStage stage;
	public MenuScreen(ScreenStateManager screenStateManager)	
	{
	    Resources.loadGraphics();
	    this.screenStateManager = screenStateManager;
	    this.stage = new MenuStage();
		stage.setViewport(getViewport());
		super.addInputProcessor(stage);
	}

    @Override
    public void show()
    {
    	super.show();
        // retrieve the default table actor
       // Table table =  new Table();
       // table.setFillParent( true );
        TextureAtlas menuAtlas = Resources.getAtlas(Resources.MENU_ATLAS);
        
        Resources.loadGraphics();
        Image background = new Image( menuAtlas.findRegion("background"));
        background.setFillParent(true);
        stage.addActor(background);
       // table.setBackground(background.getDrawable());
        
        TextButton play = new TextButton("Play", Resources.getSkin(Resources.SKIN_JSON));
        play.setPosition(0-play.getWidth(), 100); //200
        play.addListener(new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                //screenStateManager.setState(ScreenState.LEVEL_SELECTION);
            }
        } );
        stage.addActor(play);
        play.addAction(Actions.moveTo(200, 100, PLAY_MOVE_SPEED));
        //table.add( play ).spaceBottom( 10 ).padTop(80).size(150, 75);
        //table.row();
        
/*
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
        table.add( btnHighScores );*/
        //stage.addActor(table);
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