package com.foxholedefense.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.ui.presenter.SupportPresenter;
import com.foxholedefense.game.ui.view.interfaces.ISupportView;
import com.foxholedefense.game.ui.view.widgets.SupportButton;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * View class for Support. Shows Support window as well as the options to
 * place the various Support actors.
 *
 * @author Eric
 */
public class SupportView extends Group implements ISupportView, InputProcessor {

    private Array<SupportButton> supportButtons = new Array<>(4);
    private ImageButton btnPlacingCancel;
    private ImageButton btnPlace;
    private SupportPresenter presenter;
    private Group choosingGroup;
    private Label lblMoney;

    public SupportView(SupportPresenter presenter, Skin skin) {

        this.presenter = presenter;
        choosingGroup = new Group();
        choosingGroup.setTransform(false);
        this.setTransform(false);
        addActor(choosingGroup);
        createControls(skin);
    }

    /**
     * Creates the controls
     */
    private void createControls(Skin skin) {

        Logger.info("Support View: creating controls");

        Table container = new Table();
        container.setTransform(false);
        container.setSize(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT);
        choosingGroup.addActor(container);
        Table supportTable = new Table();
        supportTable.setTransform(false);
        // table.debug();

        final ScrollPane scroll = new ScrollPane(supportTable, skin);
        supportTable.defaults().expandX();

        container.add(scroll).expand().fill().colspan(1);
        container.setBackground(skin.getDrawable("main-panel"));

        Label lblTitle = new Label("SUPPORT", skin);
        lblTitle
            .setPosition(container.getX() + (container.getWidth() / 2) - (lblTitle.getWidth() / 2),
                container.getY() + container.getHeight() - lblTitle.getHeight() + 1);
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(.9f);
        choosingGroup.addActor(lblTitle);

        LabelStyle lblMoneyStyle = new LabelStyle(skin.get("money_label", LabelStyle.class));
        lblMoneyStyle.background.setLeftWidth(60);
        lblMoneyStyle.background.setTopHeight(10);
        lblMoney = new Label("0", lblMoneyStyle);
        lblMoney.setSize(200, 52);
        lblMoney.setPosition(100, 10);
        lblMoney.setAlignment(Align.left);
        lblMoney.setFontScale(0.6f);
        choosingGroup.addActor(lblMoney);

        SupportButton landmineButton = new SupportButton(skin, "Landmine", LandMine.COST);
        supportButtons.add(landmineButton);
        supportTable.add(landmineButton).size(133, 100).spaceBottom(5);
        setLandmineListener(landmineButton);

        SupportButton supplydropButton = new SupportButton(skin, "Supply Drop",
            SupplyDropCrate.COST);
        supportButtons.add(supplydropButton);
        supportTable.add(supplydropButton).size(133, 100).spaceBottom(5);
        setSupplyDropListener(supplydropButton);

        SupportButton airstrikeButton = new SupportButton(skin, "Airstrike", AirStrike.COST);
        supportButtons.add(airstrikeButton);
        supportTable.add(airstrikeButton).size(133, 100).spaceBottom(5);
        setAirStrikeListener(airstrikeButton);

        supportTable.row();

        SupportButton apacheButton = new SupportButton(skin, "Apache", Apache.COST);
        supportButtons.add(apacheButton);
        supportTable.add(apacheButton).size(133, 100).spaceBottom(5);
        setApacheListener(apacheButton);

        ImageButton btnCancel = new ImageButton(skin, "cancel");
        setCancelListener(btnCancel);
        btnCancel.setSize(64, 64);
        btnCancel.getImageCell().size(35, 36);
        btnCancel.getImage().setScaling(Scaling.stretch);
        btnCancel.setPosition(Resources.VIRTUAL_WIDTH - 75, Resources.VIRTUAL_HEIGHT - 75);
        choosingGroup.addActor(btnCancel);

        btnPlace = new ImageButton(skin, "select");
        btnPlace.setSize(50, 50);
        btnPlace.getImageCell().size(30, 23);
        btnPlace.getImage().setScaling(Scaling.stretch);
        btnPlace.setPosition(Resources.VIRTUAL_WIDTH - 60, 10);
        setPlaceListener();
        addActor(btnPlace);

        btnPlacingCancel = new ImageButton(skin, "cancel");
        btnPlacingCancel.setSize(50, 50);
        btnPlacingCancel.getImageCell().size(25, 26);
        btnPlacingCancel.getImage().setScaling(Scaling.stretch);
        btnPlacingCancel.setPosition(Resources.VIRTUAL_WIDTH - 60, btnPlace.getY() + 60);
        setCancelListener(btnPlacingCancel);
        addActor(btnPlacingCancel);

        Logger.info("Support View: controls created");
    }


    /**
     * Updates the Support buttons to disable/enable.
     */
    private void updateSupportButtons() {

        for (Actor actor : supportButtons) {
            SupportButton supportButton = (SupportButton) actor;
            boolean affordable = presenter.canAffordSupport(supportButton.cost);
            supportButton.button.setDisabled(!affordable);
        }
    }

    private void setLandmineListener(final SupportButton supportButton) {

        supportButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.createSupportActor("LandMine");
            }
        });

    }

    private void setSupplyDropListener(SupportButton button) {

        button.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.createSupplyDrop();
            }
        });
    }

    private void setAirStrikeListener(SupportButton button) {

        button.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.createAirStrike();
            }
        });
    }

    private void setApacheListener(final SupportButton supportButton) {

        supportButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.createSupportActor("Apache");
            }
        });
    }

    private void setPlaceListener() {

        btnPlace.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.placeActor();
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

        FHDVector2 coords = (FHDVector2) this.getStage()
            .screenToStageCoordinates(UtilPool.getVector2(screenX, screenY));
        presenter.screenTouch(coords, "TouchDown");
        coords.free();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        FHDVector2 coords = (FHDVector2) this.getStage()
            .screenToStageCoordinates(UtilPool.getVector2(screenX, screenY));
        presenter.screenTouch(coords, "Dragged");
        coords.free();
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
    public void supportState() {

        updateSupportButtons();
        lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
        choosingGroup.setVisible(true);
        btnPlacingCancel.setVisible(false);
        this.setVisible(true);
    }

    @Override
    public void placingSupportState() {

        btnPlacingCancel.setVisible(true);
        choosingGroup.setVisible(false);
    }

    @Override
    public void standByState() {

        btnPlacingCancel.setVisible(false);
        choosingGroup.setVisible(false);
        btnPlace.setVisible(false);
        this.setVisible(false);

    }

    @Override
    public void showBtnPlace() {

        btnPlace.setVisible(true);
    }

}
