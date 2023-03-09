package com.lastdefenders.menu.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    private Table btnTable;
    private ImageButton btnRemoveAds;

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

        this.btnTable = new Table();
        btnTable.setTransform(false);

        btnSound = new ImageButton(skin, "sound_round");
        btnSound.getImageCell().size(34, 32);
        setBtnSoundListener(btnSound);
        btnTable.add(btnSound).size(64, 64).pad(5);

        btnMusic = new ImageButton(skin, "music_round");
        btnMusic.getImageCell().size(28, 36);
        setBtnMusicListener(btnMusic);
        btnTable.add(btnMusic).size(64, 64).pad(5);

        ImageButton btnOptions = new ImageButton(skin, "options_round");
        btnOptions.getImageCell().size(34, 35);
        setBtnOptionsListener(btnOptions);
        btnTable.add(btnOptions).size(64, 64).pad(5);

        ImageButton btnPlayServices = new ImageButton(skin, "games_round");
        btnPlayServices.getImageCell().size(36, 33);
        setBtnPlayServicesListener(btnPlayServices);
        if(!presenter.isGooglePlayServicesAvailable()){
            btnPlayServices.setDisabled(true);
            btnPlayServices.setTouchable(Touchable.disabled);
        }
        btnTable.add(btnPlayServices).size(64, 64).pad(5);


        btnRemoveAds = new ImageButton(skin, "remove_ads_round");
        btnRemoveAds.getImageCell().size(65, 65);
        setBtnRemoveAdsListener(btnRemoveAds);
        addAdsBtnToTable();

        setBtnTablePosition();

        addActor(btnTable);

        Logger.info("Menu view: view initialized");
    }

    private void addAdsBtnToTable(){
        if(presenter.isAdsRemovalPurchasable() && btnTable.getCell(btnRemoveAds) == null) {
            btnTable.add(btnRemoveAds).size(64, 64).pad(5);
        }
    }

    private void removeAdsBtnFromTable(){
        if(btnTable.getCell(btnRemoveAds) != null) {
            // There is no way to remove a cell. So we have to do this...
            btnTable.getCell(btnRemoveAds).size(0,0).pad(0);
            btnRemoveAds.remove();
            setBtnTablePosition();
        }
    }

    private void setBtnTablePosition(){
        btnTable.pack();
        btnTable.setPosition(getStage().getViewport().getWorldWidth() / 2, 60, Align.center);
    }

    @Override
    public void setPurchaseManagerInstalled(boolean installed){
        if(installed) {
            addAdsBtnToTable();
        }
    }

    @Override
    public void adRemovalPurchased(boolean purchased) {
        if(purchased){
            removeAdsBtnFromTable();
        } else {
            addAdsBtnToTable();
        }
    }


    private void setBackground() {

        Image background = new Image(resources.getAsset(Resources.MENU_ATLAS, TextureAtlas.class).findRegion("main-menu-screen"));
        background.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
        background.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2,
            Align.center);
        getStage().addActor(background);
        background.setZIndex(0);
    }

    private void setBtnRemoveAdsListener(Button btnRemoveAds) {

        btnRemoveAds.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.removeAds();
            }
        });

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
