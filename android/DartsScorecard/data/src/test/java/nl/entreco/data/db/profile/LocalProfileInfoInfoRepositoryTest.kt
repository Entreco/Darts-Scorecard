package nl.entreco.data.db.profile

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.profile.Profile
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalProfileInfoInfoRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockPlayerDao: PlayerDao
    private lateinit var subject: LocalProfileInfoInfoRepository
    private var fetchedProfiles: List<Profile>? = null
    private var updatedProfile: Profile? = null

    @Test
    fun fetchAll() {
        givenSubject()
        whenFetchingAll()
        assertNotNull(fetchedProfiles)
    }

    @Test(expected = NumberFormatException::class)
    fun `it should update profile`() {
        givenSubject()
        whenUpdating(1)
        assertNotNull(updatedProfile)
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if profile does not exist`() {
        givenSubject()
        whenUpdating()
    }

    @Test(expected = NumberFormatException::class)
    fun `it should throw if profile in table does not match`() {
        givenSubject()
        whenUpdating(1)
    }

    private fun givenSubject() {
        whenever(mockDb.playerDao()).thenReturn(mockPlayerDao)
        subject = LocalProfileInfoInfoRepository(mockDb, ProfileMapper())
    }

    private fun whenFetchingAll() {
        fetchedProfiles = subject.fetchAll(longArrayOf(1, 2, 3))
    }

    private fun whenUpdating(id: Long = -1L) {
        if(id != -1L){
            val playerTable = PlayerTable()
            playerTable.id = id
            playerTable.name = "name"
            playerTable.image = "image"
            playerTable.fav = "12"
            whenever(mockPlayerDao.fetchById(id)).thenReturn(playerTable)
        }
        updatedProfile = subject.update(id, "new", "img", "double")
    }
}