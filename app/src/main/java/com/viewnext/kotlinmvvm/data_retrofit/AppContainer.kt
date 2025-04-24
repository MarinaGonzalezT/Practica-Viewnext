package com.viewnext.kotlinmvvm.data_retrofit

import android.content.Context
import co.infinum.retromock.Retromock
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.viewnext.kotlinmvvm.core.data.database.FacturaDatabase
import com.viewnext.kotlinmvvm.core.data.repository.OfflineFacturasRepository
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val facturasRepository : FacturasRepository
    val roomFacturasRepository : RoomFacturasRepository
    val filtrarFacturasUseCase : FiltrarFacturasUseCase
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val baseUrl = "https://e7cd6b3c-bd3f-482e-b78a-1b5920cef8b0.mock.pstmn.io/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retromock = Retromock.Builder()
        .retrofit(retrofit)
        .defaultBodyFactory(context.assets::open)
        .build()

    private val retrofitService: AppApiService
        get() = if(!mockActivado) {
            retrofit.create(AppApiService::class.java)
        } else {
            retromock.create(AppApiService::class.java)
        }

    private val database: FacturaDatabase = FacturaDatabase.getDatabase(context)

    override val facturasRepository: FacturasRepository
        get() = _repositorio ?: NetworkFacturasRepository(retrofitService).also {
            _repositorio = it
        }


    override val roomFacturasRepository: RoomFacturasRepository by lazy {
        OfflineFacturasRepository(database.facturaDao())
    }

    override val filtrarFacturasUseCase: FiltrarFacturasUseCase = FiltrarFacturasUseCase()

    companion object {
        private var mockActivado: Boolean = false
        private var _repositorio: FacturasRepository? = null

        fun alternarMock() {
            mockActivado = !mockActivado
            _repositorio = null
        }

        fun isMocking(): Boolean = mockActivado
    }
}