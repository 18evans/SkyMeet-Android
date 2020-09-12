package evans18.skymeet.di.module

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {

    @Singleton
    @Provides
    internal fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }
}