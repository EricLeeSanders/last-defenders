package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.projectile.Explosion;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

public class LandMine extends CombatSupportActor implements IRocket {

    public static final float COOLDOWN_TIME = 3;
    public static final int COST = 200;
    private static final float ATTACK = 15f;
    private static final float RANGE = 75;
    private static final Vector2 GUN_POS = UtilPool.getVector2(0, 0);
    private static final Dimension TEXTURE_SIZE = new Dimension(30, 30);

    private Circle body;
    private ProjectileFactory projectileFactory;

    public LandMine(SupportActorPool<LandMine> pool, EnemyGroup enemyGroup,
        ProjectileFactory projectileFactory, TextureRegion textureRegion,
        TextureRegion rangeTexture) {

        super(pool, enemyGroup, textureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK, GUN_POS);
        this.projectileFactory = projectileFactory;
        this.body = new Circle(getPositionCenter(), getWidth() / 2);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (isActive()) {
            for (Enemy enemy : getEnemyGroup().getCastedChildren()) {
                if (CollisionDetection.shapesIntersect(enemy.getBody(), getBody())) {
                    explode();
                    return;
                }
            }
        }
    }

    @Override
    public int getCost() {

        return COST;
    }


    private void explode() {

        Logger.info("Landmine: exploding");
        projectileFactory.loadProjectile(Explosion.class).initialize(this, RANGE, getPositionCenter());
        freeActor();
    }

    private Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }

}
