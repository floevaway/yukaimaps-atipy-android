package de.westnordost.streetcomplete.data.user

import android.content.SharedPreferences
import androidx.core.content.edit
import android.util.Log
import de.westnordost.osmapi.OsmConnection
import de.westnordost.streetcomplete.osm.OsmOIDCConnection
import de.westnordost.streetcomplete.Prefs
import de.westnordost.streetcomplete.data.user.OIDCStore
import oauth.signpost.OAuthConsumer
import java.util.concurrent.CopyOnWriteArrayList
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.TokenResponse;
import net.openid.appauth.AuthorizationException

class UserLoginStatusController(
    private val oidcStore: OIDCStore,
    private val osmConnection: OsmOIDCConnection,
    private val prefs: SharedPreferences,
) : UserLoginStatusSource {

    private val listeners: MutableList<UserLoginStatusSource.Listener> = CopyOnWriteArrayList()

    override val isLoggedIn: Boolean get() = oidcStore.isAuthorized

    fun startLogin(resp: AuthorizationResponse?, ex: AuthorizationException?) {
        oidcStore.replace(resp, ex)
    }

    fun loggedIn(resp: TokenResponse?, ex: AuthorizationException?) {
        oidcStore.update(resp, ex)
        osmConnection.setAccessToken(oidcStore.token)
        prefs.edit { putBoolean(Prefs.OSM_LOGGED_IN_AFTER_OAUTH_FUCKUP, true) }
        listeners.forEach { it.onLoggedIn() }
    }

    fun logOut() {
        oidcStore.state = null
        prefs.edit { putBoolean(Prefs.OSM_LOGGED_IN_AFTER_OAUTH_FUCKUP, false) }
        listeners.forEach { it.onLoggedOut() }
    }

    override fun addListener(listener: UserLoginStatusSource.Listener) {
        listeners.add(listener)
    }
    override fun removeListener(listener: UserLoginStatusSource.Listener) {
        listeners.remove(listener)
    }
}
