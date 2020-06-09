package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.service.factory.CombatActorFactory.TowerPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 12/20/2018.
 */

public abstract class TowerTurret extends Tower {

    private float bodyRotation;
    private Dimension textureSizeBody;
    private TextureRegion bodyRegion;
    private Polygon body;

    public TowerTurret(TextureRegion textureRegion, Dimension textureSizeTurret,
        TowerPool<? extends Tower> pool, GenericGroup<Enemy> targetGroup, Vector2 gunPos,
        TextureRegion rangeRegion, TextureRegion collidingRangeRegion, DeathEffectType deathEffectType, Dimension textureSizeBody,
        TextureRegion bodyRegion, float [] bodyPoints, TowerAttributes attributes) {

        super(textureRegion, textureSizeTurret, pool, targetGroup, gunPos, rangeRegion, collidingRangeRegion,
            deathEffectType, attributes);

        this.textureSizeBody = textureSizeBody;
        this.bodyRegion = bodyRegion;
        body = new Polygon(bodyPoints);
    }

    /**
     * Draws the turret body. Handles when to rotate the body of the turret
     * instead of just the turret.
     */
    @Override
    public void draw(Batch batch, float alpha) {

        // Only rotate the turret body when the turret is not active
        // (When the turret is being placed)
        if (!isActive()) {
            bodyRotation = getRotation();
        }

        if (isShowRange()) {
            drawRange(batch);
        }

        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, textureSizeBody.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, textureSizeBody.getHeight());

        batch.draw(bodyRegion, x, y, textureSizeBody.getWidth() / 2,
            textureSizeBody.getHeight() / 2, textureSizeBody.getWidth(),
            textureSizeBody.getHeight(), 1, 1, bodyRotation);

        super.draw(batch, alpha);

        if (DebugOptions.showTextureBoundaries) {
            drawDebugBody(batch);
        }

    }

    private void drawDebugBody(Batch batch) {

        ShapeRenderer bodyOutline = Resources.getShapeRenderer();
        batch.end();
        bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
        bodyOutline.begin(ShapeType.Line);
        bodyOutline.setColor(Color.YELLOW);
        bodyOutline.polygon(getBody().getTransformedVertices());
        bodyOutline.end();

        batch.begin();
    }

    /**
     * Body of the Turret Actor. CombatActor/Tower holds the turret which we
     * don't care about for collision detection.
     */
    @Override
    public Polygon getBody() {

        body.setOrigin(textureSizeBody.getWidth() / 2, textureSizeBody.getHeight() / 2);
        body.setRotation(bodyRotation);

        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, textureSizeBody.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, textureSizeBody.getHeight());
        body.setPosition(x, y);

        return body;
    }

    @Override
    public void reset() {

        super.reset();
        bodyRotation = 0;
    }
}
