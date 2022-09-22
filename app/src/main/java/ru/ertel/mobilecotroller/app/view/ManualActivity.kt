package ru.ertel.mobilecotroller.app.view

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
import ru.ertel.mobilecotroller.app.R
import ru.ertel.mobilecotroller.app.controller.KonturController
import ru.ertel.mobilecotroller.app.data.DataSourceCard
import ru.ertel.mobilecotroller.app.data.DataSourceCatalogPackage

class ManualActivity : AppCompatActivity() {

    private val bundle = Bundle()
    private var messageAnswerKontur = ""
    private lateinit var infoManulaCard: Button
    private lateinit var passageManualCard: Button
    private lateinit var editNumberCard: EditText
    private val infoCardFragment: InfoCardFragment = InfoCardFragment()
    private val passageCardFragment: PassageCardFragment = PassageCardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        infoManulaCard = findViewById(R.id.infoManualCard)
        passageManualCard = findViewById(R.id.passageManualCard)
        editNumberCard = findViewById(R.id.editNumberCard)

        val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
        val setDeivce: SharedPreferences = getSharedPreferences("DEVICE", MODE_PRIVATE)
        val bodyURL = settings.getString(SettingsActivity.SAVE_SETTINGS, "").toString()
        val device = setDeivce.getString(SettingsActivity.SAVE_SETTINGS, "").toString()

        if (bodyURL.isEmpty()) {
            Toast.makeText(this, "ip и порт не настроены", Toast.LENGTH_SHORT).show()
        }

        val dataSourceCard = DataSourceCard()
        val dataSourceCatalogPackage = DataSourceCatalogPackage()
        val konturController = KonturController()
        var url = ""
        var urlPassage = ""
        var messageInfoCard =
            "<?xml version=\"1.0\" encoding=\"windows1251\" ?>" +
                    "<spd-xml-api>" +
                    "<request version=\"1.0\" ruid=\"739F9F2B-AEDD-4D94-93FF-EB59A9A1FCF5\">" +
                    "<client identifier=\"\">" +
                    "<identifiers />" +
                    "</client>" +
                    "</request>" +
                    "</spd-xml-api>"
        val messageBlockDevice = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?> " +
                "<script session=\"85D323F3-8EBD-48E6-A085-4E652468B8D6\"> " +
                "<command name=\"cLockDevice\" device=\"$device\" guid=\"95D454F3-8EBD-50E6-A085-4E644468B8D6\"> " +
                "<param name=\"cpLocker\">Карта Тройка</param> " +
                "<param name=\"cpDuration\">30000</param> " +
                "<param name=\"cpSession\">85D323F3-8EBD-48E6-A085-4E652468B8D6</param> " +
                "</command> " +
                "</script>"
        var messagePassageCard = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?> " +
                "<script session=\"85D323F3-8EBD-48E6-A085-4E652468B8D6\"> " +
                "<command name=\"cRequest\" device=\"$device\" guid=\"44871464-8EBD-56E6-A085-4E654768B8D6\"> " +
                "<param name=\"cpCard\"></param> " +
                "<param name=\"cpCardType\">1</param> " +
                "<param name=\"cpDirection\">1</param> " +
                "<param name=\"cpText\">Запрос по карте</param> " +
                "</command> " +
                "</script>"
        val messageUnBlockDevice = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?> " +
                "<script session=\"85D323F3-8EBD-48E6-A085-4E652468B8D6\"> " +
                "<command name=\"cUnlockDevice\" device=\"$device\" guid=\"98545167-8EBD-6578-A085-4E633368B8D6\"> " +
                "<param name=\"cpLocker\">Карта Тройка</param> " +
                "<param name=\"cpDuration\">30000</param> " +
                "<param name=\"cpSession\">85D323F3-8EBD-48E6-A085-4E652468B8D6</param> " +
                "</command> " +
                "</script>"
        val answerDevice = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?> " +
                "<script session=\"85D323F3-8EBD-48E6-A085-4E652468B8D6\"> " +
                "<wait delay=\"20000\" device=\"$device\"/> " +
                "</script>"

        infoManulaCard.setOnClickListener {
            recreate()
            messageInfoCard = messageInfoCard.replace(
                "<client identifier=\"\">",
                "<client identifier=\"${editNumberCard.text}\">"
            )
            url = "$bodyURL/spd-xml-api"
            urlPassage = "$bodyURL/monitor?script=True"
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

        passageManualCard.setOnClickListener {
            recreate()
            messageInfoCard = messageInfoCard.replace(
                "<client identifier=\"\">",
                "<client identifier=\"${editNumberCard.text}\">"
            )
            messagePassageCard = messagePassageCard.replace(
                "<param name=\"cpCard\"></param>",
                "<param name=\"cpCard\">${editNumberCard.text}</param>"
            )
            url = "$bodyURL/spd-xml-api"
            urlPassage = "$bodyURL/monitor?script=True"
            updatePassageCard(
                konturController,
                dataSourceCatalogPackage,
                urlPassage,
                url,
                messageBlockDevice,
                messagePassageCard,
                answerDevice,
                messageUnBlockDevice,
                messageInfoCard
            )
            bundle.putString("deviceName", dataSourceCatalogPackage.getPassageCard().deviceName)
            bundle.putString("requestPassage", editNumberCard.text.toString())
            bundle.putString("solution", dataSourceCatalogPackage.getPassageCard().solution)
            bundle.putString("capt", dataSourceCatalogPackage.getPassageCard().capt)
            bundle.putString(
                "numberOfPasses",
                dataSourceCatalogPackage.getPassageCard().numberOfPasses
            )
            bundle.putString("datePasses", dataSourceCatalogPackage.getPassageCard().datePasses)
            bundle.putString("passageBalance", dataSourceCatalogPackage.getPassageCard().passageBalance)
            passageCardFragment.arguments = bundle
            openFragment(passageCardFragment)
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
            .replace(R.id.place_manual_fragments, fragment)
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

    private fun updatePassageCard(
        konturController: KonturController,
        dataSourceCatalogPackage: DataSourceCatalogPackage,
        urlPassage: String,
        url: String,
        messageBlockDevice: String,
        messagePassageCard: String,
        answerDevice: String,
        messageUnBlockDevice: String,
        messageInfoCard: String

    ) {
        runBlocking {
            launch(newSingleThreadContext("MyOwnThread")) {
                try {
                    konturController.requestPOST(urlPassage, messageBlockDevice)
                    var msg = konturController.requestPOST(urlPassage, messagePassageCard)
                    dataSourceCatalogPackage.setMessagePassageCard(msg)
                    msg = msg.substringAfter("<Message>")
                    msg = msg.substringBefore("</Message>")
                    msg = msg.replace("rPrior", "rFinal")
                    msg = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?> " +
                            "<script>" +
                            "<Message>$msg</Message>" +
                            "</script>"
                    konturController.requestPOST(urlPassage, msg)
                    dataSourceCatalogPackage.setAnswerDevice(
                        konturController.requestPOST(
                            urlPassage,
                            answerDevice
                        )
                    )
                    konturController.requestPOST(urlPassage, messageUnBlockDevice)
                    dataSourceCatalogPackage.setInfoCard(
                        konturController.requestPOST(
                            url,
                            messageInfoCard
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}