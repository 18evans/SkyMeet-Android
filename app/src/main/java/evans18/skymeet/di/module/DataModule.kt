package evans18.skymeet.di.module

import android.app.Application
import androidx.room.Room
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.Module
import dagger.Provides
import evans18.skymeet.BuildConfig
import evans18.skymeet.data.database.FlightDao
import evans18.skymeet.data.database.SkyMeetDatabase
import evans18.skymeet.data.remote.FirebaseTokenApiService
import evans18.skymeet.data.remote.FlightsApiService
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    internal fun provideRoomDatabase(application: Application): SkyMeetDatabase {
        return Room.databaseBuilder(
            application,
            SkyMeetDatabase::class.java, "skymeet-database"
        )
            .addMigrations(SkyMeetDatabase.MIGRATION_1_2, SkyMeetDatabase.MIGRATION_2_3, SkyMeetDatabase.MIGRATION_3_4)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideFlightDao(skyMeetDatabase: SkyMeetDatabase): FlightDao {
        return skyMeetDatabase.flightDao()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
//            .baseUrl(BuildConfig.API_URL_FLIGHT_STATS) //flightStats.com
            .baseUrl(BuildConfig.PERSONAL_API_FLIGHTS) //localhost
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            /* .client( //flightStats.com Interceptor for API ID & Key
                 OkHttpClient.Builder()
                     .addInterceptor {
                         val original = it.request()

                         val newHttpUrl: HttpUrl = original.url()
                         val requestBuilder = original.newBuilder()


                         with(original.url().toString()) {
                             when {
                                 contains("rest/v2") -> requestBuilder.url(
                                     newHttpUrl.newBuilder()
                                         .addQueryParameter(
                                             "appId",
                                             BuildConfig.FLIGHT_STATS_APP_ID
                                         )
                                         .addQueryParameter(
                                             "appKey",
                                             BuildConfig.FLIGHT_STATS_APP_KEY
                                         )
                                         .build()
                                 )
                                 contains("rest/v1") -> requestBuilder
                                     .addHeader("appId", BuildConfig.FLIGHT_STATS_APP_ID)
                                     .addHeader("appKey", BuildConfig.FLIGHT_STATS_APP_KEY)
                                 else -> throw IllegalArgumentException("Invalid API version.")
                             }
                         }

                         it.proceed(requestBuilder.build())
                     }
                     .build()
             )*/
            .build()
    }

    @Singleton
    @Provides
    internal fun provideFlightsNearLocationService(retrofit: Retrofit): FlightsApiService {
        return retrofit.create(FlightsApiService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideFirebaseTokenApiService(retrofit: Retrofit): FirebaseTokenApiService {
        return retrofit.create(FirebaseTokenApiService::class.java)
    }

    @Singleton
    @Provides
    internal fun providePusher(): Pusher {
        return Pusher(
            BuildConfig.PUSHER_APP_KEY,
            PusherOptions().apply {
                setCluster(BuildConfig.PUSHER_CLUSTER_SERVER)
            }
        )
    }

}
