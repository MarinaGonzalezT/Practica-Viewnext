package com.viewnext.kotlinmvvm.data_retrofit.di

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockProviderTest {
    @Before
    fun setUp() {
        while(MockProvider.isMocking()) {
            MockProvider.alternarMock()
        }
    }

    @Test
    fun `por defecto el mock esta desactivado`() {
        assertFalse(MockProvider.isMocking())
    }

    @Test
    fun `alternarMock activa y luego desactiva correctamente`() {
        MockProvider.alternarMock()
        assertTrue(MockProvider.isMocking())

        MockProvider.alternarMock()
        assertFalse(MockProvider.isMocking())
    }
}