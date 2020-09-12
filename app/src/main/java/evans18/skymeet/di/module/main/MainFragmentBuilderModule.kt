package evans18.skymeet.di.module.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import evans18.skymeet.ui.fragment.map.MyMapsFragment

@Module
abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMyMapsFragment(): MyMapsFragment

}
