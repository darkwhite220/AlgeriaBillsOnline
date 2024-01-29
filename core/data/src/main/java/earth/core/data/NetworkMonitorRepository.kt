package earth.core.data

import kotlinx.coroutines.flow.Flow

interface NetworkMonitorRepository {
    val networkStatus: Flow<Boolean>
}
