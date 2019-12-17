package com.lastdefenders.game.model.actor.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager.CombatActorEventEnum;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.interfaces.Collidable;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

/**
 * Represents both a Tower and Enemy.
 *
 * @author Eric
 */
public abstract class CombatActor extends GameActor implements Pool.Poolable, Collidable, Attacker,
    Targetable {

    public final String ID = ActorUtil.getRandomID();

    private final float RESET_ATTACK_SPEED, RESET_RANGE, MAX_HEALTH, MAX_ARMOR, RESET_ATTACK;
    private float attackSpeed, range, health, attack, armor;
    private Vector2 gunPos;
    private Vector2 rotatedGunPos = UtilPool.getVector2();
    private Circle rangeCircle = new Circle();
    private boolean hasArmor, dead, active;
    private CombatActorPool<? extends CombatActor> pool;
    private DeathEffectType deathEffectType;
    private Group targetGroup;
    private EventManager eventManager;

    protected CombatActor(TextureRegion textureRegion, Dimension textureSize,
        CombatActorPool<? extends CombatActor> pool, Group targetGroup, Vector2 gunPos, float health, float armor,
        float attack, float attackSpeed, float range, DeathEffectType deathEffectType) {

        super(textureSize);
        this.MAX_HEALTH = health;
        this.MAX_ARMOR = armor;
        this.RESET_ATTACK = attack;
        this.RESET_RANGE = range;
        this.RESET_ATTACK_SPEED = attackSpeed;
        this.health = health;
        this.armor = armor;
        this.attackSpeed = attackSpeed;
        this.attack = attack;
        this.gunPos = gunPos;
        this.range = range;
        this.pool = pool;
        this.targetGroup = targetGroup;
        this.deathEffectType = deathEffectType;
        setTextureRegion(textureRegion);


    }

    @Override
    public void reset() {

        Logger.debug("Combat Actor " + ID + ": " + this.getClass().getSimpleName() + " Resetting");
        health = MAX_HEALTH;
        armor = MAX_ARMOR;
        hasArmor = false;
        attack = RESET_ATTACK;
        attackSpeed = RESET_ATTACK_SPEED;
        range = RESET_RANGE;
        this.setRotation(0);
        this.clear();
        this.remove();
        setActive(false);
    }

    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        if (DebugOptions.showTextureBoundaries) {
            drawDebugBody(batch);
        }
    }

    public void setEventManager(EventManager eventManager) {

        this.eventManager = eventManager;
    }

    private void drawDebugBody(Batch batch) {

        batch.end();
        ShapeRenderer debugBody = Resources.getShapeRenderer();
        debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
        debugBody.begin(ShapeType.Line);
        debugBody.setColor(Color.RED);
        Shape2D body = getBody();
        if (body instanceof Polygon) {
            Polygon polyBody = (Polygon) body;
            debugBody.polygon(polyBody.getTransformedVertices());
        } else if (body instanceof Circle) {
            Circle circleBody = (Circle) body;
            debugBody.circle(circleBody.x, circleBody.y, circleBody.radius);
        }
        debugBody.end();
        batch.begin();
    }

    public float getHealth() {

        return health;
    }

    public void takeDamage(float damage) {

        if (hasArmor()) {
            if ((armor - damage) <= 0) {
                health = health - (damage - armor);
                setHasArmor(false);
            } else {
                armor = armor - damage;
            }

        } else {
            health = health - damage;
        }
        if (health <= 0) {
            this.setDead(true);
        }

    }

    public Vector2 getGunPos() {

        Vector2 centerPosition = getPositionCenter();
        LDVector2 rotatedCoords = ActorUtil
            .calculateRotatedCoords((getPositionCenter().x + gunPos.x),
                (getPositionCenter().y + gunPos.y), centerPosition.x, centerPosition.y,
                Math.toRadians(getRotation()));
        rotatedGunPos.set(rotatedCoords.x, rotatedCoords.y);
        rotatedCoords.free();
        return rotatedGunPos;
    }

    @Override
    public Circle getRangeShape() {

        rangeCircle.set(getPositionCenter().x, getPositionCenter().y, range);
        return rangeCircle;
    }

    public float getAttackSpeed() {

        return attackSpeed;
    }

    protected void setAttackSpeed(float attackSpeed) {

        this.attackSpeed = attackSpeed;
    }

    protected float getRange() {

        return range;
    }

    protected void setRange(float range) {

        this.range = range;
    }

    public abstract void attackTarget(Targetable target);

    public DeathEffectType getDeathEffectType() {

        return deathEffectType;
    }

    public abstract Shape2D getBody();

    protected abstract void deadState();

    public boolean isDead() {

        return dead;
    }

    public void setDead(boolean dead) {

        this.dead = dead;
        if (isDead()) {
            Logger.info("Combat Actor " + ID  + ": " + this.getClass().getSimpleName() + " Dead");
            deadState();
            freeActor();
        }
    }

    /**
     * Returns health percent
     * 0 - 1.
     */
    public float getHealthPercent() {

        return this.getHealth() / this.getMaxHealth();
    }

    /**
     * Returns armor percent
     * 0 - 1.
     */
    public float getArmorPercent() {

        return this.armor / this.MAX_ARMOR;
    }

    public float getMaxHealth() {

        return MAX_HEALTH;
    }

    protected void resetHealth() {

        health = MAX_HEALTH;
    }

    public void resetArmor() {

        if (hasArmor()) {
            armor = MAX_ARMOR;
        }
    }

    public float getAttack() {

        return attack;
    }

    protected void setAttack(float attack) {

        this.attack = attack;
    }

    public float getArmor() {

        return armor;
    }

    public boolean hasArmor() {

        return hasArmor;
    }

    public void setHasArmor(boolean hasArmor) {

        if (hasArmor() && !hasArmor) {
            armor = 0;
            eventManager.sendEvent(CombatActorEventEnum.ARMOR_DESTROYED);
        }
        resetArmor();
        this.hasArmor = hasArmor;
    }

    public void freeActor() {

        pool.free(this);
    }

    public Group getTargetGroup() {

        return targetGroup;
    }

    /**
     * Combat actor is an active actor on the stage.
     * It can be targeted, and attacked.
     */
    @Override
    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }

    public void setPool(CombatActorPool<? extends CombatActor> pool) {

        this.pool = pool;
    }

    public CombatActorPool<? extends Actor> getPool(){
        return pool;
    }

}
