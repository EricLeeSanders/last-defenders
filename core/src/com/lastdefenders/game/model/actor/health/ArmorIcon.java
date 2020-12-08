package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 1/6/2017.
 */

public class ArmorIcon extends GameActor {

    private static final Dimension TEXTURE_SIZE = new Dimension(12, 13);
    private static final Dimension TEXTURE_PADDING_HEALTH_BAR_SHOWING = new Dimension(-2, 0);
    private static final float DESTROYED_ICON_DURATION = 2;
    private boolean healthBarShowing;
    private TextureRegion icon;
    private TextureRegion destroyedIcon;


    public ArmorIcon(TextureRegion icon, TextureRegion destroyedIcon) {

        super(TEXTURE_SIZE);

        this.setTextureRegion(icon);
        this.icon = icon;
        this.destroyedIcon = destroyedIcon;

        setVisible(false);
        setY(0, Align.center);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(healthBarShowing){
            setX(-TEXTURE_SIZE.getWidth() + TEXTURE_PADDING_HEALTH_BAR_SHOWING.getWidth(), Align.right);
        } else {
            setX(0, Align.center);
        }
    }

    public void setHealthBarShowing(boolean healthBarShowing){
        this.healthBarShowing = healthBarShowing;
    }

    public void armorDestroyed(){
        initArmorDestroyedAction();
    }

    public void armorActive(){
        this.setVisible(true);
        this.setTextureRegion(icon);
    }

    private void initArmorDestroyedAction(){
        this.setTextureRegion(destroyedIcon);
        addAction(
            Actions.sequence(
                Actions.delay(DESTROYED_ICON_DURATION),
                Actions.visible(false)
            )
        );
    }




}
