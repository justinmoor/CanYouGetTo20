package com.moor.justin.Views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.moor.justin.MainGame;
import com.moor.justin.Models.Bal;

import java.util.ArrayList;
import java.util.Random;


import static com.badlogic.gdx.Gdx.input;

//Dit is de daadwerkelijke spelklasse. Hier is waar het allemaal gebeurt, alle game-logica bevindt zich in de klasse.
//De interface InputProccesor zorgt dat er input verwerkt kan worden op dit scherm, en Screen geeft aan dat het een scherm
//is, praktische methoden worden hierdoor  geimplemeteerd.
public class myGameScreen implements InputProcessor, Screen {

    private SpriteBatch batch;

    //Sprites en textures worden aangemaakt en geinitialiseerd. Elke sprite bestaat uit een texture.
    private Texture achtergrond;
    private TextureRegion backgroundTexture;

    private Texture balImg;

    private Texture driehoekImg;
    private Sprite driehoek;

    private Texture vierkantImg;
    private Sprite vierkant;

    //ArrayList van ballen die zich op het scherm bevinden.
    // De bal erft over van Sprite, want heeft aparte eigenschappen(aan de kant waar die op gaat e.d.)
    private ArrayList<Bal> ballen;

    //Rectangles worden gebruikt om botsingen af te handelen.
    private Rectangle balRect;
    private Rectangle driehoekRect;
    private Rectangle vierkantRect;

    //De score, statisch want de score blijft de gehele sessie hetzelfde en moet worden meegegeven door alle menu's.
    private static int score;
    private int tempScore = 0;

    //Font
    private BitmapFont font;

    //Geluiden
    private Sound sound;

    private int randomizer;

    private MainGame spel;

    //Gameover scherm
    private GameOverScreen gameOver;

    //Snelheid van de bal.
    private float ballSpeed = 5;


    public myGameScreen(final MainGame spel) {

        score = 0; //Score wordt op 0 gezet elke keer als er een nieuw spel begint.
        this.spel = spel;

        gameOver = new GameOverScreen(spel);
        batch = new SpriteBatch();

        //Achtergrond van het spel.
        achtergrond = new Texture("GUI/achtergrond.png");
        achtergrond.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        backgroundTexture = new TextureRegion(achtergrond, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Sprites en texures worden geinitialiseerd.
        balImg = new Texture("Sprites/bal.png");

        sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/klik.wav"));

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2.5f, 2.5f);

        driehoekImg = new Texture("Sprites/driehoek.png");
        driehoek = new Sprite(driehoekImg);

        vierkantImg = new Texture("Sprites/vierkant.png");
        vierkant = new Sprite(vierkantImg);

        ballen = new ArrayList<Bal>();
        initBallen();

        balRect = new Rectangle();
        driehoekRect = new Rectangle();
        vierkantRect = new Rectangle();

        //Zet het driehoekje op het midden van het scherm.
        driehoek.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        //Zet het vierkantje op een willekeurige locatie op het scherm, niet te dicht bij de zijkanten.
        vierkant.setPosition(randInt(20, Gdx.graphics.getWidth() - 35),
                randInt(20, Gdx.graphics.getHeight() - 20));

        //Zorgt dat dit scherm input kan ontvangen en verwerken.
        input.setInputProcessor(this);
    }

    //De render methode is de main game loop. Deze methode wordt ~60 keer per seconde aangeroepen.
    //Alle game updates gebeuren uiteindelijk in deze methode.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //Scherm wordt gecleard.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Spritebatch begint, alle grafische elementen worden getekent.
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.draw(driehoek, driehoek.getX(), driehoek.getY());
        batch.draw(vierkant, vierkant.getX(), vierkant.getY());
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() / 2 - 60, Gdx.graphics.getHeight() - 10);

        //De ballen in de arraylist Ballen worden getekent.
        if (ballen.size() > 0) {
            for (int i = 0; i < ballen.size(); i++) {
                batch.draw(ballen.get(i), ballen.get(i).getX(), ballen.get(i).getY());
            }
        }

        batch.end();

        //Elke keer als er een vierkantje wordt gepakt komt er een nieuwe bal bij.
        if (tempScore != score) {
            initBallen(); // Score is +1, laten we een nieuwe bal spawnen!
        }

        tempScore = score;

        //Beweegt de ballen
        beweegBallen();

        //Controlleert op botsingen
        checkCollision();


    }

    //Deze methode controlleert alle botsingen.
    //De rectangle klasse wordt gebruikt om collisions te detecteren.
    //De rectangles worden gebind aan de X en Y van de desbetreffende spel elementen.
    private void checkCollision() {

        //Hier wordt gecontrolleerd of het driehoekjes (de speler dus) geraakt wordt door een bal.
        //Is dit het geval? Dan wordt het game over scherm geopent.
        for (int i = 0; i < ballen.size(); i++) {
            driehoekRect.set(driehoek.getX(), driehoek.getY(), driehoek.getWidth(), driehoek.getHeight());
            balRect.set(ballen.get(i).getX(), ballen.get(i).getY(), ballen.get(i).getWidth(), ballen.get(i).getHeight());
            if (Intersector.overlaps(driehoekRect, balRect)) {
                spel.setScreen(gameOver); //Openen van gameover scherm.
            }
        }

        //Rectangles zelfde X, Y, hoogte en breedte geven van de spelelementen.
        driehoekRect.set(driehoek.getX(), driehoek.getY(), driehoek.getWidth(), driehoek.getHeight());
        vierkantRect.set(vierkant.getX(), vierkant.getY(), vierkant.getWidth(), vierkant.getHeight());


        //Hier wordt gekeken over de speler een vierkantje pakt.
        if (Intersector.overlaps(driehoekRect, vierkantRect))  //Rectangles controlleren
        {
            sound.play(0.5f);
            score++; //Vierkantje te pakken? Score ophogen met 1.
            vierkant.setPosition(randInt(20, Gdx.graphics.getWidth() - 35),
                    randInt(20, Gdx.graphics.getHeight() - 20)); //Het vierkantje wordt verplaatst naar een nieuwe locatie.
                                                                 // Ook deze niet te dict bij de zijkant.
        }

        //Controlleren of de speler het scherm niet uit komt.
        if (driehoek.getX() < 0) {
            driehoek.setX(0);
        }

        if (driehoek.getX() > Gdx.graphics.getWidth() - driehoek.getWidth()) {
            driehoek.setX(Gdx.graphics.getWidth() - driehoek.getWidth());
        }

        if (driehoek.getY() < 0) {
            driehoek.setY(0);
        }

        if (driehoek.getY() > Gdx.graphics.getHeight() - driehoek.getHeight()) {
            driehoek.setY(Gdx.graphics.getHeight() - driehoek.getHeight());
        }

    }

    //Om het spel moeilijker te maken, wordt bij elke 2 punten die erbij komen de snelheid van de ballen verhoogd.
    private void speedUp() {
        if (score % 2 == 0) {
            ballSpeed += 0.2;
        }
    }

    //Nieuwe ballen worden geinitialiseerd.
    private void initBallen() {
        //Nieuwe random locatie op de X-as tussen 30 en width - 30
        randomizer = randInt(30, Gdx.graphics.getWidth() - 30);

        //Nieuwe bal wordt aan de arraylist ballen toegevoegt.
        ballen.add(new Bal(balImg));
        ballen.get(ballen.size() - 1).setX(randomizer); //Random X-coordinaat wordt toegewezen.
        ballen.get(ballen.size() - 1).setY(Gdx.graphics.getHeight() + 100); //Bal komt boven het scherm.
        ballen.get(ballen.size() - 1).setSpawned(true); //

        //Random richting van de bal
        randomizer = (int) (Math.random() * 2 + 1);

        if (randomizer == 1)   {
            ballen.get(ballen.size() - 1).setVerX(-1);
        } else {
            ballen.get(ballen.size() - 1).setVerX(1);
        }

        ballen.get(ballen.size() - 1).setVerY(-1);


        speedUp();

    }

    private void beweegBallen() {
        for (int i = 0; i < ballen.size(); i++) {
            // Ballen laten bewegen
            ballen.get(i).setX(ballen.get(i).getX() + (ballSpeed * ballen.get(i).getverX()));
            ballen.get(i).setY(ballen.get(i).getY() + (ballSpeed * ballen.get(i).getverY()));

            // De bal collisions bevinden zich niet in collision methode om overzicht te houden.
            // Is er een collision? De richting van de bal wordt vermenigvuldigd met -1 zodat hij de andere kant op
            // kaatst.
            // Als een bal gespawned is wordt er nog niet gecontrolleerd op collisions van boven de hoogte van
            // het scherm, anders zou het balletje nooit het speelscherm bereiken.
            if (!ballen.get(i).getSpawned()) {
                if (ballen.get(i).getY() < 0 || ballen.get(i).getY() > Gdx.graphics.getHeight() - ballen.get(i).getHeight()) {
                    ballen.get(i).setVerY(ballen.get(i).getverY() * -1);
                }
            }

            //X-as op botsingen controlleren
            if (ballen.get(i).getX() <= 0  || ballen.get(i).getX() >= Gdx.graphics.getWidth() - ballen.get(i).getWidth()) {
                ballen.get(i).setVerX(ballen.get(i).getverX() * -1);
            }

            //Ballen in het speelscherm? Vanaf dan wordt er gecontrolleerd of de bal ook bovenin botst.
            if (ballen.get(i).getSpawned() && ballen.get(i).getY() < Gdx.graphics.getHeight() - 30) {
                ballen.get(i).setSpawned(false);
            }

        }
    }

    //Methode om makkelijker en overzichtelijker random waardes te creeeren.
    private int randInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }


    // Zorgen dat de driekhoek mee beweegt met je vinger.
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        driehoek.translate(Gdx.input.getDeltaX(), Gdx.input.getDeltaY() * -1);
        return false;
    }

    // Verkrijgen van de score voor het game over scherm.
    public int getScore() {
        return score;
    }

    // Alle resources worden opgeruimd om memoryleaks te voorkomen.
    @Override
    public void dispose() {
        batch.dispose();
        balImg.dispose();
        driehoekImg.dispose();
        vierkantImg.dispose();
        achtergrond.dispose();
        font.dispose();
        sound.dispose();
    }

    // Overige methodes die moeten worden over worden ge-erft, echter niet nodig.
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
