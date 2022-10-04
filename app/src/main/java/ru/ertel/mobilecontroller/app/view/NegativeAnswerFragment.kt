package ru.ertel.mobilecontroller.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.ertel.mobilecontroller.app.R

class NegativeAnswerFragment : Fragment() {

    private lateinit var textNegativeAnswer: TextView
    private lateinit var btn: Button
    private lateinit var answer: String
    private lateinit var activateFragment: ActivateFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        answer = arguments?.getString("answerMessage").toString()
        return inflater.inflate(R.layout.fragment_negative_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textNegativeAnswer = view.findViewById(R.id.textNegativeAnswer)
        textNegativeAnswer.text = answer
        btn = view.findViewById(R.id.buttonAct)
        btn.setOnClickListener {
            activateFragment = ActivateFragment()
            openFragment(activateFragment)
        }
    }

    private fun openFragment(fragment: Fragment) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.placeDateFragments, fragment)
            ?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(answerMessage: String): NegativeAnswerFragment {
            val fragment = NegativeAnswerFragment()
            val args = Bundle()
            args.putString("answerMessage", answerMessage)
            fragment.arguments = args
            return fragment
        }
    }
}