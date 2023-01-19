package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val intent = intent


        val name = intent.getStringExtra("fileName")
        val stat = intent.getStringExtra("status")

        file_name.text = name
        status.text = stat

        ok_button.setOnClickListener {
            finish()
        }


    }

}
