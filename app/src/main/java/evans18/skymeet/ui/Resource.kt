package evans18.skymeet.ui

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    class Error<T>(message: String? = null) : Resource<T>(message = message)
}