package evans18.skymeet.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import evans18.skymeet.data.FlightManager
import evans18.skymeet.data.model.entities.Flight
import evans18.skymeet.ui.Resource
import evans18.skymeet.util.TAG
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val flightManager: FlightManager
) :
    ViewModel() {

    val cachedFlights get() = flightManager.cachedPersistedFlights

    private fun manageSource(source: LiveData<Resource<List<Flight>>>) {
        cachedFlights.value = Resource.Loading()
        cachedFlights.addSource<Resource<List<Flight>>>(
            source
        ) { flightResource ->
            cachedFlights.value = flightResource
            cachedFlights.removeSource<Resource<List<Flight>>>(source)
        }
    }

    fun loadTrackedFlights() {
        manageSource(LiveDataReactiveStreams.fromPublisher(
            Flowable.fromCallable {
                flightManager.flightDao.getAll()
            }.subscribeOn(io())
                .map {
                    Resource.Success(it) as Resource<List<Flight>>
                }.onErrorReturn {
                    val msg = "Error while loading saved aircrafts."
                    Log.e(TAG, msg, it)
                    Resource.Error(msg)
                }
        ))
    }

    fun removeTrackedFlights() {
        val sourceRemoval = LiveDataReactiveStreams.fromPublisher(
            Flowable.fromCallable {
                flightManager.flightDao.nukeTable()
            }.subscribeOn(io())
                .map {
                    Resource.Success(emptyList<Flight>()) as Resource<List<Flight>>
                }
        )

        manageSource(sourceRemoval)
    }

}
