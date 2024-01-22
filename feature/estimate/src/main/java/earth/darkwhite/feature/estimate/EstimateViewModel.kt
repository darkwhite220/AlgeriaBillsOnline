package earth.darkwhite.feature.estimate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.EstimateData
import earth.core.data.EstimateRepository
import earth.core.database.Bill
import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.database.StateSupport
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO Persist estimateScreen data
@HiltViewModel
class EstimateViewModel @Inject constructor(
    private val estimateRepository: EstimateRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EstimateUiState())
    val uiState = _uiState.asStateFlow()
    
    var billEstimate by mutableStateOf(Bill())
        private set
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.launch {
            _uiState.collectLatest {
                estimateBill(it.asEstimateData())
            }
        }
    }
    
    fun onEvent(event: EstimateEvent) {
        when (event) {
            EstimateEvent.OnMenageTypeClick -> updateMenageType()
            EstimateEvent.OnStateSupportClick -> updateStateSupport()
            EstimateEvent.OnElectricityDPMClick -> updateElectricityDPM()
            is EstimateEvent.OnGasPCSClick -> updateGasPCS(event.newValue)
            is EstimateEvent.OnGasConsumptionChange -> updateGasConsumption(event.newValue)
            is EstimateEvent.OnElectricityConsumptionChange -> updateElectConsumption(event.newValue)
        }
    }
    
    private fun updateMenageType() {
        val newValue = (_uiState.value.menageType.ordinal + 1) % menageTypeEntries.size
        _uiState.update { it.copy(menageType = menageTypeEntries[newValue]) }
    }
    
    private fun updateStateSupport() {
        val newValue = (_uiState.value.stateSupport.ordinal + 1) % stateSupportEntries.size
        _uiState.update { it.copy(stateSupport = stateSupportEntries[newValue]) }
    }
    
    private fun updateElectricityDPM() {
        val newValue = (_uiState.value.electricityPMD.ordinal + 1) % electricityPMDEntries.size
        _uiState.update { it.copy(electricityPMD = electricityPMDEntries[newValue]) }
    }
    
    private fun updateGasPCS(newValue: String) {
        val value = newValue.checkPrep()
        
        try {
            _uiState.update {
                it.copy(
                    gasPcs = when {
                        value.isEmpty() -> EMPTY_VALUE
                        value.toBigDecimal() < "1".toBigDecimal() -> "1"
                        value.toBigDecimal() > "20".toBigDecimal() -> "20"
                        else -> value
                    }
                )
            }
        } catch (e: Exception) {
            println("updateGasPCS $e")
        }
    }
    
    private fun updateGasConsumption(newValue: String) {
        _uiState.update {
            it.copy(
                gasConsumption = if (newValue.isDigitsOnly()) {
                    if (newValue.isEmpty()) EMPTY_VALUE
                    else if (newValue.toInt() < MAX_CONSUMPTION_VALUE) newValue
                    else MAX_CONSUMPTION_VALUE.toString()
                } else it.gasConsumption
            )
        }
    }
    
    private fun updateElectConsumption(newValue: String) {
        _uiState.update {
            it.copy(
                electricityConsumption = if (newValue.isDigitsOnly()) {
                    if (newValue.isEmpty()) EMPTY_VALUE
                    else if (newValue.toInt() < MAX_CONSUMPTION_VALUE) newValue
                    else MAX_CONSUMPTION_VALUE.toString()
                } else it.electricityConsumption
            )
        }
    }
    
    private fun estimateBill(estimateData: EstimateData) {
        billEstimate = estimateRepository.getEstimateBill(estimateData)
    }
    
    companion object {
        private const val TAG = "EstimateViewModel"
        private const val MAX_CONSUMPTION_VALUE = 20000
        private const val EMPTY_VALUE = ""
        private val menageTypeEntries = MenageType.entries
        private val stateSupportEntries = StateSupport.entries
        private val electricityPMDEntries = ElectricityPMD.entries
    }
}

private fun String.checkPrep(): String = this.replace(",", ".").trimStart('0')
