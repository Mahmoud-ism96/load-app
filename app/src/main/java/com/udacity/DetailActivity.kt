package com.udacity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var okButton: Button
    private lateinit var fileName: TextView
    private lateinit var fileStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val intent = intent

        val name = intent.getStringExtra("fileName")
        val status = intent.getStringExtra("status")

        okButton = findViewById<Button>(R.id.ok_button)
        fileName = findViewById<TextView>(R.id.file_name)
        fileStatus = findViewById<TextView>(R.id.status)

        fileName.text = name
        fileStatus.text = status

        okButton.setOnClickListener {
            finish()
        }


    }

}
