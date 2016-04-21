package com.eric.mtd.load;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.screen.AbstractScreen;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.Resources;

public class LoadingScreen extends AbstractScreen {
	private TextureAtlas atlas;
	private TextureRegion logoRegion;
	private Image logoImage;
	private Stage stage;

	public LoadingScreen(GameStateManager gameStateManager) {
		super(gameStateManager);
		this.stage = new Stage();
		stage.setViewport(getViewport());
		super.addInputProcessor(stage);
		// super.setStage(new Stage());
	}

	@Override
	public void show() {
		super.show();
		Resources.loadGraphics();
		// atlas = Resources.getAtlas(Resources.IMAGES_ATLAS);
		logoRegion = atlas.findRegion("logo");
		logoImage = new Image(logoRegion);

		logoImage.addAction(sequence(fadeIn(0.75f), delay(1.75f), fadeOut(0.75f), new Action() {
			@Override
			public boolean act(float delta) {
				// the last action will move to the next screen
				// game.setScreen( new MenuScreen( stateManager ) );
				// gameStateManager.setState(LevelState.MENU);
				return true;
			}
		}));

		stage.addActor(logoImage);
	}

	@Override
	public void renderElements(float delta) {
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();

	}
}