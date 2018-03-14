package nl.entreco.domain.setup.settings

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.repository.SetupPrefRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class StorePreferredSettingsUsecaseTest{
    @Mock private lateinit var mockPrefsRepo : SetupPrefRepository
    private lateinit var subject : StorePreferredSettingsUsecase
    private val givenRequest = StoreSettingsRequest(11, 12, 13, 14, 15)

    @Test
    fun `it should store settings in PreferenceRepo`() {
        givenSubject()
        whenExecuting()
        thenRepoIsUsedToStoreRequest()
    }

    private fun whenExecuting() {
        subject.exec(givenRequest)
    }

    private fun givenSubject() {
        subject = StorePreferredSettingsUsecase(mockPrefsRepo)
    }

    private fun thenRepoIsUsedToStoreRequest() {
        verify(mockPrefsRepo).storePreferredSetup(givenRequest)
    }
}