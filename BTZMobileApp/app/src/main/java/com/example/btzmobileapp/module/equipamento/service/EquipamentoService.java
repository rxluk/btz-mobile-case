package com.example.btzmobileapp.module.equipamento.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.btzmobileapp.config.AppDatabase;
import com.example.btzmobileapp.module.equipamento.dao.EquipamentoDao;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.util.QrCodeUtil;

import java.util.List;

public class EquipamentoService {
    private EquipamentoDao equipamentoDao;

    public EquipamentoService(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        equipamentoDao = db.equipamentoDao();
    }

    public Bitmap insert(Equipamento equipamento) throws Exception {
        // Inserir equipamento no banco de dados para obter o ID
        long id = equipamentoDao.insert(equipamento);
        equipamento.setId(id);

        // Gerar um identificador único usando o ID do equipamento
        String uniqueIdentifier = String.valueOf(id);
        Bitmap qrCodeBitmap = QrCodeUtil.generateQrCode(uniqueIdentifier, 300, 300);
        String qrCodeBase64 = QrCodeUtil.convertBitmapToBase64(qrCodeBitmap);
        equipamento.setQrCode(qrCodeBase64);
        Log.d("EquipamentoService", "QR Code gerado e salvo: " + uniqueIdentifier); // Log do identificador

        // Atualizar o equipamento com o QR Code gerado
        equipamentoDao.update(equipamento);
        Log.d("EquipamentoService", "Equipamento atualizado com QR Code no banco de dados");

        return qrCodeBitmap;
    }

    public void update(Equipamento equipamento) {
        equipamentoDao.update(equipamento);
    }

    public void delete(Equipamento equipamento) {
        equipamentoDao.delete(equipamento);
    }

    public Equipamento getEquipamentoById(Long id) {
        return equipamentoDao.getEquipamentoById(id);
    }

    public List<Equipamento> getEquipamentosByUserId(Long userId) {
        return equipamentoDao.getEquipamentosByUserId(userId);
    }

    public Equipamento getEquipamentoByQrCode(String qrCode) {
        Log.d("EquipamentoService", "Buscando equipamento com QR Code: " + qrCode);
        List<Equipamento> equipamentos = equipamentoDao.getAllEquipamentos();
        for (Equipamento e : equipamentos) {
            // Decodificar o QR Code do banco de dados para comparar com o identificador único
            String decodedQrCode = QrCodeUtil.decodeQrCodeBase64(e.getQrCode());
            Log.d("EquipamentoService", "Comparando com QR Code salvo: " + decodedQrCode);
            if (qrCode.equals(decodedQrCode)) {
                Log.d("EquipamentoService", "Equipamento encontrado: " + e.getName());
                return e;
            }
        }
        Log.d("EquipamentoService", "Equipamento não encontrado com QR Code: " + qrCode);
        return null;
    }

    public List<Equipamento> getAllEquipamentos() {
        return equipamentoDao.getAllEquipamentos();
    }

    public Bitmap convertQrCodeBase64ToBitmap(String qrCodeBase64) {
        return QrCodeUtil.convertBase64ToBitmap(qrCodeBase64);
    }
}