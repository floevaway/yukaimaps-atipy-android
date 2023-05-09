package de.westnordost.streetcomplete.screens.user.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import de.westnordost.streetcomplete.ApplicationConstants
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.databinding.FragmentOauthBinding
import de.westnordost.streetcomplete.screens.BackPressedListener
import de.westnordost.streetcomplete.screens.HasTitle
import de.westnordost.streetcomplete.util.ktx.toast
import de.westnordost.streetcomplete.util.ktx.viewLifecycleScope
import de.westnordost.streetcomplete.util.viewBinding
import de.westnordost.streetcomplete.BuildConfig
import de.westnordost.streetcomplete.data.user.OIDCDanser
import de.westnordost.streetcomplete.screens.user.UserActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oauth.signpost.exception.OAuthCommunicationException
import oauth.signpost.exception.OAuthExpectationFailedException
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.util.Locale
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/** Fragment that manages the OAuth 1 authentication process in a webview*/
class OIDCFragment : Fragment(R.layout.fragment_oauth), BackPressedListener, HasTitle {

    private val binding by viewBinding(FragmentOauthBinding::bind)

    public var danser: OIDCDanser? = null

    override val title: String get() = getString(R.string.user_login)


    /* --------------------------------------- Lifecycle --------------------------------------- */

    override fun onCreate(inState: Bundle?) {
        super.onCreate(inState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "YUKAIMAPS oidc fragment created")
        viewLifecycleScope.launch { continueAuthentication() }
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onBackPressed(): Boolean {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.stopLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "YUKAIMAPS Result called")
    }


    /* ------------------------------------------------------------------------------------------ */

    private suspend fun continueAuthentication() {
        val parentActivity = getActivity()!! as UserActivity
        danser?.doAuth(parentActivity)
    }

    /* ---------------------------------------------------------------------------------------- */

    companion object {

        const val TAG = "OAuthDialogFragment"
    }
}

