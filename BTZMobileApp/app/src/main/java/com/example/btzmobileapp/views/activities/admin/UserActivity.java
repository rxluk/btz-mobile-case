package com.example.btzmobileapp.views.activities.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.module.user.controller.UserController;

public class UserActivity extends AppCompatActivity {

    private EditText username, nome, email, password;
    private RadioGroup roleGroup;
    private RadioButton radioUser, radioAdmin;
    private Button btnCadastrar, btnConsultar, btnAlterar, btnDeletar;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicializar UserController
        userController = new UserController(this);

        // Inicializar componentes do layout
        username = findViewById(R.id.username);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        roleGroup = findViewById(R.id.roleGroup);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnAlterar = findViewById(R.id.btnAlterar);
        btnDeletar = findViewById(R.id.btnDeletar);

        // Configurar botão Cadastrar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUsername = username.getText().toString();
                String nomeUser = nome.getText().toString();
                String emailUser = email.getText().toString();
                String pass = password.getText().toString();
                String userRole = roleGroup.getCheckedRadioButtonId() == R.id.radioUser ? "USER" : "ADMIN";

                User newUser = new User(userUsername, nomeUser, emailUser, pass, userRole);
                try {
                    userController.insertUser(newUser);
                    Toast.makeText(UserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } catch (Exception e) {
                    Toast.makeText(UserActivity.this, "Erro ao cadastrar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar botão Consultar
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Consultar Usuário");
                builder.setMessage("Insira o username para consulta:");
                final EditText input = new EditText(UserActivity.this);
                builder.setView(input);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userUsername = input.getText().toString();
                        User foundUser = userController.getUserByUsername(userUsername);
                        if (foundUser != null) {
                            username.setText(foundUser.getUsername());
                            nome.setText(foundUser.getNome());
                            email.setText(foundUser.getEmail());
                            password.setText(foundUser.getPassword());
                            if (foundUser.getRole().equals("USER")) {
                                radioUser.setChecked(true);
                            } else {
                                radioAdmin.setChecked(true);
                            }
                            Toast.makeText(UserActivity.this, "Usuário encontrado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // Configurar botão Alterar
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Alterar Usuário");
                builder.setMessage("Insira o antigo username caso tenha mudado:");
                final EditText input = new EditText(UserActivity.this);
                builder.setView(input);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldUsername = input.getText().toString();
                        User foundUser = userController.getUserByUsername(oldUsername);
                        if (foundUser != null) {
                            try {
                                User updatedUser = new User(
                                        username.getText().toString(),
                                        nome.getText().toString(),
                                        email.getText().toString(),
                                        password.getText().toString(),
                                        roleGroup.getCheckedRadioButtonId() == R.id.radioUser ? "USER" : "ADMIN"
                                );
                                userController.updateUser(oldUsername, updatedUser);
                                Toast.makeText(UserActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } catch (Exception e) {
                                Toast.makeText(UserActivity.this, "Erro ao atualizar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // Configurar botão Deletar
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Deletar Usuário");
                builder.setMessage("Insira o username para deletar:");
                final EditText input = new EditText(UserActivity.this);
                builder.setView(input);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userUsername = input.getText().toString();
                        try {
                            userController.deleteUser(userUsername);
                            Toast.makeText(UserActivity.this, "Usuário deletado com sucesso!", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } catch (Exception e) {
                            Toast.makeText(UserActivity.this, "Erro ao deletar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    // Implementação do método clearFields
    private void clearFields() {
        username.setText("");
        nome.setText("");
        email.setText("");
        password.setText("");
        roleGroup.clearCheck();
    }
}
