package com.foxholedefense.game.model.actor.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Represents a Flame from a FlameThrower. Deals periodic damage as an AOE.
 *
 * @author Eric
 */
public class Flame extends GameActor implements Pool.Poolable {

    public static final float TICK_ATTACK_SPEED = 0.1f;
    private static final float FRAME_DURATION = 0.025f;
    private static final int NUM_OF_FRAMES = 29;
    public static final float DURATION = FRAME_DURATION * NUM_OF_FRAMES;
    private Animation<TextureRegion> flameAnimation;
    private float stateTime;
    private float tickTime = TICK_ATTACK_SPEED;
    private CombatActor attacker;
    private Dimension flameSize;
    private Pool<Flame> pool;
    private float[] bodyPoints = new float[8];
    private Polygon flameBody = new Polygon();
    private Group targetGroup;

    /**
     * Constructs a flame
     */
    public Flame(Pool<Flame> pool, Array<AtlasRegion> regions) {

        this.pool = pool;
        flameAnimation = new Animation<TextureRegion>(FRAME_DURATION, regions);
        flameAnimation.setPlayMode(PlayMode.LOOP);
        bodyPoints[0] = bodyPoints[1] = bodyPoints[2] = bodyPoints[7] = 0;
    }

    /**
     * Initializes a flame.
     */
    public Actor initialize(CombatActor attacker, Group targetGroup, Dimension flameSize) {

        this.attacker = attacker;
        this.targetGroup = targetGroup;
        stateTime = 0;
        this.flameSize = flameSize;

        // Use only width to make the flame fatter
        setSize(new Dimension(flameSize.getWidth(), flameSize.getWidth()));
        setOrigin(0, flameSize.getWidth() / 2);
        setPosition(attacker.getGunPos().x, attacker.getGunPos().y - getOriginY());
        setRotation(attacker.getRotation());

        bodyPoints[3] = bodyPoints[5] = flameSize.getHeight();
        bodyPoints[4] = bodyPoints[6] = flameSize.getWidth();
        flameBody.setVertices(bodyPoints);

        return this;
    }


    @Override
    public void act(float delta) {

        super.act(delta);
        stateTime += delta;
        if (attacker.isDead() || flameAnimation.isAnimationFinished(stateTime)) {
            pool.free(this);
        }
        setRotation(attacker.getRotation());
        setPosition(attacker.getGunPos().x, attacker.getGunPos().y - getOriginY());
        attackHandler(delta);
    }

    private void attackHandler(float delta) {

        tickTime += delta;
        if (tickTime > TICK_ATTACK_SPEED) {
            Damage.dealFlameGroupDamage(attacker, targetGroup.getChildren(), getFlameBody());
            tickTime = 0;
        }
    }

    /**
     * Draws the Flame and determines when it has finished.
     */
    @Override
    public void draw(Batch batch, float alpha) {

        setTextureRegion(flameAnimation.getKeyFrame(stateTime, true));

        if (DebugOptions.showTextureBoundaries) {
            drawDebugBody(batch);
        }
        super.draw(batch, alpha);

    }

    private void drawDebugBody(Batch batch) {

        ShapeRenderer flameOutline = Resources.getShapeRenderer();
        batch.end();
        Polygon poly = getFlameBody();
        flameOutline.setProjectionMatrix(getParent().getStage().getCamera().combined);
        flameOutline.begin(ShapeType.Line);
        flameOutline.setColor(Color.RED);
        flameOutline.polygon(poly.getTransformedVertices());
        flameOutline.end();
        batch.begin();
    }

    /**
     * Get the flame body.
     * Must be a polygon because a rectangle can't be rotated.
     */
    public Polygon getFlameBody() {

        flameBody.setPosition(attacker.getGunPos().x,
            attacker.getGunPos().y - (flameSize.getHeight() / 2));
        flameBody.setOrigin(0, flameSize.getHeight() / 2);
        flameBody.setRotation(getRotation());
        return flameBody;
    }

    @Override
    public void reset() {

        this.clear();
        this.remove();
        stateTime = 0;
        tickTime = TICK_ATTACK_SPEED;
    }
}
