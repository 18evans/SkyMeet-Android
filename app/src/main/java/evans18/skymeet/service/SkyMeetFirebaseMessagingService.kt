package evans18.skymeet.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import evans18.skymeet.R
import evans18.skymeet.data.local.PreferencesHelper
import evans18.skymeet.data.remote.FirebaseTokenApiService
import evans18.skymeet.ui.activity.MainActivity
import evans18.skymeet.util.TAG
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class SkyMeetFirebaseMessagingService : FirebaseMessagingService() {

//    private lateinit var token: String

    private companion object {
        const val KEY_AIRCRAFT_TAIL_SIGN = "tail_sign"
        const val KEY_DISTANCE_FROM_USER = "distance"
    }

    @Inject
    lateinit var firebaseTokenApiService: FirebaseTokenApiService
    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: " + remoteMessage.from)
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.apply {
                Log.d(TAG, "Message data payload: $data")
                showNotification(
                    getString(
                        R.string.notification_body_tracked_flight_nearby,
                        data.getValue(KEY_AIRCRAFT_TAIL_SIGN),
                        data.getValue(KEY_DISTANCE_FROM_USER)
                    )
                )
            }
        }

    }

    //todo: see onDeletedMessages - might get called if app is not open in long while
//    https://firebase.google.com/docs/cloud-messaging/android/receive#override-ondeletedmessages

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
// manage this apps subscriptions on the server side, send the
// Instance ID token to your app server.


        preferencesHelper.setFirebaseToken(token)
        //push to server
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        compositeDisposable.add(
            firebaseTokenApiService.createToken(token)
                .subscribe(
                    {
                        Log.d(TAG, "Token submitted to Server.")
                    },
                    { throwable ->
                        Log.e(TAG, "Error while getting new token.", throwable)
                    }
                )
        )
    }

    private fun showNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //todo: check foreground?
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.notification_channel_flight_tracked_nearby)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .apply {
                setSmallIcon(R.drawable.ic_airplane)
                color = ContextCompat.getColor(this@SkyMeetFirebaseMessagingService, R.color.colorPrimary)
                setContentTitle(getString(R.string.notification_title_tracked_flight_nearby))
                setContentText(messageBody)
                setAutoCancel(true)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                setContentIntent(pendingIntent)
            }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SkyMeet Channel for notification of nearby tracked flights",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

//    private fun checkRegistrationToken() {
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener {
//                if (!it.isSuccessful) {
//                    Log.w(TAG, "getInstanceId failed", it.exception)
//                    return@addOnCompleteListener
//                }
//                // Get new Instance ID token
//                token = it.result!!.token
//                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//            }
//    }
}