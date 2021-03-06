package app.anull.net.team_null;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mLastNameView;
    private View mProgressView;
    private View mLoginFormView;
    //private boolean EXTRA_INITIAL_REGISTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //boolean for register vs re-register
        final boolean isInitialRegister = getIntent().getBooleanExtra("EXTRA_INITIAL_REGISTER", true);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mFirstNameView = (AutoCompleteTextView) findViewById(R.id.firstName);
        mLastNameView = (AutoCompleteTextView) findViewById(R.id.lastName);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        Button skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(isInitialRegister);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(boolean isInitialRegister) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid first name, last name, email.
        if(TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        } else if (!isFirstNameValid(firstName)) {
            mFirstNameView.setError(getString(R.string.error_invalid_name));
            focusView = mFirstNameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        } else if (!isLastNameValid(lastName)) {
            mLastNameView.setError(getString(R.string.error_invalid_name));
            focusView = mLastNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, firstName, lastName, isInitialRegister);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isFirstNameValid(String firstName) {
        return firstName.matches("^[a-zA-Z]+$");
    }

    private boolean isLastNameValid(String lastName) {
        return lastName.matches("^[a-zA-Z]+$");
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mFirstName;
        private final String mLastName;
        private String response = "";
        private int responseCode;
        private boolean isFirstRegister;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Glance", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        private final String UID = pref.getString("UID", "");

        UserLoginTask(String email, String firstName, String lastName, boolean isInitialRegister) {
            mEmail = email;
            mFirstName = firstName;
            mLastName = lastName;
            isFirstRegister = isInitialRegister;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String postArray;
            String registerType;
            if(!isFirstRegister) {
                registerType = "reregister";
                postArray = registerType+"("+UID+","+mEmail+","+mFirstName+","+mLastName+")";
            } else {
                registerType = "register";
                postArray = registerType+"("+mEmail+","+mFirstName+","+mLastName+")";
            }

            try {
                InetAddress addr = InetAddress.getByName("184.72.86.223");
                URL url = new URL("http://"+addr.getHostAddress()+"/"+registerType);

                HttpURLConnection client = null;
                try {
                    client = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try{
                    client.setRequestMethod("POST");
                } catch (Exception e) {
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
                    e.printStackTrace();

                    Context context = getApplicationContext();
                    CharSequence text = ("Failed to connect!");
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent registerActivity = new Intent(RegisterActivity.this, RegisterActivity.class);
                    startActivity(registerActivity);
                    finish();
                }

                OutputStreamWriter out = null;
                try {
                    out = new OutputStreamWriter(client.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();

                    Context context = getApplicationContext();
                    CharSequence text = ("Failed to connect!");
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent registerActivity = new Intent(RegisterActivity.this, RegisterActivity.class);
                    startActivity(registerActivity);
                    finish();
                }
                try {
                    out.write(postArray);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                responseCode = client.getResponseCode();
                if(isFirstRegister && responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line);
                    }
                    response = sb.toString();
                    //System.out.println("**-- Response from server: " + response + "--**");
                    editor = pref.edit();
                    editor.putString("UID", response.substring(7,43));
                    editor.putBoolean("Successful Reregister", false);
                    editor.commit();
                } else if(!isFirstRegister && responseCode == 200) {
                    editor = pref.edit();
                    editor.putBoolean("Successful Reregister", true);
                    editor.commit();
                }
            }  catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            System.out.println("**--  Code: " + responseCode + "--**");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (responseCode == 200) {
                editor = pref.edit();
                editor.putBoolean("LoggedIn", true);
                editor.commit();
                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            } else {
                Log.d("STATUS", "login failed, Code:" + responseCode);

                Context context = getApplicationContext();
                CharSequence text = ("Failed to register!");
                pref.edit().putBoolean("Successful Reregister", false);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent registerActivity = new Intent(RegisterActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

