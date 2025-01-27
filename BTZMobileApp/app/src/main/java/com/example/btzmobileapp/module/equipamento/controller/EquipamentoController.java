package com.example.btzmobileapp.module.equipamento.controller;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.btzmobileapp.module.equipamento.domain.Equipamento;
import com.example.btzmobileapp.module.equipamento.service.EquipamentoService;
import java.util.List;

public class EquipamentoController {
    private EquipamentoService equipamentoService;

    public EquipamentoController(Context context) {
        equipamentoService = new EquipamentoService(context);
    }

    public Bitmap insertEquipamento(Equipamento equipamento) throws Exception {
        return equipamentoService.insert(equipamento);
    }

    public Equipamento getEquipamentoByQrCode(String qrCode) {
        return equipamentoService.getEquipamentoByQrCode(qrCode);
    }

    public List<Equipamento> getEquipamentosByUserId(Long id) {
        return List.of(equipamentoService.getEquipamentoById(id));
    }
    public Bitmap convertQrCodeBase64ToBitmap(String qrCodeBase64) {
        return equipamentoService.convertQrCodeBase64ToBitmap(qrCodeBase64);
    }

    public void updateEquipamento(Equipamento equipamento) {
        equipamentoService.update(equipamento);
    }

    public void deleteEquipamento(Equipamento equipamento) {
        equipamentoService.delete(equipamento);
    }

    public Equipamento getEquipamentoById(Long id) {
        return equipamentoService.getEquipamentoById(id);
    }

    public List<Equipamento> getAllEquipamentos() {
        return equipamentoService.getAllEquipamentos();
    }
}