package evans18.skymeet.ui.activity.onboarding

import android.Manifest
import android.os.Bundle
import androidx.annotation.Nullable
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import evans18.skymeet.R


class OnboardingActivity : IntroActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(
            SimpleSlide.Builder()
                .title(R.string.onboarding_welcome)
                .description(R.string.onboarding_welcome_description)
                .image(R.drawable.ic_onboarding_scan)
                .background(R.color.colorPrimary)
                .build()
        )

        addSlide(
            SimpleSlide.Builder()
                .title(R.string.onboarding_permission_location)
                .description(R.string.onboarding_permission_location_rationale)
                .image(R.drawable.location_on_map)
                .background(R.color.colorPrimary)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .build()
        )

        addSlide(
            SimpleSlide.Builder()
                .title(getString(R.string.onboarding_augmented_reality))
                .description(getString(R.string.onboarding_augmented_reality_description))
                .image(R.drawable.man_sky_vr)
                .background(R.color.colorAccent) //use same background color as status bar
                .build()
        )

    }

}