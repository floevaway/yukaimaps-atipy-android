package de.westnordost.streetcomplete.data.user

import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.TokenResponse;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.connectivity.ConnectionBuilder;
import net.openid.appauth.connectivity.DefaultConnectionBuilder;
import android.net.Uri
import android.util.Log
import de.westnordost.streetcomplete.BuildConfig
import de.westnordost.streetcomplete.screens.BaseActivity
import de.westnordost.streetcomplete.util.ConnectionBuilderForTesting
import java.util.concurrent.atomic.AtomicReference
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity


private const val oidcServer = BuildConfig.OIDC_SERVER
private const val clientId = BuildConfig.OAUTH_CLIENT_ID
private val insecure = BuildConfig.OIDC_INSECURE
private const val redirectUri = "org.yukaimaps://oidc"

private const val TAG = "OIDC"

class OIDCDanser {

    private var authService: AuthorizationService? = null
    private var config: AuthorizationServiceConfiguration? = null
    private val authRequest = AtomicReference<AuthorizationRequest?>()

    private fun onFetchCompleted(
        serviceConfig: AuthorizationServiceConfiguration?,
        ex: AuthorizationException?
    ) {
        if (ex != null) {
          Log.e(TAG, "failed to fetch configuration");
          return;
        }
        this.config = serviceConfig;
    }

    fun isConfigured(): Boolean {
        return config != null
    }

    fun getConnectionBuilder(): ConnectionBuilder {
        if (insecure) {
            // XXX only for testing
            return ConnectionBuilderForTesting.INSTANCE
        }
        return DefaultConnectionBuilder.INSTANCE
    }

    fun configure() {
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(oidcServer),
            { config: AuthorizationServiceConfiguration?, ex: AuthorizationException? ->
                onFetchCompleted(config, ex)
            },
            ConnectionBuilderForTesting.INSTANCE
        )
    }

    private fun createAuthRequest(loginHint: String?) {
        Log.i(TAG, "Creating auth request for login hint: $loginHint")
        val authRequestBuilder = AuthorizationRequest.Builder(
            config!!,
            clientId,
            ResponseTypeValues.CODE,
            Uri.parse(redirectUri),
        )
        .setScope("openid")
        authRequest.set(authRequestBuilder.build())
    }

    private fun createAuthorizationService(activity: FragmentActivity) {
        Log.i(TAG, "Creating authorization service")
        authService = AuthorizationService(
            activity,
            AppAuthConfiguration.Builder()
                .setConnectionBuilder(getConnectionBuilder())
                .setSkipIssuerHttpsCheck(insecure)
                .build()
        )
    }

    fun doAuth(activity: FragmentActivity) {
        createAuthorizationService(activity)
        createAuthRequest(null)
        val intentBuilder = authService!!.createCustomTabsIntentBuilder(authRequest.get()!!.toUri())
        val intent = authService!!.getAuthorizationRequestIntent(
            authRequest.get()!!,
            intentBuilder.build()
        )
        activity.startActivityForResult(intent, 100)
    }

    fun exchangeToken(
        resp: AuthorizationResponse,
        onResponse: (tokenresp: TokenResponse?, ex: AuthorizationException?) -> Unit
    ) {
        authService!!.performTokenRequest(
            resp.createTokenExchangeRequest(),
            {tokenresp: TokenResponse?, ex: AuthorizationException? ->
                onResponse.invoke(tokenresp, ex)
            },
        )
    }
}
