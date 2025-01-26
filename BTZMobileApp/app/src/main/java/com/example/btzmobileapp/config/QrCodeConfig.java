package com.example.btzmobileapp.config;

import android.app.Activity;
import com.journeyapps.barcodescanner.ScanOptions;

public class QrCodeConfig {

    public static ScanOptions getScanOptions() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Aponte a câmera para o QR Code");
        options.setCameraId(0); // Use a câmera traseira
        options.setBeepEnabled(false); // Desative o som de beep
        options.setBarcodeImageEnabled(true); // Salvar imagem do código de barras
        return options;
    }
}
