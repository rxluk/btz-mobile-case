package com.example.btzmobileapp.views.admin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.config.QrCodeConfig;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.module.user.controller.UserController;
import com.example.btzmobileapp.module.user.domain.User;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LerEquipamentoActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private TextView txtResultado;
    private EquipamentoController equipamentoController;
    private UserController userController;
    private ExecutorService executorService;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Log.d("LerEquipamentoActivity", "QR Code lido: " + result.getContents());
                    buscarEquipamentoPorId(Long.parseLong(result.getContents())); // Buscar pelo ID
                } else {
                    Toast.makeText(LerEquipamentoActivity.this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar para AdminActivity
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_equipamento);

        // Inicializar componentes do layout
        txtResultado = findViewById(R.id.txtResultado);

        // Inicializar controladores
        equipamentoController = new EquipamentoController(this);
        userController = new UserController(this);

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Solicitar permissão para usar a câmera ao iniciar a atividade
        if (ContextCompat.checkSelfPermission(LerEquipamentoActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LerEquipamentoActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            iniciarLeituraQrCode();
        }
    }

    private void iniciarLeituraQrCode() {
        qrCodeLauncher.launch(QrCodeConfig.getScanOptions());
    }

    private void buscarEquipamentoPorId(Long id) {
        Log.d("LerEquipamentoActivity", "ID lido: " + id);
        executorService.execute(() -> {
            Equipamento equipamento = equipamentoController.getEquipamentoById(id);
            runOnUiThread(() -> {
                if (equipamento != null) {
                    Log.d("LerEquipamentoActivity", "Equipamento encontrado: " + equipamento.getName());
                    User user = userController.getUserById(equipamento.getUserId());
                    String resultado = "ID: " + equipamento.getId() + "\n" +
                            "Nome: " + equipamento.getName() + "\n" +
                            "Código: " + equipamento.getCode() + "\n" +
                            "Último Inventário: " + equipamento.getLastInventoryDate() + "\n" +
                            "Usuário: " + (user != null ? user.getUsername() : "Nenhum");
                    txtResultado.setText(resultado);
                    txtResultado.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Centralizar texto
                } else {
                    Log.d("LerEquipamentoActivity", "Equipamento não encontrado");
                    Toast.makeText(LerEquipamentoActivity.this, "Equipamento não encontrado", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar para AdminActivity
                }
            });
        });
    }

    private void voltarParaAdminActivity() {
        Intent intent = new Intent(LerEquipamentoActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        voltarParaAdminActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLeituraQrCode();
            } else {
                Toast.makeText(this, "Permissão para usar a câmera negada", Toast.LENGTH_SHORT).show();
                voltarParaAdminActivity(); // Voltar para AdminActivity se a permissão for negada
            }
        }
    }
}