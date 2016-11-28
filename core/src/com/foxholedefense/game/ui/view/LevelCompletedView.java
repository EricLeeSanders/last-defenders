package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
	private TextButton btnContinueLevel, btnNewGame;
	private Label lblTitle, lblContinue;
	
	public LevelCompletedView(LevelCompletedPresenter presenter, Skin skin){
		this.presenter = presenter;
		this.setTransform(false);
		createControls(skin);
	}
	
	public void createControls(Skin skin){
		Table table = new Table();
		table.setTransform(false);
		table.setBackground(skin.getDrawable("main-panel"));
		table.setSize(400,260);
		table.setPosition((Resources.VIRTUAL_WIDTH/2)-(table.getWidth()/2), (Resources.VIRTUAL_HEIGHT/2)-(table.getHeight()/2));
		this.addActor(table);
		
		lblTitle = new Label("LEVEL COMPLETED", skin);
		lblTitle.setFontScale(0.45f);
		lblTitle.setAlignment(Align.center);
		lblTitle.setPosition(table.getX() + (table.getWidth()/2) - (lblTitle.getWidth()/2)
					,table.getY() + table.getHeight() - lblTitle.getHeight() + 4);
		this.addActor(lblTitle);

		
		lblContinue = new Label("Do you want to\ncontinue playing this\nlevel and compete\nfor a high score?".toUpperCase(), skin, "hollow");
		lblContinue.setFontScale(0.45f);
		lblContinue.setAlignment(Align.center);
		table.add(lblContinue).colspan(2).width(280).height(120).padTop(30).spaceBottom(15);
		
		
		table.row();
		btnContinueLevel = new TextButton("CONTINUE", skin);
		btnContinueLevel.getLabel().setFontScale(0.45f);
		table.add(btnContinueLevel).width(130).height(45);
		setBtnContinueLevel();

		btnNewGame = new TextButton("NEW GAME", skin);
		btnNewGame.getLabel().setFontScale(0.45f);
		table.add(btnNewGame).width(130).height(45);
		setbtnNewGameListener();
	}
	
	private void setBtnContinueLevel() {
		btnContinueLevel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				presenter.continueLevel();
			}
		});

	}
	
	private void setbtnNewGameListener() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
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
