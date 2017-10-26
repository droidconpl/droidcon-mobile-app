package pl.droidcon.app.data.local

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConvertersTest {

    private val systemUnderTest = Converters()

    @Test
    fun `converts lists of long to string and vice versa`() {
        val ids = listOf(1L, 100L, 20L, 2121L, 500L)

        val joinedString = systemUnderTest.fromLongList(ids)
        val result = systemUnderTest.fromStringToLongList(joinedString)

        assertThat(result).isEqualTo(ids)
    }
}