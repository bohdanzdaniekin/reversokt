package io.github.mrnemo.reversokt

import kotlin.test.Test
import kotlin.test.assertEquals

class JvmPlatformTest {

    @Test
    fun `test is jvm`() {
        assertEquals("JVM", Platform.name)
    }
}