package earth.core.data

import earth.core.EstimateData
import earth.core.data.util.Constants.MIN_TIMBRE_VALUE
import earth.core.data.util.ExtractionUtil
import earth.core.data.util.ExtractionUtil.calculateElectricity
import earth.core.data.util.ExtractionUtil.calculateGaz
import earth.core.data.util.round
import earth.core.data.util.timbreRound
import earth.core.database.Bill
import javax.inject.Inject

class EstimateImplementation @Inject constructor() : EstimateRepository {
    
    override fun getEstimateBill(data: EstimateData): Bill {
        val electricity = calculateElectricity(
            consumption = data.electricityConsumption,
            menageType = data.menageType,
            defaultValue = data.electricityPMD.price
        )
        
        val gas = calculateGaz(
            consumptionTH = data.gasConsumption * data.gasPcs,
            menageType = data.menageType,
        )
        
        val stateSupport =
            ((electricity.totalHT - data.electricityPMD.price) * data.stateSupport.value).round()
        
        val rightsAndTaxes = ExtractionUtil.rightsAndTaxesPreviousTrimester(
            menageType = data.menageType,
            electConsumption = data.electricityConsumption.toInt()
        )
        
        val totalTTCNoTimbre =
            electricity.totalHT + electricity.totalTVA + gas.totalHT + gas.totalTVA +
                rightsAndTaxes.toBigDecimal() - stateSupport
        
        var timbre = (totalTTCNoTimbre * "0.01".toBigDecimal()).round()
        timbre = if (timbre.timbreRound() < MIN_TIMBRE_VALUE) {
            MIN_TIMBRE_VALUE
        } else {
            timbre.timbreRound()
        }
        
        return Bill(
            reference = "",
            isPaid = false,
            pdfByteArray = null,
            billNumber = "",
            date = "",
            trimester = "1",
            year = "",
            ebp = "",
            ebb = "",
            electricityMeterNumber = "",
            electNewValue = 0,
            electOldValue = 0,
            electConsumption = data.electricityConsumption,
            electConsumptionCost = electricity.totalHT,
            gazMeterNumber = "",
            gazNewValue = 0,
            gazOldValue = 0,
            gazConsumption = data.gasConsumption,
            gazConsumptionCost = gas.totalHT,
            stateSupport = stateSupport,
            rightsAndTaxes = rightsAndTaxes,
            totalHT = electricity.totalHT + gas.totalHT,
            electricityTva = electricity.totalTVA,
            gazTva = gas.totalTVA,
            totalTva = electricity.totalTVA + gas.totalTVA,
            totalTTCNoTimbre = totalTTCNoTimbre,
            timbre = timbre,
            totalTTC = totalTTCNoTimbre + timbre
        )
    }
    
}