package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private var buttonPosition: Int = 0
    private var selectedPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(
            getString(R.string.loadapp_channel_id), getString(R.string.loadapp_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radio_group.setOnCheckedChangeListener { rGroup, checkedId ->
            val radioB: View = rGroup.findViewById(checkedId)
            buttonPosition = rGroup.indexOfChild(radioB)
        }

        custom_button.setOnClickListener {
            if (radio_group.checkedRadioButtonId == -1) {
                radioChecker()
            } else {
                custom_button.buttonClicked()
                selectedPosition = buttonPosition
                when (buttonPosition) {
                    0 -> download(GLIDE_URL)
                    1 -> download(LOADAPP_URL)
                    2 -> download(RETROFIT_URL)
                }
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            val c = downloadManager.query(query)

            if(c.moveToFirst())
            Log.i("MainActivity","Queue:"+c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)))


            if (downloadID == id) {
                custom_button.completeLoading()
                when (selectedPosition) {
                    0 -> sendNotification(getString(R.string.glide_title), "Success")
                    1 -> sendNotification(getString(R.string.loadapp_title), "Success")
                    2 -> sendNotification(getString(R.string.retrofit_title), "Success")
                }
            }
        }
    }

    private fun download(URL: String) {
        val request = DownloadManager.Request(Uri.parse(URL)).setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description)).setRequiresCharging(false)
            .setAllowedOverMetered(true).setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val LOADAPP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    }

    private fun radioChecker() {
        Toast.makeText(this, R.string.noSelectionToast, Toast.LENGTH_SHORT).show()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = resources.getString(R.string.notification_description)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun sendNotification(fileName: String, status: String) {
        notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
        notificationManager.sendNotification(
            this.getText(R.string.notification_description).toString(), this, fileName, status
        )
    }
}
