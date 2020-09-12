package evans18.skymeet.di.module.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import evans18.skymeet.di.ViewModelKey
import evans18.skymeet.ui.activity.DrawerViewModel
import evans18.skymeet.ui.fragment.map.MyMapViewModel

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyMapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MyMapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DrawerViewModel::class)
    abstract fun bindDrawerViewModel(drawerViewModel: DrawerViewModel): ViewModel

}