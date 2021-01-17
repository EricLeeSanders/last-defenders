package com.lastdefenders.game.model.actor.combat.tower;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.ai.TowerAIType;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.event.EnemyEventEnum;
import com.lastdefenders.game.model.actor.combat.event.EventObserver;
import com.lastdefenders.game.model.actor.combat.event.EventObserverManager;
import com.lastdefenders.game.model.actor.combat.state.StateManager;
import com.lastdefenders.game.model.actor.combat.tower.event.TowerEventEnum;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.service.factory.CombatActorFactory.TowerPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Represents a Tower
 *
 * @author Eric
 */
public abstract class Tower extends CombatActor {

    private static final float TOWER_RANGE_INCREASE_RATE = (1 / 3f);
    private static final float TOWER_SPEED_INCREASE_RATE = 0.33f;
    private static final float TOWER_ATTACK_INCREASE_RATE = 0.33f;
    private static final float TOWER_SELL_RATE = 0.75f;
    private static final float UPGRADE_MODIFIER = .3f;
    private EventObserverManager<EventObserver<Tower, TowerEventEnum>, TowerEventEnum, Tower> towerEventObserverManager = new EventObserverManager<>();
    private int cost, armorCost, speedIncreaseCost, rangeIncreaseCost, attackIncreaseCost;
    private boolean rangeIncreaseEnabled, speedIncreaseEnabled, attackIncreaseEnabled;
    private TowerAIType ai = TowerAIType.CLOSEST;
    private boolean showRange;
    private TextureRegion rangeRegion, collidingRangeRegion;
    private boolean towerColliding;
    private StateManager<TowerStateEnum, Tower> stateManager;

    private TowerPool<? extends Tower> pool;

    private GenericGroup<Enemy> targetGroup;

    public Tower(TextureRegion textureRegion, Dimension textureSize, TowerPool<? extends Tower> pool,
        GenericGroup<Enemy> targetGroup, Vector2 gunPos, TextureRegion rangeRegion,
        TextureRegion collidingRangeRegion, DeathEffectType deathEffectType, TowerAttributes attributes) {

        super(textureRegion, textureSize, gunPos, deathEffectType, attributes);
        this.cost = attributes.getCost();
        this.armorCost = (int)(UPGRADE_MODIFIER * cost);
        this.speedIncreaseCost = (int)(UPGRADE_MODIFIER * cost);
        this.rangeIncreaseCost = (int)(UPGRADE_MODIFIER * cost);
        this.attackIncreaseCost = (int)(UPGRADE_MODIFIER * cost);
        this.collidingRangeRegion = collidingRangeRegion;
        this.rangeRegion = rangeRegion;
        this.pool = pool;
        this.targetGroup = targetGroup;
    }

    public void init() {
        super.init();
        stateManager.transition(TowerStateEnum.ACTIVE);
        setActive(true);
    }

    /**
     * Gets the selling price for the tower. Adds up the upgraded attributes and
     * their cost and multiplies by a rate.
     */
    public int getSellCost() {

        int networth = cost;
        if (speedIncreaseEnabled) {
            networth += speedIncreaseCost;
        }
        if (attackIncreaseEnabled) {
            networth += attackIncreaseCost;
        }
        if (rangeIncreaseEnabled) {
            networth += rangeIncreaseCost;
        }
        if (hasArmor()) {
            networth = networth + armorCost;
        }
        return (int) (TOWER_SELL_RATE * networth);

    }

    public int getCost() {

        return cost;
    }

    public boolean isShowRange() {

        return showRange;
    }

    public void setShowRange(boolean showRange) {

        this.showRange = showRange;
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if (showRange) {
            drawRange(batch);
        }
        super.draw(batch, alpha);
    }

    void drawRange(Batch batch) {

        TextureRegion currentRangeRegion = rangeRegion;
        if (isTowerColliding()) {
            currentRangeRegion = collidingRangeRegion;
        }
        float width = getRange() * 2;
        float height = getRange() * 2;
        float x = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, width);
        float y = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, height);
        batch.draw(currentRangeRegion, x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
    }

    /**
     * Finds targets while active. Always looks for a target.
     */
    @Override
    public void act(float delta) {

        super.act(delta);
        stateManager.update(delta);
    }

    @Override
    public void reset() {

        super.reset();
        Logger.debug("Tower " + ID  + ": " + this.getClass().getSimpleName() + " Resetting");
        rangeIncreaseEnabled = false;
        speedIncreaseEnabled = false;
        attackIncreaseEnabled = false;
        this.setShowRange(false);
    }

    public void deadState() {

        stateManager.transition(TowerStateEnum.DEAD);
    }

    public void heal() {

        Logger.info("Tower " + ID  + ": " + this.getClass().getSimpleName() + " Healing");
        resetHealth();
        resetArmor();
    }

    public void increaseRange() {

        if (!rangeIncreaseEnabled) {
            Logger.info("Tower " + ID  + ": "  + this.getClass().getSimpleName() + " Increasing Range");
            rangeIncreaseEnabled = true;
            this.setRange(this.getRange() + (this.getRange() * TOWER_RANGE_INCREASE_RATE));
        }
    }

    public void increaseSpeed() {

        if (!speedIncreaseEnabled) {
            Logger.info("Tower " + ID  + ": " + this.getClass().getSimpleName() + " Increasing Speed");
            speedIncreaseEnabled = true;
            this.setAttackSpeed(
                this.getAttackSpeed() - (this.getAttackSpeed() * TOWER_SPEED_INCREASE_RATE));
        }
    }

    public void increaseAttack() {

        if (!attackIncreaseEnabled) {
            Logger.info("Tower " + ID  + ": "  + this.getClass().getSimpleName() + " Increasing attack");
            attackIncreaseEnabled = true;
            this.setAttack(this.getAttack() + (this.getAttack() * TOWER_ATTACK_INCREASE_RATE));
        }
    }

    public boolean hasIncreasedRange() {

        return rangeIncreaseEnabled;
    }

    public boolean hasIncreasedAttack() {

        return attackIncreaseEnabled;
    }

    public boolean hasIncreasedSpeed() {

        return speedIncreaseEnabled;
    }

    public int getAttackIncreaseCost() {

        return attackIncreaseCost;
    }

    public int getArmorCost() {

        return armorCost;
    }

    public int getRangeIncreaseCost() {

        return rangeIncreaseCost;
    }

    public int getSpeedIncreaseCost() {

        return speedIncreaseCost;
    }

    public void sellTower() {

        removeTower();
    }

    private void removeTower() {

        freeActor();
    }

    public TowerAIType getAI() {

        return ai;
    }

    public void setAI(TowerAIType ai) {

        this.ai = ai;
    }

    public boolean isTowerColliding() {

        return towerColliding;
    }

    public void setTowerColliding(boolean towerColliding) {

        this.towerColliding = towerColliding;
    }

    public TowerStateEnum getState() {

        return stateManager.getCurrentStateName();
    }

    public StateManager<TowerStateEnum, Tower> getStateManager(){
        return stateManager;
    }

    public void setStateManager(StateManager<TowerStateEnum, Tower> stateManager){
        this.stateManager = stateManager;
    }

    public void waveReset(){
        if(isActive()){
            stateManager.transition(TowerStateEnum.WAVE_END);
            stateManager.transition(TowerStateEnum.ACTIVE);
        }
    }

    public GenericGroup<Enemy> getEnemyGroup(){
        return targetGroup;
    }

    @Override
    public void freeActor() {
        pool.free(this);
    }

    public EventObserverManager<EventObserver<Tower, TowerEventEnum>, TowerEventEnum, Tower> getTowerEventObserverManager() {

        return towerEventObserverManager;
    }
}
