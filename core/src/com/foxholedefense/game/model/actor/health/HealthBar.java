package com.foxholedefense.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.util.Logger;

/**
 * Healthbar that is placed over Actors. Is only displayed when that actor has
 * taken damage. Gray - Armor Green - Health Red - Death!
 * 
 * @author Eric
 *
 */
public class HealthBar extends Actor implements Pool.Poolable {
	public static final float X_OFFSET = -10;
	public static final float Y_OFFSET = 20;
	public static final float MAX_BAR_WIDTH = 30;
	public static final float BAR_HEIGHT = 4;
	private CombatActor actor = null;
	private Pool<HealthBar> pool;
	private TextureRegion backgroundBar, healthBar, armorBar;
	public HealthBar(Pool<HealthBar> pool, TextureRegion backgroundBar, TextureRegion healthBar, TextureRegion armorBar) {
		this.pool = pool;
		this.backgroundBar = backgroundBar;
		this.healthBar = healthBar;
		this.armorBar = armorBar;
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if (actor != null) {
			float healthPercentage = actor.getHealthPercent();
			float armorPercentage = actor.getArmorPercent();
			// Only show if the actor has been hit
			if (((healthPercentage < 1 && !actor.hasArmor()) || (actor.hasArmor() && armorPercentage < 1)) && healthPercentage > 0) {
				float healthBarSize = MAX_BAR_WIDTH * healthPercentage;
				float armorBarSize = MAX_BAR_WIDTH * armorPercentage;
				setPosition(actor.getPositionCenter().x + X_OFFSET, actor.getPositionCenter().y + Y_OFFSET);

				batch.draw(backgroundBar, getX(), getY(), MAX_BAR_WIDTH, BAR_HEIGHT);
				batch.draw(healthBar, getX(), getY(), healthBarSize, BAR_HEIGHT);

				if (actor.hasArmor()) {
					batch.draw(armorBar, getX(), getY(), armorBarSize, BAR_HEIGHT);
				}
			}
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (actor == null || actor.isDead()) {
			pool.free(this);
		}
	}

	public void setActor(CombatActor actor) {
		Logger.info("HealthBar: setting actor: " + actor.getClass().getSimpleName());
		this.actor = actor;
		this.setSize(30, 4);

	}

	@Override
	public void reset() {
		Logger.info("HealthBar: setting resetting");
		this.actor = null;
		this.remove();

	}
}
