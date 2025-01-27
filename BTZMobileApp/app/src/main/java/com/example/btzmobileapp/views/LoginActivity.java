package com.example.btzmobileapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.btzmobileapp.security.EncryptionUtil;
import com.example.btzmobileapp.views.admin.AdminActivity;
import com.example.btzmobileapp.views.user.UserActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private UserController userController;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate chamado");
        setContentView(R.layout.activity_login);
        Log.d(TAG, "setContentView chamado");

        // Inicializar o UserController e o ExecutorService
        userController = new UserController(this);
        executorService = Executors.newSingleThreadExecutor();

        Log.d(TAG, "UserController e ExecutorService inicializados");

        // Adicionar usuário de teste em segundo plano
        executorService.execute(() -> {
            try {
                addTestUser();
            } catch (Exception e) {
                Log.e(TAG, "Erro ao adicionar usuário de teste", e);
            }
        });

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

                // Executar login em segundo plano
                executorService.execute(() -> login(user, pass));
            }
        });
    }

    private void addTestUser() throws Exception {
        User user = new User("admin", "Lucas Gabriel Costa", "admin@example.com", "123", "ADMIN", "11223344");
        userController.insertUser(user);
        Log.d(TAG, "Usuário de teste adicionado: " + user.getUsername());
    }

    private void login(String username, String password) {
        User foundUser = userController.getUserByUsername(username);
        Log.d(TAG, "Usuário encontrado: " + (foundUser != null ? foundUser.getUsername() : "null"));

        runOnUiThread(() -> {
            try {
                if (foundUser != null && EncryptionUtil.verifyPassword(password, foundUser.getPassword())) {
                    Log.d(TAG, "Senha verificada com sucesso");

                    // Armazenar o id e a role do usuário em SharedPreferences
                    storeUserDetails(foundUser);

                    if ("ADMIN".equals(foundUser.getRole())) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "Iniciando AdminActivity");
                    } else if ("USER".equals(foundUser.getRole())) {
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "Iniciando UserActivity");
                    } else {
                        Toast.makeText(LoginActivity.this, "Role não reconhecida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Login ou senha incorretos");
                    Toast.makeText(LoginActivity.this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao verificar a senha", e);
                Toast.makeText(LoginActivity.this, "Erro ao verificar a senha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserDetails(User user) {
        SharedPreferences preferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("USER_ID", user.getId());
        editor.putString("ROLE", user.getRole());
        editor.apply();
    }
}