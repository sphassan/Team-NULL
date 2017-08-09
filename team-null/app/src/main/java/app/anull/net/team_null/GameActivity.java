package app.anull.net.team_null;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int correctButton; //ID for the correct button
    private int points; //current score
    private int incorrect; //number of incorrect buttons selected
    private int dots; //number of dots
    private int questions; //number of questions asked in the game
    private int time; //time taken to select the correct dot
    private ArrayList<String> selections; //ArrayList of selected dots, format of entries is {'dot name':time}, e.g. {'bottom-left':2}
    private JSONObject question; //JSONified collection of all question data
    private JSONObject game; //JSONified array of all questions in a game
    private TextView score;

    private TimerTask onTime;

    ImageButton point1; //bottom-left
    ImageButton point2; //top-left
    ImageButton point3; //bottom-right
    ImageButton point4; //top-right
    ImageButton point5; //center-top
    ImageButton point6; //center-bottom
    ImageButton point7; //center-left
    ImageButton point8; //center-right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setupActionBar();

        /* create and define start button and click action */

        final Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                setGame();
                Log.d("POST-SET", "begin first run");
                startGame();
            }
        });


        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.game_layout);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        questions = 0;
        time = 0;
        selections = new ArrayList<>();
        question = new JSONObject();
        game = new JSONObject();

        score = new TextView(this);
        score.setVisibility(View.INVISIBLE);
        layout.addView(score, params);

        /* Initialize all buttons */

        point1 = (ImageButton) findViewById(R.id.point1); //bottom-left
        point2 = (ImageButton) findViewById(R.id.point2); //top-left
        point3 = (ImageButton) findViewById(R.id.point3); //bottom-right
        point4 = (ImageButton) findViewById(R.id.point4); //top-right
        point5 = (ImageButton) findViewById(R.id.point5); //center-top
        point6 = (ImageButton) findViewById(R.id.point6); //center-bottom
        point7 = (ImageButton) findViewById(R.id.point7); //center-left
        point8 = (ImageButton) findViewById(R.id.point8); //center-right

        /* set all buttons defined in the xml to be invisible */

        point1.setVisibility(View.INVISIBLE);
        point2.setVisibility(View.INVISIBLE);
        point3.setVisibility(View.INVISIBLE);
        point4.setVisibility(View.INVISIBLE);
        point5.setVisibility(View.INVISIBLE);
        point6.setVisibility(View.INVISIBLE);
        point7.setVisibility(View.INVISIBLE);
        point8.setVisibility(View.INVISIBLE);

        /* set resting face */

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
        boolean female = pref.getBoolean("isFemale", true);
        String type = pref.getString("faceType", "2D");
        ImageView face = (ImageView) findViewById(R.id.face);

        if (!female && type.equals("2D"))
            face.setImageResource(R.drawable.emoji_resting_m);
        //else if (female && type.equals("3D"))
        //else if (type.equals("3D"))
    }

    // handles all dot clicks
    public void onClick(View v) {
        Log.d("CLICKED", ""+v.getId());
        time = 60 - (int)(onTime.scheduledExecutionTime() - (new Date().getTime()))/1000;
        selections.add("{" + getButtonFromId(v.getId()) + ":" + time + "}");
        if (v.getId() == correctButton) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
            mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Correct!");
            AlertDialog dialog = builder.create();
            dialog.show();
            points += 100 - ((double)incorrect/dots * 100);

            questions++;

            try {
                addQuestion();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            time = 0;
            selections = new ArrayList<>();
            question = new JSONObject();

            hideButtons();
            startGame();
        }
        else {
            incorrect++;
            v.setVisibility(View.GONE);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
            mp.start();
        }
    }

    // sets up game timer
    private void setGame() {
        points = 0;
        Timer time = new Timer();
        final GameActivity game = this;
        onTime = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();

                // TODO: add changing face as a pseudorandom change
                Random r = new Random();
                int rand = r.nextInt(2);
                if (rand == 0) {
                    int temp = pref.getInt("dotNum", 2);
                    Log.d("GET DOTS", ""+temp);
                    if (temp + 2 > 8)
                        temp = 8;
                    else
                        temp += 2;
                    editor.putInt("dotNum", temp);
                    editor.commit();
                    Log.d("SET DOTS", ""+temp);
                }
                else if (rand == 1) {
                    rand = r.nextInt(6);
                    String shape = "dot";
                    switch (rand) {
                        case 0:
                            shape = "circle";
                            break;
                        case 1:
                            shape = "diamond";
                            break;
                        case 2:
                            shape = "heart";
                            break;
                        case 3:
                            shape = "square";
                            break;
                        case 4:
                            shape = "star";
                            break;
                        case 5:
                            shape = "triangle";
                            break;
                    }
                    editor.putString("dotType", shape);
                    editor.commit();
                }

                try {
                    game.send();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                game.refresh();
                game.endGame();
            }
        };
        time.schedule(onTime, 60 * 1000);
        Log.d("SET", "game set");
    }


    // TODO: transfer to review activity instead of main menu
    private void endGame() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // TODO: gather statistics from in game
    // TODO: finalize 3D assets, real pictures and implement
    // TODO: resize face to not touch sides of screen
    // sets a singular game state
    private void startGame() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", MODE_PRIVATE);

        int x = getResources().getDisplayMetrics().widthPixels;
        //int y = getResources().getDisplayMetrics().heightPixels;

        Random r = new Random();

        incorrect = 0;
        dots = pref.getInt("dotNum", 2);
        Log.d("START DOTS", ""+dots);
        String shape = pref.getString("dotType", "circle");
        int dotResId;

        switch (shape) {
            case "circle":
                dotResId = R.drawable.dot_circle;
                break;
            case "diamond":
                dotResId = R.drawable.dot_diamond;
                break;
            case "heart":
                dotResId = R.drawable.dot_heart;
                break;
            case "square":
                dotResId = R.drawable.dot_square;
                break;
            case "star":
                dotResId = R.drawable.dot_star;
                break;
            case "triangle":
                dotResId = R.drawable.dot_triangle;
                break;
            default:
                dotResId = R.drawable.dot;
        }

        ArrayList<ImageButton> buttons = new ArrayList<>();
        ArrayList<Integer> picked = new ArrayList<>();

        for (int i = 0; i < dots; i++) {
            int rand;
            if (dots != 8) {
                rand = r.nextInt(8) + 1;
                if (picked.contains(rand)) {
                    i--;
                    continue;
                }
                picked.add(rand);
            }
            else
                rand = i+1;

            switch (rand) { // TODO: use literally anything besides a switch statement why am I like this
                case 1:
                    point1.setImageResource(dotResId);
                    point1.setVisibility(View.VISIBLE);
                    point1.setOnClickListener(this);
                    buttons.add(point1);
                    break;
                case 2:
                    point2.setImageResource(dotResId);
                    point2.setVisibility(View.VISIBLE);
                    point2.setOnClickListener(this);
                    buttons.add(point2);
                    break;
                case 3:
                    point3.setImageResource(dotResId);
                    point3.setVisibility(View.VISIBLE);
                    point3.setOnClickListener(this);
                    buttons.add(point3);
                    break;
                case 4:
                    point4.setImageResource(dotResId);
                    point4.setVisibility(View.VISIBLE);
                    point4.setOnClickListener(this);
                    buttons.add(point4);
                    break;
                case 5:
                    point5.setImageResource(dotResId);
                    point5.setVisibility(View.VISIBLE);
                    point5.setOnClickListener(this);
                    buttons.add(point5);
                    break;
                case 6:
                    point6.setImageResource(dotResId);
                    point6.setVisibility(View.VISIBLE);
                    point6.setOnClickListener(this);
                    buttons.add(point6);
                    break;
                case 7:
                    point7.setImageResource(dotResId);
                    point7.setVisibility(View.VISIBLE);
                    point7.setOnClickListener(this);
                    buttons.add(point7);
                    break;
                case 8:
                    point8.setImageResource(dotResId);
                    point8.setVisibility(View.VISIBLE);
                    point8.setOnClickListener(this);
                    buttons.add(point8);
                    break;
            }
        }

        score.setVisibility(View.VISIBLE);
        score.setText("Score: " + points); // TODO: switch to Android resource strings
        score.setX(x/2 - 80);
        score.setY(200);

        correctButton = buttons.get(r.nextInt(dots)).getId();
        Log.d("CORRECT", ""+correctButton);

        String type = pref.getString("faceType", "2D");
        boolean female = pref.getBoolean("isFemale", true);

        ImageView face = (ImageView) findViewById(R.id.face);
        switch (correctButton) {
            case R.id.point1:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_bottomleft_f);
                else if (type.equals("2D"))
                    face.setImageResource(R.drawable.emoji_bottomleft_m);
                break;
            case R.id.point2:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_topleft_f);
                else if (type.equals("2D"))
                    face.setImageResource(R.drawable.emoji_topleft_m);
                break;
            case R.id.point3:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_bottomright_f);
                else if (type.equals("2D"))
                    face.setImageResource(R.drawable.emoji_bottomright_m);
                break;
            case R.id.point4:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_topright_f);
                else if (type.equals("2D"))
                  face.setImageResource(R.drawable.emoji_up_m);
                break;
            case R.id.point5:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_up_f);
                else if (type.equals("2D"))
                  face.setImageResource(R.drawable.emoji_up_m);
                break;
            case R.id.point6:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_down_f);
                else if (type.equals("2D"))
                  face.setImageResource(R.drawable.emoji_down_m);
                break;
            case R.id.point7:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_left_f);
                else if (type.equals("2D"))
                  face.setImageResource(R.drawable.emoji_left_m);
                break;
            case R.id.point8:
                if (type.equals("2D") && female)
                    face.setImageResource(R.drawable.emoji_right_f);
                else if (type.equals("2D"))
                  face.setImageResource(R.drawable.emoji_right_m);
                break;
        }
    }

    private void addQuestion() throws JSONException {
        question.put("points", points);
        question.put("times", time);
        question.put("incorrect", incorrect);
        question.put("selections", selections);
        game.put("Question " + questions, question);
    }

    private void send() throws JSONException {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
        game.put("dotNum", pref.getInt("dotNum", 2));
        game.put("dotType", pref.getString("dotType", "circle"));
        game.put("isFemale", pref.getBoolean("isFemale", true));
        game.put("faceType", pref.getString("faceType", "2D"));

        // call HTTP request method with Stringified game

        /*
        * Example Stringified Game JSON
        *
        * {'dotNum':2, 'dotType':circle, 'isFemale':true, 'faceType':'2D', 'Question 1':{'points':1000, 'time':12, 'incorrect':1, selections:{'bottom-left':5, 'bottom-right':12}}}
        *
        * Sent as a String, no logic is required until it hits the server at which point it must be JSONified and parsed to form the email
        * */
    }

    /* refresh values after every 60 second play session */
    private void refresh() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);

        if (pref.getInt("oldScore", 0) < points) {
            // TODO: do something when this happens
        }

        SharedPreferences.Editor editor;
        editor = pref.edit();
        editor.putInt("oldScore", points);
        editor.commit();

        points = 0;

        game = new JSONObject();
    }

    /* hides all buttons that may have been active in last round */
    private void hideButtons() {
        point1.setVisibility(View.INVISIBLE);
        point2.setVisibility(View.INVISIBLE);
        point3.setVisibility(View.INVISIBLE);
        point4.setVisibility(View.INVISIBLE);
        point5.setVisibility(View.INVISIBLE);
        point6.setVisibility(View.INVISIBLE);
        point7.setVisibility(View.INVISIBLE);
        point8.setVisibility(View.INVISIBLE);
    }

    public String getButtonFromId(int id) {
        if (id == point1.getId())
            return "bottom-left";
        if (id == point2.getId())
            return "top-left";
        if (id == point3.getId())
            return "bottom-right";
        if (id == point4.getId())
            return "top-right";
        if (id == point5.getId())
            return "center-top";
        if (id == point6.getId())
            return "center-bottom";
        if (id == point7.getId())
            return "center-left";
        if (id == point8.getId())
            return "center-right";
        return "error";
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Play");
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}