package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.presenter.GameOverPresenter;
import com.eric.mtd.game.ui.view.interfaces.IGameOverView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class GameOverView extends Group implements IGameOverView {
	private GameOverPresenter presenter;
	private MTDTextButton btnNewGame, btnHighScores, btnMainMenu;
	private MTDLabel lblWavesCompleted, lblWavesCompletedCount, lblGameOver;
	private MTDImage panel;
	public GameOverView(GameOverPresenter presenter){
		this.presenter = presenter;
		createControls();
	}
	public void createControls(){
		panel = new MTDImage("UI_GameOver", "panel",Resources.GAME_OVER_ATLAS, "panel",true, false);
			panel.getColor().set(1f,1f,1f,.75f);
			addActor(panel);
	
		btnNewGame =new MTDTextButton("UI_GameOver", "btnNewGame","New Game", true);
			setBtnNewGameListener();
			addActor(btnNewGame);
		
		btnHighScores = new MTDTextButton("UI_GameOver", "btnHighScores","High Scores", true);
			setBtnHighScoresListener();
			addActor(btnHighScores);
			
		btnMainMenu =new MTDTextButton("UI_GameOver", "btnMainMenu","Main Menu", true);
			setBtnMainMenuListener();
			addActor(btnMainMenu);
		
		lblGameOver = new MTDLabel("UI_GameOver", "lblGameOver","Game Over!",true, Color.valueOf("FF7F2A"), Align.center, 0.75f); //
			addActor(lblGameOver);
		
		lblWavesCompleted = new MTDLabel("UI_GameOver", "lblWavesCompleted","Waves Completed",true, Color.WHITE, Align.center, 0.55f);
			addActor(lblWavesCompleted);
			
		lblWavesCompletedCount = new MTDLabel("UI_GameOver", "lblWavesCompletedCount","0",true, Color.WHITE, Align.center, .75f);
			addActor(lblWavesCompletedCount);
	}
	@Override
	public void standByState(){
		this.setVisible(false);
	}
	@Override
	public void gameOverState(){
		this.setVisible(true);
	}
	@Override
	public void setWavesCompleted(String wavesCompleted){
		lblWavesCompletedCount.setText(wavesCompleted);
	}
	private void setBtnNewGameListener(){
		btnNewGame.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("New Game Pressed");
	    		presenter.newGame();
	        }
	    } );
	    
	}
	private void setBtnHighScoresListener(){
		btnHighScores.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("High Scores Pressed");
	    		presenter.highScores();
	        }
	    } );
	    
	}
	private void setBtnMainMenuListener(){
		btnMainMenu.addListener(new ClickListener() {
	    	@Override
	        public void touchUp(InputEvent event, float x, float y, int pointer, int button )
	        {
	    		super.touchUp( event, x, y, pointer, button );
	    		if(Logger.DEBUG)System.out.println("Main Menu Pressed");
	    		presenter.mainMenu();
	        }
	    } );
	    
	}
}
