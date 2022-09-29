package ru.ertel.mobilecontroller.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.ertel.mobilecontroller.app.R


class InfoCardFragment : Fragment() {

    private lateinit var idCard: String
    private lateinit var idPackage: String
    private lateinit var numberCard:String
    private lateinit var balance: String
    private lateinit var url: String
    private lateinit var packageArray: String
    private lateinit var packages: Array<String>
    private var detailPackageFragment = DetailPackageFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idCard = arguments?.getString("idCard").toString()
        url = arguments?.getString("url").toString()
        packageArray = arguments?.getString("packageArray").toString()
        numberCard = arguments?.getString("numberCard").toString()
        balance = arguments?.getString("balance").toString()
        if (balance != "Нет баланса") {
            balance = "${balance.substringBeforeLast(balance[balance.lastIndex].toString())} руб"
        }
        if (packageArray != "{Данные не=найдены}") {
            packageArray = packageArray.replace("=", ": ")
            packageArray = packageArray.replace("{", "").replace("}", "")
            packages = packageArray.split(", ").toTypedArray()
        }
        return inflater.inflate(R.layout.fragment_info_card, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listPackage: ListView = view.findViewById(R.id.packageList)
        val textNumberCard: TextView = view.findViewById(R.id.textNumberCard)
        val textBalance: TextView = view.findViewById(R.id.textBalance)

        textNumberCard.text = "Карта №$numberCard"
        textBalance.text = "Баланс карты: $balance."

        if (packageArray != "{Данные не=найдены}") {
            val adapter = activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_activated_1,
                    packages.toList()
                )
            }
            listPackage.adapter = adapter
            listPackage.setOnItemClickListener { _, itemClicked, _, _ ->
                idPackage = (itemClicked as TextView).text.toString().substringBefore(":")
                openFragment(detailPackageFragment.newInstance(idCard, idPackage, url))
            }
        } else {
            val noData: TextView = view.findViewById(R.id.textView3)
            noData.text = "Данные не найдены"
            listPackage.visibility = View.GONE
        }
    }


    private fun openFragment(fragment: Fragment) {
        val activityName = activity?.componentName.toString()
        if (activityName.contains("MainActivity")) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.place_fragments, fragment)
                ?.commit()
        } else if (activityName.contains("ManualActivity")) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.placeManualFragments, fragment)
                ?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoCardFragment()
    }
}