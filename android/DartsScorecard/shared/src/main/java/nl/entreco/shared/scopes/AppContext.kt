package nl.entreco.shared.scopes

import javax.inject.Qualifier


@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext(val value: String = "AppContext")