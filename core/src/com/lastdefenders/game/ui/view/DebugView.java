package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.ui.presenter.DebugPresenter;
import com.lastdefenders.game.ui.view.interfaces.IDebugView;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 3/12/2017.
 */

public class DebugView extends Group implements IDebugView {

    private DebugPresenter presenter;
    private Label framesLabel;
    private CheckBox btnShowTextureBoundaries, btnShowFPS;
    private TextButton btnCrash, btnAssets, btnShowTutorial, btnRemoveAdPref;;
    private Resources resources;
    private Table mainTable;
    private boolean assetsDispalyed = false;

    public DebugView(DebugPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        setTransform(false);
    }

    public void init(){
        createControls();
        addOptionControlsToMainTable();
    }
    private void addOptionControlsToMainTable(){
        mainTable.clearChildren();

        mainTable.add(btnShowFPS).left().spaceLeft(15).spaceBottom(10).colspan(2);

        mainTable.row();

        mainTable.add(btnShowTextureBoundaries).left().spaceLeft(15).spaceBottom(10).colspan(2);
        
        mainTable.row();

        mainTable.add(btnShowTutorial).left().spaceLeft(15).spaceBottom(10).size(150,45);

        mainTable.add(btnRemoveAdPref).left().spaceLeft(15).spaceBottom(10).size(150,45);

        mainTable.row();

        mainTable.add(btnCrash).left().spaceLeft(15).spaceBottom(10).size(150,45);

        mainTable.add(btnAssets).left().spaceLeft(15).spaceBottom(10).size(150,45);
    }
    private void createControls() {

        Logger.info("Debug View: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(500, 360);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center);
        addActor(container);

        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("hollow"));
        mainTable.setTransform(false);
        container.add(mainTable);

        Label lblTitle = new Label("Debug", skin);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setHeight(60);
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        addActor(lblTitle);

        framesLabel = new Label("x", skin);
        framesLabel.setFontScale(0.35f * resources.getFontScale());
        framesLabel.setColor(1f, 1f, 1f, 0.30f);
        framesLabel.setPosition(200, 320);

        ImageButton btnClose = new ImageButton(skin, "cancel");
        btnClose.setSize(50, 50);
        btnClose.getImageCell().size(28, 28);
        btnClose.setPosition(getStage().getViewport().getWorldWidth() - 131, lblTitleY - 30);
        addActor(btnClose);
        setBtnCloseListener(btnClose);

        btnShowFPS = new CheckBox(" Show FPS", skin);
        btnShowFPS.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnShowFPS.getImageCell().width(32).height(32);
        btnShowFPS.getImage().setScaling(Scaling.stretch);
        setBtnShowFPSListener(btnShowFPS);

        btnShowTextureBoundaries = new CheckBox(" Show Boundaries", skin);
        btnShowTextureBoundaries.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnShowTextureBoundaries.getImageCell().width(32).height(32);
        btnShowTextureBoundaries.getImage().setScaling(Scaling.stretch);
        setBtnShowTextureBoundariesListener(btnShowTextureBoundaries);

        btnShowTutorial = new TextButton(" Show Tutorial Next Start", skin);
        btnShowTutorial.getLabel().setFontScale(0.30f * resources.getFontScale());
        btnShowTutorial.setSize(150, 45);
        setBtnShowTutorialListener(btnShowTutorial);

        btnRemoveAdPref = new TextButton(" Remove Ad Preferences", skin);
        btnRemoveAdPref.getLabel().setFontScale(0.30f * resources.getFontScale());
        btnRemoveAdPref.setSize(150, 45);
        setBtnRemoveAdPrefListener(btnRemoveAdPref);

        btnCrash = new TextButton("Test Crash", skin);
        btnCrash.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnCrash.setSize(150, 45);
        setBtnCrashListener(btnCrash);

        btnAssets = new TextButton("Assets", skin);
        btnAssets.getLabel().setFontScale(0.5f * resources.getFontScale());
        btnAssets.setSize(150, 45);
        setBtnAssetsListener(btnAssets);

        Logger.info("Debug View: controls created");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        framesLabel
            .setText("FPS: " + Integer.valueOf(Gdx.graphics.getFramesPerSecond()).toString());
    }

    private void setBtnRemoveAdPrefListener(Button button){
        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.removeAdPreferences();
            }
        });
    }

    private void setBtnCrashListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.crash();
            }
        });

    }

    private void setBtnAssetsListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                showAssets();
            }
        });

    }

    private void setBtnShowFPSListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showFPSPressed();
            }
        });
    }

    private void setBtnShowTextureBoundariesListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showTextureBoundariesPressed();
            }
        });
    }

    private void setBtnShowTutorialListener(Button button){

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.showTutorialPressed();
            }
        });
    }

    private void setBtnCloseListener(Button btnClose) {

        btnClose.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                if(assetsDispalyed){
                    assetsDispalyed = false;
                    addOptionControlsToMainTable();
                } else {
                    presenter.resumeGame();
                }
            }
        });

    }

    @Override
    public void showFPS(boolean show) {

        if (show) {
            this.getParent().addActor(framesLabel);
        } else {
            this.getParent().removeActor(framesLabel);
        }
    }

    @Override
    public void setFPSChecked(boolean isChecked) {

        btnShowFPS.setChecked(isChecked);
    }

    @Override
    public void setTextureBoundariesChecked(boolean isChecked) {

        btnShowTextureBoundaries.setChecked(isChecked);
    }

    @Override
    public void setTutorialChecked(boolean isChecked) {

        btnShowTutorial.setChecked(isChecked);
    }

    @Override
    public void debugState() {

        this.setVisible(true);
    }

    @Override
    public void standByState() {

        this.setVisible(false);
    }

    private void showAssets(){
        assetsDispalyed = true;
        ScrollPane assetScrollPane = createAssetScrollPane();
        mainTable.clearChildren();
        mainTable.add(assetScrollPane).expand().fill().pad(10);
    }

    private ScrollPane createAssetScrollPane(){

        Table table = new Table();
        table.setTransform(false);

        Array<String> assetNames = resources.getManager().getAssetNames();
        assetNames.sort();
        for(String name : assetNames){
            Label label = new Label(name, resources.getSkin());
            label.setFontScale(0.35f * resources.getFontScale());
            table.row();
            table.add(label).left();
        }
        ScrollPane scrollPane = new ScrollPane(table, resources.getSkin());

        return scrollPane;
    }
}
