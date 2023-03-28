package ru.ertel.mobilecontroller.app.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import ru.ertel.mobilecontroller.app.R
import ru.ertel.mobilecontroller.app.controller.KonturController
import ru.ertel.mobilecontroller.app.data.DataSourceToken
import java.text.SimpleDateFormat

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val SAVE_SETTINGS = "save_settings"
    }

    private lateinit var buttonSaveSettings: Button
    private lateinit var buttonSaveLicense: Button
    private lateinit var showURL: TextView
    private lateinit var textDateLicense: TextView
    private lateinit var editURLBody: EditText
    private lateinit var editURLPort: EditText
    private lateinit var editLicense: EditText
    private lateinit var dataSourceToken: DataSourceToken
    private lateinit var konturController: KonturController

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setContentView(R.layout.activity_settings)
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings)
        buttonSaveLicense = findViewById(R.id.saveLicense)
        showURL = findViewById(R.id.showURL)
        textDateLicense = findViewById(R.id.textDateLicense)

        dataSourceToken = DataSourceToken()
        konturController = KonturController()


        val settingsToken: SharedPreferences = getSharedPreferences("konturToken", MODE_PRIVATE)
        val endDate: SharedPreferences = getSharedPreferences("date", MODE_PRIVATE)
        val settingsDate: SharedPreferences = getSharedPreferences("endDate", MODE_PRIVATE)

        val bodyDate = endDate.getString(SAVE_SETTINGS, "no").toString()
        textDateLicense.text = "Лицензия действует до $bodyDate\nОбновить лицензию:"

        val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
        showURL.text = settings.getString(SAVE_SETTINGS, "").toString()

        buttonSaveLicense.setOnClickListener {
            editLicense = findViewById(R.id.editLicense)

            val urlToken =
                "http://license.u1733524.isp.regruhosting.ru/api/tokens/${editLicense.text}"

            infoToken(konturController, dataSourceToken, urlToken)

            if (dataSourceToken.getToken().nameToken == editLicense.text.toString()) {

                val formDate = SimpleDateFormat("yyyy-MM-dd")
                val getEndDayOfYear = SimpleDateFormat("D")
                val currentDate = formDate.parse(dataSourceToken.getToken().endDate)
                val endDayOfYear = getEndDayOfYear.format(currentDate)
                val endYear = "${dataSourceToken.getToken().endDate[0]}" +
                        "${dataSourceToken.getToken().endDate[1]}" +
                        "${dataSourceToken.getToken().endDate[2]}" +
                        "${dataSourceToken.getToken().endDate[3]}"

                val saveEndDateToken: SharedPreferences.Editor = settingsDate.edit()
                saveEndDateToken.putString(SAVE_SETTINGS, "$endDayOfYear/$endYear")
                saveEndDateToken.commit()

                val numberKontur = dataSourceToken.getToken().nameToken.substringAfterLast("*")
                val saveKonturToken: SharedPreferences.Editor = settingsToken.edit()
                saveKonturToken.putString(SAVE_SETTINGS, numberKontur)
                saveKonturToken.commit()

                val saveEndDate: SharedPreferences.Editor = endDate.edit()
                saveEndDate.putString(SAVE_SETTINGS, dataSourceToken.getToken().endDate)
                saveEndDate.commit()

                val upDate: SharedPreferences = getSharedPreferences("date", MODE_PRIVATE)
                val upBodyDate = upDate.getString(SAVE_SETTINGS, "no").toString()
                textDateLicense.text = "Лицензия действует до $upBodyDate\nОбновить лицензию:"
            } else {
                Toast.makeText(this, "Введен некорректный ключ", Toast.LENGTH_LONG).show()
            }
        }

        buttonSaveSettings.setOnClickListener {
            editURLBody = findViewById(R.id.editURLBody)
            editURLPort = findViewById(R.id.editURLPort)
            var url = ""

            if (editURLBody.text.isNotEmpty() && editURLPort.text.isNotEmpty()) {
                url = "http://${editURLBody.text.toString()}:${editURLPort.text.toString()}"
                val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
                val saveUrl: SharedPreferences.Editor = settings.edit()
                saveUrl.putString(SAVE_SETTINGS, url)
                saveUrl.commit()
            } else if (editURLBody.text.isNotEmpty()) {
                val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
                val saveUrl: SharedPreferences.Editor = settings.edit()
                val body = editURLBody.text.toString()
                val port = settings.getString(SAVE_SETTINGS, "").toString().substringAfterLast(":")
                url = "http://$body:$port"
                saveUrl.putString(SAVE_SETTINGS, url)
                saveUrl.commit()
            } else if (editURLPort.text.isNotEmpty()) {
                val settings: SharedPreferences = getSharedPreferences("URL", MODE_PRIVATE)
                val saveUrl: SharedPreferences.Editor = settings.edit()
                val body = settings.getString(SAVE_SETTINGS, "").toString().substringAfter("//").substringBeforeLast(":")
                val port = editURLPort.text.toString()
                url = "http://$body:$port"
                saveUrl.putString(SAVE_SETTINGS, url)
                saveUrl.commit()
            }

            showURL.text = url
            Toast.makeText(this, "$url сохранён", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 2, 2, "Ручной ввод")
        menu?.add(Menu.NONE, 3, 2, "Автоматический")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            2 -> {
                val intent = Intent(this@SettingsActivity, ManualActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            3 -> {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.action_about -> {
                val intent = Intent(this@SettingsActivity, AboutActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private fun infoToken(
        konturController: KonturController,
        dataSourceToken: DataSourceToken,
        url: String
    ) {
        runBlocking {
            launch(newSingleThreadContext("MyOwnThread")) {
                try {
                    when (val message = konturController.requestGetToken(url)) {
                        "\"Превышено количество активаций\"" -> {
                            dataSourceToken.setAnswer(message)
                        }
                        "\"Отказано в активации, ключ не действительный\"" -> {
                            dataSourceToken.setAnswer(message)
                        }
                        else -> {
                            dataSourceToken.setAnswer("Одобрено")
                            dataSourceToken.setToken(message)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}