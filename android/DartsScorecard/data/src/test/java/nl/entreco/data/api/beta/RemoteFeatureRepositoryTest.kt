package nl.entreco.data.api.beta

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.mockito.kotlin.whenever
import nl.entreco.domain.beta.Feature
import nl.entreco.liblog.Logger
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@Ignore("Unable to mock FireStore")
@RunWith(MockitoJUnitRunner::class)
class RemoteFeatureRepositoryTest {

    @Mock private lateinit var mockDb: FirebaseFirestore
    @Mock private lateinit var mockReference: CollectionReference
    @Mock private lateinit var mockLogger: Logger
    private lateinit var subject: RemoteFeatureRepository

    private lateinit var results: List<Feature>

    @Test
    fun `subscribe should return empty list initially`() {
        givenSubject()
        whenSubscribing {}
        thenResultIsEmptyList()
    }

    @Test
    fun `subscribe should store onChange method reference`() {
        givenSubject()
        whenSubscribing {}
        thenOnChangeIsNotNull()
    }

    @Test
    fun unsubscribe() {
        givenSubject()
        whenUnsubscribing()
        thenOnChangeIsNull()
    }

    @Test
    fun onEvent() {
    }

    private fun givenSubject() {
        whenever(mockDb.collection("features")).thenReturn(mockReference)
        subject = RemoteFeatureRepository(mockDb, mockLogger)
    }

    private fun whenSubscribing(onChange: (List<Feature>) -> Unit) {
        results = subject.subscribe(onChange)
    }

    private fun whenUnsubscribing() {
        subject.unsubscribe()
    }

    private fun thenResultIsEmptyList() {
        assertEquals(emptyList<Feature>(), results)
    }

    private fun thenOnChangeIsNotNull() {
        assertNotNull(subject.onChange)
    }

    private fun thenOnChangeIsNull() {
        assertNull(subject.onChange)
    }

}