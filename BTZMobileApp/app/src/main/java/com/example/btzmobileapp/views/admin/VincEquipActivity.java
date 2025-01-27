package com.example.btzmobileapp.views.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.btzmobileapp.R;
import com.example.btzmobileapp.module.equipamento.controller.EquipamentoController;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.module.user.controller.UserController;
import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.views.BaseActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VincEquipActivity extends BaseActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private TextView txtResultado;
    private EditText inputCpf;
    private Button btnSalvar;
    private EquipamentoController equipamentoController;
    private UserController userController;
    private ExecutorService executorService;

    private Long equipamentoId;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Log.d("VincEquipActivity", "QR Code lido: " + result.getContents());
                    equipamentoId = Long.parseLong(result.getContents()); // Usar o QR Code lido como ID
                    buscarEquipamentoPorId(equipamentoId);
                } else {
                    Toast.makeText(VincEquipActivity.this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar para AdminActivity
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular_equipamento);

        // Inicializar componentes do layout
        txtResultado = findViewById(R.id.txtResultado);
        inputCpf = findViewById(R.id.inputCpf);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Inicializar controladores
        equipamentoController = new EquipamentoController(this);
        userController = new UserController(this);

        // Inicializar ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Solicitar permissão para usar a câmera ao iniciar a atividade
        if (ContextCompat.checkSelfPermission(VincEquipActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VincEquipActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            iniciarLeituraQrCode();
        }

        btnSalvar.setOnClickListener(v -> {
            String cpf = inputCpf.getText().toString();
            if (!cpf.isEmpty()) {
                vincularEquipamentoAoUsuario(cpf);
            } else {
                Toast.makeText(VincEquipActivity.this, "Por favor, insira o CPF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarLeituraQrCode() {
        qrCodeLauncher.launch(new ScanOptions().setPrompt("Scan a QR code").setBeepEnabled(true));
    }

    private void buscarEquipamentoPorId(Long id) {
        executorService.execute(() -> {
            Equipamento equipamento = equipamentoController.getEquipamentoById(id);
            runOnUiThread(() -> {
                if (equipamento != null) {
                    Log.d("VincEquipActivity", "Equipamento encontrado: " + equipamento.getName());
                    String resultado = "ID: " + equipamento.getId() + "\n" +
                            "Nome: " + equipamento.getName() + "\n" +
                            "Código: " + equipamento.getCode() + "\n" +
                            "Último Inventário: " + equipamento.getLastInventoryDate();
                    txtResultado.setText(resultado);
                    txtResultado.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Centralizar texto
                } else {
                    Log.d("VincEquipActivity", "Equipamento não encontrado");
                    Toast.makeText(VincEquipActivity.this, "Equipamento não encontrado", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar se não encontrar o equipamento
                }
            });
        });
    }

    private void vincularEquipamentoAoUsuario(String cpf) {
        executorService.execute(() -> {
            User user = userController.getUserByCpf(cpf);
            if (user != null) {
                Equipamento equipamento = equipamentoController.getEquipamentoById(equipamentoId);
                equipamento.setUserId(user.getId());
                equipamentoController.updateEquipamento(equipamento);
                runOnUiThread(() -> {
                    Toast.makeText(VincEquipActivity.this, "Equipamento vinculado ao usuário com sucesso!", Toast.LENGTH_SHORT).show();
                    voltarParaAdminActivity(); // Voltar após salvar
                });
            } else {
                runOnUiThread(() -> Toast.makeText(VincEquipActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void voltarParaAdminActivity() {
        Intent intent = new Intent(VincEquipActivity.this, AdminActivity.class);
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