package ru.ertel.mobilecontroller.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.ertel.mobilecontroller.app.R

class DemoFragment : Fragment() {

    private lateinit var textDemo: TextView
    private lateinit var message: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        message = arguments?.getString("demo").toString()
        return inflater.inflate(R.layout.fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textDemo = view.findViewById(R.id.textDemo)
        textDemo.text = message
    }

    companion object {
        @JvmStatic
        fun newInstance() = DemoFragment()
    }
}