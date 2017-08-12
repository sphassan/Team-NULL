package app.anull.net.team_null;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);

        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        Button settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finishAndRemoveTask(); //might not do anything
                //System.exit(0); //might not do anything either
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }
}
