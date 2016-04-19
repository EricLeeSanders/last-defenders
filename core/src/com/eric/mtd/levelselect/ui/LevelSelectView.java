package com.eric.mtd.levelselect.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * View for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectView extends Table {
	private LevelSelectPresenter presenter;
	private ImageButton close, btnLevel1, btnLevel2;

	public LevelSelectView(LevelSelectPresenter presenter) {
		this.presenter = presenter;
		createControls();
	}

	public void createControls() {
		TextureAtlas levelAtlas = Resources.getAtlas(Resources.LEVEL_SELECT_ATLAS);

		this.setFillParent(true);
		Image background = new Image(new TextureRegionDrawable(levelAtlas.findRegion("background")));
		this.setBackground(background.getDrawable());

		btnLevel1 = new ImageButton(new TextureRegionDrawable(levelAtlas.findRegion("level1")));
		this.add(btnLevel1).expand().center();
		setBtnLevel1Listener();

		btnLevel2 = new ImageButton(new TextureRegionDrawable(levelAtlas.findRegion("level2")));
		this.add(btnLevel2).expand().center();
		setBtnLevel2Listener();

	}

	private void setBtnLevel1Listener() {
		btnLevel1.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Level 1 Pressed");
				presenter.playLevel(1);
			}
		});

	}

	private void setBtnLevel2Listener() {
		btnLevel2.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (Logger.DEBUG)
					System.out.println("Level 2 Pressed");
				presenter.playLevel(2);
			}
		});

	}
}
