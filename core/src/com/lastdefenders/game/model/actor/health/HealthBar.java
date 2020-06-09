package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.ui.view.widget.progressbar.LDProgressBar.LDProgressBarStyle;
/**
 * Healthbar that is placed over Actors. Is only displayed when that actor has
 * taken damage. Gray - Armor Green - Health Red - Death!
 *
 * @author Eric
 */
public class HealthBar extends Group {

    public static final float X_OFFSET = -10;
    public static final float Y_OFFSET = 20;
    public static final float BAR_WIDTH = 25;
    public static final float BAR_HEIGHT = 5;
    private CombatActor actor;
    private TextureRegionDrawable greenBar, orangeBar, redBar, grayBar;
    private LDProgressBar progressBar;

    public HealthBar( TextureRegionDrawable green, TextureRegionDrawable orange,
        TextureRegionDrawable red, TextureRegionDrawable gray, TextureRegionDrawable unfilled, CombatActor actor) {

        setTransform(false);
        setVisible(false);

        this.greenBar = green;
        this.orangeBar = orange;
        this.redBar = red;
        this.grayBar = gray;
        this.actor = actor;
        progressBar = new LDProgressBar(0,1, 0.000001f,
            new LDProgressBarPadding(0),
            new LDProgressBarStyle(null, green, unfilled));

        addActor(progressBar);
        progressBar.setSize(BAR_WIDTH, BAR_HEIGHT);

    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (actor == null || actor.isDead() || !actor.isActive()) {
            setVisible(false);
            return;
        }

        if(showArmor() || showHealth()){
            progressBar.setFilledTextureRegion(getBar().getRegion());
            setVisible(true);
            progressBar.setValue(actor.hasArmor() ? actor.getArmorPercent() : actor.getHealthPercent());
        } else {
            setVisible(false);
        }

        setPosition(actor.getPositionCenter().x + X_OFFSET,
            actor.getPositionCenter().y + Y_OFFSET);

    }

    private boolean showArmor(){
        return actor.hasArmor() && actor.getArmorPercent() < 1;
    }

    private boolean showHealth(){
        return actor.getHealthPercent() < 1;
    }

    private TextureRegionDrawable getBar(){

        if(actor.hasArmor()){
            return grayBar;
        }

        float healthPercentage = actor.getHealthPercent();
        if(healthPercentage >= .66f){
            return greenBar;
        } else if(healthPercentage >= .33f){
            return orangeBar;
        }

        return redBar;
    }

}
