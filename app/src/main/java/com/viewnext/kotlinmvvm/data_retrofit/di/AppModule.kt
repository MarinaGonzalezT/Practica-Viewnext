package com.viewnext.kotlinmvvm.data_retrofit.di

import android.content.Context
import androidx.room.Room
import co.infinum.retromock.Retromock
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.viewnext.kotlinmvvm.core.data.database.FacturaDao
import com.viewnext.kotlinmvvm.core.data.database.FacturaDatabase
import com.viewnext.kotlinmvvm.core.data.repository.OfflineFacturasRepository
import com.viewnext.kotlinmvvm.core.data.repository.RoomFacturasRepository
import com.viewnext.kotlinmvvm.data_retrofit.apiService.FacturasApiService
import com.viewnext.kotlinmvvm.data_retrofit.apiService.SmartSolarApiService
import com.viewnext.kotlinmvvm.data_retrofit.repository.DetallesRepository
import com.viewnext.kotlinmvvm.data_retrofit.repository.FacturasRepository
import com.viewnext.kotlinmvvm.data_retrofit.repository.NetworkDetallesRepository
import com.viewnext.kotlinmvvm.data_retrofit.repository.NetworkFacturasRepository
import com.viewnext.kotlinmvvm.domain.usecases.FiltrarFacturasUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://e7cd6b3c-bd3f-482e-b78a-1b5920cef8b0.mock.pstmn.io/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetromock(retrofit: Retrofit, @ApplicationContext context: Context): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory(context.assets::open)
            .build()
    }

    @Provides
    fun provideFacturasApi(retrofit: Retrofit, retromock: Retromock): FacturasApiService {
        return if(MockProvider.isMocking()) {
            retromock.create(FacturasApiService::class.java)
        } else {
            retrofit.create(FacturasApiService::class.java)
        }
    }

    @Provides
    fun provideSmartSolarApi(retromock: Retromock): SmartSolarApiService =
        retromock.create(SmartSolarApiService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FacturaDatabase =
        Room.databaseBuilder(
            context,
            FacturaDatabase::class.java,
            "factura_database"
        ).build()

    @Provides
    fun provideDao(database: FacturaDatabase): FacturaDao =
        database.facturaDao()

    @Provides
    fun provideFacturasRepository(api: FacturasApiService): FacturasRepository =
        NetworkFacturasRepository(api)

    @Provides
    fun provideDetallesRepository(api: SmartSolarApiService): DetallesRepository =
        NetworkDetallesRepository(api)

    @Provides
    fun provideRoomRepository(dao: FacturaDao): RoomFacturasRepository =
        OfflineFacturasRepository(dao)

    @Provides
    fun provideUseCase(): FiltrarFacturasUseCase = FiltrarFacturasUseCase()
}