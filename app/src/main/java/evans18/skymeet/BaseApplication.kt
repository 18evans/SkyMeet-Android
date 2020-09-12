package evans18.skymeet

import android.app.Service
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import evans18.skymeet.di.component.DaggerAppComponent
import javax.inject.Inject


/**
 * The Base Application which will live entire lifecycle of an application.
 *
 * I want to make our AppComponent live in the entire lifecycler of an
 * application so we can instantiate it on applicationInjection
 * overrided method which is comming from [DaggerApplication]
 */
class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerAppComponent.builder().application(this).build()
    }

    fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }
}