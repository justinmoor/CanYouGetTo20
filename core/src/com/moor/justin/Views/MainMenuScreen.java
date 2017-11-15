package com.moor.justin.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.moor.justin.API.Database;
import com.moor.justin.MainGame;

// Deze klasse in het hoofd menu scherm.
public class MainMenuScreen implements Screen {

    private MainGame game;

    private Texture achtergrond;
    private TextureRegion backgroundTexture;
    private Texture logo;
    private Sprite logoS;
    private BitmapFont font;

    // Een stage wordt aangemaakt zodat daar knoppen op kwijt kunnen.
    private Stage stage;
    private Texture startTexture, hightScoreTexture;
    private TextureRegion textureRegion, highScoreTextureRegion;
    private TextureRegionDrawable textureRegionDrawable, scoreDrawable;

    // 2 Knoppen
    private ImageButton start, highScores;

    // Database connectie
    private Database db;
    private Sound buttonClick;

    public MainMenuScreen(final MainGame game){
        this.game = game;

        logo = new Texture("GUI/logo3.png");

        logoS = new Sprite(logo);

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2.5f, 2.5f );

        db = new Database();
        buttonClick = Gdx.audio.newSound(Gdx.files.internal("Sounds/buttonClick.wav"));

        achtergrond = new Texture("GUI/achtergrond.png");
        achtergrond.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(achtergrond, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        startTexture = new Texture("Buttons/start.png");
        textureRegion = new TextureRegion(startTexture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        start = new ImageButton(textureRegionDrawable);

        hightScoreTexture = new Texture("Buttons/scores.png");
        highScoreTextureRegion = new TextureRegion(hightScoreTexture);
        scoreDrawable = new TextureRegionDrawable(highScoreTextureRegion);
        highScores = new ImageButton(scoreDrawable);

        stage = new Stage(new ScreenViewport());
        start.setX(Gdx.graphics.getWidth() / 2 - startTexture.getWidth() / 2);
        start.setY(550);

        highScores.setX(Gdx.graphics.getWidth() / 2 - startTexture.getWidth() / 2);
        highScores.setY(400);

        // Functionaliteit toevoegen aan de knoppen.
        // Startknop start het spelscherm, dus een nieuwe sessie wordt gestart.
        start.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                buttonClick.play(0.5f);
                game.setScreen(new myGameScreen(game));
            }
        });

        //Opent het highscore scherm.
        highScores.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                buttonClick.play(0.5f);
                game.setScreen(new HighScoreScreen(game));

            }
        });

        stage.addActor(start);
        stage.addActor(highScores);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(logoS, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, 1000);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime()); // Stage renderen.
        stage.draw();
    }

    // De show() methode wordt opgeroepen op het moment dat een scherm wordt geopend.
    // In het main menu worden alvast alle scores opgehaald, zodat deze zonder vertraging van bijvoorbeeld
    // het internet in het highscore menu kunnen worden geladen, op het moment dat de speler daar op drukt.
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Zorgen dat de stage input kan ontvangen (dus de knoppen werken).
        db.getScores();
    }

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

    // Alle resources opruimen, wederom.
    @Override
    public void dispose() {
        achtergrond.dispose();
        logo.dispose();
        stage.dispose();
        font.dispose();
        startTexture.dispose();
        hightScoreTexture.dispose();
        buttonClick.dispose();
    }
}
