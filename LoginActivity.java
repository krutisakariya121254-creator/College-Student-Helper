package com.example.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studybuddy.data.database.AppDatabase;
import com.example.studybuddy.data.entity.UserEntity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Input fields
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        // Checkbox
        CheckBox cbShowPassword = findViewById(R.id.cbShowPassword);

        // Buttons / links
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        AppDatabase db = AppDatabase.getInstance(this);

        /* ===============================
           👁️ SHOW / HIDE PASSWORD (Checkbox)
           =============================== */
        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
            } else {
                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
            }
            // Keep cursor at end
            etPassword.setSelection(etPassword.getText().length());
        });

        /* ===============================
           ✅ LOGIN BUTTON
           =============================== */
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity user = db.userDao().login(email, password);

            if (user == null) {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs =
                    getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);

            prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putInt("user_id", user.id)
                    .putString("username", user.name)
                    .apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        /* ===============================
           🔁 NEW USER → REGISTER
           =============================== */
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish(); // recommended
        });
    }
}
