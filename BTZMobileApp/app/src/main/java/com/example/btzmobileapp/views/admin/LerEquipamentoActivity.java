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

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LerEquipamentoActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "LerEquipamentoActivity";
    private TextView txtResultado;
    private EquipamentoController equipamentoController;
    private UserController userController;
    private ExecutorService executorService;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Log.d(TAG, "QR Code lido: " + result.getContents());
                    buscarEquipamentoPorId(Long.parseLong(result.getContents())); // Buscar pelo ID
                } else {
                    Log.d(TAG, "Leitura cancelada");
                    Toast.makeText(LerEquipamentoActivity.this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar para AdminActivity
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate chamado");
        setContentView(R.layout.activity_ler_equipamento);
        Log.d(TAG, "setContentView chamado");

        // Inicializar componentes do layout
        txtResultado = findViewById(R.id.txtResultado);
        Log.d(TAG, "Componentes do layout inicializados");

        // Inicializar controladores
        equipamentoController = new EquipamentoController(this);
        userController = new UserController(this);
        Log.d(TAG, "Controladores inicializados");

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();
        Log.d(TAG, "ExecutorService inicializado");

        // Solicitar permissão para usar a câmera ao iniciar a atividade
        if (ContextCompat.checkSelfPermission(LerEquipamentoActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissão da câmera não concedida, solicitando permissão");
            ActivityCompat.requestPermissions(LerEquipamentoActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Permissão da câmera já concedida, iniciando leitura do QR Code");
            iniciarLeituraQrCode();
        }
    }

    private void iniciarLeituraQrCode() {
        Log.d(TAG, "Iniciando leitura do QR Code");
        qrCodeLauncher.launch(QrCodeConfig.getScanOptions());
    }

    private void buscarEquipamentoPorId(Long id) {
        Log.d(TAG, "ID lido: " + id);
        executorService.execute(() -> {
            try {
                Equipamento equipamento = equipamentoController.getEquipamentoById(id);
                User user = userController.getUserById(equipamento.getUserId());
                runOnUiThread(() -> {
                    if (equipamento != null) {
                        Log.d(TAG, "Equipamento encontrado: " + equipamento.getName());

                        // Formatar a data
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String lastInventoryDate = (equipamento.getLastInventoryDate() != null) ? dateFormat.format(equipamento.getLastInventoryDate()) : "N/A";

                        if (user != null) {
                            Log.d(TAG, "Usuário encontrado: " + user.getNome());
                        } else {
                            Log.d(TAG, "Usuário não encontrado para o equipamento");
                        }

                        String cpf = (user != null && user.getCpf() != null) ? user.getCpf() : "Nenhum";
                        String resultado = "ID: " + equipamento.getId() + "\n" +
                                "Nome: " + equipamento.getName() + "\n" +
                                "Código: " + equipamento.getCode() + "\n" +
                                "Último Inventário: " + lastInventoryDate + "\n" +
                                "Nome usuário: " + (user != null ? user.getNome() : "Nenhum") + "\n" +
                                "CPF: " + cpf;

                        txtResultado.setText(resultado);
                        txtResultado.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Centralizar texto
                        Log.d(TAG, "Dados exibidos no layout");
                    } else {
                        Log.d(TAG, "Equipamento não encontrado");
                        Toast.makeText(LerEquipamentoActivity.this, "Equipamento não encontrado", Toast.LENGTH_SHORT).show();
                        voltarParaAdminActivity(); // Voltar para AdminActivity
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar equipamento por ID", e);
                runOnUiThread(() -> {
                    Toast.makeText(LerEquipamentoActivity.this, "Erro ao buscar equipamento", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar para AdminActivity
                });
            }
        });
    }

    private void voltarParaAdminActivity() {
        Log.d(TAG, "Voltando para AdminActivity");
        Intent intent = new Intent(LerEquipamentoActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed chamado, voltando para AdminActivity");
        voltarParaAdminActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissão da câmera concedida, iniciando leitura do QR Code");
                iniciarLeituraQrCode();
            } else {
                Log.d(TAG, "Permissão da câmera negada, voltando para AdminActivity");
                Toast.makeText(this, "Permissão para usar a câmera negada", Toast.LENGTH_SHORT).show();
                voltarParaAdminActivity(); // Voltar para AdminActivity se a permissão for negada
            }
        }
    }
}
