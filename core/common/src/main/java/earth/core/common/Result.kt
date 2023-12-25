package earth.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

sealed interface ResultNoData<out T> {
    data object Success : ResultNoData<Nothing>
    data class Error(val exception: Throwable? = null) : ResultNoData<Nothing>
    data object Loading : ResultNoData<Nothing>
}

fun <T> Flow<T>.asResultNoData(): Flow<ResultNoData<T>> {
    return this
        .map<T, ResultNoData<T>> {
            ResultNoData.Success
        }
        .onStart { emit(ResultNoData.Loading) }
        .catch { emit(ResultNoData.Error(it)) }
}