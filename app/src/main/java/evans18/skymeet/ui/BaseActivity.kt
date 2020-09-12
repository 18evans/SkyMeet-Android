package evans18.skymeet.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import evans18.skymeet.R
import kotlinx.android.synthetic.main.app_bar_main.*

abstract class BaseActivity : DaggerAppCompatActivity() {

    fun onCreate(savedInstanceState: Bundle?, @LayoutRes layoutResId: Int? = null, hasSupportActionBar: Boolean = true) {
        super.onCreate(savedInstanceState)
        layoutResId?.apply { setContentView(this) }
        if (hasSupportActionBar) setSupportActionBar(toolbar)
    }

    fun showError(msg: String = getString(R.string.snackbar_message_error_unexpected)) {
        Snackbar.make(window.decorView, msg, Snackbar.LENGTH_SHORT).show()
    }

    abstract fun showProgress(toShow: Boolean)

}
