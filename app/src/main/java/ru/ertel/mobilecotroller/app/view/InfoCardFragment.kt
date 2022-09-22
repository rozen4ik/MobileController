package ru.ertel.mobilecotroller.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.ertel.mobilecotroller.app.R

class InfoCardFragment : Fragment() {

    private var condition = ""
    private var number = ""
    private var ruleOfUse = ""
    private var permittedRates = ""
    private var startAction = ""
    private var endAction = ""
    private var balance = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        condition = arguments?.getString("condition").toString()
        number = arguments?.getString("number").toString()
        ruleOfUse = arguments?.getString("ruleOfUse").toString()
        permittedRates = arguments?.getString("permittedRates").toString()
        startAction = arguments?.getString("startAction").toString()
        endAction = arguments?.getString("endAction").toString()
        balance = arguments?.getString("balance").toString()
        return inflater.inflate(R.layout.fragment_info_card, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameBar: TextView = view.findViewById(R.id.nameBar)
        val textCondition: TextView = view.findViewById(R.id.textCondition)
        val textNumber: TextView = view.findViewById(R.id.textNumber)
        val textRuleOfUse: TextView = view.findViewById(R.id.textRuleOfUse)
        val textPermittedRates: TextView = view.findViewById(R.id.textPermittedRates)
        val textStartAction: TextView = view.findViewById(R.id.textStartAction)
        val textEndAction: TextView = view.findViewById(R.id.textEndAction)
        val textBalance: TextView = view.findViewById(R.id.textBalance)
        nameBar.text = "Информация о индефикаторе --$number--"
        textCondition.text = condition
        textNumber.text = number
        textRuleOfUse.text = ruleOfUse
        textPermittedRates.text = permittedRates
        textStartAction.text = startAction
        textEndAction.text = endAction
        textBalance.text = balance
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoCardFragment()
    }
}