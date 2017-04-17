package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.game.ui.presenter.LevelCompletedPresenter;
import com.foxholedefense.game.ui.view.interfaces.ILevelCompletedView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

public class LevelCompletedView extends Group implements ILevelCompletedView{
	private LevelCompletedPresenter presenter;

	public LevelCompletedView(LevelCompletedPresenter presenter, Skin skin){
		this.presenter = presenter;
		this.setTransform(false);
		createControls(skin);
	}
	
	public void createControls(Skin skin){

		Logger.info("Level Completed View: creating controls");

		Table table = new Table();
		table.setTransform(false);
		table.setBackground(skin.getDrawable("main-panel"));
		table.setSize(500,260);
		table.setPosition((Resources.VIRTUAL_WIDTH/2)-(table.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(table.getHeight()/2));
		this.addActor(table);
		
		Label lblTitle = new Label("LEVEL COMPLETED", skin);
		lblTitle.setFontScale(0.40f);
		lblTitle.setAlignment(Align.center);
		lblTitle.setPosition(table.getX() + (table.getWidth()/2) - (lblTitle.getWidth()/2)
					,table.getY() + table.getHeight() - lblTitle.getHeight() + 7);
		this.addActor(lblTitle);

		
		Label lblContinue = new Label("Do you want to\ncontinue playing this\nlevel and compete\nfor a high score?".toUpperCase(), skin, "hollow");
		lblContinue.setFontScale(0.45f);
		lblContinue.setAlignment(Align.center);
		table.add(lblContinue).colspan(3).width(380).height(120).padTop(30).spaceBottom(15);
		
		
		table.row();
		TextButton btnContinueLevel = new TextButton("CONTINUE", skin);
		btnContinueLevel.getLabel().setFontScale(0.45f);
		table.add(btnContinueLevel).width(130).height(45);
		setBtnContinueLevel(btnContinueLevel);

		TextButton btnNewGame = new TextButton("NEW GAME", skin);
		btnNewGame.getLabel().setFontScale(0.45f);
		table.add(btnNewGame).width(130).height(45).spaceLeft(15).spaceRight(15);
		setbtnNewGameListener(btnNewGame);

		TextButton btnMainMenu = new TextButton("MAIN MENU", skin);
		btnMainMenu.getLabel().setFontScale(0.45f);
		table.add(btnMainMenu).width(130).height(45);
		setbtnMainMenuListener(btnMainMenu);

		Logger.info("Level Completed View: controls created");
	}
	
	private void setBtnContinueLevel(Button button) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.continueLevel();
			}
		});

	}
	
	private void setbtnNewGameListener(Button button) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.levelSelect();
			}
		});

	}

	private void setbtnMainMenuListener(Button button) {
		button.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.mainMenu();
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
