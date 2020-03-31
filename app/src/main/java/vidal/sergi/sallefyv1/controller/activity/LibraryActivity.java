package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vidal.sergi.sallefyv1.R;

public class LibraryActivity extends AppCompatActivity {
    private BottomNavigationView mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        initViews();

    }

    private void initViews() {
        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_library);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_search:
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
//                case R.id.action_library:
//                    intent = new Intent(getApplicationContext(), LibraryActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });
    }
}