package com.example.btzmobileapp.views.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.user.controller.UserController;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.views.BaseActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerfilActivity extends BaseActivity {
    private static final String TAG = "PerfilActivity";
    private EditText inputNome, inputUsername, inputEmail, inputSenha, inputCPF;
    private Button btnSalvarPerfil;
    private UserController userController;
    private User currentUser;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar componentes do layout
        inputNome = findViewById(R.id.inputNome);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);
        inputCPF = findViewById(R.id.inputCPF);
        btnSalvarPerfil = findViewById(R.id.btnSalvarPerfil);

        // Inicializar controlador de usuário e ExecutorService
        Log.d(TAG, "Inicializando UserController e ExecutorService");
        userController = new UserController(this);
        executorService = Executors.newSingleThreadExecutor();

        // Carregar dados do usuário logado
        executorService.execute(this::loadUserData);

        // Configurar botão de salvar perfil
        btnSalvarPerfil.setOnClickListener(v -> executorService.execute(this::saveUserProfile));
    }

    private void loadUserData() {
        try {
            // Recuperar o usuário logado do banco de dados
            currentUser = getLoggedUser();
            if (currentUser != null) {
                Log.d(TAG, "ID atual: " + currentUser.getId() + " Nome: " + currentUser.getNome());
                Log.d(TAG, "Usuário encontrado: " + currentUser.getUsername());
                runOnUiThread(this::updateUIWithUserData);
            } else {
                Log.e(TAG, "Usuário não encontrado");
                runOnUiThread(() -> Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao carregar os dados do usuário", e);
            runOnUiThread(() -> Toast.makeText(this, "Erro ao carregar os dados do usuário", Toast.LENGTH_SHORT).show());
        }
    }

    private User getLoggedUser() {
        SharedPreferences preferences = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        long userId = preferences.getLong("USER_ID", -1);
        Log.d(TAG, "Recuperado USER_ID das SharedPreferences: " + userId);

        if (userId != -1) {
            User user = userController.getUserById(userId);
            if (user != null) {
                Log.d(TAG, "Usuário recuperado: " + user.getUsername());
                return user;
            } else {
                Log.e(TAG, "Usuário não encontrado com o ID: " + userId);
            }
        } else {
            Log.e(TAG, "USER_ID inválido");
        }
        return null;
    }

    private void updateUIWithUserData() {
        try {
            Log.d(TAG, "Atualizando UI com os dados do usuário");
            inputNome.setText(currentUser.getNome());
            inputUsername.setText(currentUser.getUsername());
            inputEmail.setText(currentUser.getEmail());
            inputSenha.setText(currentUser.getPassword());
            inputCPF.setText(currentUser.getCpf());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao atualizar a UI com os dados do usuário", e);
        }
    }

    private void saveUserProfile() {
        try {
            String nome = inputNome.getText().toString();
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String senha = inputSenha.getText().toString();
            String cpf = inputCPF.getText().toString(); // Não será editado

            if (nome.isEmpty() || username.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show());
                return;
            }

            currentUser.setNome(nome);
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPassword(senha);
            currentUser.setCpf(cpf);

            userController.updateUserByCpf(currentUser.getCpf(), currentUser);

            runOnUiThread(() -> Toast.makeText(this, "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao salvar o perfil", e);
            runOnUiThread(() -> Toast.makeText(this, "Erro ao salvar o perfil", Toast.LENGTH_SHORT).show());
        }
    }
}