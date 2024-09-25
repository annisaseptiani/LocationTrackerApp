package com.example.presentation.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.domain.usecase.DeleteAllUseCase
import com.example.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteDataReceiver  : BroadcastReceiver() {

    @Inject
    lateinit var deleteAllUseCase: DeleteAllUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("DataDeletionReceiver", "Deleting data and showing notification at 10 PM")

        GlobalScope.launch(Dispatchers.IO) {
            deleteData(context)
        }
    }

    private suspend fun deleteData(context: Context) {
        try {
            deleteAllUseCase.invoke()
            Log.d("DataDeletionReceiver", "Data deleted successfully")
            withContext(Dispatchers.Main) {
                showNotification(context)
            }
        } catch (e: Exception) {
            Log.e("DataDeletionReceiver", "Error deleting data: ${e.message}")
        }
    }

    private fun showNotification(context: Context) {
        val notificationText = "Menghapus Data Tracking Hari Ini"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "data_deletion_channel",
                "Data Deletion Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "data_deletion_channel")
            .setContentTitle("Data Deletion at 10 PM")
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(2, notification)
    }
}