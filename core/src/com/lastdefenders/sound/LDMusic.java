package com.lastdefenders.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class LDMusic implements LDAudio<LDMusic> {
    private boolean loop;
    private boolean playImmediately;
    private Music music;

    public LDMusic(Builder builder){
        this.loop = builder.loop;
        this.playImmediately = builder.playImmediately;
        this.music = builder.music;
    }

    public boolean isLoop() {

        return loop;
    }

    public boolean isPlayImmediately() {

        return playImmediately;
    }

    public Music getMusic() {

        return music;
    }

    @Override
    public LDMusic getAudio() {

        return this;
    }

    public void stop(){
        music.stop();
    }

    public void dispose(){
        stop();
        music.dispose();
    }

    public static class Builder {
        private boolean loop;
        private boolean playImmediately;
        private Music music;

        public Builder(String file){
            Object j = Gdx.files.internal(file);
            this.music = Gdx.audio.newMusic(Gdx.files.internal(file));
        }

        public Builder loop(boolean loop) {

            this.loop = loop;
            return this;
        }

        public Builder playImmediately(boolean playImmediately) {

            this.playImmediately = playImmediately;
            return this;
        }
    }

    public enum Type {

        MENU(
            new Builder("audio/insurgence.ogg")
                .loop(true)
        ),
        GAME_OVER(
            new Builder("audio/fighting_forces.ogg")
                .playImmediately(true)
        );

        private LDMusic music;
        Type(Builder builder){
            this.music = new LDMusic(builder);
        }

        public LDMusic getLDMusic(){
            return music;
        }
    }
}
