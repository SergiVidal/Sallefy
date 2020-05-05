package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.activity.MainActivity;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserRegister;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.LoginManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.PreferenceUtils;
import vidal.sergi.sallefyv1.utils.Session;

public class RegisterFragment extends Fragment implements UserCallback {

    public static final String TAG = RegisterFragment.class.getName();

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    private EditText etEmail;
    private EditText etLogin;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    private void initViews (View v) {
        etEmail = v.findViewById(R.id.register_email);
        etLogin = v.findViewById(R.id.register_login);
        etPassword = v.findViewById(R.id.register_password);

        btnRegister = v.findViewById(R.id.register_btn_action);
        btnRegister.setOnClickListener(v1 -> {
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            Session.getInstance(getContext()).setUserRegister(new UserRegister(email, login, password));
            LoginManager.getInstance(getContext()).registerAttempt(email, login, password, RegisterFragment.this);
        });
    }

    private void doLogin(String username, String password) {
        LoginManager.getInstance(getContext())
                .loginAttempt(username, password, RegisterFragment.this);
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getContext()).setUserToken(userToken);
        PreferenceUtils.saveUser(getContext(), etLogin.getText().toString());
        PreferenceUtils.savePassword(getContext(), etPassword.getText().toString());
        UserManager.getInstance(getContext()).getUserData(etLogin.getText().toString(), this);

    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Session.getInstance(getContext())
                .setUserRegister(null);
    }

    @Override
    public void onRegisterSuccess() {
        UserRegister userData = Session.getInstance(getContext()).getUserRegister();
        doLogin(userData.getLogin(), userData.getPassword());
    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Session.getInstance(getContext()).setUserRegister(null);
        Toast.makeText(getContext(), "Register failed", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getContext()).setUser(userData);
        Intent intent= new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUsersReceived(List<User> users) {}

    @Override
    public void onUsersFailure(Throwable throwable) {}

    @Override
    public void onFailure(Throwable throwable) {}
}
