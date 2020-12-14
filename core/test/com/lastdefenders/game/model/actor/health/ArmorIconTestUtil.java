package com.lastdefenders.game.model.actor.health;


import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class ArmorIconTestUtil {

    public static class ArmorIconBuilder {

        private TextureRegion icon;
        private TextureRegion destroyedIcon;

        public ArmorIconBuilder setIcon(TextureRegion icon){
            this.icon = icon;
            return this;
        }
        public ArmorIconBuilder setDestroyedIcon(TextureRegion destroyedIcon){
            this.destroyedIcon = destroyedIcon;
            return this;
        }

        public ArmorIcon build(){
            if(icon == null){
                icon = mock(TextureRegion.class);
            }
            if(destroyedIcon == null){
                destroyedIcon = mock(TextureRegion.class);
            }

            ArmorIcon armorIcon = new ArmorIcon(icon, destroyedIcon);

            return armorIcon;
        }


    }
}

