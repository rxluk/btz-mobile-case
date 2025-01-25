package com.example.btzmobileapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;

public class AdminActivity extends AppCompatActivity {
    private View navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigationView = findViewById(R.id.navigationView2);

        // Definir comportamento do botão "Cadastrar Usuário"
        findViewById(R.id.btnCadastrarUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, CadastrarUserActivity.class));
            }
        });

        // Definir comportamentos para outros botões da mesma forma
    }

    @Override
    public void onBackPressed() {
        // Redirecionar para LoginActivity ao pressionar o botão de voltar
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}