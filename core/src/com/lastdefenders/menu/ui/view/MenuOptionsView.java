package com.lastdefenders.menu.ui.view;

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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.menu.ui.MenuPresenter;
import com.lastdefenders.menu.ui.view.interfaces.IMenuOptionsView;
import com.lastdefenders.ui.view.widget.LDSlider;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 12/19/2017.
 */

public class MenuOptionsView extends Group implements IMenuOptionsView {

    private MenuPresenter presenter;
    private CheckBox btnSound, btnMusic;
    private ImageButton btnClose;
    private Resources resources;


    public MenuOptionsView(MenuPresenter presenter, Resources resources){

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){
        createControls();
    }

    private void createControls(){

        Logger.info("Options View: creating controls");

        Skin skin = resources.getSkin();
        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center );
        addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        container.add(mainTable);


        Label lblTitle = new Label("Options", skin);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setHeight(60);
        float x = container.getX(Align.center);
        float y = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(x, y, Align.center);
        addActor(lblTitle);

        btnClose = new ImageButton(skin, "cancel");
        btnClose.setSize(50, 50);
        btnClose.getImageCell().size(25, 26);
        btnClose.getImage().setScaling(Scaling.stretch);
        btnClose.setPosition(500, 300);
        addActor(btnClose);
        setBtnCloseListener();

        btnSound = new CheckBox(" Sound On", skin);
        btnSound.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnSound.getLabel().setAlignment(Align.center);
        btnSound.getImageCell().width(32).height(32);
        btnSound.getImage().setScaling(Scaling.stretch);
        setBtnSoundListener(btnSound);

        btnMusic = new CheckBox(" Music On", skin);
        btnMusic.getLabel().setFontScale(0.45f * resources.getFontScale());
        btnMusic.getImageCell().width(32).height(32);
        btnMusic.getImage().setScaling(Scaling.stretch);
        setBtnMusicListener(btnMusic);

        Label lblVolZero = new Label("0", skin);
        lblVolZero.setAlignment(Align.center);
        lblVolZero.setFontScale(0.35f * resources.getFontScale());

        Label lblVolHundred = new Label("100", skin);
        lblVolHundred.setAlignment(Align.center);
        lblVolHundred.setFontScale(0.35f * resources.getFontScale());

        Label lblVol = new Label("Volume", skin);
        lblVol.setFontScale(0.5f * resources.getFontScale());

        LDSlider volSlider = createVolSlider(skin);

        mainTable.add(btnMusic).colspan(2).left().spaceLeft(15).spaceBottom(10);

        mainTable.row();

        mainTable.add(btnSound).colspan(2).left().spaceLeft(15).spaceBottom(15);

        mainTable.row();

        mainTable.add(lblVolZero).left().bottom();
        mainTable.add(lblVol);
        mainTable.add(lblVolHundred).right().bottom();

        mainTable.row();

        mainTable.add(volSlider).colspan(3).spaceTop(7).width(300).height(22);
        volSlider.setSize(300,22);

        Logger.info("Options View: controls created");
    }

    private LDSlider createVolSlider(Skin skin) {


        LDProgressBarPadding progressBarPadding = new LDProgressBarPadding(2,2,2,2);
        LDProgressBar progressBar = new LDProgressBar(0,1, 0.000001f, progressBarPadding, skin);
        LDSlider slider = new LDSlider(progressBar, skin, new Vector2(33, 24) );
        addActor(slider);
        slider.setValue(presenter.getMasterVolume());
        volSliderListener(slider.getSlider());

        return slider;
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

    private void setBtnCloseListener() {

        btnClose.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.closeMenuOptions();
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
