package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower Tank
 *
 * @author Eric
 */
public class TowerTank extends Tower implements IVehicle, PlatedArmor, IRotatable, IRocket {

    public static final int COST = 1500;
    private static final float HEALTH = 20;
    private static final float ARMOR = 10;
    private static final float ATTACK = 12;
    private static final float ATTACK_SPEED = 3f;
    private static final float RANGE = 60;
    private static final float AOE_RADIUS = 40f;
    private static final int ARMOR_COST = 1200;
    private static final int RANGE_INCREASE_COST = 650;
    private static final int SPEED_INCREASE_COST = 650;
    private static final int ATTACK_INCREASE_COST = 650;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 57);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(138, 36);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 56, 75, 56, 75, 0};

    private Polygon body;
    private TextureRegion bodyRegion;
    private TextureRegion turretRegion;
    private float bodyRotation;
    private ProjectileFactory projectileFactory;
    private LDAudio audio;

    public TowerTank(TextureRegion bodyRegion, TextureRegion turretRegion,
        CombatActorPool<TowerTank> pool, Group targetGroup, TextureRegion rangeRegion,
        TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory, LDAudio audio) {

        super(turretRegion, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST,
            RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE);
        this.bodyRegion = bodyRegion;
        this.turretRegion = turretRegion;
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
        // Only Rotate the tank body when the tank is not active
        // (When the tank is being placed)
        if (!isActive()) {
            bodyRotation = getRotation();
        }
        if (isShowRange()) {
            drawRange(batch);
        }

        float x = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
        // draw body
        batch.draw(bodyRegion, x, y, TEXTURE_SIZE_BODY.getWidth() / 2,
            TEXTURE_SIZE_BODY.getHeight() / 2, TEXTURE_SIZE_BODY.getWidth(),
            TEXTURE_SIZE_BODY.getHeight(), 1, 1, bodyRotation);
        batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(),
            TEXTURE_SIZE_TURRET.getWidth(), TEXTURE_SIZE_TURRET.getHeight(), 1, 1, getRotation());

        if (DebugOptions.showTextureBoundaries) {
            drawDebugBody(batch);
        }
    }

    private void drawDebugBody(Batch batch) {

        batch.end();

        ShapeRenderer bodyOutline = Resources.getShapeRenderer();

        bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
        bodyOutline.begin(ShapeType.Line);
        bodyOutline.setColor(Color.YELLOW);
        bodyOutline.polygon(getBody().getTransformedVertices());
        bodyOutline.end();

        batch.begin();
    }

    /**
     * Body of the Tank. CombatActor/Tower holds the Turret but not the body Which
     * we don't care about for collision detection.
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
            projectileFactory.loadProjectile(Rocket.class)
                .initialize(this, target.getPositionCenter(), ROCKET_SIZE, AOE_RADIUS);
        }
    }

    @Override
    public String getName() {

        return "Tank";
    }

}
