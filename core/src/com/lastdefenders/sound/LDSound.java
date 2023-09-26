package com.lastdefenders.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class LDSound implements LDAudio<LDSound> {

    private Sound sound;

    public LDSound(Builder builder){
        this.sound = builder.sound;
    }

    public Sound getSound(){
        return this.sound;
    }

    @Override
    public LDSound getAudio() {
        return this;
    }

    public void dispose(){
        sound.stop();
        sound.dispose();
    }

    public static class Builder {
        private Sound sound;

        public Builder(String file){
            this.sound = Gdx.audio.newSound(Gdx.files.internal(file));

        }

        public Builder build() {
            return this;
        }
    }
    public enum Type {

        ROCKET_EXPLOSION(
            new Builder("audio/rocket_explosion.ogg")
                .build()
        ),
        ROCKET_LAUNCH(
            new Builder("audio/rocket_launch.ogg")
                .build()
        ),
        FLAME(
            new Builder("audio/flame_burst.ogg")
                .build()
        ),
        RIFLE_SHOT(
            new Builder("audio/rifle_shot.ogg")
                .build()
        ),
        MACHINE_GUN_SHOT(
            new Builder("audio/machine_gun_shot.ogg")
                .build()
        ),
        SNIPER_SHOT(
            new Builder("audio/sniper_shot.ogg")
                .build()
        ),
        ACTOR_PLACE(
            new Builder("audio/actor_place.ogg")
                .build()
        ),
        SELL(
            new Builder("audio/sell.ogg")
                .build()
        ),
        SMALL_CLICK(
            new Builder("audio/button_small_click.ogg")
                .build()
        ),
        LARGE_CLICK(
            new Builder("audio/button_large_click.ogg")
                .build()
        ),
        VEHICLE_EXPLOSION(
            new Builder("audio/vehicle_explosion.ogg")
                .build()
        ),
        HELICOPTER_HOVER(
            new Builder("audio/helicopter_hover.ogg")
                .build()
        ),
        AIRCRAFT_FLYOVER(
            new Builder("audio/aircraft_flyover.ogg")
                .build()
        );

        private LDSound sound;
        Type(Builder builder){
            this.sound = new LDSound(builder);
        }

        public LDSound getLDSound(){
            return sound;
        }
    }
}
