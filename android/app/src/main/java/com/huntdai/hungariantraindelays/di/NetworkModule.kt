package com.huntdai.hungariantraindelays.di

import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseApi
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionApi
import com.huntdai.hungariantraindelays.data.network.stats.StatsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(StatsApi.ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitContainer(okHttpClient: OkHttpClient): RetrofitContainer {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val statsRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(StatsApi.ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val delayCauseRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DelayCauseApi.ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val delayPredictionRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DelayPredictionApi.ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return RetrofitContainer(
            statsRetrofit = statsRetrofit,
            delayCauseRetrofit =  delayCauseRetrofit,
            delayPredictionRetrofit =  delayPredictionRetrofit
        )

    }

    @Provides
    @Singleton
    fun provideStatsApi(retrofitContainer: RetrofitContainer): StatsApi {
        return retrofitContainer.statsRetrofit.create(StatsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDelayPredictionApi(retrofitContainer: RetrofitContainer): DelayPredictionApi {
        return retrofitContainer.delayPredictionRetrofit.create(DelayPredictionApi::class.java)
    }
    @Provides
    @Singleton
    fun provideDelayCauseApi(retrofitContainer: RetrofitContainer): DelayCauseApi {
        return retrofitContainer.delayCauseRetrofit.create(DelayCauseApi::class.java)
    }
}

data class RetrofitContainer(
    val statsRetrofit : Retrofit,
    val delayCauseRetrofit : Retrofit,
    val delayPredictionRetrofit : Retrofit,
)