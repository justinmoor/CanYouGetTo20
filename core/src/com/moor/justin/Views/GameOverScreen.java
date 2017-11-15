package com.moor.justin.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.moor.justin.API.Database;
import com.moor.justin.MainGame;

// Deze klasse is het game over scherm.
public class GameOverScreen implements Screen {

    private MainGame game;
    private Texture achtergrond;
    private TextureRegion backgroundTexture;
    private Texture logo;
    private BitmapFont font;

    // Alles staat weer in een stage.
    private Stage stage;

    // Textures en knoppen
    private Texture playAgainTexture, mainMenuTexture, submitTexture;
    private TextureRegion textureRegion, textureRegionMenu, textureRegionSubmit;
    private TextureRegionDrawable regionDrawable, regionDrawableMenu, regionDrawableSubmit;
    private ImageButton playAgain, mainMenu, submitScore;

    private TextField naam;
    private Skin nSkin;

    private Label message;
    private Skin mSkin;

    // Database connectie om je score naar de api te kunnen sturen.
    private Database db;

    private Sound buttonClick;

    // Preferences slaat data persistent op.
    // Android klasse SharedPreferences wordt gebruikt. Dit houdt in dat
    // opgeslagen data crashes en updates overleeft, maar het uninstalleren van de app niet.
    // Ik gebruik Preferences om je naam persistent op te slaan om je score te submitten, zodat
    // je deze niet telkens opnieuw hoeft in te typen.
    private Preferences pref;

    public GameOverScreen(final MainGame game) {
        this.game = game;
        db = new Database();

        logo = new Texture("GUI/logo3.png");
        achtergrond = new Texture("GUI/achtergrond.png");
        achtergrond.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(achtergrond, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(3, 3);

        submitTexture = new Texture("Buttons/submit.png");
        textureRegionSubmit = new TextureRegion(submitTexture);
        regionDrawableSubmit = new TextureRegionDrawable(textureRegionSubmit);
        submitScore = new ImageButton(regionDrawableSubmit);

        mainMenuTexture = new Texture("Buttons/mainMenu.png");
        textureRegionMenu = new TextureRegion(mainMenuTexture);
        regionDrawableMenu = new TextureRegionDrawable(textureRegionMenu);
        mainMenu = new ImageButton(regionDrawableMenu);

        playAgainTexture = new Texture("Buttons/playAgain.png");
        textureRegion = new TextureRegion(playAgainTexture);
        regionDrawable = new TextureRegionDrawable(textureRegion);
        playAgain = new ImageButton(regionDrawable);

        stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
        nSkin = new Skin(Gdx.files.internal("styles/styleNaam.json"));
        naam = new TextField(null, nSkin);

        mSkin = new Skin(Gdx.files.internal("styles/message.json"));
        message = new Label("", mSkin);

        buttonClick = Gdx.audio.newSound(Gdx.files.internal("Sounds/buttonClick.wav"));

        playAgain.setX(Gdx.graphics.getWidth() / 2 - playAgainTexture.getWidth() / 2);
        playAgain.setY(400);

        mainMenu.setX(Gdx.graphics.getWidth() / 2 - mainMenuTexture.getWidth() / 2);
        mainMenu.setY(250);

        naam.setX(Gdx.graphics.getWidth() / 2 - playAgainTexture.getWidth() / 2);
        naam.setY(700);

        submitScore.setY(550);
        submitScore.setX(Gdx.graphics.getWidth() / 2 - submitTexture.getWidth() / 2);

        pref = Gdx.app.getPreferences("submitNaam");

        // Mainmenu knop brengt je terug naar 't hoofdmenu.
        mainMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClick.play(0.5f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Nieuwe potje spelen.
        playAgain.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClick.play(0.5f);
                game.setScreen(new myGameScreen(game));
            }
        });

        // Score wordt naar de database gestuurd nadat menselijke fouten zijn gecontrolleerd.
        submitScore.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClick.play(0.5f);
                controlleer();
            }
        });

        naam.setAlignment(1);
        naam.setMessageText("Name");
        naam.setSize(300, 104);

        message.setY(150);

        stage.addActor(submitScore);
        stage.addActor(playAgain);
        stage.addActor(mainMenu);
        stage.addActor(naam);
        stage.addActor(message);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, 1000);
        font.draw(game.batch, "Game over!\n  Score: " + game.spelScherm.getScore(), Gdx.graphics.getWidth() / 2 - 115, 950);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
        // De naam uit de preferences file halen en in het textfield zetten.
        if (Gdx.app.getPreferences("submitNaam").contains("naam")) {
            naam.setText(Gdx.app.getPreferences("submitNaam").getString("naam"));
        }
        Gdx.input.setInputProcessor(stage);
    }

    // De naam wordt gecontrolleerd op verschillende fouten, is die leeg of te lang?
    // Ook wordt er gecontrolleerd of je wel internet hebt. Als er wat fout gaan in de database klasse
    // stuurt deze false terug en zal er een foutmelding gegeven worden.
    private void controlleer() {
        if (naam.getText().length() > 0 && naam.getText().length() <= 15) {
            if (db.submitScore(game.spelScherm.getScore(), naam.getText())) {
                message.setX(Gdx.graphics.getWidth() / 2 - 200);
                message.setText("Score submitted!");
                pref.putString("naam", naam.getText());
                pref.flush();
                submitScore.setTouchable(Touchable.disabled);
            } else {
                message.setX(Gdx.graphics.getWidth() / 2 - 455);
                game.actionResolver.showToast("Something went wrong, please check your internet connection.");
            //    message.setText("            Something went wrong... \nPlease check your internet connection.");
            }
        } else if (naam.getText().length() > 15) {
            message.setX(Gdx.graphics.getWidth() / 2 - 360);
            game.actionResolver.showToast("Sorry! Your name is too long...");
            message.setText("Sorry! Your name is too long...");
        } else {
            message.setX(Gdx.graphics.getWidth() / 2 - 290);
            game.actionResolver.showToast("Please enter your name");
         //   message.setText("Please enter your name!");
        }
    }

    // Resources weggooien.
    @Override
    public void dispose() {
        achtergrond.dispose();
        playAgainTexture.dispose();
        mainMenuTexture.dispose();
        submitTexture.dispose();
        mainMenuTexture.dispose();
        mSkin.dispose();
        nSkin.dispose();
        stage.dispose();
        font.dispose();
        logo.dispose();
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

}
