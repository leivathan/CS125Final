package edu.illinois.cs.cs125.lab12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "CS125FinalProject:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    private Integer livesStart = 3;
    private Integer scoreStart = 0;
    private String difficulty = "";
    private String answerChoice = "";
    private String a_choice = "";
    private String b_choice = "";
    private String c_choice = "";
    private String d_choice = "";

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "created");
        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        updateLives();
        updateScore();

        final Button restartGame = findViewById(R.id.button_restart);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                restartGame();
            }
        });

        final Button chooseA = findViewById(R.id.button_A);
        chooseA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice A picked");
                Log.d(TAG, "Difficulty: " + difficulty);
                if (answerChoice.equals(a_choice)) {
                    updateScore();
                } else {
                    updateLives();
                }
                startAPICall();
            }
        });

        final Button chooseB = findViewById(R.id.button_B);
        chooseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice B picked");
                if (answerChoice.equals(b_choice)) {
                    updateScore();
                } else {
                    updateLives();
                }
                startAPICall();
            }
        });

        final Button chooseC = findViewById(R.id.button_C);
        chooseC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice C picked");
                if (answerChoice.equals(c_choice)) {
                    updateScore();
                } else {
                    updateLives();
                }
                startAPICall();
            }
        });

        final Button chooseD = findViewById(R.id.button_D);
        chooseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice D picked");
                if (answerChoice.equals(d_choice)) {
                    updateScore();
                } else {
                    updateLives();
                }
                startAPICall();
            }
        });
    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the trivia API.
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://opentdb.com/api.php?amount=1&type=multiple", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                setAnswerChoices(response);
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set number of lives left.
     */
    void updateLives() {
        if (livesStart > 0) {
            TextView lives = findViewById(R.id.lives);
            lives.setText("Lives: " + livesStart.toString());
            lives.setVisibility(View.VISIBLE);
            livesStart--;
        } else if (livesStart == 0) {
            gameOver();
        }
    }

    /**
     * Updates score.
     */
    void updateScore() {
        TextView scores = findViewById(R.id.scores);

        if (difficulty.equals("easy")) {
            scoreStart += 100;
        } else if (difficulty.equals("medium")) {
            scoreStart += 200;
        } else if (difficulty.equals("hard")) {
            scoreStart += 300;
        }
        scores.setText("Score: " + scoreStart.toString());
        scores.setVisibility(View.VISIBLE);
    }

    /**
     * Randomly assigns answer choices to buttons.
     * @param jsonResult jsonResult from API call
     * @throws JSONException exception
     */
    void setAnswerChoices(final JSONObject jsonResult) throws JSONException{
        Log.d(TAG, "test");
        JSONObject results = jsonResult.getJSONArray("results").getJSONObject(0);
        Log.d(TAG, results.toString());
        difficulty = results.getString("difficulty");
        answerChoice = results.getString("correct_answer");
        JSONArray incorrect_answers = results.getJSONArray("incorrect_answers");

        TextView question = findViewById(R.id.question);
        question.setText("Question: " + results.getString("question"));
        question.setVisibility(View.VISIBLE);

        Button chooseA = findViewById(R.id.button_A);
        Button chooseB = findViewById(R.id.button_B);
        Button chooseC = findViewById(R.id.button_C);
        Button chooseD = findViewById(R.id.button_D);

        ArrayList<String> choices =  new ArrayList<>();
        choices.add(answerChoice);
        choices.add(incorrect_answers.getString(0));
        choices.add(incorrect_answers.getString(1));
        choices.add(incorrect_answers.getString(2));
        Collections.shuffle(choices);

        chooseA.setText(choices.get(0));
        a_choice = choices.get(0);
        chooseB.setText(choices.get(1));
        b_choice = choices.get(1);
        chooseC.setText(choices.get(2));
        c_choice = choices.get(2);
        chooseD.setText(choices.get(3));
        d_choice = choices.get(3);
    }

    /**
     * Restarts game.
     */
    void restartGame() {
        Log.d(TAG, "Game restarted");

        livesStart = 3;
        updateLives();

        difficulty = "";
        scoreStart = 0;
        updateScore();

        startAPICall();
    }

    /**
     * Screen popup when out of lives showing score, high score, and asks to play again.
     */
    void gameOver() {
        restartGame();
    }
}
