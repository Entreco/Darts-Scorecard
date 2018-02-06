package nl.entreco.dartsscorecard.di.viewmodel.api

import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class FeatureApiModuleTest {

    @Test
    fun provideFbDb() {
        assertNotNull(FeatureApiModule().provideFbDb(mock {  }, mock {  }))
    }

}