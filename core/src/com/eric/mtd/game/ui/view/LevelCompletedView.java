package com.eric.mtd.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.eric.mtd.game.ui.presenter.LevelCompletedPresenter;
import com.eric.mtd.game.ui.view.interfaces.ILevelCompletedView;
import com.eric.mtd.game.ui.view.widget.MTDImage;
import com.eric.mtd.game.ui.view.widget.MTDLabel;
import com.eric.mtd.game.ui.view.widget.MTDTextButton;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class LevelCompletedView extends Group implements ILevelCompletedView{
	private LevelCompletedPresenter presenter;
	private MTDTextButton btnContinueLevel, btnNewGame;
	private MTDLabel lblLevelCompleted, lblContinue;
	private MTDImage panel;
	
	public LevelCompletedView(LevelCompletedPresenter presenter){
		this.presenter = presenter;
		createControls();
	}
	
	public void createControls(){
		Table table = new Table();
		Skin skin = Resources.getSkin(Resources.SKIN_JSON);
		table.setBackground(skin.getDrawable("main-panel-vert"));
		panel = new MTDImage("UI_LevelCompleted", "panel", Resources.SKIN_ATLAS, "main-panel-horz", true, false);
		panel.getColor().set(1f, 1f, 1f, .75f);
		addActor(panel);
		
		lblLevelCompleted = new MTDLabel("UI_LevelCompleted", "lblLevelCompleted", "Level Completed!", true, Color.valueOf("FF7F2A"), Align.center, Resources.getFont("default-font-22")); //
		addActor(lblLevelCompleted);

		lblContinue = new MTDLabel("UI_LevelCompleted", "lblContinue", "Do you want to \n continue playing this \n level and compete \n for a high score?", true, Color.WHITE, Align.center, Resources.getFont("default-font-22"));
		addActor(lblContinue);
		
		btnContinueLevel = new MTDTextButton("UI_LevelCompleted", "btnContinueLevel", "Continue", Align.center, 0.45f, true);
		setBtnContinueLevel();
		addActor(btnContinueLevel);

		btnNewGame = new MTDTextButton("UI_LevelCompleted", "btnNewGame", "New Level", Align.center, 0.45f, true);
		setbtnNewGameListener();
		addActor(btnNewGame);
	}
	
	private void setBtnContinueLevel() {
		btnContinueLevel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Continue Level Pressed");
				presenter.continueLevel();
			}
		});

	}
	
	private void setbtnNewGameListener() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Level Select Pressed");
				presenter.levelSelect();
			}
		});

	}
	
	@Override
	public void levelCompletedState() {
		this.setVisible(true);
		
	}

	@Override
	public void standByState() {
		this.setVisible(false);
		
	}

}
