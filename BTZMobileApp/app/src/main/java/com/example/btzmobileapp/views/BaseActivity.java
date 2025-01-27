package com.example.btzmobileapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isUserAuthenticated()) {
            redirectToLogin();
        }
    }

    private boolean isUserAuthenticated() {
        String role = getUserRole();
        return role != null;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String getUserRole() {
        SharedPreferences preferences = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        return preferences.getString("ROLE", null);
    }
}