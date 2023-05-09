package de.westnordost.streetcomplete.data.user

import oauth.signpost.OAuthConsumer
import oauth.signpost.OAuthProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider
import de.westnordost.streetcomplete.BuildConfig
import de.westnordost.streetcomplete.osm.OsmOIDCConnection

private const val apiRoot = BuildConfig.API_ROOT

private const val BASE_OAUTH_URL = "$apiRoot/oauth/"
private const val CONSUMER_KEY = "NV4cEoqQ94Kuoowh8qGJvUJLnbts40WiNykyeC1T"
private const val CONSUMER_SECRET = "r68v1Bd7RewTixAp0dMdCwn3w5iQvmpk4HlJcH2y"
private const val CALLBACK_SCHEME = "streetcomplete"
private const val CALLBACK_HOST = "oauth"

val userModule = module {
    factory(named("OAuthCallbackScheme")) { CALLBACK_SCHEME }
    factory(named("OAuthCallbackHost")) { CALLBACK_HOST }
    factory<OAuthConsumer> { OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET) }
    factory<OAuthProvider> { OkHttpOAuthProvider(
        BASE_OAUTH_URL + "request_token",
        BASE_OAUTH_URL + "access_token",
        BASE_OAUTH_URL + "authorize"
    ) }
    factory { OAuthStore(get()) }
    factory { OIDCStore(get()) }

    single<UserDataSource> { get<UserDataController>() }
    single { UserDataController(get(), get()) }

    single<UserLoginStatusSource> { get<UserLoginStatusController>() }
    single { UserLoginStatusController(get(), get(), get()) }

    single { UserUpdater(get(), get(), get(), get(), get()) }
}
