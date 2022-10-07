package ru.ertel.mobilecontroller.app.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import ru.ertel.mobilecontroller.app.R
import ru.ertel.mobilecontroller.app.controller.KonturController
import ru.ertel.mobilecontroller.app.data.DataSourceToken
import ru.ertel.mobilecontroller.app.view.StartActivity.Companion.SAVE_TOKEN
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ActivateFragment : Fragment() {

    private lateinit var editTextToken: EditText
    private lateinit var buttonToken: Button
    private lateinit var settings: SharedPreferences
    private lateinit var daySet: SharedPreferences
    private lateinit var urlToken: String
    private lateinit var dataSourceToken: DataSourceToken
    private lateinit var konturController: KonturController
    private var negativeAnswerFragment = NegativeAnswerFragment
    private lateinit var demoFragment: DemoFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataSourceToken = DataSourceToken()
        konturController = KonturController()
        return inflater.inflate(R.layout.fragment_activate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextToken = view.findViewById(R.id.editTextToken)
        buttonToken = view.findViewById(R.id.buttonToken)

        buttonToken.setOnClickListener {
            urlToken =
                "http://license.u1733524.isp.regruhosting.ru/api/tokens/${editTextToken.text}"
            infoToken(konturController, dataSourceToken, urlToken)

            if (dataSourceToken.getToken().nameToken == editTextToken.text.toString()) {
                settings =
                    requireActivity().getSharedPreferences("konturToken", AppCompatActivity.MODE_PRIVATE)
                daySet =
                    requireActivity().getSharedPreferences("endDate", AppCompatActivity.MODE_PRIVATE)

                val formDate = SimpleDateFormat("yyyy-MM-dd")
                val getEndDayOfYear = SimpleDateFormat("D")
                val currentDate = formDate.parse(dataSourceToken.getToken().endDate)
                val endDayOfYear = getEndDayOfYear.format(currentDate)
                val endYear = "${dataSourceToken.getToken().endDate[0]}" +
                        "${dataSourceToken.getToken().endDate[1]}" +
                        "${dataSourceToken.getToken().endDate[2]}" +
                        "${dataSourceToken.getToken().endDate[3]}"
                val dateNow = Calendar.getInstance()
                val dateDayOfYearNow = dateNow.get(Calendar.DAY_OF_YEAR)
                val dateYearNow = dateNow.get(Calendar.YEAR)

                if (dateYearNow < endYear.toInt()) {
                    val saveEndDateToken: SharedPreferences.Editor = daySet.edit()
                    saveEndDateToken.putString(SAVE_TOKEN, "$endDayOfYear/$endYear")
                    saveEndDateToken.commit()

                    val numberKontur = dataSourceToken.getToken().nameToken.substringAfterLast("*")
                    val saveKonturToken: SharedPreferences.Editor = settings.edit()
                    saveKonturToken.putString(SAVE_TOKEN, numberKontur)
                    saveKonturToken.commit()

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else if (dateYearNow == endYear.toInt()) {
                    if ((dateDayOfYearNow < endDayOfYear.toInt()) && (dateYearNow <= endYear.toInt())) {
                        val saveEndDateToken: SharedPreferences.Editor = daySet.edit()
                        saveEndDateToken.putString(SAVE_TOKEN, "$endDayOfYear/$endYear")
                        saveEndDateToken.commit()

                        val numberKontur = dataSourceToken.getToken().nameToken.substringAfterLast("*")
                        val saveKonturToken: SharedPreferences.Editor = settings.edit()
                        saveKonturToken.putString(SAVE_TOKEN, numberKontur)
                        saveKonturToken.commit()

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        demoFragment = DemoFragment()
                        openFragment(demoFragment)
                    }
                } else {
                    demoFragment = DemoFragment()
                    openFragment(demoFragment)
                }
            } else {
                openFragment(negativeAnswerFragment.newInstance(dataSourceToken.getAnswer()))
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.placeDateFragments, fragment)
            ?.commit()
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

    companion object {

        @JvmStatic
        fun newInstance() = ActivateFragment()
    }
}