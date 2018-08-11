package nl.entreco.dartsscorecard.di.service

import android.app.Service
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ServiceModuleTest{

    @Mock private lateinit var mockService: Service

    @Test
    fun `can be created`() {
        assertNotNull(ServiceModule(mockService))
    }
}