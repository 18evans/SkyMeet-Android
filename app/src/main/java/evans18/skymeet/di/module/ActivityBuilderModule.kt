package evans18.skymeet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import evans18.skymeet.di.module.main.MainFragmentBuilderModule
import evans18.skymeet.di.module.main.MainViewModelsModule
import evans18.skymeet.ui.activity.MainActivity
import evans18.skymeet.ui.activity.SplashActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [MainFragmentBuilderModule::class, MainViewModelsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

}
