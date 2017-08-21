package com.foxholedefense.levelselect.ui;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.foxholedefense.levelselect.LevelName;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import java.util.Iterator;

/**
 * View for the Level Select Menu
 *
 * @author Eric
 */
public class LevelSelectView extends Group {

    private static final int MAX_LEVELS = 6;

    private LevelSelectPresenter presenter;
    private Label lblLevel;
    private ObjectMap<LevelName, Image> levels = new ObjectMap<>(MAX_LEVELS);
    private Group levelGroup;
    private FHDAudio audio;
    private LevelName selectedLevel;

    public LevelSelectView(LevelSelectPresenter presenter, Resources resources, FHDAudio audio) {

        this.presenter = presenter;
        this.audio = audio;
        TextureAtlas levelSelectAtlas = resources
            .getAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);
        Skin skin = resources.getSkin();
        createControls(levelSelectAtlas, skin);
        levelGroup = new Group();
        this.addActor(levelGroup);
        levelGroup.setVisible(false);
        levelGroup.setTransform(false);
        this.setTransform(false);
        createConfirmLevelControls(levelSelectAtlas, skin);
    }

    private void createControls(TextureAtlas levelSelectAtlas, Skin skin) {

        Logger.info("Level select view: creating controls");

        ImageButton btnLevel1 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel1.setSize(64, 64);
        btnLevel1.setPosition(240 - (btnLevel1.getWidth() / 2), 40);
        this.addActor(btnLevel1);
        setBtnLevelListener(btnLevel1, LevelName.THE_GREENLANDS);

        ImageButton btnLevel2 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel2.setSize(64, 64);
        btnLevel2.setPosition(280 - (btnLevel2.getWidth() / 2), 100);
        this.addActor(btnLevel2);
        setBtnLevelListener(btnLevel2, LevelName.SERPENTINE_RIVER);

        ImageButton btnLevel3 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel3.setSize(64, 64);
        btnLevel3.setPosition(400 - (btnLevel3.getWidth() / 2), 70);
        this.addActor(btnLevel3);
        setBtnLevelListener(btnLevel3, LevelName.THE_GOLD_COAST);

        ImageButton btnLevel4 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel4.setSize(64, 64);
        btnLevel4.setPosition(528 - (btnLevel4.getWidth() / 2), 85);
        this.addActor(btnLevel4);
        setBtnLevelListener(btnLevel4, LevelName.STARFISH_ISLAND);

        ImageButton btnLevel5 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel5.setSize(64, 64);
        btnLevel5.setPosition(467 - (btnLevel5.getWidth() / 2), 228);
        this.addActor(btnLevel5);
        setBtnLevelListener(btnLevel5, LevelName.TUNDRA);

        ImageButton btnLevel6 = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("pointer")));
        btnLevel6.setSize(64, 64);
        btnLevel6.setPosition(380 - (btnLevel6.getWidth() / 2), 300);
        this.addActor(btnLevel6);
        setBtnLevelListener(btnLevel6, LevelName.THE_BADLANDS);

        ImageButton btnMenu = new ImageButton(skin, "arrow-left");
        btnMenu.setSize(64, 64);
        btnMenu.getImageCell().size(40, 27);
        btnMenu.getImage().setScaling(Scaling.stretch);
        btnMenu.setPosition(15, 15);
        this.addActor(btnMenu);
        setBtnMenuListener(btnMenu);

        Logger.info("Level select view: controls created");
    }

    public void setBackground(TextureAtlas levelSelectAtlas) {

        Image background = new Image(levelSelectAtlas.findRegion("background"));
        background.setFillParent(true);
        this.getStage().addActor(background);
        background.setZIndex(0);
    }

    private void createConfirmLevelControls(TextureAtlas levelSelectAtlas, Skin skin) {

        Logger.info("Level select view: creating confirm level controls");

        for(LevelName levelName : LevelName.values()){
            createLevelImage(levelName, levelSelectAtlas);
        }

        ImageButton btnMap = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_icon")));
        btnMap.setSize(64, 64);
        btnMap.setPosition(15, 15);
        levelGroup.addActor(btnMap);
        setBtnMapListener(btnMap);

        ImageButton btnPlay = new ImageButton(skin, "arrow-right");
        btnPlay.setSize(64, 64);
        btnPlay.getImageCell().size(40, 27);
        btnPlay.getImage().setScaling(Scaling.stretch);
        btnPlay.setPosition(Resources.VIRTUAL_WIDTH - btnPlay.getWidth() - 15, 15);
        levelGroup.addActor(btnPlay);
        setBtnPlayListener(btnPlay);

        lblLevel = new Label("LEVEL X", skin);
        lblLevel.setPosition((Resources.VIRTUAL_WIDTH / 2),
            Resources.VIRTUAL_HEIGHT - lblLevel.getHeight() - 25, Align.center);
        lblLevel.setAlignment(Align.center);
        levelGroup.addActor(lblLevel);

        Logger.info("Level select view: confirm level controls created");
    }

    private void createLevelImage(LevelName levelName, TextureAtlas levelSelectAtlas){

        Image level = new Image(levelSelectAtlas.findRegion(levelName.toString()));
        level.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
        level.setVisible(false);
        levelGroup.addActor(level);
        levels.put(levelName, level);
    }

    private void showConfirmWindow() {

        Logger.info("Level select view: show confirm window");

        for (LevelName levelName : levels.keys()) {
            levels.get(levelName).setVisible(false);
        }

        lblLevel.setText(selectedLevel.getName());
        levels.get(selectedLevel).setVisible(true);
    }

    private void setBtnMenuListener(Button btnMenu) {

        btnMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(FHDSound.LARGE_CLICK);
                presenter.mainMenu();

            }
        });
    }

    private void setBtnLevelListener(Button button, final LevelName level) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(FHDSound.SMALL_CLICK);
                selectedLevel = level;
                levelGroup.setVisible(true);
                showConfirmWindow();

            }
        });
    }

    private void setBtnMapListener(Button btnMap) {

        btnMap.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(FHDSound.LARGE_CLICK);
                levelGroup.setVisible(false);

            }
        });
    }

    private void setBtnPlayListener(Button btnPlay) {

        btnPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(FHDSound.LARGE_CLICK);
                presenter.loadLevel(selectedLevel);

            }
        });
    }
}
