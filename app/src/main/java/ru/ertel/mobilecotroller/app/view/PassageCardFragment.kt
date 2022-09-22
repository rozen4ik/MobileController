package ru.ertel.mobilecotroller.app.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.ertel.mobilecotroller.app.R

class PassageCardFragment : Fragment() {

    private var deviceName = ""
    private var requestPassage = ""
    private var solution = ""
    private var capt = ""
    private var numberOfPasses = ""
    private var datePasses = ""
    private var passageBalance = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        deviceName = arguments?.getString("deviceName").toString()
        requestPassage = arguments?.getString("requestPassage").toString()
        requestPassage = requestPassage.replace("/", "")
        solution = arguments?.getString("solution").toString()
        capt = arguments?.getString("capt").toString()
        numberOfPasses = arguments?.getString("numberOfPasses").toString()
        datePasses = arguments?.getString("datePasses").toString()
        passageBalance = arguments?.getString("passageBalance").toString()
        return inflater.inflate(R.layout.fragment_passage_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceNameText: TextView = view.findViewById(R.id.deviceName)
        val requestPassageText: TextView = view.findViewById(R.id.requestPassage)
        val solutionText: TextView = view.findViewById(R.id.solution)
        val captText: TextView = view.findViewById(R.id.capt)
        val numberOfPassesText: TextView = view.findViewById(R.id.numberOfPasses)
        val datePassesText: TextView = view.findViewById(R.id.datePasses)
        val passageBalanceText: TextView = view.findViewById(R.id.passageBalance)
        deviceNameText.text = deviceName
        requestPassageText.text = "Запрос на проход $requestPassage"
        solutionText.text = solution
        if ((solutionText.text != "Проход разрешен") && (solutionText.text != "Карта сотрудника")) {
            solutionText.setTextColor(Color.RED)
        }
        captText.text = capt
        numberOfPassesText.text = numberOfPasses
        datePassesText.text = datePasses
        passageBalanceText.text = passageBalance
    }

    companion object {

        @JvmStatic
        fun newInstance() = PassageCardFragment()
    }
}