package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 12/20/2018.
 */

public abstract class EnemyTurret extends Enemy {

    private float bodyRotation;
    private Dimension textureSizeBody;
    private TextureRegion bodyRegion;
    private Polygon body;

    public EnemyTurret(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, Dimension textureSizeTurret,
        CombatActorPool<? extends CombatActor> pool, Group targetGroup, Vector2 gunPos, float speed,
        float health, float armor, float attack, float attackSpeed, float range, int killReward,
        DeathEffectType deathEffectType, Dimension textureSizeBody, TextureRegion bodyRegion,
        float [] bodyPoints) {

        super(stationaryTextureRegion, animatedRegions, textureSizeTurret, pool, targetGroup, gunPos,
            speed,  health, armor, attack, attackSpeed, range, killReward, deathEffectType);

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

        // If the turret is not attacking, then rotate the body as well
        if (!isAttacking()) {
            bodyRotation = getRotation();
        }
        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, textureSizeBody.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, textureSizeBody.getHeight());
        // draw body
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
     * Body of the Turret Actor. CombatActor/Enemy holds the turret which we
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
