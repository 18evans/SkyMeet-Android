package evans18.skymeet.di.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject
constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        var provider = creators[modelClass]
        if (provider == null) { // if the viewModel has not been created

            // loop through the Map elements
            for ((key, value) in creators) {

                // if key (Class) is equal or child of modelClass, allow assigning of this key's value (Provider).
                if (modelClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }

        // if this is not one of the allowed keys, throw exception
        if (provider == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }

        // return the Provider
        try {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}
