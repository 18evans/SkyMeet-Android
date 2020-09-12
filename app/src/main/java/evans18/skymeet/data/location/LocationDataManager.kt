package evans18.skymeet.data.location

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import evans18.skymeet.ui.Resource
import evans18.skymeet.util.TAG
import javax.inject.Inject


class LocationDataManager @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {

    val cachedLocation: MediatorLiveData<Resource<Location>> = MediatorLiveData()


    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val builderRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)

    /**
     * @return - Purpose of return is so that user may add actions to the response of the request.
     * Such as for example on onFailureListener can prompt user to enable device location (GPS).
     */
    fun requestFrequentLocationUpdates(): Task<LocationSettingsResponse> {
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(context)

        cachedLocation.value = Resource.Loading()

        return settingsClient.checkLocationSettings(builderRequest.build())
            .addOnSuccessListener {
                //if Location enabled, start location retrieval
                client.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {

                            val locationList = locationResult.locations
                            if (locationList.size > 0) {
                                //The last location in the list is the newest
                                val lastLocation = locationList[locationList.size - 1]
                                Log.i(
                                    TAG,
                                    "Location: " + lastLocation.latitude + " " + lastLocation.longitude
                                )

                                cachedLocation.value = Resource.Success(lastLocation)
                            }
                        }
                    },
                    Looper.myLooper()
                )
            }.addOnFailureListener {
                cachedLocation.value = Resource.Error()
            }

    }


}
