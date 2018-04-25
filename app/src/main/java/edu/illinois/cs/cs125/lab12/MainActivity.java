package edu.illinois.cs.cs125.lab12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "CS125FinalProject:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    private Integer livesStart = 3;

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

        final Button restartGame = findViewById(R.id.button_restart);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Game restarted");
                startAPICall();
            }
        });

        final Button chooseA = findViewById(R.id.button_A);
        chooseA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice A picked");
                updateLives();
            }
        });

        final Button chooseB = findViewById(R.id.button_B);
        chooseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice B picked");
            }
        });

        final Button chooseC = findViewById(R.id.button_C);
        chooseC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice C picked");
            }
        });

        final Button chooseD = findViewById(R.id.button_D);
        chooseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Answer choice D picked");
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
        if (livesStart >= 0) {
            TextView lives = findViewById(R.id.lives);
            lives.setText("Lives: " + livesStart.toString());
            lives.setVisibility(View.VISIBLE);
        }
        livesStart--;
    }
}
