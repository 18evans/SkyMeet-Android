package evans18.skymeet.ui.fragment.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import evans18.skymeet.R
import evans18.skymeet.data.FlightManager
import evans18.skymeet.data.location.LocationDataManager
import evans18.skymeet.data.model.entities.Flight
import evans18.skymeet.ui.Resource
import evans18.skymeet.util.TAG
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MyMapViewModel @Inject constructor(
    private val locationDataManager: LocationDataManager,
    private val flightManager: FlightManager
) : ViewModel(), GoogleMap.OnMarkerClickListener {

    //live-data
    val selectedFlight = MediatorLiveData<Resource<Flight>>()
    val cachedLocation = this.locationDataManager.cachedLocation
    val cachedLatestFlightsPositions = this.flightManager.cachedLatestFlights

    //maps properties
    lateinit var map: GoogleMap //stored in ViewModel to persist state
    private val markerIcon by lazy {
        BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_aircraft)
    }
    private val listMarkers = mutableListOf<Marker>()

    fun manageSource(source: LiveData<Resource<Flight>>) {
        selectedFlight.addSource<Resource<Flight>>(source) { flightResource ->
            selectedFlight.value = flightResource
            selectedFlight.removeSource<Resource<Flight>>(source)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        selectedFlight.value = Resource.Loading()

        (marker.tag as Int).let { id ->
            //tag must be retrieved on main thread

            manageSource(LiveDataReactiveStreams.fromPublisher(
                Flowable.fromCallable {
                    flightManager.flightsApiService
                        .getFlightByAircraftId(id)
                        .blockingGet()
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { flight ->
                        return@map Resource.Success(flight) as Resource<Flight>
                    }
                    .onErrorReturn { throwable ->
                        val msg = "Error occurred while getting data for selected flight."
                        Log.e(TAG, msg, throwable)
                        return@onErrorReturn Resource.Error(msg)
                    }
            )
            )
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    fun requestLocation(): Task<LocationSettingsResponse> {
        return locationDataManager.requestFrequentLocationUpdates().addOnFailureListener {
            if (it !is ResolvableApiException) {
                val msg = "Error occurred while gathering user location."
                Log.e(TAG, msg, it)
            }
            //else handle ResolvableApiException in calling method
        }
    }

    fun initMap(map: GoogleMap) {
        this.map = map.apply {
            setMaxZoomPreference(14f)
            isIndoorEnabled = false

            uiSettings.isMapToolbarEnabled = false;
            uiSettings.isTiltGesturesEnabled = false
            setOnMarkerClickListener(this@MyMapViewModel)
        }
    }

    /**
     * Responsible for visualising active aircrafts on the maps as marker icons.
     * Checks if marker for passed flight id exists. If not, places marker at passed location.
     * Else moves marker to new location.
     */
    fun setActiveFlightMarker(idAircraft: Int, latLng: LatLng) {
        if (!::map.isInitialized) {
            return //if map not ready yet
        }

        listMarkers.firstOrNull { item ->
            item.tag == idAircraft //get marker for this aircraft id
        }?.apply { position = latLng } //update marker position
            ?: addActiveFlightMarker(idAircraft, latLng) //else if no such, create new marker at position
    }

    /**
     * Adds marker on the map, associating it with an id of an Aircraft.
     * Marker is placed at the passed location and added to the local marker collection.
     */
    private fun addActiveFlightMarker(idAircraft: Int, latLng: LatLng): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(markerIcon)
        ).apply {
            tag = idAircraft //give the id of the aircraft to the marker
        }.also {
            listMarkers.add(it)
        }
    }

    fun moveCameraToLocation(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8f)
        map.animateCamera(cameraUpdate)
    }

    fun trackSelectedFlight() {
        selectedFlight.value!!.data!!.let { flight ->
            //            flightManager.cachedPersistedFlights.value!!.run {
//                Resource.Success(data!!.run {
//                    listOf(*this.toTypedArray(), flight) //recreate the value with the previous elements
//                    // along with the newest tracked element appended to it
//                })
//            }
            //todo: try again to make more efficient like commented code above
            flightManager.apply {
                Single.fromCallable {
                    flightDao.insertAll(flight)
                }.subscribeOn(Schedulers.io())
                    .blockingGet()
                cachedPersistedFlights.value = Resource.Success(listOf(*cachedPersistedFlights.value!!.data!!.toTypedArray(), flight))
            }
        }
    }


}