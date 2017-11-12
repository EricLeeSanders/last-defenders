package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy Tank.
 *
 * @author Eric
 */
public class EnemyTank extends Enemy implements PlatedArmor, IVehicle, IRocket {

    private static final float HEALTH = 20;
    private static final float ARMOR = 10;
    private static final float ATTACK = 4;
    private static final float ATTACK_SPEED = 0.9f;
    private static final float RANGE = 80;
    private static final float SPEED = 45;
    private static final float AOE_RADIUS = 75f;
    private static final int KILL_REWARD = 15;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(76, 50);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(120, 33);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 50, 75, 50, 75, 0};

    private TextureRegion bodyRegion;
    private float bodyRotation;
    private Polygon body;
    private ProjectileFactory projectileFactory;
    private LDAudio audio;

    public EnemyTank(TextureRegion bodyRegion, TextureRegion turretRegion,
        TextureRegion[] animatedRegions, CombatActorPool<EnemyTank> pool, Group targetGroup,
        ProjectileFactory projectileFactory, LDAudio audio) {

        super(turretRegion, animatedRegions, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, SPEED,
            HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
        this.bodyRegion = bodyRegion;
        this.projectileFactory = projectileFactory;
        this.audio = audio;
        body = new Polygon(BODY_POINTS);
    }

    /**
     * Draws the tank. Handles when to rotate the body of the tank instead of
     * just the turret.
     */
    @Override
    public void draw(Batch batch, float alpha) {

        // If the tank is not attacking, then rotate the body as well
        if (!isAttacking()) {
            bodyRotation = getRotation();
        }
        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
        // draw body
        batch.draw(bodyRegion, x, y, TEXTURE_SIZE_BODY.getWidth() / 2,
            TEXTURE_SIZE_BODY.getHeight() / 2, TEXTURE_SIZE_BODY.getWidth(),
            TEXTURE_SIZE_BODY.getHeight(), 1, 1, bodyRotation);
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
     * Body of the Tank. CombatActor/Enemy holds the Body of the turret Which we
     * don't care about for collision detection.
     */
    @Override
    public Polygon getBody() {

        body.setOrigin(TEXTURE_SIZE_BODY.getWidth() / 2, TEXTURE_SIZE_BODY.getHeight() / 2);
        body.setRotation(bodyRotation);

        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
        body.setPosition(x, y);

        return body;
    }

    @Override
    public void reset() {

        super.reset();
        bodyRotation = 0;
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.ROCKET_LAUNCH);
            projectileFactory.loadRocket()
                .initialize(this, target.getPositionCenter(), ROCKET_SIZE, AOE_RADIUS);
        }
    }
}
