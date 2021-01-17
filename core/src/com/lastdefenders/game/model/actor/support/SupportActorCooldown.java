package com.lastdefenders.game.model.actor.support;

public class SupportActorCooldown {

    private float cooldown;
    private float remaining;

    private boolean onCooldown;

    public SupportActorCooldown(float cooldown){
        this.cooldown = cooldown;
    }

    public void begin(){
        remaining = cooldown;
        onCooldown = true;
    }

    public void reset(){
        remaining = 0;
        onCooldown = false;
    }
    public void update(float delta){
        remaining -= delta;

        onCooldown = remaining > 0;

    }

    public boolean isOnCooldown(){
        return onCooldown;
    }

}
