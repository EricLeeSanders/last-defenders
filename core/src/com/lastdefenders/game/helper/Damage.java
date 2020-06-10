package com.lastdefenders.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.util.Logger;

/**
 * Class that deals damage
 *
 * @author Eric
 */
public class Damage {

    private static Circle aoeRadius = new Circle();

    private static void dealTargetDamage(Attacker attacker, Targetable target) {

        if (target != null && !target.isDead() && target.isActive()) {
            Logger.debug("Doing " + attacker.getAttack() + " damage to: " + target.getClass()
                .getSimpleName());
            target.takeDamage(attacker.getAttack());
            if (target.isDead() && attacker instanceof Tower) {
                // Only give the tower a kill if it is alive.
                if (!((Tower) attacker).isDead()) {
                    Logger.debug("Target: giving kill to attacker");
                    ((Tower) attacker).giveKill();
                }
            }
        }
    }

    public static void dealBulletDamage(Attacker attacker, Targetable target) {

        if (!(target instanceof PlatedArmor)) {
            dealTargetDamage(attacker, target);
        }
    }

    public static void dealFlameGroupDamage(Attacker attacker, SnapshotArray<? extends Actor> targetGroup,
        Polygon flameBody) {

        for (int i = targetGroup.size - 1; i >= 0; i--) {
            Actor actor = targetGroup.get(i);
            Targetable flameTarget = (Targetable) actor;
            if (flameTarget != null && !flameTarget.isDead() && flameTarget.isActive()) {
                Shape2D targetBody = flameTarget.getBody();
                if (CollisionDetection.shapesIntersect(targetBody, flameBody)) {
                    if (!(flameTarget instanceof PlatedArmor)) {
                        dealTargetDamage(attacker, flameTarget);
                    }
                }
            }
        }
    }

    private static final Circle immediateAOECircle = new Circle();
    public static void dealExplosionDamage(Attacker attacker, float radius, Vector2 position,
        SnapshotArray<? extends Actor> targetGroup) {

        aoeRadius.setPosition(position.x, position.y);
        aoeRadius.setRadius(radius);

        immediateAOECircle.setPosition(position.x, position.y);
        immediateAOECircle.setRadius(aoeRadius.radius / 2);
//        immediateAOECircle.setPosition(0, 0);
//        immediateAOECircle.setRadius(0);
        for (int i = targetGroup.size - 1; i >= 0; i--) {
            Actor actor = targetGroup.get(i);
            Targetable aoeTarget = (Targetable) actor;
            if (!aoeTarget.isDead() && aoeTarget.isActive()) {
                if (CollisionDetection.shapesIntersect(aoeTarget.getBody(), aoeRadius)) {
                    Logger.debug("Explosion: dst: " + position.dst(aoeTarget.getPositionCenter()));

                    float dst = position.dst(aoeTarget.getPositionCenter());
                    dst = dst <= radius ? dst : radius;
                    float dmgFallOff;
                    if(CollisionDetection.shapesIntersect(aoeTarget.getBody(), immediateAOECircle)){
                        dmgFallOff = 0;
                    } else {
                        dmgFallOff = attacker.getAttack() * (dst/ radius);
                    }

                    float damage = attacker.getAttack() - dmgFallOff;
                    damage = damage > 1 ? damage : 1;

                    Logger.debug("Explosion: dmg: " + damage);
                    Logger.debug("Explosion: dst: " + dst);
                    Logger.debug("Explosion: dmgFallOff: " + dmgFallOff);
                    aoeTarget.takeDamage(damage);
                    if (aoeTarget.isDead() && attacker instanceof Tower) {
                        if (!((Tower) attacker).isDead()) {
                            Logger.debug("Explosion: giving kill to attacker");
                            ((Tower) attacker).giveKill();
                        }
                    }
                }
            }
        }
    }
}
