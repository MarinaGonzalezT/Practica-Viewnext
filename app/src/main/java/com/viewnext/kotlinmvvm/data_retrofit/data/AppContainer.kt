package com.viewnext.kotlinmvvm.data_retrofit.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.viewnext.kotlinmvvm.core.data.database.FacturaDatabase
import com.viewnext.kotlinmvvm.core.data.repository.OfflineFacturasRepository
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.data_retrofit.FacturasApiService
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

    private val retrofitService: FacturasApiService by lazy {
        retrofit.create(FacturasApiService::class.java)
    }

    private val database: FacturaDatabase = FacturaDatabase.getDatabase(context)

    override val facturasRepository: FacturasRepository by lazy {
        NetworkFacturasRepository(retrofitService)
    }

    override val roomFacturasRepository: RoomFacturasRepository by lazy {
        OfflineFacturasRepository(database.facturaDao())
    }

    override val filtrarFacturasUseCase: FiltrarFacturasUseCase = FiltrarFacturasUseCase()
}