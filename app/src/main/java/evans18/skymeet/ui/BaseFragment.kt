package evans18.skymeet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import evans18.skymeet.R

abstract class BaseFragment : DaggerFragment() {


    private lateinit var mRootView: View

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        @LayoutRes layoutRes: Int
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layoutRes, container, false).also { mRootView = it }
    }

    fun showError(msg: String = getString(R.string.snackbar_message_error_unexpected)) {
        Snackbar.make(mRootView, msg, Snackbar.LENGTH_SHORT).show()
    }

    abstract fun showProgress(toShow: Boolean)

}
