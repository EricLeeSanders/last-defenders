package com.eric.mtd.game.model.actor.health;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Healthbar that is placed over Actors. Is only displayed when that actor has
 * taken damage. Gray - Armor Green - Health Red - Death!
 * 
 * @author Eric
 *
 */
public class HealthBar extends Actor implements Pool.Poolable {
	private CombatActor actor = null;
	/*private ShapeRenderer backgroundBar = Resources.getShapeRenderer();;
	private ShapeRenderer healthBar = Resources.getShapeRenderer();;
	private ShapeRenderer armorBar = Resources.getShapeRenderer();;*/
	private Sprite backgroundBar, healthBar, armorBar;
	private float healthPercentage;
	private float armorPercentage;
	private float healthBarSize;
	private float armorBarSize;
	private Pool<HealthBar> pool;
	public HealthBar(Pool<HealthBar> pool) {
		this.pool = pool;
		createHealthBarSprites();
	}
	private void createHealthBarSprites(){
		Pixmap bgPixmap = new Pixmap(100, 100, Format.RGBA8888);
		bgPixmap.setColor(Color.RED);
		bgPixmap.fillRectangle(0, 0, 100, 100);
		backgroundBar = new Sprite(new Texture(bgPixmap));
		bgPixmap.dispose();
		
		Pixmap healthPixmap = new Pixmap(100, 100, Format.RGBA8888);
		healthPixmap.setColor(Color.GREEN);
		healthPixmap.fillRectangle(0, 0, 100, 100);
		healthBar = new Sprite(new Texture(healthPixmap));
		healthPixmap.dispose();
		
		Pixmap armorPixmap = new Pixmap(100, 100, Format.RGBA8888);
		armorPixmap.setColor(Color.DARK_GRAY);
		armorPixmap.fillRectangle(0, 0, 100, 100);
		armorBar = new Sprite(new Texture(armorPixmap));
		armorPixmap.dispose();
	}	
	@Override
	public void draw(Batch batch, float alpha) {
		if (actor != null) {
			healthPercentage = actor.getHealthPercent();
			armorPercentage = actor.getArmorPercent();
			// Only show if the actor has been hit
			if (((healthPercentage < 100 && actor.hasArmor() == false) || (actor.hasArmor() && armorPercentage < 100)) && healthPercentage > 0) {
				healthBarSize = (((30) * (healthPercentage)) / 100);
				armorBarSize = (((30) * (armorPercentage)) / 100);
				setPosition(actor.getPositionCenter().x - 10, actor.getPositionCenter().y + 20);
	
				backgroundBar.setBounds(getX(), getY(), 30, 4);
				backgroundBar.draw(batch);
				
				healthBar.setBounds(getX(), getY(), healthBarSize, 4);
				healthBar.draw(batch);
				
				if (actor.hasArmor()) {
					armorBar.setBounds(getX(), getY(), armorBarSize, 4);
					armorBar.draw(batch);

				}
			}
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (actor.isDead() || actor == null) {
			if (Logger.DEBUG)
				System.out.println("Freeing Healthbar");
			pool.free(this);
		}
	}

	public void setActor(CombatActor actor) {
		if (Logger.DEBUG)
			System.out.println("Healthbar: Setting actor");
		this.actor = actor;
		this.setSize(30, 4);

	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("Resetting healthbar");
		this.actor = null;
		this.remove();
		healthPercentage = 100;
		armorPercentage = 100;
		healthBarSize = 30;
		armorBarSize = 30;

	}
}
