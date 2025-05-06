package com.viewnext.kotlinmvvm.data_retrofit.di

import co.infinum.retromock.Retromock
import com.viewnext.kotlinmvvm.data_retrofit.apiService.FacturasApiService
import org.junit.After
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Retrofit
import kotlin.test.Test

class AppModuleTest {
    private lateinit var retrofit: Retrofit
    private lateinit var retromock: Retromock

    @Before
    fun setUp() {
        retrofit = mock()
        retromock = mock()

        while(MockProvider.isMocking()) {
            MockProvider.alternarMock()
        }
    }

    @After
    fun cleanUp() {
        while(MockProvider.isMocking()) {
            MockProvider.alternarMock()
        }
    }

    @Test
    fun `provideFacturasApi usa retrofit cuando mocking esta desactivado`() {
        val servicio = mock<FacturasApiService>()
        whenever(retrofit.create(FacturasApiService::class.java)).thenReturn(servicio)

        val resultado = AppModule.provideFacturasApi(retrofit, retromock)

        verify(retrofit).create(FacturasApiService::class.java)
        verify(retromock, never()).create(FacturasApiService::class.java)
        assert(resultado === servicio)
    }

    @Test
    fun `provideFacturasApi usa retromock cuando mocking esta activado`() {
        MockProvider.alternarMock()

        val servicio = mock<FacturasApiService>()
        whenever(retromock.create(FacturasApiService::class.java)).thenReturn(servicio)

        val resultado = AppModule.provideFacturasApi(retrofit, retromock)

        verify(retromock).create(FacturasApiService::class.java)
        verify(retrofit, never()).create(FacturasApiService::class.java)
        assert(resultado === servicio)
    }
}