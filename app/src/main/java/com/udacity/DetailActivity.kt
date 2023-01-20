package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        cancelAll()

        val intent = intent

        val name = intent.getStringExtra("fileName")
        val stat = intent.getStringExtra("status")

        if (stat == "Success")
            status.setTextColor(resources.getColor(R.color.success))
        else
            status.setTextColor(resources.getColor(R.color.fail))

        file_name.text = name
        status.text = stat

        ok_button.setOnClickListener {
            finish()
        }
    }

    private fun cancelAll() {
        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()
    }

}
