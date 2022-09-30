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
import android.widget.TextView
import ru.ertel.mobilecontroller.gear.NfcAct
import java.math.BigInteger
import ru.ertel.mobilecontroller.app.R

class ScanCardActivity : NfcAct() {

    companion object {
        const val SCANINFOCARD = "SCANINFOCARD"
    }

    private var resScan = ""
    private lateinit var textStatusScan: TextView
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)

        val fromCamera = intent.extras?.getString(SCANINFOCARD)
        resScan = fromCamera.toString()
        mediaPlayer = MediaPlayer.create(this, R.raw.payment_succes)
        textStatusScan = findViewById(R.id.textStatusScan)
    }

    public override fun onNewIntent(paramIntent: Intent) {
        super.onNewIntent(paramIntent)
        val dataFull = getMAC(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as? Tag).replace(":", "")
        textStatusScan.text = "Идет сканирование, не убирайте карту!"
//        val decimalString = BigInteger(dataFull, 16).toString()
        val decimalString = "${dataFull[6]}${dataFull[7]}${dataFull[4]}${dataFull[5]}${dataFull[2]}${dataFull[3]}${dataFull[0]}${dataFull[1]}"
        vibroFone()
        textStatusScan.text = "Данные отсканированны!"
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