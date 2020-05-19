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

    public static final int COST = 200;
    private static final float ATTACK = 15f;
    private static final float RANGE = 75;
    private static final Vector2 GUN_POS = UtilPool.getVector2(0, 0);
    private static final Dimension TEXTURE_SIZE = new Dimension(30, 30);

    private Circle body;
    private ProjectileFactory projectileFactory;

    public LandMine(SupportActorPool<LandMine> pool, Group targetGroup,
        ProjectileFactory projectileFactory, TextureRegion textureRegion,
        TextureRegion rangeTexture) {

        super(pool, targetGroup, textureRegion, TEXTURE_SIZE, rangeTexture, RANGE, ATTACK, GUN_POS,
            COST);
        this.projectileFactory = projectileFactory;
        this.body = new Circle(getPositionCenter(), getWidth() / 2);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        if (isActive()) {
            for (Actor enemy : getTargetGroup().getChildren()) {
                if (enemy instanceof Enemy) {
                    if (CollisionDetection.shapesIntersect(((Enemy) enemy).getBody(), getBody())) {
                        explode();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);
        if (DebugOptions.showTextureBoundaries) {
            drawDebugBody(batch);
        }
    }

    private void drawDebugBody(Batch batch) {

        ShapeRenderer debugBody = Resources.getShapeRenderer();
        batch.end();

        debugBody.setProjectionMatrix(getParent().getStage().getCamera().combined);
        debugBody.begin(ShapeType.Line);
        debugBody.setColor(Color.YELLOW);
        debugBody.circle(getPositionCenter().x, getPositionCenter().y, getWidth() / 2);
        debugBody.end();

        batch.begin();
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
