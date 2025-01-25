package com.example.btzmobileapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.user.controller.UserController;
import com.example.btzmobileapp.module.user.service.UserService;

public class CadastrarUserActivity extends AppCompatActivity {

    private EditText login, nome, email, password;
    private RadioGroup roleGroup;
    private RadioButton radioUser, radioAdmin;
    private Button btnSubmit;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_user);

        // Inicializar UserController
        userController = new UserController(new UserService(this));

        // Inicializar componentes do layout
        login = findViewById(R.id.login);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        roleGroup = findViewById(R.id.roleGroup);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Configurar botão de envio
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userLogin = login.getText().toString();
                String nomeUser = nome.getText().toString();
                String emailUser = email.getText().toString();
                String pass = password.getText().toString();
                String userRole = roleGroup.getCheckedRadioButtonId() == R.id.radioUser ? "USER" : "ADMIN";

                User newUser = new User(userLogin, nomeUser, emailUser, pass, userRole);
                try {
                    userController.insertUser(newUser);
                    Toast.makeText(CadastrarUserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Redirecionar de volta para AdminActivity após 2 segundos
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CadastrarUserActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000); // 2000 milissegundos = 2 segundos

                } catch (Exception e) {
                    Toast.makeText(CadastrarUserActivity.this, "Erro ao cadastrar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}