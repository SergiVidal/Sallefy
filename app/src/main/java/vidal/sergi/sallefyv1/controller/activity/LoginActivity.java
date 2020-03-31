package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.PreferenceUtils;
import vidal.sergi.sallefyv1.utils.Session;


public class LoginActivity extends AppCompatActivity implements UserCallback {

    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvToRegister;


    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_login);
        initViews();
        checkSavedData();
    }

    private void initViews () {

        etLogin = (EditText) findViewById(R.id.login_user);
        etPassword = (EditText) findViewById(R.id.login_password);
        tvToRegister = (TextView) findViewById(R.id.login_to_register);
        tvToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin = (Button) findViewById(R.id.login_btn_action);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                doLogin(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void doLogin(String username, String userpassword) {
        UserManager.getInstance(getApplicationContext())
                .loginAttempt(username, userpassword, LoginActivity.this);
    }
    private void checkSavedData() {
        if (checkExistingPreferences()) {
            etLogin.setText(PreferenceUtils.getUser(this));
            etPassword.setText(PreferenceUtils.getPassword(this));
        }
    }
    private boolean checkExistingPreferences () {
        return PreferenceUtils.getUser(this) != null
                && PreferenceUtils.getPassword(this) != null;
    }
    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);
        PreferenceUtils.saveUser(this, etLogin.getText().toString());
        PreferenceUtils.savePassword(this, etPassword.getText().toString());
        UserManager.getInstance(this).getUserData(etLogin.getText().toString(), this);
    }


    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getApplicationContext())
                .setUser(userData);
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUsersReceived(List<User> users) {

    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

}

