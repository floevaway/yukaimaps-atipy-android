package de.westnordost.streetcomplete.data.user

import org.json.JSONException
import android.content.SharedPreferences
import androidx.core.content.edit
import android.util.Log
import de.westnordost.streetcomplete.Prefs
import oauth.signpost.OAuthConsumer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationException
import net.openid.appauth.TokenResponse;

/** Manages saving and loading OAuthConsumer persistently  */
class OIDCStore(
    private val prefs: SharedPreferences,
) : KoinComponent {
    var state: AuthState?
        get() {
            val currentState = prefs.getString(Prefs.OIDC_STATE, null)
                    ?: return AuthState()
            try {
                return AuthState.jsonDeserialize(currentState)
            } catch (ex: JSONException) {
                return AuthState()
            }
        }
        set(state) {
            if (state != null) {
                val serialized = state.jsonSerializeString()
                prefs.edit {
                    putString(Prefs.OIDC_STATE, serialized)
                    putString(Prefs.OAUTH_ACCESS_TOKEN, state.getAccessToken())
                }
            } else {
                prefs.edit {
                    remove(Prefs.OIDC_STATE)
                    remove(Prefs.OAUTH_ACCESS_TOKEN)
                }
            }
        }
    var token: String? = null
        get() {
            val result = prefs.getString(Prefs.OAUTH_ACCESS_TOKEN, null)
            return result
        }

    val isAuthorized: Boolean
        get() = prefs.getString(Prefs.OAUTH_ACCESS_TOKEN, null) != null


    fun replace(resp: AuthorizationResponse?, ex: AuthorizationException?) {
        val currentState = state
        currentState!!.update(resp, ex)
        state = currentState
    }

    fun update(resp: TokenResponse?, ex: AuthorizationException?) {
        val currentState = state
        currentState!!.update(resp, ex)
        state = currentState
    }
}
