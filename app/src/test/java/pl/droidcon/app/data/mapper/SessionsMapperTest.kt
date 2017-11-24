package pl.droidcon.app.data.mapper

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import pl.droidcon.app.data.local.SessionLocal
import pl.droidcon.app.data.network.SessionRemote
import pl.droidcon.app.domain.Session
import util.createSpeaker

@RunWith(JUnitParamsRunner::class)
class SessionsMapperTest {

    private val sessionTypeMapper = SessionTypeMapper()
    private val systemUnderTest = SessionsMapper(sessionTypeMapper)

    private val speakers = listOf(
            createSpeaker(1),
            createSpeaker(2),
            createSpeaker(3)
    )

    @Test
    @Parameters(method = "mapsParams")
    fun maps(id: Long, type: String, title: String, description: String, ids: List<Long>, length: String, capacity: Int) {
        val remote = SessionRemote(
                sessionId = id,
                sessionType = type,
                sessionTitle = title,
                sessionDescription = description,
                speakerIds = ids,
                sessionLength = length.toDouble(),
                workshopCapacity = capacity
        )

        val domain = Session(
                sessionId = id,
                sessionType = sessionTypeMapper.map(type),
                sessionDescription = description,
                sessionTitle = title,
                speakers = speakers,
                sessionLength = length,
                workshopCapacity = capacity
        )

        val local = SessionLocal(
                sessionId = id,
                sessionType = type,
                sessionTitle = title,
                sessionDescription = description,
                speakerIds = ids,
                sessionLength = length,
                workshopCapacity = capacity
        )

        val resultLocal = systemUnderTest.map(domain)
        val resultDomainFromLocal = systemUnderTest.map(local, speakers)
        val resultDomainFromRemote = systemUnderTest.map(remote, speakers)

        assertThat(resultLocal).isEqualTo(local)
        assertThat(resultDomainFromLocal).isEqualTo(domain)
        // TODO: we will remove the SessionRemote Object
//        assertThat(resultDomainFromRemote).isEqualTo(domain)
    }

    @Suppress("unused")
    private fun mapsParams() = arrayOf(
            arrayOf(100, "Talk", "title 1", "description 1", listOf(1L, 2L, 3L), "45", 10),
            arrayOf(121, "Workshop", "title 2", "description 2", listOf(1L, 2L, 3L), "30", 9),
            arrayOf(1, "Talk", "title 3", "description 3", listOf(1L, 2L, 3L), "0", 0)
    )
}
