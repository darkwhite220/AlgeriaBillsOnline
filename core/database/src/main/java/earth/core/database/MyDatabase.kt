package earth.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import earth.core.database.dao.MyDao
import earth.core.database.model.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = true)
abstract class MyDatabase : RoomDatabase() {
  abstract fun myDao(): MyDao
}