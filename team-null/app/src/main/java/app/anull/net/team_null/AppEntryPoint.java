package app.anull.net.team_null;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Bligj on 7/4/2017.
 */

public class AppEntryPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);
        Boolean loggedIn;
        Intent redirect;

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", MODE_PRIVATE);

        /* comment out the following block unless testing requires that ALL preferences be removed at startup */
        /*
        SharedPreferences.Editor editor;
        editor = pref.edit();
        editor.clear();
        editor.commit();
        */
        /* end block */
        
        loggedIn = pref.getBoolean("LoggedIn", false);

        if (loggedIn == true) {
            redirect = new Intent(AppEntryPoint.this, MainActivity.class);
            startActivity(redirect);
            finish();
        } else { //if first time login
            redirect = new Intent(AppEntryPoint.this, RegisterActivity.class);
            startActivity(redirect);
            finish();
        }
    }
}
