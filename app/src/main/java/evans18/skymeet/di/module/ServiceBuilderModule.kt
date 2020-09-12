package evans18.skymeet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import evans18.skymeet.service.SkyMeetFirebaseMessagingService

@Module
internal abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSkyMeetFirebaseMessagingService(): SkyMeetFirebaseMessagingService
}