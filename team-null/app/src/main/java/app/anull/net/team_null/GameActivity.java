package app.anull.net.team_null;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int correctButton;
    private int points;
    private int incorrect;
    private int dots; // TODO: get this value from settings
    private TextView score;

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

        score = new TextView(this);
        score.setVisibility(View.INVISIBLE);
        layout.addView(score, params);

        /* set all buttons defined in the xml to be invisible */

        ImageButton point1 = (ImageButton) findViewById(R.id.point1); //bottom-left
        point1.setVisibility(View.INVISIBLE);

        ImageButton point2 = (ImageButton) findViewById(R.id.point2); //top-left
        point2.setVisibility(View.INVISIBLE);

        ImageButton point3 = (ImageButton) findViewById(R.id.point3); //bottom-right
        point3.setVisibility(View.INVISIBLE);

        ImageButton point4 = (ImageButton) findViewById(R.id.point4); //top-right
        point4.setVisibility(View.INVISIBLE);

        ImageButton point5 = (ImageButton) findViewById(R.id.point5); //center-top
        point5.setVisibility(View.INVISIBLE);

        ImageButton point6 = (ImageButton) findViewById(R.id.point6); //center-bottom
        point6.setVisibility(View.INVISIBLE);

        ImageButton point7 = (ImageButton) findViewById(R.id.point7); //center-left
        point7.setVisibility(View.INVISIBLE);

        ImageButton point8 = (ImageButton) findViewById(R.id.point8); //center-right
        point8.setVisibility(View.INVISIBLE);
    }

    // handles all dot clicks
    public void onClick(View v) {
        Log.d("CLICKED", ""+v.getId());
        if (v.getId() == correctButton) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
            mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Correct!");
            AlertDialog dialog = builder.create();
            dialog.show();
            points += 100 - ((double)incorrect/dots * 100);
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
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                game.endGame();
            }
        }, 60 * 1000);
        Log.d("SET", "game set");
    }


    // TODO: transfer to stats activity instead of main menu
    private void endGame() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // sets a singular game state
    // TODO: programatically unhide the buttons in the layout xml based off of which are needed for a given level - needs DotsActivity to set Preference
    private void startGame() {
        int x = getResources().getDisplayMetrics().widthPixels;
        //int y = getResources().getDisplayMetrics().heightPixels;

        incorrect = 0;
        dots = 4; // TODO: don't hardcode this

        ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

        final ImageButton point1 = (ImageButton) findViewById(R.id.point1);
        point1.setVisibility(View.VISIBLE);
        point1.setOnClickListener(this);
        buttons.add(point1);

        final ImageButton point2 = (ImageButton) findViewById(R.id.point2);
        point2.setVisibility(View.VISIBLE);
        point2.setOnClickListener(this);
        buttons.add(point2);

        final ImageButton point3 = (ImageButton) findViewById(R.id.point3);
        point3.setVisibility(View.VISIBLE);
        point3.setOnClickListener(this);
        buttons.add(point3);

        final ImageButton point4 = (ImageButton) findViewById(R.id.point4);
        point4.setVisibility(View.VISIBLE);
        point4.setOnClickListener(this);
        buttons.add(point4);

        score.setVisibility(View.VISIBLE);
        score.setText("Score: " + points); // TODO: switch to Android resource strings
        score.setX(x/2 - 80);
        score.setY(200);

        Random r = new Random();
        correctButton = buttons.get(r.nextInt(4)).getId();
        Log.d("CORRECT", ""+correctButton);

        ImageView face = (ImageView) findViewById(R.id.face);
        switch (correctButton) {
            case R.id.point1:
                face.setImageResource(R.drawable.emoji_bottomleft);
                break;
            case R.id.point2:
                face.setImageResource(R.drawable.emoji_topleft);
                break;
            case R.id.point3:
                face.setImageResource(R.drawable.emoji_bottomright);
                break;
            case R.id.point4:
                face.setImageResource(R.drawable.emoji_topright);
                break;
        }
    }

    /* refresh high score after every 60 second play session */
    // TODO: use Preferences to log score
    private void refresh() {
        // check to see if score beats old score, display alert if yes
        // log score, overwriting prior
        points = 0;
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