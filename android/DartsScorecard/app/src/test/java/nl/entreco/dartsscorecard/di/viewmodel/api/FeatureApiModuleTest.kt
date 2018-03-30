package nl.entreco.dartsscorecard.di.viewmodel.api

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.Logger
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FeatureApiModuleTest {

    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockStore: FirebaseFirestore
    @Mock private lateinit var mockFeatureRef: CollectionReference

    @Test
    fun provideFbDb() {
        whenever(mockStore.collection(any())).thenReturn(mockFeatureRef)
        assertNotNull(FeatureApiModule().provideRemoteFeatureRepository(mockStore, mockLogger))
    }
}