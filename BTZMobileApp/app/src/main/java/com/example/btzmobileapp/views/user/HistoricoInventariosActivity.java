package com.example.btzmobileapp.views.user;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.views.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoricoInventariosActivity extends BaseActivity {

    private ListView listViewHistorico;
    private EquipamentoController equipamentoController;
    private ExecutorService executorService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_inventarios);

        equipamentoController = new EquipamentoController(this);
        executorService = Executors.newSingleThreadExecutor();

        listViewHistorico = findViewById(R.id.listViewHistorico);

        loadHistoricoInventarios();
    }

    private void loadHistoricoInventarios() {
        executorService.execute(() -> {
            List<Equipamento> equipamentos = equipamentoController.getAllEquipamentos();
            runOnUiThread(() -> {
                if (equipamentos != null && !equipamentos.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            HistoricoInventariosActivity.this,
                            android.R.layout.simple_list_item_1,
                            formatHistorico(equipamentos)
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = view.findViewById(android.R.id.text1);

                            // Alterar a cor da data se for N/A ou mais de 30 dias
                            if (textView.getText().toString().contains("N/A") ||
                                    isDateMoreThan30Days(textView.getText().toString())) {
                                textView.setTextColor(Color.RED);
                            } else {
                                textView.setTextColor(Color.BLACK);
                            }

                            return view;
                        }
                    };
                    listViewHistorico.setAdapter(adapter);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(HistoricoInventariosActivity.this, android.R.layout.simple_list_item_1, new String[]{"N/A"});
                    listViewHistorico.setAdapter(adapter);
                }
            });
        });
    }

    private List<String> formatHistorico(List<Equipamento> equipamentos) {
        List<String> historicoList = new ArrayList<>();
        Date currentDate = new Date();
        for (Equipamento equipamento : equipamentos) {
            String formattedDate = (equipamento.getLastInventoryDate() != null)
                    ? dateFormat.format(equipamento.getLastInventoryDate())
                    : "N/A";
            historicoList.add("Data: " + formattedDate + " - " + equipamento.getName() + " - " + equipamento.getCode());
        }
        return historicoList;
    }

    private boolean isDateMoreThan30Days(String formattedDate) {
        try {
            Date date = dateFormat.parse(formattedDate);
            Date currentDate = new Date();
            long diff = currentDate.getTime() - date.getTime();
            return diff > (30L * 24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return formattedDate.contains("N/A");
        }
    }
}