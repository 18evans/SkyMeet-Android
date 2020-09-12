package evans18.skymeet.util

import com.google.gson.Gson

//inline fun <reified T> Any.TAG(): String = T::class.java.simpleName
val Any.TAG: String
    get() = javaClass.simpleName

const val REQUEST_CODE_LOCATION = 0

val gson by lazy {
    Gson()
}