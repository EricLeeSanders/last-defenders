package com.lastdefenders.game.model.actor.health;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.service.factory.HealthFactory.HealthPool;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarStyle;
import com.lastdefenders.util.Logger;

/**
 * Healthbar that is placed over Actors. Is only displayed when that actor has
 * taken damage. Gray - Armor Green - Health Red - Death!
 *
 * @author Eric
 */
public class HealthBar extends Group implements Pool.Poolable {

    public static final float X_OFFSET = -10;
    public static final float Y_OFFSET = 20;
    public static final float BAR_WIDTH = 25;
    public static final float BAR_HEIGHT = 5;
    private CombatActor actor = null;
    private HealthPool<HealthBar> pool;
    private TextureRegionDrawable greenBar, orangeBar, redBar, grayBar, unfilledBar;
    private LDProgressBar progressBar;

    public HealthBar(HealthPool<HealthBar> pool, TextureRegionDrawable green, TextureRegionDrawable orange,
        TextureRegionDrawable red, TextureRegionDrawable gray, TextureRegionDrawable unfilled) {

        setTransform(false);
        setVisible(false);

        this.greenBar = green;
        this.orangeBar = orange;
        this.redBar = red;
        this.grayBar = gray;
        this.unfilledBar = unfilled;
        this.pool = pool;
        progressBar = new LDProgressBar(0,1, 0.000001f,
            new LDProgressBarPadding(0),
            new LDProgressBarStyle(null, green, unfilled));

        addActor(progressBar);

    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (actor == null || actor.isDead() || !actor.isActive()) {
            pool.free(this);
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

    @Override
    public void setSize(float width, float height){

        progressBar.setSize(width, height);
    }

    public void setActor(CombatActor actor) {

        Logger.info("HealthBar: setting actor: " + actor.getClass().getSimpleName());
        this.actor = actor;
        this.setSize(BAR_WIDTH, BAR_HEIGHT);

    }

    @Override
    public void reset() {

        Logger.info("HealthBar: setting resetting");
        this.actor = null;
        setVisible(false);
        this.remove();

    }
}
