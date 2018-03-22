package com.lastdefenders.levelselect.ui;


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
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for the Level Select Menu
 *
 * @author Eric
 */
public class LevelSelectView extends Group {

    private LevelSelectPresenter presenter;
    private Label lblLevel;
    private ObjectMap<LevelName, Image> levels = new ObjectMap<>();
    private Group levelGroup;
    private LDAudio audio;
    private LevelName selectedLevel;
    private TextureAtlas levelSelectAtlas;
    private Skin skin;

    public LevelSelectView(LevelSelectPresenter presenter, Resources resources, LDAudio audio) {

        this.presenter = presenter;
        this.audio = audio;
        this.levelSelectAtlas = resources
            .getAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);

        skin = resources.getSkin();
        setTransform(false);
    }

    public void init(){

        createControls();
        createConfirmLevelControls();
        setBackground();
    }

    private void createControls() {

        Logger.info("Level select view: creating controls");

        ImageButton btnTheGreenlands = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnTheGreenlands.setSize(52, 52);
        btnTheGreenlands.setPosition(240 - (btnTheGreenlands.getWidth() / 2), 220);
        this.addActor(btnTheGreenlands);
        setBtnLevelListener(btnTheGreenlands, LevelName.THE_GREENLANDS);

        ImageButton btnSerpentineRiver = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnSerpentineRiver.setSize(52, 52);
        btnSerpentineRiver.setPosition(150 - (btnSerpentineRiver.getWidth() / 2), 140);
        this.addActor(btnSerpentineRiver);
        setBtnLevelListener(btnSerpentineRiver, LevelName.SERPENTINE_RIVER);

        ImageButton btnTheGoldCoast = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnTheGoldCoast.setSize(52, 52);
        btnTheGoldCoast.setPosition(295 - (btnTheGoldCoast.getWidth() / 2), 145);
        this.addActor(btnTheGoldCoast);
        setBtnLevelListener(btnTheGoldCoast, LevelName.THE_GOLD_COAST);

        ImageButton btnStarfishIsland = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnStarfishIsland.setSize(52, 52);
        btnStarfishIsland.setPosition(430 - (btnStarfishIsland.getWidth() / 2), 75);
        this.addActor(btnStarfishIsland);
        setBtnLevelListener(btnStarfishIsland, LevelName.STARFISH_ISLAND);

        ImageButton btnTundra = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnTundra.setSize(52, 52);
        btnTundra.setPosition(65 - (btnTundra.getWidth() / 2), 250);
        this.addActor(btnTundra);
        setBtnLevelListener(btnTundra, LevelName.TUNDRA);

        ImageButton btnTheBadlands = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnTheBadlands.setSize(52, 52);
        btnTheBadlands.setPosition(565 - (btnTheBadlands.getWidth() / 2), 260);
        this.addActor(btnTheBadlands);
        setBtnLevelListener(btnTheBadlands, LevelName.THE_BADLANDS);

        ImageButton btnWhisperingThicket = new ImageButton(
            new TextureRegionDrawable(levelSelectAtlas.findRegion("map_select")));
        btnWhisperingThicket.setSize(52, 52);
        btnWhisperingThicket.setPosition(385 - (btnWhisperingThicket.getWidth() / 2), 240);
        this.addActor(btnWhisperingThicket);
        setBtnLevelListener(btnWhisperingThicket, LevelName.WHISPERING_THICKET);

        ImageButton btnMenu = new ImageButton(skin, "arrow-left");
        btnMenu.setSize(52, 52);
        btnMenu.getImageCell().size(40, 27);
        btnMenu.getImage().setScaling(Scaling.stretch);
        btnMenu.setPosition(15, 15);
        this.addActor(btnMenu);
        setBtnMenuListener(btnMenu);

        Logger.info("Level select view: controls created");
    }

    private void setBackground() {

        Image background = new Image(levelSelectAtlas.findRegion("background"));
        background.setFillParent(true);
        this.getStage().addActor(background);
        background.setZIndex(0);
    }

    private void createConfirmLevelControls() {

        Logger.info("Level select view: creating confirm level controls");

        levelGroup = new Group();
        addActor(levelGroup);
        levelGroup.setVisible(false);
        levelGroup.setTransform(false);

        for(LevelName levelName : LevelName.values()){
            createLevelImage(levelName);
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
        btnPlay.setPosition(getStage().getViewport().getWorldWidth() - btnPlay.getWidth() - 15, 15);
        levelGroup.addActor(btnPlay);
        setBtnPlayListener(btnPlay);

        lblLevel = new Label("", skin);
        lblLevel.setPosition(getStage().getViewport().getWorldWidth() / 2, (getStage().getViewport().getWorldHeight() / 2) + 110, Align.center);
        lblLevel.setAlignment(Align.center);
        levelGroup.addActor(lblLevel);

        Logger.info("Level select view: confirm level controls created");
    }

    private void createLevelImage(LevelName levelName){

        Image level = new Image(levelSelectAtlas.findRegion(levelName.toString()));
        level.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
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
                audio.playSound(LDSound.LARGE_CLICK);
                presenter.mainMenu();

            }
        });
    }

    private void setBtnLevelListener(Button button, final LevelName level) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(LDSound.SMALL_CLICK);
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
                audio.playSound(LDSound.LARGE_CLICK);
                levelGroup.setVisible(false);

            }
        });
    }

    private void setBtnPlayListener(Button btnPlay) {

        btnPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                audio.playSound(LDSound.LARGE_CLICK);
                presenter.loadLevel(selectedLevel);

            }
        });
    }
}
