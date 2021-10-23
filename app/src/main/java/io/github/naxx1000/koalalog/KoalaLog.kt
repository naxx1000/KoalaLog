package io.github.naxx1000.koalalog

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object KoalaLog {

    private const val channelId = "KOALA_DEBUG_LOG_ANDROID"
    private const val notificationId: Int = 4923754

    private var appPid: Int? = null
    private var hasInitialized: Boolean = false

    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Koala Debug Log"
            val descriptionText =
                "Shows contextual information about the app in runtime, and provides the means to see your application log on the phone"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            hasInitialized = true
        }

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.runningAppProcesses.forEach { info ->
            if (info.processName.equals(context.packageName, ignoreCase = true)) {
                appPid = info.pid
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun log(context: Context, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!hasInitialized) {
                println("In Android O and above, the KoalaLog.init() is required to be called before calling this function.")
                return
            }
        }

        val fullLogIntent = Intent(context, LogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "show full log"
            putExtra("full_log", true)
            putExtra("pid", appPid)
        }
        val fullLogPendingIntent =
            PendingIntent.getActivity(context, 0, fullLogIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val fragmentIntent = Intent(context, LogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "show log by tag"
            putExtra("tag", message)
            putExtra("pid", appPid)
        }
        val fragmentPendingIntent =
            PendingIntent.getActivity(context, 0, fragmentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val fatalIntent = Intent(context, LogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "show fatal log"
            putExtra("tag", "FATAL")
            putExtra("pid", appPid)
        }
        val fatalPendingIntent =
            PendingIntent.getActivity(context, 0, fatalIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_source_24)
            .setContentTitle("Koala Log")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_baseline_remove_red_eye_24,
                "Crash log",
                fatalPendingIntent
            )
            .addAction(
                R.drawable.ic_baseline_remove_red_eye_24,
                "Fragment log",
                fragmentPendingIntent
            )
            .addAction(
                R.drawable.ic_baseline_remove_red_eye_24,
                "Full log",
                fullLogPendingIntent
            )
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun cancel(context: Context) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}