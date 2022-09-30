package ru.ertel.mobilecontroller.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.ertel.mobilecontroller.app.R
import java.util.*

class StartActivity : AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var demoFragment: DemoFragment
    private lateinit var dateNow: Calendar
    private val dateDayOfYearEnd: Int = 335
    private val dateYearEnd: Int = 2022

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        bundle = Bundle()
        demoFragment = DemoFragment()

//      Месяц считается от 0 до 11, так как хранится в виде массива
        dateNow = Calendar.getInstance()
        val dateDayOfYearNow = dateNow.get(Calendar.DAY_OF_YEAR)
        val dateYearNow = dateNow.get(Calendar.YEAR)

        if ((dateDayOfYearNow < dateDayOfYearEnd) && (dateYearNow == dateYearEnd)) {
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            bundle.putString("demo", "Пробный период приложения закончился!")
            demoFragment.arguments = bundle
            openFragment(demoFragment)
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeDateFragments, fragment)
            .commit()
    }
}