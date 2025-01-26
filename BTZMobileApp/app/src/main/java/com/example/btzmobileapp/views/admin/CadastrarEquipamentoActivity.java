package com.example.btzmobileapp.views.admin;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.views.BaseAdminActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadastrarEquipamentoActivity extends BaseAdminActivity {

    private EditText inputEquipamentoNome, inputEquipamentoCodigo;
    private Button btnCadastrarEquipamento;
    private ImageView imgQrCode;
    private EquipamentoController equipamentoController;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_equipamento);

        // Inicializar componentes do layout
        inputEquipamentoNome = findViewById(R.id.inputEquipamentoNome);
        inputEquipamentoCodigo = findViewById(R.id.inputEquipamentoCodigo);
        btnCadastrarEquipamento = findViewById(R.id.btnCadastrarEquipamento);
        imgQrCode = findViewById(R.id.imgQrCode);

        // Inicializar controlador
        equipamentoController = new EquipamentoController(this);

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Configurar botão de cadastro de equipamento
        btnCadastrarEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = inputEquipamentoNome.getText().toString();
                String codigo = inputEquipamentoCodigo.getText().toString();

                if (nome.isEmpty() || codigo.isEmpty()) {
                    Toast.makeText(CadastrarEquipamentoActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Equipamento equipamento = new Equipamento(nome, codigo, null, null, null);
                cadastrarEquipamento(equipamento);
            }
        });
    }

    private void cadastrarEquipamento(Equipamento equipamento) {
        executorService.execute(() -> {
            try {
                Bitmap qrCodeBitmap = equipamentoController.insertEquipamento(equipamento);
                String qrCodeBase64 = equipamento.getQrCode();
                Log.d("CadastrarEquipamento", "QR Code salvo: " + qrCodeBase64); // Adicionando log

                // Atualizar a UI com o QR Code gerado
                runOnUiThread(() -> {
                    imgQrCode.setImageBitmap(qrCodeBitmap);
                    imgQrCode.setVisibility(View.VISIBLE);
                    Toast.makeText(CadastrarEquipamentoActivity.this, "Equipamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    clearFields();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CadastrarEquipamento", "Erro ao cadastrar equipamento", e); // Adicionando log de erro
                runOnUiThread(() -> Toast.makeText(CadastrarEquipamentoActivity.this, "Erro ao cadastrar equipamento!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void clearFields() {
        inputEquipamentoNome.setText("");
        inputEquipamentoCodigo.setText("");
    }
}