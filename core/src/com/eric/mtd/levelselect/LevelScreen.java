package com.eric.mtd.levelselect;

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

public class LevelScreen extends AbstractScreen
{	
	private ScreenStateManager screenStateManager;
	private Stage stage;
	public LevelScreen(ScreenStateManager screenStateManager)
	{
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
        
        
        TextureAtlas levelAtlas = Resources.getAtlas(Resources.LEVEL_SELECT_ATLAS);
        
        Image imgChoose = new Image(levelAtlas.findRegion("choose"));
        table.add(imgChoose ).spaceBottom( 50 );
        table.row();

        Image btnLevel1 =  new Image(levelAtlas.findRegion("level1"));
        btnLevel1.addListener(new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                screenStateManager.setState(ScreenState.LEVEL_1_SELECTED);
                //game.setScreen( new GameScreen( game, new GameStage(game, 1)) );
            }
        } );
        table.add( btnLevel1 ).size( 150, 70).uniform().spaceBottom( 10 );
        table.row();

        Image btnLevel2 =  new Image(levelAtlas.findRegion("level2"));
        btnLevel2.addListener( new ClickListener() {
        	@Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button )
            {
                super.touchUp( event, x, y, pointer, button );
                screenStateManager.setState(ScreenState.LEVEL_2_SELECTED);
               // game.setScreen( new GameScreen( game, new GameStage(game, 2)) );
            }
        } );
        table.add( btnLevel2 ).size( 150, 70).uniform().spaceBottom( 10 );
        table.row();

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