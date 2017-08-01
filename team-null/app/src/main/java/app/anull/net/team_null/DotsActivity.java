package app.anull.net.team_null;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class DotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dots);

        setupActionBar();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);



        final TextView textView = (TextView) findViewById(R.id.textView);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(pref.getInt("dotNum", 2)-1);
        textView.setText( (seekBar.getProgress()+1) + "/" + (seekBar.getMax() + 1));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putInt("dotNum", progress+1);
                editor.apply();

                Context context = getApplicationContext();
                CharSequence text = (progress+1)+ " Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                textView.setText((progress+1) + "/" + (seekBar.getMax()+ 1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here, if you want to do anything at the start of
                // touching the seekbar


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview
                textView.setText((progress+1) + "/" + (seekBar.getMax()+1));

            }
        });

        Button circle = (Button) findViewById(R.id.button1);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "circle");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Circle Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button diamond = (Button) findViewById(R.id.button2);
        diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "diamond");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Diamond Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button heart = (Button) findViewById(R.id.button3);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "heart");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Heart Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button square = (Button) findViewById(R.id.button4);
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "square");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Square Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button star = (Button) findViewById(R.id.button5);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "star");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Star Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button triangle = (Button) findViewById(R.id.button6);
        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("dotType", "triangle");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Triangle Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dots");
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
