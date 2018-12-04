package nl.entreco.data.db.profile

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import nl.entreco.data.TestProvider
import nl.entreco.data.db.DscDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ProfileDaoTest {
    private lateinit var profileDao: ProfileDao
    private lateinit var db: DscDatabase
    private var givenProfile: ProfileTable? = null
    private var actualProfiles: List<ProfileTable> = emptyList()
    private var actualRow: Long = -1

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, DscDatabase::class.java).build()
        profileDao = db.profileDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun create() {
        givenProfile(11)
        whenCreatingPlayer()
        assertEquals(1, actualRow)
    }

    @Test
    fun fetchById() {
        createProfile(1)
        whenFetchingById(1)
        assertNotNull(actualProfiles)
    }

    private fun createProfile(id: Long) {
        val player = TestProvider.createProfile(id, 1, 2, 100, 90, 100, 190, 9, true)
        profileDao.create(player)
    }

    private fun givenProfile(id: Long) {
        givenProfile = TestProvider.createProfile(id, 1, 2, 100, 90, 100, 190, 9, true)
    }

    private fun whenCreatingPlayer() {
        actualRow = profileDao.create(givenProfile!!)
    }

    private fun whenFetchingById(id: Long) {
        actualProfiles = profileDao.fetchByPlayerId(id)
    }
}