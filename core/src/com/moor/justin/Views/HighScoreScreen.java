package com.moor.justin.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.moor.justin.API.Database;
import com.moor.justin.MainGame;

// Deze klasse is het high score scherm.
public class HighScoreScreen implements Screen {

    private MainGame game;

    private Texture achtergrond;
    private TextureRegion backgroundTexture;
    private Texture highScoresLogo;

    private Texture backTexture;
    private TextureRegion backRegion;
    private TextureRegionDrawable backDrawable;
    private ImageButton back;

    //Wederom een stage, met een array van labels van scores en namen, highscore bestaat uit 10 plekken
    // dus 10 labels per array gaat worden geinitialiseerd.
    private Stage stage;
    private Label[] scoreNaam;
    private Label[] scoresS;

    //Een skin die is gemaakt voor de knoppen.
    private Skin scoreSkin;

    //Een tabel om de scores mooi in te kunnen weergeven.
    private Table table;
    private Sound buttonClick;

    public HighScoreScreen(final MainGame game) {
        this.game = game;

        achtergrond = new Texture("GUI/achtergrond.png");
        achtergrond.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(achtergrond, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        highScoresLogo = new Texture("GUI/highScoreLogo.png");

        backTexture = new Texture("Buttons/back.png");
        backRegion = new TextureRegion(backTexture);
        backDrawable = new TextureRegionDrawable(backRegion);
        back = new ImageButton(backDrawable);

        buttonClick = Gdx.audio.newSound(Gdx.files.internal("Sounds/buttonClick.wav"));

        stage = new Stage(new ScreenViewport());
        stage.addActor(back);

        scoreNaam = new Label[10];
        scoresS = new Label[10];

        scoreSkin = new Skin(Gdx.files.internal("styles/highScoreStyle.json"));

        table = new Table();

        //Labels initialiseren
        for (int i = 0; i < scoreNaam.length; i++) {
            scoreNaam[i] = new Label("", scoreSkin);
            scoreNaam[i].setAlignment(Align.right);
            scoresS[i] = new Label("", scoreSkin);
            scoresS[i].setAlignment(Align.left);
        }

        //Labels toevoegen aan de tabel
        for (int i = 0; i < scoreNaam.length; i++) {
            table.add(scoreNaam[i]).pad(20).align(Align.left);
            table.add(scoresS[i]).pad(20).align(Align.right);
            table.row(); //Maakt een nieuwe rij aan. Elke iteratie is dus 1 rij die wordt toegevoegd.
        }

        back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttonClick.play(0.5f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        back.setX(Gdx.graphics.getWidth() / 2 - backTexture.getWidth() / 2);
        back.setY(150);

        table.setX(Gdx.graphics.getWidth() / 2);
        table.setY(Gdx.graphics.getHeight() - 1000);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(highScoresLogo, Gdx.graphics.getWidth() / 2 - highScoresLogo.getWidth() / 2, Gdx.graphics.getHeight() - 300);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Scores ophalen uit de arraylist van scores in de database klasse.
        // De methode die al in de mainmenu stond eheft deze arraylist alvast gevuld met de juiste informatie.
        if (Database.scores != null) {
            for (int i = 0; i < Database.scores.size(); i++) {
                scoreNaam[i].setText((i + 1) + ". " + Database.scores.get(i).getNaam());
                scoresS[i].setText("" + Database.scores.get(i).getScore());
            }
        } else if (Database.scores == null) {
            //Arraylist null? Foutmelding geven.
            scoreNaam[4].setText("No internet connection...");
        }
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

    // Resources weggooien.
    @Override
    public void dispose() {
        backTexture.dispose();
        highScoresLogo.dispose();
        stage.dispose();
        scoreSkin.dispose();
    }
}
