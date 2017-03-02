package com.foxholedefense.game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Healthbar that is placed over Actors. Is only displayed when that actor has
 * taken damage. Gray - Armor Green - Health Red - Death!
 * 
 * @author Eric
 *
 */
public class HealthBar extends Actor implements Pool.Poolable {
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
			if (((healthPercentage < 100 && actor.hasArmor() == false) || (actor.hasArmor() && armorPercentage < 100)) && healthPercentage > 0) {
				float healthBarSize = (((30) * (healthPercentage)) / 100);
				float armorBarSize = (((30) * (armorPercentage)) / 100);
				setPosition(actor.getPositionCenter().x - 10, actor.getPositionCenter().y + 20);

				batch.draw(backgroundBar, getX(), getY(), 30, 4);
				batch.draw(healthBar, getX(), getY(), healthBarSize, 4);

				if (actor.hasArmor()) {
					batch.draw(armorBar, getX(), getY(), armorBarSize, 4);
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
