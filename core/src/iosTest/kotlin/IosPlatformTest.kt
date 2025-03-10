package io.github.mrnemo.reversokt

import kotlin.test.Test
import kotlin.test.assertEquals

class IosPlatformTest {

    @Test
    fun `test is ios`() {
        assertEquals("IOS", Platform.name)
    }
}