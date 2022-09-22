package ru.ertel.mobilecotroller.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import ru.ertel.mobilecotroller.app.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        var count = 0
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setOnClickListener {
            count++
            if (count == 5) {
                count = 0
                val intent = Intent(this@AboutActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, 2, 2, "Ручной ввод")
        menu?.add(Menu.NONE, 3, 2, "Автоматический")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            2 -> {
                val intent = Intent(this@AboutActivity, ManualActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            3 -> {
                val intent = Intent(this@AboutActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }
}