package com.lastdefenders.game.ui.view.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.util.ActorUtil;

/**
 * Created by Eric on 11/12/2016.
 */

public class SupportButton extends Group {

    public ImageButton button;
    public int cost;
    private SupportActorCooldown cooldown;
    private Label lblCooldown;
    private SupportPresenter presenter;

    public SupportButton(Skin skin, String name, int cost, float fontScale, SupportActorCooldown cooldown,
        SupportPresenter presenter) {

        this.setTransform(false);
        this.cooldown = cooldown;
        this.presenter = presenter;


        this.button = new ImageButton(skin, "support");
        button.setSize(133, 100);
        this.cost = cost;
        addActor(button);

        Label lblTitle = new Label(name.replaceAll(" ", "\n"), skin);
        lblTitle.setFontScale(0.5f * fontScale);
        lblTitle.setAlignment(Align.center);
        lblTitle.pack();
        float lblTitleX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblTitle.getWidth());
        float lblTitleY = ActorUtil
            .calcBotLeftPointFromCenter(button.getHeight() / 2, lblTitle.getHeight());
        lblTitle.setPosition(lblTitleX, lblTitleY + 12);
        addActor(lblTitle);

        Label lblCost = new Label(String.valueOf(cost), skin);
        lblCost.setFontScale(0.45f * fontScale);
        lblCost.setAlignment(Align.center);
        lblCost.pack();
        float lblCostX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCost.getWidth());
        lblCost.setPosition(lblCostX, 10);
        addActor(lblCost);


        LabelStyle lblCooldownStyle = new Label.LabelStyle(skin.get(LabelStyle.class));
        lblCooldownStyle.fontColor = Color.RED;
        lblCooldown = new Label("", lblCooldownStyle);
        lblCooldown.setFontScale(0.75f * fontScale);
        lblCooldown.setAlignment(Align.center);
        lblCooldown.pack();
        float lblCooldownX = ActorUtil
            .calcBotLeftPointFromCenter(button.getWidth() / 2, lblCooldown.getWidth());
        float lblCooldownY = ActorUtil
            .calcBotLeftPointFromCenter(button.getHeight() / 2, lblCooldown.getHeight());
        lblCooldown.setPosition(lblCooldownX, lblCooldownY + 12);
        lblCooldown.setVisible(false);
        addActor(lblCooldown);

    }

    public void update(){
        boolean affordable = presenter.canAffordSupport(cost);
        boolean onCooldown = cooldown.isOnCooldown();

        button.setDisabled(!affordable || onCooldown);

        lblCooldown.setVisible(onCooldown);
        if(onCooldown){
            lblCooldown.setText((int) cooldown.getRemaining());
        }

    }

    @Override
    public void act(float delta) {

        super.act(delta);
        update();
    }
}
