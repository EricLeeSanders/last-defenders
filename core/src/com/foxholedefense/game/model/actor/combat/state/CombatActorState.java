package com.foxholedefense.game.model.actor.combat.state;

import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public interface CombatActorState {

    void loadParameters(Map<String, Object> parameters);

    void preState();

    void update(float delta);

    void postState();
}
