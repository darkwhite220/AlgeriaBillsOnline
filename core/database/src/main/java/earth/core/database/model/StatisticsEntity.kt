package earth.core.database.model

import androidx.room.ColumnInfo
import earth.core.database.Statistics

data class StatisticsEntity(
    @ColumnInfo(name = "year_avg")
    val yearAgv: Float,
    @ColumnInfo(name = "trimester_avg")
    val trimesterAgv: Float,
    @ColumnInfo(name = "day_avg")
    val dayAvg: Float,
    @ColumnInfo(name = "max_pay")
    val maxPay: Float,
    @ColumnInfo(name = "max_pay_elect")
    val maxPayElect: Float,
    @ColumnInfo(name = "max_pay_gaz")
    val maxPayGaz: Float,
    @ColumnInfo(name = "min_pay")
    val minPay: Float,
    @ColumnInfo(name = "min_pay_elect")
    val minPayElect: Float,
    @ColumnInfo(name = "min_pay_gaz")
    val minPayGaz: Float,
)

fun StatisticsEntity.asExternalModel() = Statistics(
    yearAgv = yearAgv,
    trimesterAgv = trimesterAgv,
    dayAvg = dayAvg,
    maxPay = maxPay,
    maxPayElect = maxPayElect,
    maxPayGaz = maxPayGaz,
    minPay = minPay,
    minPayElect = minPayElect,
    minPayGaz = minPayGaz,
)

fun Statistics.asEntity() = StatisticsEntity(
    yearAgv = yearAgv,
    trimesterAgv = trimesterAgv,
    dayAvg = dayAvg,
    maxPay = maxPay,
    maxPayElect = maxPayElect,
    maxPayGaz = maxPayGaz,
    minPay = minPay,
    minPayElect = minPayElect,
    minPayGaz = minPayGaz,
)