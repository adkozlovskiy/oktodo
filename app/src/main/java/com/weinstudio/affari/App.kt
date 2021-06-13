package com.weinstudio.affari

import android.app.Application
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
}