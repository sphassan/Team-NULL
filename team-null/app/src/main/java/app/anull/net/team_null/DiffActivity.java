package app.anull.net.team_null;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class DiffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff);
        setupActionBar();
/*
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diff_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
  */


        Button basic2D = (Button) findViewById(R.id.button1);
        basic2D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("faceType", "2D");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Basic 2D Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button basic3D = (Button) findViewById(R.id.button2);
        basic3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("faceType", "3D");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Basic 3D Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button simple = (Button) findViewById(R.id.button3);
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("faceType", "real");
                editor.putString("backgroundType", "simple");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Simple Real Images Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button complex = (Button) findViewById(R.id.button4);
        complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString("faceType", "real");
                editor.putString("backgroundType", "complex");
                editor.apply();
                Context context = getApplicationContext();
                CharSequence text = "Complex Real Images Selected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Difficulty");
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
