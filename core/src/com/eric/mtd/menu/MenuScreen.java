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
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.menu.ui.MenuPresenter;
import com.eric.mtd.menu.ui.MenuView;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class MenuScreen extends AbstractScreen
{
	private static final float PLAY_MOVE_DURATION = 0.5f;
	private ScreenStateManager screenStateManager;
	private MenuStage stage;
	private MenuPresenter presenter;
	private MenuView menuView;
	public MenuScreen(ScreenStateManager screenStateManager)	
	{
	    Resources.loadGraphics();
	    this.screenStateManager = screenStateManager;
	    this.stage = new MenuStage(screenStateManager);
		stage.setViewport(getViewport());
		super.addInputProcessor(stage);
		AudioUtil.load();
		AudioUtil.playMusic();
	}
    @Override
    public void show()
    {
    	super.show();
        // retrieve the default table actor

    }

	@Override
	public void renderElements(float delta) {
		stage.act( delta );
		stage.draw();
		
	}
	@Override
	public void dispose() {
		super.dispose();
		if(Logger.DEBUG)System.out.println("Disposing menu screen");
		stage.dispose();
		AudioUtil.dispose();

	}
}