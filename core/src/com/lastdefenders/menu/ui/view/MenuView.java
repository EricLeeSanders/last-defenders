package com.lastdefenders.menu.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.view.interfaces.IMenuView;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for the Main Menu
 *
 * @author Eric
 */
public class MenuView extends Group implements IMenuView {

    private MenuPresenter presenter;
    private ImageButton btnSound, btnMusic;

    public MenuView(MenuPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.setTransform(false);
        createControls(resources);
    }

    private void createControls(Resources resources) {

        Logger.info("Menu view: initializing view");

        Skin skin = resources.getSkin();

        Label lblTitle = new Label("LAST DEFENDERS", skin);
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(Resources.VIRTUAL_WIDTH / 2, lblTitle.getWidth());
        float lblTitleY =
            ActorUtil.calcBotLeftPointFromCenter(Resources.VIRTUAL_HEIGHT / 2, lblTitle.getHeight())
                + 110;
        lblTitle.setPosition(lblTitleX, lblTitleY);
        addActor(lblTitle);

        TextButton btnPlay = new TextButton("PLAY", skin, "transparent");
        btnPlay.setSize(126, 56);
        float btnPlayX = ActorUtil
            .calcBotLeftPointFromCenter(Resources.VIRTUAL_WIDTH / 2, btnPlay.getWidth());
        float btnPlayY = ActorUtil
            .calcBotLeftPointFromCenter(Resources.VIRTUAL_HEIGHT / 2, btnPlay.getHeight());
        btnPlay.setPosition(btnPlayX, btnPlayY);
        addActor(btnPlay);
        setBtnPlayListener(btnPlay);

        btnSound = new ImageButton(skin, "sound");
        btnSound.setSize(64, 64);
        btnSound.getImageCell().size(34, 32);
        btnSound.setPosition(175, 22);
        addActor(btnSound);
        setBtnSoundListener(btnSound);

        btnMusic = new ImageButton(skin, "music");
        btnMusic.setSize(64, 64);
        btnMusic.getImageCell().size(28, 36);
        btnMusic.setPosition(250, 22);
        addActor(btnMusic);
        setBtnMusicListener(btnMusic);

        ImageButton btnOptions = new ImageButton(skin, "options");
        btnOptions.setSize(64, 64);
        btnOptions.getImageCell().size(34, 35);
        btnOptions.setPosition(325, 22);
        addActor(btnOptions);
        setBtnOptionsListener(btnOptions);

        ImageButton btnPlayServices = new ImageButton(skin, "games");
        btnPlayServices.setSize(64, 64);
        btnPlayServices.getImageCell().size(36, 33);
        btnPlayServices.setPosition(400, 22);
        addActor(btnPlayServices);

        Logger.info("Menu view: view initialized");
    }


    public void setBackground(TextureAtlas menuAtlas) {

        Image background = new Image(menuAtlas.findRegion("main-menu-screen"));
        background.setFillParent(true);
        this.getStage().addActor(background);
        background.setZIndex(0);
    }

    private void setBtnOptionsListener(Button btnOptions) {

        btnOptions.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.menuOptions();
            }
        });

    }

    private void setBtnPlayListener(Button btnPlay) {

        btnPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.playGame();
            }
        });
    }

    private void setBtnSoundListener(Button btnSound) {

        btnSound.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.soundPressed();
            }
        });
    }

    private void setBtnMusicListener(Button btnMusic) {

        btnMusic.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.musicPressed();
            }
        });
    }

    @Override
    public void setBtnSoundOn(boolean soundOn) {

        btnSound.setChecked(soundOn);
    }

    @Override
    public void setBtnMusicOn(boolean musicOn) {

        btnMusic.setChecked(musicOn);
    }
}
