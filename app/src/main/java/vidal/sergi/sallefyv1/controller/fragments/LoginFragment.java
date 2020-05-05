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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.activity.MainActivity;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.LoginManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.PreferenceUtils;
import vidal.sergi.sallefyv1.utils.Session;

public class LoginFragment extends Fragment implements UserCallback {

    public static final String TAG = LoginFragment.class.getName();

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    private FragmentCallback fragmentCallback;

    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvToRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(v);
        checkSavedData();
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
        fragmentCallback = (FragmentCallback) context;
    }

    private void initViews (View v) {

        etLogin = v.findViewById(R.id.login_user);
        etPassword = v.findViewById(R.id.login_password);

        tvToRegister = v.findViewById(R.id.login_to_register);
        tvToRegister.setOnClickListener(v12 -> fragmentCallback.onChangeFragment(RegisterFragment.getInstance()));

        btnLogin = v.findViewById(R.id.login_btn_action);
        btnLogin.setOnClickListener(v1 -> doLogin(etLogin.getText().toString(), etPassword.getText().toString()));
    }

    private void doLogin(String username, String password) {
        LoginManager.getInstance(getContext())
                .loginAttempt(username, password, this);
    }
    private void checkSavedData() {
        if (checkExistingPreferences()) {
            etLogin.setText(PreferenceUtils.getUser(getContext()));
            etPassword.setText(PreferenceUtils.getPassword(getContext()));
        }
    }
    private boolean checkExistingPreferences () {
        return PreferenceUtils.getUser(getContext()) != null
                && PreferenceUtils.getPassword(getContext()) != null;
    }
    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getContext())
                .setUserToken(userToken);
        PreferenceUtils.saveUser(getContext(), etLogin.getText().toString());
        PreferenceUtils.savePassword(getContext(), etPassword.getText().toString());
        UserManager.getInstance(getContext()).getUserData(etLogin.getText().toString(), this);
    }


    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRegisterSuccess() {}

    @Override
    public void onRegisterFailure(Throwable throwable) {}

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getContext()).setUser(userData);
        Intent intent= new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUsersReceived(List<User> users) {}

    @Override
    public void onUsersFailure(Throwable throwable) {}

    @Override
    public void onFailure(Throwable throwable) {}
}
