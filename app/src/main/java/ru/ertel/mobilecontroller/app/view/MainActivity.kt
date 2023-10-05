package ru.ertel.mobilecontroller.app.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.ertel.mobilecontroller.app.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import ru.ertel.mobilecontroller.app.controller.KonturController
import ru.ertel.mobilecontroller.app.data.DataSourceCard
import ru.ertel.mobilecontroller.app.view.SettingsActivity.Companion.SAVE_SETTINGS
import ru.ertel.mobilecontroller.gear.NfcAct

class MainActivity : NfcAct(), KoinComponent {

    private val bundle = Bundle()
    private var messageAnswerKontur = ""
    private lateinit var infoCard: Button
    private lateinit var infoCardQR: Button
    private val infoCardFragment: InfoCardFragment = InfoCardFragment()
    private val startFragment: StartFragment = StartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(startFragment)

        infoCard = findViewById(R.id.infoCard)
        infoCardQR = findViewById(R.id.infoCardQr)

        val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
        val bodyURL = settings.getString(SettingsActivity.SAVE_SETTINGS, "").toString()

        if (bodyURL.isEmpty()) {
            Toast.makeText(this, "ip и порт не настроены", Toast.LENGTH_SHORT).show()
        }

        val resultScanInfoCard = intent?.extras?.getString(ScanCardActivity.SCANINFOCARD)
        val dataSourceCard = DataSourceCard()
        val konturController = KonturController()
        var messageInfoCard =
            "<?xml version=\"1.0\" encoding=\"windows1251\" ?>" +
                    "<spd-xml-api>" +
                    "<request version=\"1.0\" ruid=\"739F9F2B-AEDD-4D94-93FF-EB59A9A1FCF5\">" +
                    "<client identifier=\"$resultScanInfoCard\">" +
                    "<identifiers />" +
                    "</client>" +
                    "</request>" +
                    "</spd-xml-api>"

        infoCard.setOnClickListener {
            val intent = Intent(this@MainActivity, ScanCardActivity::class.java)
            startActivity(intent)
            finish()
        }

        infoCardQR.setOnClickListener {
            val intent = Intent(this@MainActivity, ScanQRActivity::class.java)
            checkCameraPermission(intent)
        }

        if (resultScanInfoCard != null) {
            val url = "$bodyURL/spd-xml-api"
            val set = getSharedPreferences("konturToken", MODE_PRIVATE)
            val numberKontur = set.getString(SAVE_SETTINGS, "no").toString()

            updateInfo(konturController, dataSourceCard, url, messageInfoCard, resultScanInfoCard, numberKontur)
            if (dataSourceCard.getPackageArray().toString() == "{Пиратская=копия}") {
                val intent = Intent(this@MainActivity, LicenseActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                bundle.putString("idCard", resultScanInfoCard)
                bundle.putString("packageArray", dataSourceCard.getPackageArray().toString())
                bundle.putString("numberCard", dataSourceCard.getValueNumberCard())
                bundle.putString("balance", dataSourceCard.getValueBalance())
                bundle.putString("url", url)
                infoCardFragment.arguments = bundle
                openFragment(infoCardFragment)
            }
        }
        enableBeam()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 2, 2, "Ручной ввод")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            2 -> {
                val intent = Intent(this@MainActivity, ManualActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 12) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private fun checkCameraPermission(intent: Intent) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), 12)
        } else {
            startActivity(intent)
            finish()
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.place_fragments, fragment)
            .commit()
    }

    private fun updateInfo(
        konturController: KonturController,
        dataSourceCard: DataSourceCard,
        url: String,
        messageInfoCard: String,
        number: String,
        numberKontur: String
    ) {
        runBlocking {
            launch(newSingleThreadContext("MyOwnThread")) {
                try {
                    messageAnswerKontur = konturController.requestPOST(url, messageInfoCard)
                    dataSourceCard.setMessageInfoPackage(messageAnswerKontur, number, numberKontur)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}