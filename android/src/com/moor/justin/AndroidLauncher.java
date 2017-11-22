package com.moor.justin;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

// De startklasse. Dit is een activity die gestart wordt.
public class AndroidLauncher extends AndroidApplication {

	ActionResolverAndroid actionResolverAndroid;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionResolverAndroid = new ActionResolverAndroid(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainGame(actionResolverAndroid), config); //Het spel wordt opgestart vanuit een Android activity.
	}
}
