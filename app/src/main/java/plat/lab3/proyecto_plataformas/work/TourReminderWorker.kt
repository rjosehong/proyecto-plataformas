package plat.lab3.proyecto_plataformas.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import plat.lab3.proyecto_plataformas.data.calendar.tourStartMillis
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import java.util.concurrent.TimeUnit
class TourReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Tour"
        val place = inputData.getString(KEY_PLACE).orEmpty()
        showNotification(applicationContext, title, place)
        return Result.success()
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_PLACE = "place"
        const val CHANNEL_ID = "tour_reminders"
    }
}

fun scheduleTourReminder(
    context: Context,
    tour: Tour,
    minutesBefore: Long = 60
) {
    val startMillis = tourStartMillis(tour)
    val now = System.currentTimeMillis()
    val triggerAt = startMillis - minutesBefore * 60_000L
    val delayMs = if (triggerAt > now) triggerAt - now else 15_000L

    val request = OneTimeWorkRequestBuilder<TourReminderWorker>()
        .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
        .setInputData(
            workDataOf(
                TourReminderWorker.KEY_TITLE to "Tour ${tour.tour_nombre}",
                TourReminderWorker.KEY_PLACE to tour.lugar
            )
        )
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "reminder_tour_${tour.tourId}",
        ExistingWorkPolicy.REPLACE,
        request
    )
}

private fun showNotification(context: Context, title: String, place: String) {
    // En Android 13 o superior hace falta el permiso de notificaciones
    if (Build.VERSION.SDK_INT >= 33 &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        != PackageManager.PERMISSION_GRANTED
    ) return

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(
        NotificationChannel(
            TourReminderWorker.CHANNEL_ID,
            "Recordatorios de tours",
            NotificationManager.IMPORTANCE_DEFAULT
        )
    )

    val notification = NotificationCompat.Builder(context, TourReminderWorker.CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Tu tour comienza pronto")
        .setContentText(if (place.isBlank()) title else "$title en $place")
        .setAutoCancel(true)
        .build()

    manager.notify(title.hashCode(), notification)
}
