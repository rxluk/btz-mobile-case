package com.example.btzmobileapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.user.controller.UserController;
import com.example.btzmobileapp.views.activities.admin.AdminActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate chamado");
        setContentView(R.layout.activity_login);
        Log.d(TAG, "setContentView chamado");

        // Inicializar o UserController com o contexto
        userController = new UserController(this);

        Log.d(TAG, "UserController inicializado");

        // Adicionar usuário de teste
        try {
            addTestUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        EditText username = findViewById(R.id.inputLogin);
        EditText password = findViewById(R.id.inputPassword);
        Button loginButton = findViewById(R.id.btnEntrar);

        Log.d(TAG, "Elementos da UI inicializados");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botão Entrar clicado");
                String user = username.getText().toString();
                String pass = password.getText().toString();

                User foundUser = userController.getUserByUsername(user);
                Log.d(TAG, "Usuário encontrado: " + (foundUser != null ? foundUser.getUsername() : "null"));

                if (foundUser != null && foundUser.getPassword().equals(pass)) {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTestUser() throws Exception {
        User user = new User("admin", "Lucas Gabriel Costa", "admin@example.com", "password", "ADMIN");
        userController.insertUser(user);
        Log.d(TAG, "Usuário de teste adicionado: " + user.getUsername());
    }
}