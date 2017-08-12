package app.anull.net.team_null;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);

        int scores = pref.getInt("oldScore", 0);
        int questions = pref.getInt("questions", 0);
        boolean beat = pref.getBoolean("beat", false);

        String additional;
        if (beat)
            additional = "\nYou beat your old high score!\n";
        else
            additional = "";

        String strReviewFormat = getResources().getString(R.string.review, scores, questions, additional);

        TextView Review = (TextView) findViewById(R.id.review);
        Review.setText(strReviewFormat);

        Button play = (Button) findViewById(R.id.reviewPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        Button settings = (Button) findViewById(R.id.reviewSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button menu = (Button) findViewById(R.id.reviewMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}