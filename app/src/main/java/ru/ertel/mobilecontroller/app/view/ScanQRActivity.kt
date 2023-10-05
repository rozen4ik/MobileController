package ru.ertel.mobilecontroller.app.view

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import com.budiyev.android.codescanner.*
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import ru.ertel.mobilecontroller.app.R
import ru.ertel.mobilecontroller.gear.NfcAct
import java.math.BigInteger

class ScanQRActivity : NfcAct() {

    companion object {
        const val SCANINFOCARD = "SCANINFOCARD"
    }

    private var resScan = ""
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)


        val fromCamera = intent.extras?.getString(SCANINFOCARD)
        resScan = fromCamera.toString()
        mediaPlayer = MediaPlayer.create(this, R.raw.payment_succes)

        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                vibroFone()
                val intent = Intent(this@ScanQRActivity, MainActivity::class.java)
                if (resScan == "/") {
                    intent.putExtra(SCANINFOCARD, "${it.text}/")
                } else {
                    intent.putExtra(SCANINFOCARD, it.text)
                }
                startActivity(intent)
                finish()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Инициализация камеры закончилась с ошибкой: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun vibroFone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        val milliseconds = 300L
        mediaPlayer.start()
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(milliseconds)
            }
        }
    }
}