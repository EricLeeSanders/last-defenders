package com.lastdefenders.menu.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.view.interfaces.IMenuView;
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
    private Resources resources;

    public MenuView(MenuPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){
        createControls();
        setBackground();
    }

    private void createControls() {

        Logger.info("Menu view: initializing view");


        Skin skin = resources.getSkin();

        ImageButton btnPlay = new ImageButton(skin, "play_round");
        btnPlay.setSize(64, 64);
        btnPlay.getImageCell().size(33, 38);
        btnPlay.setPosition(getStage().getViewport().getWorldWidth() / 2, (getStage().getViewport().getWorldHeight() / 2) -25, Align.center);
        addActor(btnPlay);
        setBtnPlayListener(btnPlay);

        btnSound = new ImageButton(skin, "sound_round");
        btnSound.setSize(64, 64);
        btnSound.getImageCell().size(34, 32);
        btnSound.setPosition(175, 22);
        addActor(btnSound);
        setBtnSoundListener(btnSound);

        btnMusic = new ImageButton(skin, "music_round");
        btnMusic.setSize(64, 64);
        btnMusic.getImageCell().size(28, 36);
        btnMusic.setPosition(250, 22);
        addActor(btnMusic);
        setBtnMusicListener(btnMusic);

        ImageButton btnOptions = new ImageButton(skin, "options_round");
        btnOptions.setSize(64, 64);
        btnOptions.getImageCell().size(34, 35);
        btnOptions.setPosition(325, 22);
        addActor(btnOptions);
        setBtnOptionsListener(btnOptions);

        ImageButton btnPlayServices = new ImageButton(skin, "games_round");
        btnPlayServices.setSize(64, 64);
        btnPlayServices.getImageCell().size(36, 33);
        btnPlayServices.setPosition(400, 22);
        addActor(btnPlayServices);
        setBtnPlayServicesListener(btnPlayServices);
        if(!presenter.isGooglePlayServicesAvailable()){
            btnPlayServices.setDisabled(true);
            btnPlayServices.setTouchable(Touchable.disabled);
        }

        Logger.info("Menu view: view initialized");
    }


    private void setBackground() {

        Image background = new Image(resources.getAsset(Resources.MENU_ATLAS, TextureAtlas.class).findRegion("main-menu-screen"));
        background.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
        background.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2,
            Align.center);
        getStage().addActor(background);
        background.setZIndex(0);
    }

    private void setBtnPlayServicesListener(Button btnOptions) {

        btnOptions.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.playServicesPressed();
            }
        });

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

        btnSound.setDisabled(!soundOn);
    }

    @Override
    public void setBtnMusicOn(boolean musicOn) {

        btnMusic.setDisabled(!musicOn);
    }
}
