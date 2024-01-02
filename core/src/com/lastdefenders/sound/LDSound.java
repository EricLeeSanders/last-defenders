package com.lastdefenders.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

public class LDSound implements LDAudio<LDSound> {

    private Sound sound;
    private float delay;
    private long playStartTime;

    public LDSound(Builder builder){
        this.sound = builder.sound;
        this.delay = builder.delay;
    }

    public Sound getSound(){
        return this.sound;
    }

    public void play(float volume){
        playStartTime = TimeUtils.millis();
        sound.play(volume);
    }

    public boolean isReady(){
        return TimeUtils.timeSinceMillis(playStartTime) > delay * 1000;
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
        private float delay = 0;

        public Builder(String file){
            this.sound = Gdx.audio.newSound(Gdx.files.internal(file));

        }

        public Builder playMultipleAtATime(){
            this.delay = 0;
            return this;
        }

        public Builder delay(float delay){
            this.delay = delay;
            return this;
        }

        public Builder build() {
            return this;
        }
    }
    public enum Type {

        ROCKET_EXPLOSION(
            new Builder("audio/rocket_explosion.ogg")
                .playMultipleAtATime()
                .build()
        ),
        ROCKET_LAUNCH(
            new Builder("audio/rocket_launch.ogg")
                .playMultipleAtATime()
                .build()
        ),
        FLAME(
            new Builder("audio/flame_burst.ogg")
                .delay(.75F)
                .build()
        ),
        RIFLE_SHOT(
            new Builder("audio/rifle_shot.ogg")
                .delay(0.1F)
                .build()
        ),
        MACHINE_GUN_SHOT(
            new Builder("audio/machine_gun_shot.ogg")
                .delay(0.1F)
                .build()
        ),
        SNIPER_SHOT(
            new Builder("audio/sniper_shot.ogg")
                .delay(0.1F)
                .build()
        ),
        ACTOR_PLACE(
            new Builder("audio/actor_place.ogg")
                .playMultipleAtATime()
                .build()
        ),
        SELL(
            new Builder("audio/sell.ogg")
                .playMultipleAtATime()
                .build()
        ),
        SMALL_CLICK(
            new Builder("audio/button_small_click.ogg")
                .playMultipleAtATime()
                .build()
        ),
        LARGE_CLICK(
            new Builder("audio/button_large_click.ogg")
                .playMultipleAtATime()
                .build()
        ),
        VEHICLE_EXPLOSION(
            new Builder("audio/vehicle_explosion.ogg")
                .playMultipleAtATime()
                .build()
        ),
        HELICOPTER_HOVER(
            new Builder("audio/helicopter_hover.ogg")
                .playMultipleAtATime()
                .build()
        ),
        AIRCRAFT_FLYOVER(
            new Builder("audio/aircraft_flyover.ogg")
                .playMultipleAtATime()
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
