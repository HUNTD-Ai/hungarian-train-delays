package com.huntdai.hungariantraindelays.di

import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseApi
import com.huntdai.hungariantraindelays.data.network.delay_cause.DelayCauseDataSource
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionApi
import com.huntdai.hungariantraindelays.data.network.prediction.DelayPredictionDataSource
import com.huntdai.hungariantraindelays.data.network.stats.StatsApi
import com.huntdai.hungariantraindelays.data.network.stats.StatsDataSource
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

    @Provides
    fun provideDelayCauseDataSource(delayCauseApi: DelayCauseApi): DelayCauseDataSource {
        return DelayCauseDataSource(delayCauseApi)
    }

    @Provides
    fun provideDelayPredictionDataSource(delayPredictionApi: DelayPredictionApi): DelayPredictionDataSource {
        return DelayPredictionDataSource(delayPredictionApi)
    }

}