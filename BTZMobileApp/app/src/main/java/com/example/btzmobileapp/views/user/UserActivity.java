package com.example.btzmobileapp.views.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.btzmobileapp.R;
import com.example.btzmobileapp.views.BaseActivity;

public class UserActivity extends BaseActivity {
    private Button btnPerfil;
    private Button btnListEquipamentos;
    private Button btnHistoricoInventarios;
    private Button btnNovoInventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicializar componentes do layout
        btnPerfil = findViewById(R.id.btnPerfil);
        btnListEquipamentos = findViewById(R.id.btnListEquipamentos);
        btnHistoricoInventarios = findViewById(R.id.btnHistoricoInventarios);
        btnNovoInventario = findViewById(R.id.btnNovoInventario);

        // Configurar ações dos botões
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a atividade de perfil
                Intent intent = new Intent(UserActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btnListEquipamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a atividade de listagem de equipamentos
                Intent intent = new Intent(UserActivity.this, ListEquipamentosActivity.class);
                startActivity(intent);
            }
        });

        btnHistoricoInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a atividade de histórico de inventários
                Intent intent = new Intent(UserActivity.this, HistoricoInventariosActivity.class);
                startActivity(intent);
            }
        });

        btnNovoInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a atividade de novo inventário
                Intent intent = new Intent(UserActivity.this, NovoInventarioActivity.class);
                startActivity(intent);
            }
        });
    }
}
