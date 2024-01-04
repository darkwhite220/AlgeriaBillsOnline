package earth.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import earth.core.database.dao.BillDao
import earth.core.database.dao.UserDao
import earth.core.database.model.BillEntity
import earth.core.database.BillPreview
import earth.core.database.model.StatisticsEntity
import earth.core.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        BillEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun billDao(): BillDao
}