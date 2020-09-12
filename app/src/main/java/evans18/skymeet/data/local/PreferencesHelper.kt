package evans18.skymeet.data.local

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("SameParameterValue")
@Singleton
class PreferencesHelper @Inject constructor(
    context: Context
) {

    private val pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)


    // IN THIS SECTION ADD METHODS PER PREFERENCE
    fun isOnboardingSeen(): Boolean {
        return get(ONBOARDING_HAS_SEEN, false)
    }

    fun setIsOnboardingSeen(isSeen: Boolean) {
        save(ONBOARDING_HAS_SEEN, isSeen)
    }

    fun setFirebaseToken(token: String) {
        save(FIREBASE_TOKEN, token)
    }

    fun getFirebaseToken(): String? {
        return get(FIREBASE_TOKEN, null)
    }

    /**
     * Get a saved string from the central [SharedPreferences]
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved String
     */
    private fun get(key: String, defaultValue: String?): String? {
        return pref.getString(key, defaultValue)
    }

    /**
     * Get a saved boolean from the central [SharedPreferences]
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved bool
     */
    private fun get(key: String, defaultValue: Boolean): Boolean {
        return try {
            pref.getBoolean(key, defaultValue)
        } catch (e: ClassCastException) {
            val value = get(key, "")
            if (TextUtils.isEmpty(value)) {
                defaultValue
            } else value!!.toBoolean()
        }
    }

    /**
     * Save a boolean to the central [SharedPreferences]
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    private fun save(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    /**
     * Save a string to the central [.content.SharedPreferences]
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    private fun save(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    private companion object {
        const val PREF_FILE_NAME = "SkyMeet-Shared-Preferences"
        const val ONBOARDING_HAS_SEEN = "ONBOARDING_HAS_SEEN"
        const val FIREBASE_TOKEN = "firebase_token"
    }

}