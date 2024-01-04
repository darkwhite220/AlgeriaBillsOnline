package earth.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import earth.core.database.User
import java.util.*

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val reference: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val address: String,
    val email: String,
    val password: String,
    val username: String,
    @ColumnInfo(name = "direction_distribution")
    val directionDistribution: String,
    @ColumnInfo(name = "business_agency")
    val businessAgency: String,
    // From bills data we know if its house or store
    @ColumnInfo(name = "is_house")
    val isHouse: Boolean,
    @ColumnInfo(name = "last_bill_number")
    val lastBillNumber: String?,
    @Embedded
    val statistics: StatisticsEntity?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = Date().time,
)

fun UserEntity.asExternalModel() = User(
    fullName = fullName,
    reference = reference,
    address = address,
    email = email,
    password = password,
    username = username,
    directionDistribution = directionDistribution,
    businessAgency = businessAgency,
    isHouse = isHouse,
    lastBillNumber = lastBillNumber ?: "",
    statistics = statistics?.asExternalModel(),
)

fun User.asEntity() = UserEntity(
    fullName = fullName,
    reference = reference,
    address = address,
    email = email,
    password = password,
    username = username,
    directionDistribution = directionDistribution,
    businessAgency = businessAgency,
    isHouse = isHouse,
    lastBillNumber = lastBillNumber,
    statistics = statistics?.asEntity(),
)