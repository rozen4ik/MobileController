package ru.ertel.mobilecotroller.app.view

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
import ru.ertel.mobilecotroller.gear.NfcAct
import java.math.BigInteger

class ScanCardActivity : NfcAct() {

    companion object {
        const val SCANINFOCARD = "SCANINFOCARD"
    }

    private var resScan = ""
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
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
                val intent = Intent(this@ScanCardActivity, MainActivity::class.java)
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
                Toast.makeText(this, "Camera initialization error: ${it.message}",
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

//    override fun handleResult(result : Result?) {
//        vibroFone()
//        val intent = Intent(this@ScanCardActivity, MainActivity::class.java)
//        if (resScan == "/") {
//            intent.putExtra(SCANINFOCARD, "${result?.contents}/")
//        } else {
//            intent.putExtra(SCANINFOCARD, result?.contents)
//        }
//        startActivity(intent)
//        finish()
//    }

    public override fun onNewIntent(paramIntent: Intent) {
        super.onNewIntent(paramIntent)
        val dataFull = getMAC(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as? Tag).replace(":", "")
        val decimalString = BigInteger(dataFull, 16).toString()
        vibroFone()
        val intent = Intent(this@ScanCardActivity, MainActivity::class.java)
        if (resScan == "/") {
            intent.putExtra(SCANINFOCARD, "${decimalString}/")
        } else {
            intent.putExtra(SCANINFOCARD, decimalString)
        }
        startActivity(intent)
        finish()
    }

    private fun getMAC(tag: Tag?): String =
        Regex("(.{2})").replace(
            String.format(
                "%0" + ((tag?.id?.size ?: 0) * 2).toString() + "X",
                BigInteger(1, tag?.id ?: byteArrayOf())
            ), "$1:"
        ).dropLast(1)

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