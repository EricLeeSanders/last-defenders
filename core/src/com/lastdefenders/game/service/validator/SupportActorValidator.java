package com.lastdefenders.game.service.validator;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;

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

    public void beginCooldown(){
        if(isOnCooldown()){
            throw new IllegalStateException("Cooldown has already started");
        }
        cooldown.begin();
    }

    public ValidationResponseEnum canCreateSupportActor(){
        if(isOnCooldown()){
            return ValidationResponseEnum.ON_COOLDOWN;
        } else if (!player.hasEnoughMoney(supportCost)){
            return ValidationResponseEnum.INSUFFICIENT_MONEY;
        } else {
            return ValidationResponseEnum.OK;
        }
    }

    public SupportActorCooldown getCooldown(){
        return cooldown;
    }

    private boolean isOnCooldown(){
        return cooldown.isOnCooldown();
    }
}
