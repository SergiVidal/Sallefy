package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserRegister;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.Session;


public class RegisterActivity extends AppCompatActivity
        implements UserCallback {

    private EditText etEmail;
    private EditText etLogin;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews () {
        etEmail = (EditText) findViewById(R.id.register_email);
        etLogin = (EditText) findViewById(R.id.register_login);
        etPassword = (EditText) findViewById(R.id.register_password);

        btnRegister = (Button) findViewById(R.id.register_btn_action);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                Session.getInstance(getApplicationContext()).setUserRegister(new UserRegister(email, login, password));
                UserManager.getInstance(getApplicationContext()).registerAttempt(email, login, password, RegisterActivity.this);
            }
        });
    }

    private void doLogin(String username, String userpassword) {
        UserManager.getInstance(getApplicationContext())
                .loginAttempt(username, userpassword, RegisterActivity.this);
    }

    @Override
    public void onLoginSuccess(String username, UserToken userToken) {
        Session.getInstance(getApplicationContext()).setUserToken(userToken);
        Session.getInstance(getApplicationContext()).setUser(new User(username));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Session.getInstance(getApplicationContext())
                .setUserRegister(null);
    }

    @Override
    public void onRegisterSuccess() {
        UserRegister userData = Session.getInstance(getApplicationContext()).getUserRegister();
        doLogin(userData.getLogin(), userData.getPassword());
    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Session.getInstance(getApplicationContext())
                .setUserRegister(null);
        Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUserPlaylistsReceived(List<Playlist> playlistList) {

    }


    @Override
    public void onFailure(Throwable throwable) {

    }
}
