package com.salmonboy.submissionakhir.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.salmonboy.core.domain.usecase.EventUseCase
import com.salmonboy.submissionakhir.R
import com.salmonboy.submissionakhir.ui.detail.EventDetailActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

class EventNotificationWorker(context: Context, workerParams: WorkerParameters, private val eventUseCase: EventUseCase) :
    Worker(context, workerParams) {

    companion object {
        private val TAG = EventNotificationWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "event_notification_channel"
    }

    override fun doWork(): Result {
        return try {
            val events = runBlocking {
                eventUseCase.getAllEvents().first()
            }
            val bigPicture = try {
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(events.data?.get(0)?.imageLogo)
                    .submit()
                    .get()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image for notification: ${e.message}")
                null
            }
            val formattedDate = events.data?.get(0)?.beginTime?.let { formatDate(it) }

            events.data?.get(0)?.id?.let {
                showNotification(
                    "Upcoming Event: ${events.data!![0].name}",
                    "Starts at: $formattedDate",
                    bigPicture,
                    it
                )
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching events", e)
            Result.failure()
        }
    }

    private fun formatDate(apiDate: String): String {
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val desiredDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val date = apiDateFormat.parse(apiDate)
            desiredDateFormat.format(date)
        } catch (e: Exception) {
            apiDate
        }
    }

    private fun showNotification(
        title: String,
        description: String,
        imageBitmap: Bitmap?,
        id: Int,
    ) {
        val detailIntent = Intent(applicationContext, EventDetailActivity::class.java).apply {
            putExtra("EVENT_ID", id.toString())
        }

        val stackBuilder = TaskStackBuilder.create(applicationContext).apply {
            addNextIntentWithParentStack(detailIntent)
            getPendingIntent(
                NOTIFICATION_ID,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val pendingIntent = stackBuilder.getPendingIntent(
            id, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(imageBitmap)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}