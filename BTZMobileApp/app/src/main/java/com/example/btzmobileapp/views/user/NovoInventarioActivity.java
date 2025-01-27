package com.example.btzmobileapp.views.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.views.BaseActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovoInventarioActivity extends BaseActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private EquipamentoController equipamentoController;
    private ExecutorService executorService;

    private Long equipamentoId;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Log.d("NovoInventarioActivity", "QR Code lido: " + result.getContents());
                    equipamentoId = Long.parseLong(result.getContents()); // Usar o QR Code lido como ID
                    atualizarDataInventario(equipamentoId);
                } else {
                    Toast.makeText(NovoInventarioActivity.this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                    voltarParaUserActivity(); // Voltar para UserActivity
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_inventario);

        // Inicializar controlador
        equipamentoController = new EquipamentoController(this);

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Solicitar permissão para usar a câmera ao iniciar a atividade
        if (ContextCompat.checkSelfPermission(NovoInventarioActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NovoInventarioActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            iniciarLeituraQrCode();
        }
    }

    private void iniciarLeituraQrCode() {
        qrCodeLauncher.launch(new ScanOptions().setPrompt("Scan a QR code").setBeepEnabled(true));
    }

    private void atualizarDataInventario(Long id) {
        executorService.execute(() -> {
            Equipamento equipamento = equipamentoController.getEquipamentoById(id);
            if (equipamento != null) {
                equipamento.setLastInventoryDate(new Date());
                equipamentoController.updateEquipamento(equipamento);
                runOnUiThread(() -> {
                    Toast.makeText(NovoInventarioActivity.this, "Data do inventário atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    voltarParaUserActivity(); // Voltar após salvar
                });
            } else {
                runOnUiThread(() -> Toast.makeText(NovoInventarioActivity.this, "Erro ao atualizar a data do inventário", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void voltarParaUserActivity() {
        Intent intent = new Intent(NovoInventarioActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        voltarParaUserActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLeituraQrCode();
            } else {
                Toast.makeText(this, "Permissão para usar a câmera negada", Toast.LENGTH_SHORT).show();
                voltarParaUserActivity(); // Voltar para UserActivity se a permissão for negada
            }
        }
    }
}