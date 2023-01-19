package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private const val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(
    messageBody: String, applicationContext: Context, fileName: String, status: String
) {

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra("fileName", fileName)
    detailIntent.putExtra("status", status)
    val detailPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext, applicationContext.getString(R.string.loadapp_channel_id)
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp).setContentTitle(
        applicationContext.getString(R.string.notification_title)
    ).setContentText(messageBody).addAction(
        R.drawable.ic_assistant_black_24dp,
        applicationContext.getString(R.string.notification_action),
        detailPendingIntent
    )

    notify(NOTIFICATION_ID, builder.build())

}
