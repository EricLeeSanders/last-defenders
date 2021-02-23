package com.lastdefenders.game.model.actor.support;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class SupportActorCooldown extends Actor {

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

    @Override
    public void act(float delta){

        if(!isOnCooldown()){
            return;
        }

        remaining -= delta;

        onCooldown = remaining > 0;

    }

    public float getRemaining(){
        return remaining;
    }

    public boolean isOnCooldown(){
        return onCooldown;
    }

}
