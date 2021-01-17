package com.lastdefenders.game.service.validator;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.ui.state.GameUIStateManager;

public class SupportActorValidator {

    private int supportCost;
    private SupportActorCooldown cooldown;
    private Player player;

    public SupportActorValidator(int supportCost,
        SupportActorCooldown cooldown, Player player){

        this.supportCost = supportCost;
        this.cooldown = cooldown;
        this.player = player;
    }

    public boolean canCreateSupportActor(){
        return !isOnCooldown() && player.hasEnoughMoney(supportCost);
    }

    private boolean isOnCooldown(){
        return cooldown.isOnCooldown();
    }
}
