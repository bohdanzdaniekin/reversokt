package io.github.mrnemo.reversokt

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidPlatformTest {

    @Test
    fun `test is android`() {
        assertEquals("Android", Platform.name)
    }
}