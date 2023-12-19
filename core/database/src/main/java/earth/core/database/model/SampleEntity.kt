package earth.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SampleEntity(
  @PrimaryKey(autoGenerate = true)
  val index: Long = 0,
  val name: String
)
