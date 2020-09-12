package evans18.skymeet.data.remote

import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import evans18.skymeet.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used for maintaining a connection to a Pusher channel.
 * Through this channel received can be data from the SkyMeet back-end.
 */
@Singleton
class PusherManager @Inject constructor(
    private val pusher: Pusher
) {

    init {
        pusher.connect()
    }

    private val channelActiveFlights by lazy {
        pusher.subscribe(BuildConfig.PUSHER_CHANNEL_FLIGHTS)
    }

    //todo: see if possible to transform into LiveData?
    fun bindFlightsLocationEventListener(eventListener: SubscriptionEventListener) {
        this.channelActiveFlights.bind(BuildConfig.PUSHER_CHANNEL_FLIGHTS_EVENT_NEW_LOCATIONS, eventListener)
    }

}