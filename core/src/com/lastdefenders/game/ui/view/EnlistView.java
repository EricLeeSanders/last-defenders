package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.combat.tower.TowerTurret;
import com.lastdefenders.game.ui.presenter.EnlistPresenter;
import com.lastdefenders.game.ui.view.interfaces.IEnlistView;
import com.lastdefenders.game.ui.view.widgets.EnlistButton;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View class for Enlisting. Shows Enlisting window as well as the options to
 * place the tower.
 *
 * @author Eric
 */
public class EnlistView extends Group implements IEnlistView, InputProcessor {

    private Array<EnlistButton> enlistButtons = new Array<>(7);
    private ImageButton btnPlacingCancel, btnPlace, btnRotate;
    private ImageButton btnScrollUp;
    private ImageButton btnScrollDown;
    private EnlistPresenter presenter;
    private Group choosingGroup;
    private Label lblMoney;
    private ScrollPane scroll;
    private Resources resources;

    public EnlistView(EnlistPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.resources = resources;
        this.setTransform(false);
    }

    public void init(){
        createControls();
    }

    /**
     * Creates the controls
     */
    private void createControls() {

        Logger.info("Enlist View: creating controls");

        Skin skin = resources.getSkin();

        choosingGroup = new Group();
        choosingGroup.setTransform(false);
        addActor(choosingGroup);

        Table container = new Table();
        container.setTransform(false);
        container.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
        choosingGroup.addActor(container);
        Table enlistTable = new Table();
        enlistTable.setTransform(false);

        scroll = new ScrollPane(enlistTable, skin);
        enlistTable.defaults().expand().fill();

        container.add(scroll).expand().fill();
        container.setBackground(skin.getDrawable("main-panel"));

        Label lblTitle = new Label("Enlist", skin);
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - 30;
        lblTitle.setFontScale(.9f * resources.getFontScale());
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        choosingGroup.addActor(lblTitle);

        LabelStyle lblMoneyStyle = new LabelStyle(skin.get("money_label", LabelStyle.class));
        lblMoneyStyle.background.setLeftWidth(60);
        lblMoneyStyle.background.setTopHeight(10);
        lblMoney = new Label("$0", lblMoneyStyle);
        lblMoney.setSize(200, 52);
        lblMoney.setPosition(100, 10);
        lblMoney.setAlignment(Align.left);
        lblMoney.setFontScale(0.6f * resources.getFontScale());
        choosingGroup.addActor(lblMoney);

        // The actual values for health, attack, range, and speed are not used for the UI display.
        // TODO attribute scale should be stored somewhere.
        createTowerButton(enlistTable, skin, "Rifle", TowerRifle.COST, 4, 4, 5, 3);
        createTowerButton(enlistTable, skin, "Machine Gun", TowerMachineGun.COST, 1, 4, 4, 8);
        createTowerButton(enlistTable, skin, "Sniper", TowerSniper.COST, 7, 8, 10, 1);
        enlistTable.row();
        createTowerButton(enlistTable, skin, "Flame Thrower", TowerFlameThrower.COST, 7, 4, 6, 2);
        createTowerButton(enlistTable, skin, "Rocket Launcher", TowerRocketLauncher.COST, 10, 4, 6,
            1);
        createTowerButton(enlistTable, skin, "Humvee", TowerHumvee.COST, 3, 7, 7, 8);
        enlistTable.row();
        createTowerButton(enlistTable, skin, "Tank", TowerTank.COST, 10, 10, 8, 10);

        ImageButton btnCancel = new ImageButton(skin, "cancel");
        btnCancel.setSize(64, 64);
        btnCancel.getImageCell().size(35, 36);
        btnCancel.getImage().setScaling(Scaling.stretch);
        btnCancel.setPosition(getStage().getViewport().getWorldWidth() - 75, getStage().getViewport().getWorldHeight() - 75);
        choosingGroup.addActor(btnCancel);
        setCancelListener(btnCancel);

        btnScrollUp = new ImageButton(skin, "arrow-small-up");
        btnScrollUp.setSize(64, 64);
        btnScrollUp.getImageCell().size(35, 30);
        btnScrollUp.getImage().setScaling(Scaling.stretch);
        btnScrollUp.setPosition(getStage().getViewport().getWorldWidth() - 75, (getStage().getViewport().getWorldHeight() / 2) + 20);
        choosingGroup.addActor(btnScrollUp);

        btnScrollDown = new ImageButton(skin, "arrow-small-down");
        btnScrollDown.setSize(64, 64);
        btnScrollDown.getImageCell().size(35, 30);
        btnScrollDown.getImage().setScaling(Scaling.stretch);
        btnScrollDown
            .setPosition(getStage().getViewport().getWorldWidth() - 75, (getStage().getViewport().getWorldHeight() / 2) - 84);
        choosingGroup.addActor(btnScrollDown);

        btnPlace = new ImageButton(skin, "select");
        btnPlace.setSize(50, 50);
        btnPlace.getImageCell().size(30, 23);
        btnPlace.getImage().setScaling(Scaling.stretch);
        btnPlace.setPosition(getStage().getViewport().getWorldWidth() - 60, 10);
        setPlaceListener();
        addActor(btnPlace);

        btnPlacingCancel = new ImageButton(skin, "cancel");
        btnPlacingCancel.setSize(50, 50);
        btnPlacingCancel.getImageCell().size(25, 26);
        btnPlacingCancel.getImage().setScaling(Scaling.stretch);
        btnPlacingCancel.setPosition(getStage().getViewport().getWorldWidth() - 60, btnPlace.getY() + 60);
        setCancelListener(btnPlacingCancel);
        addActor(btnPlacingCancel);

        btnRotate = new ImageButton(skin, "rotate");
        btnRotate.setSize(50, 50);
        btnRotate.getImageCell().size(34, 32);
        btnRotate.getImage().setScaling(Scaling.stretch);
        btnRotate.setPosition(getStage().getViewport().getWorldWidth() - 60, btnPlacingCancel.getY() + 60);
        setRotateListener();
        addActor(btnRotate);
        Logger.info("Enlist View: controls created");
    }

    /**
     * Creates a tower button and adds it to the map
     */
    private void createTowerButton(Table enlistTable, Skin skin, String towerName,
        Integer towerCost, int attack, int health, int range, int speed) {

        EnlistButton towerButton = new EnlistButton(skin, attack, health, range, speed, towerName,
            towerCost, resources.getFontScale());
        enlistTable.add(towerButton).size(120, 195).spaceBottom(5);
        String filteredTowerName = towerName.replaceAll(" ", "");
        setTowerListener(towerButton, filteredTowerName);
        enlistButtons.add(towerButton);
    }

    /**
     * Updates the tower buttons to disable/enable.
     */
    private void updateTowerButtons() {

        Logger.info("Enlist View: updating tower buttons");
        for (EnlistButton button : enlistButtons) {
            boolean affordable = presenter.canAffordTower(button.cost);
            button.button.setDisabled(!affordable);
        }

    }

    /**
     * Checks if the rotate button is pressed and calls to rotate the tower
     */
    @Override
    public void act(float delta) {

        super.act(delta);
        if (btnRotate.isPressed()) {
            presenter.rotateTower(delta);
        }
        if (btnScrollUp.isPressed()) {
            scroll.setScrollY(scroll.getScrollY() - (scroll.getHeight() * 2 * delta));
        }
        if (btnScrollDown.isPressed()) {
            scroll.setScrollY(scroll.getScrollY() + (scroll.getHeight() * 2 * delta));
        }
    }

    private void setRotateListener() {

        btnRotate.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                super.touchDown(event, x, y, pointer, button);
                return true;
            }
        });
    }

    private void setTowerListener(final EnlistButton enlistButton, final String tower) {

        enlistButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.createTower(tower);
            }
        });
    }

    private void setPlaceListener() {

        btnPlace.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.placeTower();
            }
        });
    }

    private void setCancelListener(Button button) {

        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.cancel();

            }
        });
    }

    private void moveTower(float x, float y) {

        presenter.moveTower(x,y);
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        moveTower(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        moveTower(screenX, screenY);
        return false;

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        return false;
    }

    @Override
    public void enlistingState() {

        updateTowerButtons();
        lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
        choosingGroup.setVisible(true);
        btnPlacingCancel.setVisible(false);
        this.setVisible(true);
    }

    @Override
    public void placingTowerState() {

        btnPlacingCancel.setVisible(true);
        choosingGroup.setVisible(false);

    }

    @Override
    public void standByState() {

        btnPlacingCancel.setVisible(false);
        choosingGroup.setVisible(false);
        btnRotate.setVisible(false);
        btnPlace.setVisible(false);
        this.setVisible(false);
    }

    @Override
    public void showBtnPlace() {

        btnPlace.setVisible(true);

    }

    @Override
    public void showBtnRotate() {

        btnRotate.setVisible(true);

    }
}
