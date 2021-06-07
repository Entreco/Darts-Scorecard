package nl.entreco.libcore.scopes

import javax.inject.Qualifier


@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext(val value: String = "AppContext")