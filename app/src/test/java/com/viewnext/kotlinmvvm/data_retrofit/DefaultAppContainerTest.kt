package com.viewnext.kotlinmvvm.data_retrofit

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultAppContainerTest {
    @Before
    fun resetState() {
        while (DefaultAppContainer.isMocking()) {
            DefaultAppContainer.alternarMock()
        }
    }

    @Test
    fun `mocking esta desactivado por defecto`() {
        assertFalse(DefaultAppContainer.isMocking())
    }

    @Test
    fun `alternarMock alterna de true a false correctamente`() {
        DefaultAppContainer.alternarMock() // Activamos
        assertTrue(DefaultAppContainer.isMocking())

        DefaultAppContainer.alternarMock() // Desactivamos
        assertFalse(DefaultAppContainer.isMocking())
    }
}