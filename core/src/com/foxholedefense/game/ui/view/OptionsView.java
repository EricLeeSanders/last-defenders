package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.game.ui.presenter.OptionsPresenter;
import com.foxholedefense.game.ui.view.interfaces.IOptionsView;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

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

    public OptionsView(OptionsPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.setTransform(false);
        createControls(resources);
    }

    public void act(float delta) {

        super.act(delta);
        //This is a bit of a hack, but I need this here for the initial load of the screen.
        float volStartX = volSliderStartPos + volSliderEndPos * presenter.getMasterVolume();
        volSliderBg.setX(volStartX);
        volSliderBg.setWidth(volSliderEndPos - volSliderEndPos * presenter.getMasterVolume());

        float speedValue = presenter.getGameSpeed() / Resources.MAX_GAME_SPEED;
        float speedStartX = speedSliderStartPos + speedSliderEndPos * speedValue;
        speedSliderBg.setX(speedStartX);
        speedSliderBg.setWidth(speedSliderEndPos - speedSliderEndPos * speedValue);
    }

    /**
     * Create controls
     */
    private void createControls(Resources resources) {

        Logger.info("Options View: creating controls");

        Skin skin = resources.getSkin();
        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition((Resources.VIRTUAL_WIDTH / 2) - (container.getWidth() / 2),
            (Resources.VIRTUAL_HEIGHT / 2) - (container.getHeight() / 2));
        //table.debug();
        this.addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        mainTable.setBackground(skin.getDrawable("hollow"));
        container.add(mainTable);

        Label lblTitle = new Label("OPTIONS", skin);
        lblTitle
            .setPosition(container.getX() + (container.getWidth() / 2) - (lblTitle.getWidth() / 2),
                container.getY() + container.getHeight() - lblTitle.getHeight());
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(0.7f);
        this.addActor(lblTitle);

        btnClose = new TextButton("CLOSE", skin);
        btnClose.getLabel().setFontScale(0.45f);
        btnClose.pack();
        btnClose.setPosition(80, 20);
        addActor(btnClose);
        setBtnCloseListener();

        btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(0.45f);
        btnNewGame.pack();
        btnNewGame.setPosition(btnClose.getX() + btnClose.getWidth() + 15, btnClose.getY());
        addActor(btnNewGame);
        setBtnNewGameListener();

        btnMainMenu = new TextButton("MAIN MENU", skin);
        btnMainMenu.getLabel().setFontScale(0.45f);
        btnMainMenu.pack();
        btnMainMenu.setPosition(btnNewGame.getX() + btnNewGame.getWidth() + 15, btnNewGame.getY());
        addActor(btnMainMenu);
        setBtnMainMenuListener();

        TextButton btnDebug = new TextButton("DEBUG", skin);
        btnDebug.getLabel().setFontScale(0.45f);
        btnDebug.setSize(75, 35);
        btnDebug.setPosition(475, 300);
        addActor(btnDebug);
        setBtnDebugListener(btnDebug);

        btnSound = new CheckBox(" SOUND ON", skin);
        btnSound.getLabel().setFontScale(0.45f);
        btnSound.getImageCell().width(32).height(32);
        btnSound.getImage().setScaling(Scaling.stretch);
        setBtnSoundListener(btnSound);

        btnMusic = new CheckBox(" MUSIC ON", skin);
        btnMusic.getLabel().setFontScale(0.45f);
        btnMusic.getImageCell().width(32).height(32);
        btnMusic.getImage().setScaling(Scaling.stretch);
        setBtnMusicListener(btnMusic);

        btnShowRanges = new CheckBox(" SHOW RANGES", skin);
        btnShowRanges.getLabel().setFontScale(0.45f);
        btnShowRanges.getImageCell().width(32).height(32);
        btnShowRanges.getImage().setScaling(Scaling.stretch);
        setBtnShowRangesListener(btnShowRanges);

        Label lblVolZero = new Label("0", skin);
        lblVolZero.setFontScale(0.35f);

        Label lblVolHundred = new Label("100", skin);
        lblVolHundred.setFontScale(0.35f);

        Label lblVol = new Label("VOLUME", skin);
        lblVol.setFontScale(0.5f);

        Stack volumeStack = createVolSlider(skin, resources);

        Label lblSpeedZero = new Label("0", skin);
        lblSpeedZero.setFontScale(0.35f);

        Label lblSpeedTwo = new Label("2X ", skin);
        lblSpeedTwo.setFontScale(0.35f);

        Label lblSpeed = new Label("SPEED", skin);
        lblSpeed.setFontScale(0.5f);

        Stack speedStack = createSpeedSlider(skin, resources);

        //mainTable.add(btnResume).width(128).height(41).spaceBottom(10);
        mainTable.add(btnShowRanges).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        //mainTable.add(btnNewGame).width(128).height(41).spaceBottom(10);
        mainTable.add(btnMusic).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        //mainTable.add(btnMainMenu).width(128).height(41).spaceBottom(10);
        mainTable.add(btnSound).colspan(2).left().spaceLeft(15);

        mainTable.row();

        mainTable.add(lblVolZero).left();
        mainTable.add(lblVol);
        mainTable.add(lblVolHundred).right();

        mainTable.row();

        mainTable.add(volumeStack).colspan(3).width(300).height(18);

        mainTable.row();

        mainTable.add(lblSpeedZero).left();
        mainTable.add(lblSpeed);
        mainTable.add(lblSpeedTwo).right();

        mainTable.row();

        mainTable.add(speedStack).colspan(3).width(300).height(18).spaceTop(1);

        Logger.info("Options View: controls created");
    }

    private Stack createSpeedSlider(Skin skin, Resources resources) {

        Stack speedStack = new Stack();
        speedStack.setTransform(false);

        Slider speedSlider = new Slider(0, 1f, 0.01f, false, skin);
        speedSlider.getStyle().knob.setMinWidth(33);
        speedSlider.getStyle().knob.setMinHeight(24);
        speedSlider.getStyle().background.setMinHeight(22);
        speedSlider.getStyle().background.setMinWidth(300);
        float speedValue = presenter.getGameSpeed() / Resources.MAX_GAME_SPEED;
        speedSlider.setValue(speedValue);
        speedSliderListener(speedSlider);

        Image speedSliderFull = new Image(
            resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-full"));
        speedSliderFull.setSize(300, 22);

        speedSliderBg = new Image(
            resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-bg"));
        speedSliderBg.setSize(300, 22);

        this.speedSliderStartPos = speedSliderBg.getX() + 2;
        this.speedSliderEndPos = speedSliderBg.getX() + speedSliderBg.getWidth() - 6;

        speedStack.add(speedSliderFull);
        speedStack.add(speedSliderBg);
        speedStack.add(speedSlider);

        return speedStack;
    }

    private Stack createVolSlider(Skin skin, Resources resources) {

        Stack volumeStack = new Stack();
        volumeStack.setTransform(false);

        Slider volumeSlider = new Slider(0, 1f, 0.01f, false, skin);
        volumeSlider.getStyle().knob.setMinWidth(33);
        volumeSlider.getStyle().knob.setMinHeight(24);
        volumeSlider.getStyle().background.setMinHeight(22);
        volumeSlider.getStyle().background.setMinWidth(300);
        volumeSlider.setValue(presenter.getMasterVolume());
        volSliderListener(volumeSlider);

        Image volSliderFull = new Image(
            resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-full"));
        volSliderFull.setSize(300, 22);

        volSliderBg = new Image(
            resources.getAsset(Resources.SKIN_ATLAS, TextureAtlas.class).findRegion("slider-bg"));
        volSliderBg.setSize(300, 22);

        this.volSliderStartPos = volSliderBg.getX() + 2;
        this.volSliderEndPos = volSliderBg.getX() + volSliderBg.getWidth() - 6;

        volumeStack.add(volSliderFull);
        volumeStack.add(volSliderBg);
        volumeStack.add(volumeSlider);

        return volumeStack;
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
