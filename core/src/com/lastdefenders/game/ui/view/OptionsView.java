package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.ui.presenter.OptionsPresenter;
import com.lastdefenders.game.ui.view.interfaces.IOptionsView;
import com.lastdefenders.ui.view.widget.LDSlider;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for the options window
 *
 * @author Eric
 */
public class OptionsView extends Group implements IOptionsView {

    private OptionsPresenter presenter;
    private TextButton btnNewGame, btnMainMenu;
    private CheckBox btnShowRanges, btnSound, btnMusic;
    private ImageButton btnRemoveAds;
    private Resources resources;

    public OptionsView(OptionsPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){
        createControls();
    }

    /**
     * Create controls
     */
    private void createControls() {

        Logger.info("Options View: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center );
        container.padBottom(10);
        addActor(container);

        Table mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("hollow"));
        mainTable.setTransform(false);
        container.add(mainTable).colspan(3).size(350, 226);

        Label lblTitle = new Label("Options", skin);
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - 30;
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        addActor(lblTitle);


        btnNewGame = new TextButton("New Game", skin);
        btnNewGame.getLabel().setFontScale(0.5f * resources.getFontScale());
        setBtnNewGameListener();

        btnMainMenu = new TextButton("Main Menu", skin);
        btnMainMenu.getLabel().setFontScale(0.5f * resources.getFontScale());
        setBtnMainMenuListener();

        btnSound = new CheckBox(" Sound On", skin);
        btnSound.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnSound.getImageCell().width(32).height(32);
        btnSound.getImage().setScaling(Scaling.stretch);
        setBtnSoundListener(btnSound);

        btnMusic = new CheckBox(" Music On", skin);
        btnMusic.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnMusic.getImageCell().width(32).height(32);
        btnMusic.getImage().setScaling(Scaling.stretch);
        setBtnMusicListener(btnMusic);

        btnShowRanges = new CheckBox(" Show Ranges", skin);
        btnShowRanges.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnShowRanges.getImageCell().width(32).height(32);
        btnShowRanges.getImage().setScaling(Scaling.stretch);
        setBtnShowRangesListener(btnShowRanges);

        Label lblVolZero = new Label("0", skin);
        lblVolZero.setFontScale(0.35f * resources.getFontScale());

        Label lblVolHundred = new Label("100", skin);
        lblVolHundred.setFontScale(0.35f * resources.getFontScale());

        Label lblVol = new Label("Volume", skin);
        lblVol.setFontScale(0.5f * resources.getFontScale());

        LDSlider volSlider = createVolSlider(skin);

        Label lblSpeedZero = new Label("0", skin);
        lblSpeedZero.setFontScale(0.35f * resources.getFontScale());

        Label lblSpeedTwo = new Label("2x ", skin);
        lblSpeedTwo.setFontScale(0.35f * resources.getFontScale());

        Label lblSpeed = new Label("Speed", skin);
        lblSpeed.setFontScale(0.5f * resources.getFontScale());

        LDSlider speedSlider = createSpeedSlider(skin);

        mainTable.add(btnShowRanges).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        mainTable.add(btnMusic).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        mainTable.add(btnSound).colspan(2).left().spaceLeft(15);

        mainTable.row();

        mainTable.add(lblVolZero).left();
        mainTable.add(lblVol);
        mainTable.add(lblVolHundred).right();

        mainTable.row();

        mainTable.add(volSlider).colspan(3).width(300).height(22);
        volSlider.setSize(300,22);

        mainTable.row();

        mainTable.add(lblSpeedZero).left();
        mainTable.add(lblSpeed);
        mainTable.add(lblSpeedTwo).right();

        mainTable.row();

        mainTable.add(speedSlider).colspan(3).width(300).height(22).spaceTop(2);
        speedSlider.setSize(300,22);



        container.row();
        container.add(btnNewGame).size(150,45).spaceTop(10).padLeft(5);
        container.add(btnMainMenu).size(150,45).spaceTop(10);

        if(presenter.isDebug()) {
            ImageButton btnDebug = new ImageButton(skin, "debug");
            btnDebug.setSize(50, 50);
            btnDebug.getImageCell().size(30, 30);
            btnDebug.setPosition(80, lblTitleY - 30);
            addActor(btnDebug);
            setBtnDebugListener(btnDebug);
        }

        ImageButton btnClose = new ImageButton(skin, "cancel");
        btnClose.setSize(50, 50);
        btnClose.getImageCell().size(28, 28);
        btnClose.setPosition(getStage().getViewport().getWorldWidth() - 131, lblTitleY - 30);
        addActor(btnClose);
        setBtnCloseListener(btnClose);

        btnRemoveAds = new ImageButton(skin, "remove_ads_red");
        btnRemoveAds.setSize(50, 50);
        btnRemoveAds.getImageCell().size(40, 40);
        btnRemoveAds.setPosition(getStage().getViewport().getWorldWidth() - 131, btnClose.getY(Align.bottom) - 10, Align.topLeft);
        setBtnRemoveAdsListener(btnRemoveAds);
        addAdsBtnToStage();

        Logger.info("Options View: controls created");
    }

    private void addAdsBtnToStage(){
        if(presenter.isAdsRemovalPurchasable() && !btnRemoveAds.hasParent()) {
            addActor(btnRemoveAds);
        }
    }

    private void removeAdsBtnFromStage(){
        removeActor(btnRemoveAds);
    }

    @Override
    public void setPurchaseManagerInstalled(boolean installed){
        if(installed) {
            addAdsBtnToStage();
        }
    }

    @Override
    public void adRemovalPurchased(boolean purchased) {
        if(purchased){
            removeAdsBtnFromStage();
        } else {
            addAdsBtnToStage();
        }
    }

    private LDSlider createVolSlider(Skin skin){
        LDProgressBarPadding progressBarPadding = new LDProgressBarPadding(2,2,2,2);
        LDProgressBar progressBar = new LDProgressBar(0,1, 0.000001f, progressBarPadding, skin);
        LDSlider slider = new LDSlider(progressBar, skin, new Vector2(33, 24) );
        volSliderListener(slider.getSlider());
        slider.setValue(presenter.getMasterVolume());
        return slider;
    }

    private LDSlider createSpeedSlider(Skin skin){
        LDProgressBarPadding progressBarPadding = new LDProgressBarPadding(2,2,2,2);
        LDProgressBar progressBar = new LDProgressBar(0,Resources.MAX_GAME_SPEED, 0.000001f, progressBarPadding, skin);
        LDSlider slider = new LDSlider(progressBar, skin, new Vector2(33, 24) );
        speedSliderListener(slider.getSlider());
        slider.setValue(presenter.getGameSpeed());
        return slider;
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

    private void setBtnCloseListener(Button btnClose) {

        btnClose.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.closeOptions();
            }
        });

    }

    private void setBtnNewGameListener() {

        btnNewGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.newGame();
            }
        });

    }

    private void setBtnMainMenuListener() {

        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.mainMenu();
            }
        });

    }

    private void setBtnDebugListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.debug();
            }
        });

    }

    private void setBtnShowRangesListener(Button btnShowRanges) {

        btnShowRanges.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showRangesPressed();
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

    private void volSliderListener(final Slider slider) {
        slider.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;
                presenter.volumeChanged(slider.getValue());
            }

        });

    }

    private void speedSliderListener(final Slider slider) {

        slider.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;
                presenter.speedChanged(slider.getValue());
            }

        });
    }

    @Override
    public void optionsState() {

        this.setVisible(true);
    }

    @Override
    public void standByState() {

        this.setVisible(false);

    }

    @Override
    public void setBtnShowRangesOn(boolean showRangesOn) {

        btnShowRanges.setChecked(showRangesOn);

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
