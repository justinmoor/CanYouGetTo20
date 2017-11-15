package com.moor.justin.API;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.moor.justin.Models.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// Dit is de database klasse. Deze klassen handelt de database requests af.
// Deze zijn het opsturen van je persoonlijke score, en het ophalen van de highscore.
public class Database {

    private final String URLSUBMI = "http://jmoor.com/insert.php";
    private final String URLSHOW = "http://jmoor.com/showScore.php";
    private boolean done;

    // Deze methode stuurt je score op. Parameters met de behaalde score en je naam.
    public boolean submitScore(int score, String naam){

        //Score omzetten naar een String
        String scoreString = "" + score;

        // Variabelen zetten voor het php scriptje.
        Map parameters = new HashMap();
        parameters.put("naam", naam);
        parameters.put("score", scoreString);

        //Nieuw http request aanmaken. Je scores worden opgestuurd, dus een POST method wordt gebruikt.
        // Het PHP script erkent alleen POST methods.
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(URLSUBMI).build();
        httpRequest.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        // Request wordt opgestuurd. Als alles goed gaat returnt deze methode true, zo niet dan false.
        // Deze booleans worden in het gameover scherm gebruikt om te laten zien of het gelukt is of niet.
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
                    done = true;
                } else{
                    done = false;
                }
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                done = false;
            }

            @Override
            public void cancelled() {
                done = false;
            }

        });
        try {
            // Wachten op informatie vanuit de database
            Thread.sleep(httpRequest.getTimeOut() + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return done;
    }

    // ArrayList van het Score model. Deze is static want wordt in het main menu ook gebruikt.
    public static ArrayList <Score> scores;

    // Deze methode haalt een JSON string van de scores en naam de bijbehorende naam op uit de database.
    public void getScores(){
        // Nieuw http request met een GET method.
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(URLSHOW).build();
        httpRequest.setHeader("Content-Type", "application/json");

        scores = new ArrayList<Score>();

        // http request opsturen
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                // Nieuwe json wordt gemaakt a.d.h.v. de http response.
                JsonValue json = new JsonReader().parse(httpResponse.getResultAsString());
                JsonValue scoresJson = json.get("scores");

                // Models worden gevuld en toegevoegd aan de ArrayList.
                for(JsonValue scoreJ : scoresJson.iterator()){
                    Score score = new Score();
                    score.setNaam(scoreJ.getString("naam"));
                    score.setScore(scoreJ.getInt("score"));
                    scores.add(score);
                }


            }

            // Gaat er iets fout? Scores is null en er wordt een foutmelding op het highscore scherm weergeven.
            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                scores = null;
            }

            @Override
            public void cancelled() {
                scores = null;
            }
        });
    }
}
