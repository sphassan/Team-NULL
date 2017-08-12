package app.anull.net.team_null;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupActionBar();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
        final String UID = pref.getString("UID", "");

        Button reg = (Button) findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, RegisterActivity.class);
                intent.putExtra("EXTRA_INITIAL_REGISTER", false);
                startActivity(intent);
                boolean SuccessfulReReg = pref.getBoolean("Successful Reregister", true);
                if(SuccessfulReReg) {
                    pref.edit().putBoolean("Successful Reregister", false);
                    finish();
                }
            }
        });

        Button dots = (Button) findViewById(R.id.dots);
        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DotsActivity.class);
                startActivity(intent);
            }
        });

        Button convo = (Button) findViewById(R.id.convo);
        convo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ConvoActivity.class);
                startActivity(intent);
            }
        });

        Button diff = (Button) findViewById(R.id.diff);
        diff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DiffActivity.class);
                startActivity(intent);
            }
        });

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Email Statistics?")
                        .setMessage("Are you sure you want to request an email for statistics?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            makeRequest(UID);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                thread.start();

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
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

    private void makeRequest(String UID) {
        String postArray = "sendstats("+UID+")";

        int responseCode = 4333333;//default will stay as 42 if server gives no response
        try {
            //System.out.println("here 1 ");
            InetAddress addr = InetAddress.getByName("184.72.86.223");
            URL url = new URL("http://"+addr.getHostAddress()+"/sendstats");
//            System.out.println(url.toString() + "find");
            HttpURLConnection client = null;
            try {

                client = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                System.out.println("here 2 ");
                e.printStackTrace();
            }

            try{

                client.setRequestMethod("POST");
            } catch (Exception e) {
                System.out.println("here 3 ");
                e.printStackTrace();
            }
            try {

                client.setRequestProperty("Content-Type", "text/plain");
                client.setRequestProperty("charset", "utf-8");
                client.setRequestProperty("Content-Length", Integer.toString(postArray.length()));
                client.setUseCaches(false);
                client.setDoOutput(true);
                client.connect();
            } catch (Exception e) {
                System.out.println("here 4 ");
                e.printStackTrace();
            }
            OutputStreamWriter out2 = null;
            try {

                out2 = new OutputStreamWriter(client.getOutputStream());
            } catch (Exception e) {
                System.out.println("here 5 ");
                e.printStackTrace();

                Context context = getApplicationContext();
                CharSequence text = ("Failed to connect!");
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            try {

                out2.write(postArray);
                out2.flush();
                out2.close();
            } catch (Exception e) {
                System.out.println("here 6 ");
                e.printStackTrace();
            }

            responseCode = client.getResponseCode();
        }  catch (Exception e) {
            System.out.println("Here big");
            e.printStackTrace();
        }
        System.out.println("**--  Code: " + responseCode + "--**");
    }
}
