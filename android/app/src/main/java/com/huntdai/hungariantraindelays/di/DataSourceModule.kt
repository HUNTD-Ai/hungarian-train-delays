package com.huntdai.hungariantraindelays.di

import android.content.SharedPreferences
import com.huntdai.hungariantraindelays.data.network.StatsApi
import com.huntdai.hungariantraindelays.data.network.StatsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideStatsDataSource(statsApi: StatsApi): StatsDataSource {
        return StatsDataSource(statsApi)
    }

}