package com.lastdefenders.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.lastdefenders.LDGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(640, 360);
        }

		@Override
		public ApplicationListener createApplicationListener() {
			// TODO Auto-generated method stub
			return new LDGame();
		}
}
