package com.example.studybuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.studybuddy.ui.dashboard.DashboardFragment;
import com.example.studybuddy.ui.tools.ToolsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Home by default
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        // Custom bottom bar clicks
        findViewById(R.id.nav_home).setOnClickListener(v ->
                loadFragment(new DashboardFragment())
        );

        findViewById(R.id.nav_tools).setOnClickListener(v ->
                loadFragment(new ToolsFragment())
        );
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
