package evans18.skymeet.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import evans18.skymeet.BaseApplication
import evans18.skymeet.data.FlightManager
import evans18.skymeet.data.local.PreferencesHelper
import evans18.skymeet.data.remote.PusherManager
import evans18.skymeet.di.module.*
import javax.inject.Singleton

/**
 * Component is a graph. We build a component. Component will provide injected instances by using modules.
 * Extends appcomponent with [AndroidInjector] to avoid old way of injection application
 *
 * <code>
 *     fun inject(application: BaseApplication)
 * </code>
 *
 * AppComponent is act as a server whereas, [BaseApplication] act as a client.
 * Dagger interaction is like client-server interaction
 *
 * Anotated with [Singleton] Scope to tell dagger to keep it in the memory while application exists
 * and destroy it when application destroy
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        LocationModule::class,
        ViewModelFactoryModule::class,
        DataModule::class,
        AppModule::class,
        ServiceBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    /**
     * DiscoveryFlightManager can be accessed anywhere in the application
     */
    fun flightManager(): FlightManager

    /**
     * Manager for {@link SharedPreferences}
     * */
    fun preferencesHelper(): PreferencesHelper

    fun pusherManager(): PusherManager //todo: try to persist this only within flightManager

    @Component.Builder
    interface Builder {

        /**
         * [BindsInstance] annotation is used for, if you want to bind particular object or instance
         * of an object through the time of component construction
         */
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}