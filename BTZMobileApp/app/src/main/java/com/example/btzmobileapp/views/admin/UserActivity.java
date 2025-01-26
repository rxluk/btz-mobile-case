package com.example.btzmobileapp.views.admin;

import android.os.Bundle;
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
import com.example.btzmobileapp.views.BaseAdminActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserActivity extends BaseAdminActivity {

    private EditText username, nome, email, password, cpf;
    private RadioGroup roleGroup;
    private RadioButton radioUser, radioAdmin;
    private Button btnCadastrar, btnConsultar, btnAlterar, btnDeletar;
    private UserController userController;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userController = new UserController(this);
        executorService = Executors.newSingleThreadExecutor();

        username = findViewById(R.id.username);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpf = findViewById(R.id.cpf);
        roleGroup = findViewById(R.id.roleGroup);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnAlterar = findViewById(R.id.btnAlterar);
        btnDeletar = findViewById(R.id.btnDeletar);

        btnCadastrar.setOnClickListener(v -> {
            String userUsername = username.getText().toString();
            String nomeUser = nome.getText().toString();
            String emailUser = email.getText().toString();
            String pass = password.getText().toString();
            String userCpf = cpf.getText().toString();
            String userRole = roleGroup.getCheckedRadioButtonId() == R.id.radioUser ? "USER" : "ADMIN";

            User newUser = new User(userUsername, nomeUser, emailUser, pass, userRole, userCpf);

            // Executar inserção em segundo plano
            executorService.execute(() -> {
                try {
                    userController.insertUser(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(UserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(UserActivity.this, "Erro ao cadastrar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        });

        btnConsultar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle("Consultar Usuário");
            builder.setMessage("Insira o CPF para consulta:");
            final EditText input = new EditText(UserActivity.this);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String userCpf = input.getText().toString();

                // Executar consulta em segundo plano
                executorService.execute(() -> {
                    User foundUser = userController.getUserByCpf(userCpf);
                    runOnUiThread(() -> {
                        if (foundUser != null) {
                            username.setText(foundUser.getUsername());
                            nome.setText(foundUser.getNome());
                            email.setText(foundUser.getEmail());
                            password.setText(foundUser.getPassword());
                            cpf.setText(foundUser.getCpf());
                            if (foundUser.getRole().equals("USER")) {
                                radioUser.setChecked(true);
                            } else {
                                radioAdmin.setChecked(true);
                            }
                            Toast.makeText(UserActivity.this, "Usuário encontrado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        btnAlterar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle("Alterar Usuário");
            builder.setMessage("Insira o CPF do usuário:");
            final EditText input = new EditText(UserActivity.this);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String userCpf = input.getText().toString();

                // Executar atualização em segundo plano
                executorService.execute(() -> {
                    User foundUser = userController.getUserByCpf(userCpf);
                    if (foundUser != null) {
                        try {
                            User updatedUser = new User(
                                    username.getText().toString(),
                                    nome.getText().toString(),
                                    email.getText().toString(),
                                    password.getText().toString(),
                                    roleGroup.getCheckedRadioButtonId() == R.id.radioUser ? "USER" : "ADMIN",
                                    userCpf
                            );
                            userController.updateUserByCpf(userCpf, updatedUser);
                            runOnUiThread(() -> {
                                Toast.makeText(UserActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                clearFields();
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(UserActivity.this, "Erro ao atualizar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(UserActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show());
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        btnDeletar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle("Deletar Usuário");
            builder.setMessage("Insira o CPF para deletar:");
            final EditText input = new EditText(UserActivity.this);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String userCpf = input.getText().toString();

                // Executar deleção em segundo plano
                executorService.execute(() -> {
                    try {
                        userController.deleteUserByCpf(userCpf);
                        runOnUiThread(() -> {
                            Toast.makeText(UserActivity.this, "Usuário deletado com sucesso!", Toast.LENGTH_SHORT).show();
                            clearFields();
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(UserActivity.this, "Erro ao deletar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    private void clearFields() {
        username.setText("");
        nome.setText("");
        email.setText("");
        password.setText("");
        cpf.setText("");
        roleGroup.clearCheck();
    }
}