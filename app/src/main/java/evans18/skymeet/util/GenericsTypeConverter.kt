package evans18.skymeet.util

import com.google.gson.reflect.TypeToken

object GenericsTypeConverter {

    fun fromAnyObject(any: Any): String {
        return gson.toJson(any)
    }

    inline fun <reified T> toAnyObject(anyString: String): T {
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(anyString, type)
    }

}
