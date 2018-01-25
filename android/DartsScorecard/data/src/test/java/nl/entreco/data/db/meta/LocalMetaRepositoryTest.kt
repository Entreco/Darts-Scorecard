package nl.entreco.data.db.meta

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.TurnMeta
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalMetaRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMetaDao: MetaDao
    @Mock private lateinit var mockMapper: MetaMapper
    @Mock private lateinit var mockMeta: TurnMeta
    @Mock private lateinit var mockTable: MetaTable
    private lateinit var subject: LocalMetaRepository

    @Before
    fun setUp() {
        whenever(mockDb.metaDao()).thenReturn(mockMetaDao)
        subject = LocalMetaRepository(mockDb, mockMapper)
    }

    @Test
    fun `it should use mapper when creating meta`() {
        subject.create(1, 3, mockMeta, 5)
        verify(mockMapper).from(3, 1, mockMeta, 5)
    }

    @Test
    fun `it should use metaDao when creating meta`() {
        whenever(mockMapper.from(any(), any(), any(), any())).thenReturn(mockTable)
        subject.create(1, 3, mockMeta, 5)
        verify(mockMetaDao).create(mockTable)
    }

    @Test
    fun `it should undo`() {
        subject.undo(5)
        verify(mockMetaDao).undoLast(5)
    }
}