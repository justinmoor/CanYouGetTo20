package com.moor.justin;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

// De android startklasse. Dit is een activity die gestart wordt.
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration(); //Defineert de configuratie van een android applicatie.
		initialize(new MainGame(), config); //Het spel wordt opgestart vanuit een Android activity.
	}
}
