package com.huntdai.hungariantraindelays.data

sealed class DataSourceResponse<out T>

object DataSourceError : DataSourceResponse<Nothing>()

data class DataSourceResult<out T>(val result: T) : DataSourceResponse<T>()