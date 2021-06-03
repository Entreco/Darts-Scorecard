package nl.entreco.data.db.profile

import org.mockito.kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.profile.ProfileStat
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalProfileStatRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockProfileDao: ProfileDao
    private lateinit var subject : LocalProfileStatRepository
    private var actualProfileStat : ProfileStat? = null

    @Test
    fun fetchForPlayer() {
        givenSubject()
        whenFetchingForPlayer()
        assertNotNull(actualProfileStat)
    }

    private fun givenSubject() {
        whenever(mockDb.profileDao()).thenReturn(mockProfileDao)
        subject = LocalProfileStatRepository(mockDb, ProfileStatMapper())
    }

    private fun whenFetchingForPlayer() {
        actualProfileStat = subject.fetchForPlayer(12)
    }
}