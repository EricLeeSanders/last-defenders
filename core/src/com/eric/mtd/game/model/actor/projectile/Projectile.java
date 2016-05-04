package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public abstract class Projectile extends Actor implements Pool.Poolable {
	private Pool<? extends Projectile> pool;
	public Projectile(Pool<? extends Projectile> pool){
		this.pool = pool;
	}
	public abstract void initialize(IAttacker attacker, CombatActor target, Vector2 pos, Vector2 size);
	public abstract void initialize(CombatActor shooter, CombatActor target, Group targetGroup);
	public Shape2D getBody(){
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

}
