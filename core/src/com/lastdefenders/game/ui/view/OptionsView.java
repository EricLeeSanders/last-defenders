package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.ui.presenter.OptionsPresenter;
import com.lastdefenders.game.ui.view.interfaces.IOptionsView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for the options window
 *
 * @author Eric
 */
public class OptionsView extends Group implements IOptionsView {

    private OptionsPresenter presenter;
    private TextButton btnClose, btnNewGame, btnMainMenu;
    private CheckBox btnShowRanges, btnSound, btnMusic;
    private Image volSliderBg, speedSliderBg;
    private float volSliderEndPos, volSliderStartPos, speedSliderEndPos, speedSliderStartPos;
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
        mainTable.setTransform(false);
        mainTable.setBackground(skin.getDrawable("hollow"));
        container.add(mainTable).colspan(3).size(350, 226);

        Label lblTitle = new Label("OPTIONS", skin);
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        addActor(lblTitle);


        btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnNewGameListener();

        btnClose = new TextButton("CLOSE", skin);
        btnClose.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnCloseListener();

        btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f * resources.getFontScale());
        setBtnMainMenuListener();

        TextButton btnDebug = new TextButton("DEBUG", skin);
        btnDebug.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnDebug.setSize(75, 35);
        btnDebug.setPosition((getStage().getViewport().getWorldWidth() / 2) + 155, lblTitleY - 30);
        addActor(btnDebug);
        setBtnDebugListener(btnDebug);

        btnSound = new CheckBox(" SOUND ON", skin);
        btnSound.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnSound.getImageCell().width(32).height(32);
        btnSound.getImage().setScaling(Scaling.stretch);
        setBtnSoundListener(btnSound);

        btnMusic = new CheckBox(" MUSIC ON", skin);
        btnMusic.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnMusic.getImageCell().width(32).height(32);
        btnMusic.getImage().setScaling(Scaling.stretch);
        setBtnMusicListener(btnMusic);

        btnShowRanges = new CheckBox(" SHOW RANGES", skin);
        btnShowRanges.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnShowRanges.getImageCell().width(32).height(32);
        btnShowRanges.getImage().setScaling(Scaling.stretch);
        setBtnShowRangesListener(btnShowRanges);

        Label lblVolZero = new Label("0", skin);
        lblVolZero.setFontScale(0.35f * resources.getFontScale());

        Label lblVolHundred = new Label("100", skin);
        lblVolHundred.setFontScale(0.35f * resources.getFontScale());

        Label lblVol = new Label("VOLUME", skin);
        lblVol.setFontScale(0.5f * resources.getFontScale());

        WidgetGroup volSlider = createVolSlider(skin, resources);

        Label lblSpeedZero = new Label("0", skin);
        lblSpeedZero.setFontScale(0.35f * resources.getFontScale());

        Label lblSpeedTwo = new Label("2X ", skin);
        lblSpeedTwo.setFontScale(0.35f * resources.getFontScale());

        Label lblSpeed = new Label("SPEED", skin);
        lblSpeed.setFontScale(0.5f * resources.getFontScale());

        WidgetGroup speedSlider = createSpeedSlider(skin, resources);

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

        mainTable.add(volSlider).colspan(3).width(300).height(18);

        mainTable.row();

        mainTable.add(lblSpeedZero).left();
        mainTable.add(lblSpeed);
        mainTable.add(lblSpeedTwo).right();

        mainTable.row();

        mainTable.add(speedSlider).colspan(3).width(300).height(18).spaceTop(2);



        container.row();
        container.add(btnClose).size(150,45).spaceTop(10);
        container.add(btnNewGame).size(150,45).spaceTop(10).spaceLeft(5).spaceRight(5);
        container.add(btnMainMenu).size(150,45).spaceTop(10);

        Logger.info("Options View: controls created");
    }

    private WidgetGroup createSpeedSlider(Skin skin, Resources resources) {

        WidgetGroup group = new WidgetGroup();
        group.setTransform(false);

        Slider speedSlider = new Slider(0, 1f, 0.01f, false, skin);
        speedSlider.getStyle().knob.setMinWidth(33);
        speedSlider.getStyle().knob.setMinHeight(24);
        speedSlider.getStyle().background.setMinHeight(22);
        speedSlider.getStyle().background.setMinWidth(300);
        float speedValue = presenter.getGameSpeed() / Resources.MAX_GAME_SPEED;
        speedSlider.setValue(speedValue);
        speedSlider.setSize(300, 22);
        speedSliderListener(speedSlider);

        Image speedSliderFull = new Image(resources.getSkin().getRegion("slider-full"));
        speedSliderFull.setSize(299, 18);
        speedSliderFull.setPosition(0, 2);
        speedSliderFull.setAlign(Align.center);

        speedSliderBg = new Image(resources.getSkin().getRegion("slider-bg"));
        speedSliderBg.setSize(299, 18);
        speedSliderBg.setPosition(0, 2);
        speedSliderBg.setAlign(Align.center);

        this.speedSliderStartPos = speedSliderBg.getX() + 3;
        this.speedSliderEndPos = speedSliderBg.getX() + speedSliderBg.getWidth() - 6;

        group.addActor(speedSliderFull);
        group.addActor(speedSliderBg);
        group.addActor(speedSlider);

        float speedStartX = speedSliderStartPos + speedSliderEndPos * speedValue;
        speedSliderBg.setX(speedStartX);
        speedSliderBg.setWidth(speedSliderEndPos - speedSliderEndPos * speedValue);

        return group;
    }

    private WidgetGroup createVolSlider(Skin skin, Resources resources) {

        WidgetGroup group = new WidgetGroup();
        group.setTransform(false);

        Slider volumeSlider = new Slider(0, 1f, 0.01f, false, skin);
        volumeSlider.getStyle().knob.setMinWidth(33);
        volumeSlider.getStyle().knob.setMinHeight(24);
        volumeSlider.getStyle().background.setMinHeight(22);
        volumeSlider.getStyle().background.setMinWidth(300);
        volumeSlider.setValue(presenter.getMasterVolume());
        volumeSlider.setSize(300, 22);
        volSliderListener(volumeSlider);

        Image volSliderFull = new Image(resources.getSkin().getRegion("slider-full"));
        volSliderFull.setSize(299, 18);
        volSliderFull.setPosition(0, 2);
        volSliderFull.setAlign(Align.center);

        volSliderBg = new Image(resources.getSkin().getRegion("slider-bg"));
        volSliderBg.setSize(299, 18);
        volSliderBg.setPosition(0, 2);
        volSliderBg.setAlign(Align.center);

        this.volSliderStartPos = volSliderBg.getX() + 3;
        this.volSliderEndPos = volSliderBg.getX() + volSliderBg.getWidth() - 6;

        group.addActor(volSliderFull);
        group.addActor(volSliderBg);
        group.addActor(volumeSlider);

        float volValue = (volSliderEndPos - volSliderEndPos * presenter.getMasterVolume());
        float volStartX = volSliderStartPos + volSliderEndPos * presenter.getMasterVolume();
        volSliderBg.setX(volStartX);
        volSliderBg.setWidth(volValue);

        return group;
    }

    private void setBtnCloseListener() {

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

        slider.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                super.touchDragged(event, x, y, pointer);
                moveSlider();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                super.touchDown(event, x, y, pointer, button);
                moveSlider();
                return true;
            }

            private void moveSlider() {

                float startX = volSliderStartPos + volSliderEndPos * slider.getValue();
                presenter.volumeChanged(slider.getValue());
                volSliderBg.setX(startX);
                volSliderBg.setWidth(volSliderEndPos - volSliderEndPos * slider.getValue());
            }
        });
    }

    private void speedSliderListener(final Slider slider) {

        slider.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                super.touchDragged(event, x, y, pointer);
                moveSlider();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                super.touchDown(event, x, y, pointer, button);
                moveSlider();
                return true;
            }

            private void moveSlider() {

                float startX = speedSliderStartPos + speedSliderEndPos * slider.getValue();
                presenter.speedChanged(slider.getValue() * Resources.MAX_GAME_SPEED);
                speedSliderBg.setX(startX);
                speedSliderBg.setWidth(speedSliderEndPos - speedSliderEndPos * slider.getValue());
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
