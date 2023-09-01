/*
 * MIT License
 *
 * Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package hi.cosmonaut.hourly.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.activity.main.MainActivityIntent
import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.alarm.clock.NextAlarmClock
import hi.cosmonaut.hourly.store.local.AppPreferences
import hi.cosmonaut.hourly.tool.vibration.FiredHourVibration
//import hi.yo.hour.tool.vibration.FiredHourVibration
import java.text.SimpleDateFormat
import java.util.*

class FiredAlarmBroadcastReceiver : WakefulBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val vibration = FiredHourVibration(context)
        val prefs = AppPreferences(context)
        val toNextCalendarMapping = ToNextCalendarMapping()
        val mainActivityIntent = MainActivityIntent(context)
        val nextAlarm = NextAlarmClock(context)

        vibration.launch(
            longArrayOf(
                0L,
                125L,
                350L,
                125L
            ),
            -1
        )

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancelAll()

        val channelData = "channelData"
        var channelId = "channelId"
        val channelName = "channelName"

        if (Build.VERSION.SDK_INT >= 26) {

            val ex = notificationManagerCompat.getNotificationChannel(channelId)

            ex?.let {
                notificationManagerCompat.deleteNotificationChannel(it.id)
                channelId += "${System.currentTimeMillis()}"
            }

            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "Hour"

            notificationManagerCompat.createNotificationChannel(notificationChannel)
        }

        val title = context.getString(R.string.label_ring_ring)
        val format1 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val nextAlarmMillis = toNextCalendarMapping.perform(prefs)
        val info = nextAlarmMillis.timeInMillis
        val arg = format1.format(info)

        val content = context.getString(R.string.next_alarm_at, arg)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.status_bar_icon_default)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle(title)
            .setContentText(content)
            .setDefaults(android.app.Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setTimeoutAfter(60 * 1000L)
            .setOnlyAlertOnce(false)
            .setOngoing(false)
            .setLocalOnly(true)
            .addAction(
                R.drawable.empty,
                context.getString(R.string.label_got_it),
                PendingIntent.getActivity(
                    context,
                    1000,
                    mainActivityIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setCustomHeadsUpContentView(
                RemoteViews(
                    context.packageName,
                    R.layout.view_heads_up_drink_alarm
                ).apply {
                    this.setTextViewText(R.id.viewNotificationDrinkAlarmTvDataDescription, content)
                    this.setTextViewText(R.id.viewNotificationDrinkAlarmTvDataTitle, title)
                }
            )

        val result = builder.build()
        notificationManagerCompat.notify(111, result)
        nextAlarm.schedule(nextAlarmMillis.timeInMillis)

    }

    companion object {
        private val TAG: String = FiredAlarmBroadcastReceiver::class.java.simpleName
    }
}