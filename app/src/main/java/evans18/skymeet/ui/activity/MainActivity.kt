package evans18.skymeet.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import evans18.skymeet.R
import evans18.skymeet.data.local.PreferencesHelper
import evans18.skymeet.data.remote.FirebaseTokenApiService
import evans18.skymeet.di.provider.ViewModelProviderFactory
import evans18.skymeet.ui.BaseActivity
import evans18.skymeet.ui.Resource
import evans18.skymeet.ui.adapter.DrawerMenuTrackedFlightsAdapter
import evans18.skymeet.util.TAG
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var drawerViewModel: DrawerViewModel

    @Inject
    internal lateinit var preferencesHelper: PreferencesHelper
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var firebaseTokenApiService: FirebaseTokenApiService

    private val compositeDisposable = CompositeDisposable()
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreate(savedInstanceState, R.layout.activity_main)

        preferencesHelper.getFirebaseToken()?.let { token ->
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

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_maps
            ), drawer_layout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        btn_clear.onClick {
            drawerViewModel.removeTrackedFlights()
        }

        recycler_view_flights.layoutManager = LinearLayoutManager(this)
        recycler_view_flights.adapter = DrawerMenuTrackedFlightsAdapter(
            emptyList(),
            getString(R.string.list_type_tracked_aircraft_header)
        )

        drawerViewModel =
            ViewModelProviders.of(this, providerFactory).get(DrawerViewModel::class.java)
        subscribeObservers()
        drawerViewModel.loadTrackedFlights()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun subscribeObservers() {
        drawerViewModel.cachedFlights
            .observe(this, Observer { resource ->
                when (resource) {
                    is Resource.Error -> {
                        resource.message?.also { msg ->
                            showError(msg)
                        }

                        showProgress(false)
                        text_list_empty.visibility = View.VISIBLE
                    }
                    is Resource.Loading -> {
                        showProgress(true)
                        text_list_empty.visibility = View.GONE
                        Log.d(TAG, "Get tracked aircrafts: LOADING")
                    }
                    is Resource.Success -> {
                        showProgress(false)
                        Log.d(TAG, "Get tracked aircrafts: SUCCESS")
                        val listFlight = resource.data!!

                        (recycler_view_flights.adapter as DrawerMenuTrackedFlightsAdapter)
                            .setItems(listFlight)

                        btn_clear.visibility = if (listFlight.isEmpty()) View.GONE else View.VISIBLE
                        text_list_empty.visibility = if (listFlight.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun showProgress(toShow: Boolean) {
//        super.showProgress(toShow)

//        progress_bar.visibility = if (toShow) View.VISIBLE else View.GONE
        if (toShow) {
            drawer_progress_bar.visibility = View.VISIBLE
            nav_body.alpha = 0.3f
        } else {
            drawer_progress_bar.visibility = View.GONE
            nav_body.alpha = 1f
        }
    }

}
