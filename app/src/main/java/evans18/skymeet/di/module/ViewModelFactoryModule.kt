package evans18.skymeet.di.module

import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import evans18.skymeet.di.provider.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

}
