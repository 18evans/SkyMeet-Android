package evans18.skymeet.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import evans18.skymeet.data.local.PreferencesHelper
import evans18.skymeet.ui.BaseActivity
import evans18.skymeet.ui.activity.onboarding.OnboardingActivity
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.noAnimation
import javax.inject.Inject

/**
 * Launcher activity.
 * Activity is kept on back stack during the intro [OnboardingActivity], however, on return this activity must get finished.
 */
class SplashActivity : BaseActivity() {

    private companion object {
        const val REQUEST_CODE_INTRO = 0
    }

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreate(savedInstanceState, hasSupportActionBar = false)

        if (!preferencesHelper.isOnboardingSeen()) {
            startActivityForResult(
                intentFor<OnboardingActivity>().noAnimation(),
                REQUEST_CODE_INTRO
            )
        } else {
            startActivity(intentFor<MainActivity>())
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == Activity.RESULT_OK) { // Finished the intro
                Completable.fromAction {
                    preferencesHelper.setIsOnboardingSeen(true)
                }.subscribeOn(Schedulers.io()).subscribe()
                startActivity(intentFor<MainActivity>())
//            } else { // Cancelled the intro. You can then e.g. finish this activity too.
            }
            finish()
        }
    }

    override fun showProgress(toShow: Boolean) {}

}