package io.github.mrnemo.reversokt

import kotlin.test.Test
import kotlin.test.assertEquals

class LinuxPlatformTest {

    @Test
    fun testPlatform() {
        assertEquals("Linux", Platform.name)
    }
}