package com.tonikamitv.loginregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// implementing to handle click events
public class Login extends ActionBarActivity implements View.OnClickListener {

    // properties
    Button bLogin;
    TextView tvRegisterLink; //link beneath
    EditText etUsername, etPassword;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // in the login activity, it will find the view (using id) and
        // cast it to edit text and then assign to properties
        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        // starts listening for clicks on blogin button and register link
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    // overrides listener interface methods
    // switch is for distinguishing between the possible sources of click event
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin: // resembles to if statements - if the login button is notified
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);
                break;
            case R.id.tvRegisterLink: // if the register link is called
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent); // start the register activity when it is clicked
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Sorry, incorrect username or password.");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }
}
