package com.example.btzmobileapp.views.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListEquipamentosActivity extends AppCompatActivity {

    private ListView listViewEquipamentos;
    private EquipamentoController equipamentoController;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_equipamentos);

        equipamentoController = new EquipamentoController(this);
        executorService = Executors.newSingleThreadExecutor();

        listViewEquipamentos = findViewById(R.id.listViewEquipamentos);

        loadEquipamentos();
    }

    private void loadEquipamentos() {
        executorService.execute(() -> {
            List<Equipamento> equipamentos = equipamentoController.getAllEquipamentos();
            runOnUiThread(() -> {
                if (equipamentos != null && !equipamentos.isEmpty()) {
                    ArrayAdapter<Equipamento> adapter = new ArrayAdapter<>(
                            ListEquipamentosActivity.this,
                            android.R.layout.simple_list_item_1,
                            equipamentos
                    );
                    listViewEquipamentos.setAdapter(adapter);
                } else {
                    // Caso nenhum equipamento seja encontrado
                    Toast.makeText(ListEquipamentosActivity.this, "Nenhum equipamento encontrado", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}