package pl.droidcon.app.data.mapper

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import pl.droidcon.app.domain.SessionType

@RunWith(JUnitParamsRunner::class)
class SessionTypeMapperTest {

    @Rule
    @JvmField
    val exceptionRule: ExpectedException = ExpectedException.none()

    private val systemUnderTest = SessionTypeMapper()

    @Test
    @Parameters(method = "mapsToTypeParams")
    fun `maps string to type`(type: String, expected: SessionType) {
        val result = systemUnderTest.map(type)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Parameters(method = "throwsExceptionUnknownStringParams")
    fun `throws exception when string does not match know type`(type: String) {
        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("Not expecting $type as session type")

        systemUnderTest.map(type)
    }

    @Test
    @Parameters(method = "mapsTypeToStringParams")
    fun `maps session type to string`(sessionType: SessionType, expected: String) {
        val result = systemUnderTest.map(sessionType)

        assertThat(result).isEqualTo(expected)
    }

    @Suppress("unused")
    private fun mapsToTypeParams() = arrayOf(
            arrayOf("Talk", SessionType.TALK),
            arrayOf("Workshop", SessionType.WORKSHOP)
    )

    @Suppress("unused")
    private fun throwsExceptionUnknownStringParams() = arrayOf(
            "TALK", "WORKSHOP", "talk", "workshop", "xxxx", "null", "foo.bar"
    )

    @Suppress("unused")
    private fun mapsTypeToStringParams() = arrayOf(
            arrayOf(SessionType.WORKSHOP, "Workshop"),
            arrayOf(SessionType.TALK, "Talk")
    )
}