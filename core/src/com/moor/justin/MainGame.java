package com.moor.justin;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.moor.justin.Views.MainMenuScreen;
import com.moor.justin.Views.myGameScreen;

// De gameklasse. De overerving van Game (wat een Application listener is) zorgt ervoor dat de
// applicatie meerdere schermen kan hebben (Denk hierbij aan een hoofdmenu, spelscherm, etc.)
public class MainGame extends Game {

    //LibGdx variabelen
    public SpriteBatch batch; // Tekent grafische elementen op het scherm a.d.h.v afbeeldingen.
    public BitmapFont font; // Letter font.
    public myGameScreen spelScherm; //Spelscherm

    //In de create methode wordt alles geinitialiseerd. Vergelijkbaar met onCreate binnen de android libary.
    public void create(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.1f, 1.1f);
        spelScherm = new myGameScreen(this);

        //Het hoofdmenu wordt als eerste scherm weergeven zodra de applicatie start.
        // Deze klasse (MainGame dus) wordt meegegeven zodat het ook vanuit daar (eigenlijk in elk ander scherm)
        // mogelijk is om van scherm te veranderen.
        this.setScreen(new MainMenuScreen(this));
    }

    //Rendert alle grafische elementen. Nadere toelichten in de spelscherm klasse.
    public void render(){
        super.render();
    }

    //Wordt geroepen zodra de applicatie gesloten wordt. Gooit alles game resources weg, om memory leaks te voorkomen.
    public void dispose(){
       super.dispose();
    }

}
