package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.ui.presenter.InspectPresenter;
import com.lastdefenders.game.ui.view.interfaces.IInspectView;
import com.lastdefenders.game.ui.view.widgets.DischargeButton;
import com.lastdefenders.game.ui.view.widgets.UpgradeButton;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * View for inspecting a tower
 *
 * @author Eric
 */
public class InspectView extends Group implements InputProcessor, IInspectView {

    private InspectPresenter presenter;
    private DischargeButton btnDischarge;
    private UpgradeButton btnArmor, btnSpeed, btnRange, btnAttack;
    private Label lblTargetPriority, lblTitle, lblMoney, lblKills;
    private Resources resources;
    private TargetPriorityView targetPriorityView;
    public InspectView(InspectPresenter presenter, Resources resources) {

        this.presenter = presenter;
        this.setTransform(false);
        this.resources = resources;
    }

    public void init(){
        createControls();
        targetPriorityView = new TargetPriorityView(presenter, resources);
        addActor(targetPriorityView);
        targetPriorityView.init();
        targetPriorityView.setVisible(false);
    }

    /**
     * Create the controls
     */
    private void createControls() {

        Logger.info("Inspect View: creating controls");

        Skin skin = resources.getSkin();

        Table container = new Table();
        container.setTransform(false);
        container.setSize(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight());
        addActor(container);
        container.setBackground(skin.getDrawable("main-panel"));

        Table inspectTable = new Table();
        inspectTable.setTransform(false);

        inspectTable.defaults().expandX();

        container.add(inspectTable).expand().fill();

        lblTitle = new Label("Tower", skin);
        lblTitle.setHeight(60);
        lblTitle.setFontScale(0.7f * resources.getFontScale());
        lblTitle.setAlignment(Align.center);
        float lblTitleX = container.getX(Align.center);
        float lblTitleY = container.getY(Align.top) - (lblTitle.getHeight()/2);
        lblTitle.setPosition(lblTitleX, lblTitleY, Align.center);
        addActor(lblTitle);

        LabelStyle lblMoneyStyle = new LabelStyle(skin.get("money_label", LabelStyle.class));
        lblMoneyStyle.background.setLeftWidth(60);
        lblMoneyStyle.background.setTopHeight(10);
        lblMoney = new Label("$0", lblMoneyStyle);
        lblMoney.setSize(200, 52);
        lblMoney.setPosition(75, 5);
        lblMoney.setAlignment(Align.left);
        lblMoney.setFontScale(0.6f * resources.getFontScale());
        addActor(lblMoney);

        LabelStyle lblKillsStyle = new LabelStyle(skin.get("kills_label", LabelStyle.class));
        lblKillsStyle.background.setRightWidth(60);
        lblKillsStyle.background.setTopHeight(10);
        lblKills = new Label("0", lblKillsStyle);
        lblKills.setSize(200, 52);
        lblKills.setPosition(365, 5);
        lblKills.setAlignment(Align.right);
        lblKills.setFontScale(0.6f * resources.getFontScale());
        addActor(lblKills);

        ImageButton btnCancel = new ImageButton(skin, "cancel");
        setCancelListener(btnCancel);
        btnCancel.setSize(64, 64);
        btnCancel.getImageCell().size(35, 36);
        btnCancel.getImage().setScaling(Scaling.stretch);
        btnCancel.setPosition(getStage().getViewport().getWorldWidth() - 75, getStage().getViewport().getWorldHeight() - 75);
        addActor(btnCancel);

        btnArmor = new UpgradeButton(skin, "Armor", "shield", 27, 30, resources.getFontScale());
        inspectTable.add(btnArmor).size(110, 115);//.spaceBottom(10).spaceRight(10);
        setArmorListener();

        btnRange = new UpgradeButton(skin, "Increase Range", "range_icon", 28, 30, resources.getFontScale());
        inspectTable.add(btnRange).size(110, 115);//.spaceBottom(10).spaceRight(10);
        setIncreaseRangeListener();

        btnSpeed = new UpgradeButton(skin, "Increase Speed", "speed_icon", 30, 30, resources.getFontScale());
        inspectTable.add(btnSpeed).size(110, 115);//.spaceBottom(5).spaceRight(10);
        setIncreaseSpeedListener();

        btnAttack = new UpgradeButton(skin, "Increase Attack", "attack_icon", 26, 25, resources.getFontScale());
        inspectTable.add(btnAttack).size(110, 115);//.spaceBottom(5).spaceRight(10);
        setIncreaseAttackListener();

        inspectTable.row();

        Group grpTargetPriority = createTargetPriorityGroup(skin);

        inspectTable.add(grpTargetPriority).colspan(2).height(45).width(150).padTop(15).center();

        btnDischarge = new DischargeButton(skin, resources.getFontScale());
        inspectTable.add(btnDischarge).colspan(2).size(133, 83).padTop(15).center();
        setDischargeListener();

        Logger.info("Inspect View: controls created");
    }

    private Group createTargetPriorityGroup(Skin skin){
        Group grpTargetPriority = new Group();
        grpTargetPriority.setTransform(false);

        LabelStyle lblTargetPriorityStyle = new LabelStyle(
            skin.get("hollow_label", LabelStyle.class));
        lblTargetPriorityStyle.background.setLeftWidth(-2);
        lblTargetPriority = new Label("First", lblTargetPriorityStyle);
        lblTargetPriority.setAlignment(Align.center);
        lblTargetPriority.setSize(140, 41);
        lblTargetPriority.setFontScale(0.45f * resources.getFontScale());
        grpTargetPriority.addActor(lblTargetPriority);

        Label lblTarget = new Label("Priority", skin);
        lblTarget.setAlignment(Align.center);
        lblTarget.setFontScale(0.45f * resources.getFontScale());
        lblTarget.setPosition(lblTargetPriority.getX(Align.center), lblTargetPriority.getY() + 60, Align.center);
        grpTargetPriority.addActor(lblTarget);

        Label lblText = new Label("Click to change", skin);
        lblText.setAlignment(Align.center);
        lblText.setPosition(lblTargetPriority.getX(Align.center),lblTargetPriority.getY() - 12, Align.center);
        lblText.setFontScale(0.35f * resources.getFontScale());
        grpTargetPriority.addActor(lblText);

        setTargetPriorityListener(grpTargetPriority);

        return grpTargetPriority;
    }


    @Override
    public void standByState() {

        this.setVisible(false);
    }

    @Override
    public void inspectingState() {

        this.setVisible(true);
    }

    /**
     * Binds to the Inspected Tower and updates the widgets
     */
    @Override
    public void update(Tower selectedTower) {

        lblMoney.setText(String.valueOf(presenter.getPlayerMoney()));
        lblKills.setText(String.valueOf(selectedTower.getNumOfKills()));
        btnDischarge.updateCost(selectedTower.getSellCost());
        btnDischarge.button.setDisabled(presenter.isDischargeDisabled());
        lblTitle.setText(selectedTower.getName());
        lblTargetPriority.setText(selectedTower.getAI().getTitle());
        updateUpgradeControl(btnArmor, selectedTower.hasArmor(), selectedTower.getArmorCost());
        updateUpgradeControl(btnSpeed, selectedTower.hasIncreasedSpeed(),
            selectedTower.getSpeedIncreaseCost());
        updateUpgradeControl(btnRange, selectedTower.hasIncreasedRange(),
            selectedTower.getRangeIncreaseCost());
        updateUpgradeControl(btnAttack, selectedTower.hasIncreasedAttack(),
            selectedTower.getAttackIncreaseCost());
    }

    /**
     * Updates the upgrade controls
     */
    private void updateUpgradeControl(UpgradeButton upgradeButton, boolean towerHasUpgrade,
        int upgradeCost) {

        upgradeButton.updateCost(upgradeCost);
        boolean affordable = presenter.canAffordUpgrade(upgradeCost);
        upgradeButton.button.setDisabled(!affordable && !towerHasUpgrade);
        upgradeButton.setPurchased(towerHasUpgrade);
    }

    private void setCancelListener(Button btnCancel) {

        btnCancel.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.closeInspect();
            }
        });
    }

    private void setTargetPriorityListener(Group grpTargetPriority) {

        grpTargetPriority.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                targetPriorityView.show();
            }
        });
    }

    private void setIncreaseAttackListener() {

        btnAttack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.increaseAttack();
            }
        });
    }

    private void setArmorListener() {

        btnArmor.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.giveArmor();
            }
        });
    }

    private void setIncreaseRangeListener() {

        btnRange.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.increaseRange();
            }
        });
    }

    private void setIncreaseSpeedListener() {

        btnSpeed.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.increaseSpeed();
            }
        });
    }

    private void setDischargeListener() {

        btnDischarge.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                super.touchUp(event, x, y, pointer, button);
                presenter.dishcharge();
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

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        presenter.inspectTower(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        return false;
    }
}
