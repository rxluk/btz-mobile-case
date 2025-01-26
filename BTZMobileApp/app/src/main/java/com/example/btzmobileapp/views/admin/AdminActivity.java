package com.example.btzmobileapp.views.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.views.LoginActivity;

public class AdminActivity extends AppCompatActivity {
    private View navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigationView = findViewById(R.id.navigationView2);

        // Definir comportamento do botão "Usuário"
        findViewById(R.id.btnUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, UserActivity.class));
            }
        });

        // Definir comportamento do botão "Cadastrar Equipamento"
        findViewById(R.id.btnCadastrarEquipamento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, CadastrarEquipamentoActivity.class));
            }
        });

        // Definir comportamento do botão "Ler Equipamento"
        findViewById(R.id.btnLerEquipamento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, LerEquipamentoActivity.class));
            }
        });

        // Definir comportamento do botão "Vincular Equipamento"
        findViewById(R.id.btnVincularEquipamento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, VincEquipActivity.class));
            }
        });
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