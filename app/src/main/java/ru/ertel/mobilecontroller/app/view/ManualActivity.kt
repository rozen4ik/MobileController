package ru.ertel.mobilecontroller.app.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import ru.ertel.mobilecontroller.app.R
import ru.ertel.mobilecontroller.app.controller.KonturController
import ru.ertel.mobilecontroller.app.data.DataSourceCard
import ru.ertel.mobilecontroller.app.data.DataSourceCatalogPackage

class ManualActivity : AppCompatActivity() {

    private val bundle = Bundle()
    private var messageAnswerKontur = ""
    private lateinit var infoManualCard: Button
    private lateinit var editNumberCard: EditText
    private val infoCardFragment: InfoCardFragment = InfoCardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        infoManualCard = findViewById(R.id.infoManualCard)
        editNumberCard = findViewById(R.id.editNumberCard)

        val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
        val bodyURL = settings.getString(SettingsActivity.SAVE_SETTINGS, "").toString()

        if (bodyURL.isEmpty()) {
            Toast.makeText(this, "ip и порт не настроены", Toast.LENGTH_SHORT).show()
        }

        val dataSourceCard = DataSourceCard()
        val konturController = KonturController()
        var messageInfoCard =
            "<?xml version=\"1.0\" encoding=\"windows1251\" ?>" +
                    "<spd-xml-api>" +
                    "<request version=\"1.0\" ruid=\"739F9F2B-AEDD-4D94-93FF-EB59A9A1FCF5\">" +
                    "<client identifier=\"\">" +
                    "<identifiers />" +
                    "</client>" +
                    "</request>" +
                    "</spd-xml-api>"

        infoManualCard.setOnClickListener {
            recreate()
            messageInfoCard = messageInfoCard.replace(
                "<client identifier=\"\">",
                "<client identifier=\"${editNumberCard.text}\">"
            )
            val url = "$bodyURL/spd-xml-api"
            updateInfoCard(konturController, dataSourceCard, url, messageInfoCard)
            bundle.putString("condition", dataSourceCard.getInfoCard().condition)
            bundle.putString("number", dataSourceCard.getInfoCard().number)
            bundle.putString("ruleOfUse", dataSourceCard.getInfoCard().ruleOfUse)
            bundle.putString(
                "permittedRates",
                dataSourceCard.getInfoCard().permittedRates
            )
            bundle.putString("startAction", dataSourceCard.getInfoCard().startAction)
            bundle.putString("endAction", dataSourceCard.getInfoCard().endAction)
            bundle.putString("balance", dataSourceCard.getInfoCard().balance)
            infoCardFragment.arguments = bundle
            openFragment(infoCardFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 3, 2, "Автоматический")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this@ManualActivity, AboutActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            3 -> {
                val intent = Intent(this@ManualActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeManualFragments, fragment)
            .commit()
    }

    private fun updateInfoCard(
        konturController: KonturController,
        dataSourceCard: DataSourceCard,
        url: String,
        messageInfoCard: String
    ) {
        runBlocking {
            launch(newSingleThreadContext("MyOwnThread")) {
                try {
                    messageAnswerKontur = konturController.requestPOST(url, messageInfoCard)
                    dataSourceCard.setMessageInfoCard(messageAnswerKontur)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}