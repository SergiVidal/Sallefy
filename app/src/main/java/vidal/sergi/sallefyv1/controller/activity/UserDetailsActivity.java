package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;

public class UserDetailsActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        user = (User) getIntent().getSerializableExtra("User");
        System.out.println(user);
        initViews();
    }

    private void initViews() {

    }
}