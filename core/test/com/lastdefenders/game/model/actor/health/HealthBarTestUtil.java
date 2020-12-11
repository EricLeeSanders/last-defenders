package com.lastdefenders.game.model.actor.health;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import testutil.TestUtil;

public class HealthBarTestUtil {

    public static class HealthBarBuilder {

        private TextureRegionDrawable greenBar;
        private TextureRegionDrawable orangeBar;
        private TextureRegionDrawable redBar;
        private TextureRegionDrawable grayBar;
        private TextureRegionDrawable unfilledBar;
        private CombatActor actor;
        private ArmorIcon armorIcon;

        public HealthBarBuilder setGreenBar(TextureRegionDrawable greenBar){
            this.greenBar = greenBar;
            return this;
        }
        public HealthBarBuilder setOrangeBar(TextureRegionDrawable orangeBar){
            this.orangeBar = orangeBar;
            return this;
        }
        public HealthBarBuilder setRedBar(TextureRegionDrawable redBar){
            this.redBar = redBar;
            return this;
        }
        public HealthBarBuilder setGrayBar(TextureRegionDrawable grayBar){
            this.grayBar = grayBar;
            return this;
        }
        public HealthBarBuilder setUnfilledBar(TextureRegionDrawable unfilledBar){
            this.unfilledBar = unfilledBar;
            return this;
        }
        public HealthBarBuilder setActor(CombatActor actor){
            this.actor = actor;
            return this;
        }
        public HealthBarBuilder setArmorIcon(ArmorIcon armorIcon){
            this.armorIcon = armorIcon;
            return this;
        }

        public HealthBar build(){
            if(greenBar == null){
                greenBar = createMockBar();
            }
            if(orangeBar == null){
                orangeBar = createMockBar();
            }
            if(redBar == null){
                redBar = createMockBar();
            }
            if(grayBar == null){
                grayBar = createMockBar();
            }
            if(unfilledBar == null){
                unfilledBar = createMockBar();
            }
            if(actor == null){
                actor = TestUtil.createTower(TowerRifle.class, true); // Create random actor
            }
            if(armorIcon == null){
                armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().build();
            }

            HealthBar healthBar = new HealthBar(greenBar, orangeBar, redBar, grayBar, unfilledBar, actor, armorIcon);

            actor.getCombatActorEventObserverManager().attachObserver(healthBar);

            return healthBar;
        }

        private TextureRegionDrawable createMockBar(){
            TextureRegionDrawable textureMock = mock(TextureRegionDrawable.class);
            setupBarMock(textureMock);
            return textureMock;
        }

        private void setupBarMock(TextureRegionDrawable bar){
            TextureRegion textureRegionMock = mock(TextureRegion.class);
            Texture textureMock = mock(Texture.class);

            doReturn(textureRegionMock).when(bar).getRegion();
            doReturn(textureMock).when(textureRegionMock).getTexture();

            textureRegionMock.setRegionWidth(10);
            textureRegionMock.setRegionHeight(10);
            textureRegionMock.setRegionX(1);
            textureRegionMock.setRegionY(1);
        }

    }
}
