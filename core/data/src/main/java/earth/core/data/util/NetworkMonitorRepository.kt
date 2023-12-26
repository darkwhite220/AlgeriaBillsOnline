package earth.core.data.util

import kotlinx.coroutines.flow.Flow

interface NetworkMonitorRepository {
    val networkStatus: Flow<Boolean>
}
