package evans18.skymeet.ui.fragment.map

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import evans18.skymeet.R
import evans18.skymeet.data.model.entities.Flight
import evans18.skymeet.di.provider.ViewModelProviderFactory
import evans18.skymeet.ui.BaseFragment
import evans18.skymeet.ui.Resource
import evans18.skymeet.util.REQUEST_CODE_LOCATION
import evans18.skymeet.util.TAG
import kotlinx.android.synthetic.main.layout_fragment_maps.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject


class MyMapsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mapsViewModel: MyMapViewModel
    private var hasLoadedLatestLocationOnce = false

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    //super-class overridable methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mapsViewModel = ViewModelProviders.of(this, providerFactory)
            .get(MyMapViewModel::class.java)

        subscribeObservers()

        return super.onCreateView(inflater, container, savedInstanceState, R.layout.layout_fragment_maps)
            .also {
                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
                    .apply {
                        getMapAsync(this@MyMapsFragment)
                    }
            }
    }


    override fun showProgress(toShow: Boolean) {
        progress_bar.visibility = if (toShow) View.VISIBLE else View.GONE
    }

    override fun onMapReady(map: GoogleMap) {
        mapsViewModel.apply {
            initMap(map)
            requestLocation().addOnFailureListener { ex ->
                if (ex is ResolvableApiException) { //if Location(GPS) is disabled
                    startIntentSenderForResult(
                        ex.resolution.intentSender,
                        REQUEST_CODE_LOCATION, null, 0, 0, 0, null
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                when (resultCode) {
                    Activity.RESULT_CANCELED -> {
                        Log.i(TAG, "Location/GPS DENIED.")
                        mapsViewModel.cachedLocation.value =
                            Resource.Error(getString(R.string.location_denied))
                    }
                    Activity.RESULT_OK -> {
                        Log.i(TAG, "Location/GPS ENABLED.")
                        mapsViewModel.requestLocation()
                    }
                }
            }
        }

    }

    //private methods
    private fun subscribeObservers() {
        subscribeToUserLocationUpdate() //user location
        subscribeToFlightLocationsUpdate() //active flights
        subscribeToFlightSelected() //react to load of specific flight
    }

    private fun subscribeToFlightSelected() {
        mapsViewModel.selectedFlight.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Error -> {
                    showProgress(false)
                    resource.message?.let { msg ->
                        showError(msg)
                    }
                }
                is Resource.Loading -> {
                    showProgress(true)
                }
                is Resource.Success -> {
                    showProgress(false)
                    resource.data!!.let { flight ->
                        //                        view?.run {//only once view is initialized
                        toast("Aircraft ${flight.aircraft.aircraftId} has been clicked.")
                        DialogManager().showSelectedActiveFlightDialog(flight)
//                        }
                    }
                }
            }
        })
    }

    /**
     * React to updates of User location.
     */
    private fun subscribeToUserLocationUpdate() {
        mapsViewModel.apply {
            cachedLocation.observe(this@MyMapsFragment, Observer { resource ->
                when (resource) {
                    is Resource.Error -> {
                        resource.message?.let { msg ->
                            showError(msg)
                        }
                    }
                    is Resource.Success -> {
                        map.apply {
                            uiSettings.isMyLocationButtonEnabled = true
                            isMyLocationEnabled = true
                        }

                        resource.data!!.let { location ->
                            if (!hasLoadedLatestLocationOnce) { //move camera to user location on first location load since fragment creation
                                moveCameraToLocation(LatLng(location.latitude, location.longitude))
                                hasLoadedLatestLocationOnce = true
                            }

                        }

                    }
                }
            })
        }
    }

    /**
     * React to updates of active flights.
     */
    private fun subscribeToFlightLocationsUpdate() {
        mapsViewModel.apply {
            cachedLatestFlightsPositions.observe(this@MyMapsFragment, Observer { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data!!.let { listFlightWithPositions ->
                            for (itemFlight in listFlightWithPositions) {
                                //place a marker at each position
                                setActiveFlightMarker(itemFlight.idAircraft, itemFlight.flightPosition.location.run {
                                    LatLng(latitude, longitude)
                                })
                            }

                        }
                    }
                }

            })
        }
    }

    inner class DialogManager {

        private var flagBackPressAgain = false

        private val onKeyListener = DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) { //if back key pressed && key action is up (instead of "down")
                if (flagBackPressAgain) {
                    //if back was pressed twice
                    dialog.dismiss()
                    return@OnKeyListener true
                }
                toast("Press again to cancel dialog.")
                flagBackPressAgain = true //msg was shown
                return@OnKeyListener false
            }
            return@OnKeyListener true
        }
        private val onDismissListener = DialogInterface.OnDismissListener {
            flagBackPressAgain = false //reset for each opening of the Dialog
        }

        //dialog for single discovered flight
        private val dialogSelectedActiveFlight by lazy {
            val onDialogActionClickListener: (DialogInterface, Int) -> Unit = { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_POSITIVE -> {
                        //track flight
                        mapsViewModel.trackSelectedFlight()
                    }
                }
            }

            AlertDialog.Builder(activity!!)
                .setCustomTitle(layoutInflater.inflate(R.layout.dialog_title_flight_discovered, null))
                .setNegativeButton("Cancel", onDialogActionClickListener)
                .setPositiveButton("Track", onDialogActionClickListener)
//                .setCancelable(false) //todo: uncomment if not working
                .create()
        }

        fun showSelectedActiveFlightDialog(
            flight: Flight,
            isCarefulCancel: Boolean = true
        ) { //todo: pass more flight args or pass just flight
            dialogSelectedActiveFlight
                .apply {
                    if (isCarefulCancel) {
                        setOnKeyListener(onKeyListener)
                        setOnDismissListener(onDismissListener)
                    }
                    setCanceledOnTouchOutside(!isCarefulCancel)
                    show()

                    //elements can be accessed only after "show()" //todo check
                    find<TextView>(R.id.title_text).text =
                        getString(
                            R.string.tracked_aircraft_text_template,
                            flight.operatedBy.name, flight.aircraft.tailSign
                        )
                }
        }
    }


}
