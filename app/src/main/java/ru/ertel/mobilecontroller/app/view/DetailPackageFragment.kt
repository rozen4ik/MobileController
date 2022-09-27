package ru.ertel.mobilecontroller.app.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import ru.ertel.mobilecontroller.app.R
import ru.ertel.mobilecontroller.app.controller.KonturController

class DetailPackageFragment : Fragment() {

    private lateinit var idCard: String
    private lateinit var idPackage: String
    private lateinit var url: String
    private lateinit var buttonYes: Button
    private lateinit var buttonNo: Button
    private lateinit var konturController: KonturController
    private val startFragment: StartFragment = StartFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idCard = arguments?.getString("idCard").toString()
        idPackage = arguments?.getString("idPackage").toString()
        url = arguments?.getString("url").toString()
        konturController = KonturController()
        return inflater.inflate(R.layout.fragment_detail_package, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonYes = view.findViewById(R.id.buttonYes)
        buttonNo = view.findViewById(R.id.buttonNo)

        val messagePackage = "<?xml version=\"1.0\" encoding=\"windows1251\" ?>\n" +
                "<spd-xml-api>\n" +
                "    <request version=\"1.0\" ruid=\"739F9F2B-AEDD-4D94-93FF-EB59A9A1FCF5\">          \n" +
                "        <client identifier=\"$idCard\">                        \n" +
                "            <identifiers>                        \n" +
                "                <package id=\"$idPackage\" use=\"activate\" eid=\"6FB84DEE-B5D5-42A0-B8A9-3E9D63B07C3F\"/>\n" +
                "                <package id=\"$idPackage\" use=\"done\" eid=\"6FB84DEE-B5D5-42A0-B8A9-3E9D63B07C3F\"/>          \n" +
                "            </identifiers>\n" +
                "        </client>\n" +
                "    </request>\n" +
                "</spd-xml-api>"

        buttonYes.setOnClickListener {
            updateInfo(konturController, url, messagePackage)
            openFragment(startFragment)
        }

        buttonNo.setOnClickListener {
            openFragment(startFragment)
        }
    }

    private fun updateInfo(
        konturController: KonturController,
        url: String,
        messagePackage: String
    ) {
        runBlocking {
            launch(newSingleThreadContext("MyOwnThread")) {
                try {
                    konturController.requestPOST(url, messagePackage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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
        fun newInstance(card: String, packages: String, url: String): DetailPackageFragment {
            val fragment = DetailPackageFragment()
            val args = Bundle()
            args.putString("idCard", card)
            args.putString("idPackage", packages)
            args.putString("url", url)
            fragment.arguments = args
            return fragment
        }
    }
}