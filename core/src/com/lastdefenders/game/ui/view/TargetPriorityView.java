package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.model.actor.ai.TowerAIType;
import com.lastdefenders.game.ui.presenter.InspectPresenter;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Created by Eric on 5/30/2018.
 */

public class TargetPriorityView extends Group {

    private InspectPresenter presenter;
    private Resources resources;
    private ButtonGroup<TargetPriorityCheckBox> btnGroup;


    public TargetPriorityView(InspectPresenter presenter, Resources resources){
        this.presenter = presenter;
        this.resources = resources;
        setTransform(false);
    }

    public void init(){
        createControls();
    }

    private void createControls(){
        Logger.info("Target Priority View: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setBackground(skin.getDrawable("main-panel"));
        container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
        container.setPosition(getStage().getViewport().getWorldWidth() / 2, getStage().getViewport().getWorldHeight() / 2, Align.center);
        //table.debug();
        addActor(container);

        Table mainTable = new Table();
        mainTable.setTransform(false);
        container.add(mainTable).width(420).height(260);

        Label lblTitle = new Label("TARGET PRIORITY", skin);
        lblTitle.setFontScale(0.6f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        lblTitle.setHeight(60);
        float x = container.getX(Align.center);
        float y = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(x, y, Align.center);
        addActor(lblTitle);

        ImageButton btnClose = new ImageButton(skin, "cancel");
        setCloseListener(btnClose);
        btnClose.setSize(64, 64);
        btnClose.getImageCell().size(35, 36);
        btnClose.getImage().setScaling(Scaling.stretch);
        btnClose.setPosition(getStage().getViewport().getWorldWidth() - 75, getStage().getViewport().getWorldHeight() - 75);
        addActor(btnClose);


        TargetPriorityCheckBox btnClosest = createCheckBox(TowerAIType.CLOSEST, skin);
        TargetPriorityCheckBox btnFarthest = createCheckBox(TowerAIType.FARTHEST, skin);
        TargetPriorityCheckBox btnLeastHP = createCheckBox(TowerAIType.LEAST_HP, skin);
        TargetPriorityCheckBox btnMostHP = createCheckBox(TowerAIType.MOST_HP, skin);
        TargetPriorityCheckBox btnStrongest = createCheckBox(TowerAIType.STRONGEST, skin);
        TargetPriorityCheckBox btnWeakest = createCheckBox(TowerAIType.WEAKEST, skin);

        Label closestDescription = createCheckBoxDescription("ENEMY CLOSEST TO END", skin);
        Label farthestDescription = createCheckBoxDescription("ENEMY FARTHEST FROM END", skin);
        Label leastHPDescription = createCheckBoxDescription("ENEMY WITH LEAST HP", skin);
        Label mostHPDescription = createCheckBoxDescription("ENEMY WITH MOST HP", skin);
        Label strongestDescription = createCheckBoxDescription("ENEMY THAT DOES THE MOST DAMAGE", skin);
        Label weakestDescription = createCheckBoxDescription("ENEMY THAT DOES THE LEAST DAMAGE", skin);

        btnGroup = new ButtonGroup<>();
        btnGroup.add(btnClosest);
        btnGroup.add(btnFarthest);
        btnGroup.add(btnLeastHP);
        btnGroup.add(btnMostHP);
        btnGroup.add(btnStrongest);
        btnGroup.add(btnWeakest);
        btnGroup.setMaxCheckCount(1);
        btnGroup.setMinCheckCount(1);

        mainTable.add(btnClosest).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(closestDescription).expandX().left().spaceLeft(15).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnFarthest).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(farthestDescription).expandX().left().spaceLeft(15).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnLeastHP).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(leastHPDescription).expandX().left().spaceLeft(15).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnMostHP).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(mostHPDescription).expandX().left().spaceLeft(15).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnStrongest).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(strongestDescription).expandX().left().spaceLeft(15).spaceBottom(10);
        mainTable.row();
        mainTable.add(btnWeakest).left().spaceLeft(15).spaceBottom(10);
        mainTable.add(weakestDescription).expandX().left().spaceLeft(15).spaceBottom(10);
    }

    private TargetPriorityCheckBox createCheckBox(TowerAIType towerAIType, Skin skin){
        TargetPriorityCheckBox chkBox = new TargetPriorityCheckBox(towerAIType, skin);
        chkBox.getLabel().setFontScale(0.45f * resources.getFontScale());
        chkBox.getImageCell().width(32).height(32);
        chkBox.getImage().setScaling(Scaling.stretch);

        return chkBox;
    }

    private Label createCheckBoxDescription(String text, Skin skin){
        Label label = new Label(text, skin);
        label.setFontScale(0.35f * resources.getFontScale());
        label.pack();
        return label;
    }

    public void show(){
        this.setVisible(true);
        TowerAIType towerAIType = presenter.getTowerAIType();
        btnGroup.setChecked(formatCheckBoxLabel(towerAIType.getTitle()));
    }

    private void setCloseListener(Button btnClose) {

        btnClose.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                setVisible(false);
                TargetPriorityCheckBox chkBox = btnGroup.getChecked();
                presenter.changeTargetPriority(chkBox.getTowerAIType());

            }
        });
    }

    private static String formatCheckBoxLabel(String title){
        return " " + title;
    }

    private static class TargetPriorityCheckBox extends CheckBox {

        private TowerAIType towerAIType;

        public TargetPriorityCheckBox(TowerAIType towerAIType, Skin skin) {
            super(formatCheckBoxLabel(towerAIType.getTitle()), skin);
            this.towerAIType = towerAIType;
        }

        public TowerAIType getTowerAIType(){
            return towerAIType;
        }
    }
}
