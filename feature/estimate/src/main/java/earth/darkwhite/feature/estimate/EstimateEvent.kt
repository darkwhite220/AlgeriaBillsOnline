package earth.darkwhite.feature.estimate

sealed interface EstimateEvent {
    
    data object OnMenageTypeClick : EstimateEvent
    data object OnStateSupportClick : EstimateEvent
    data object OnElectricityDPMClick : EstimateEvent
    data class OnGasPCSClick(val newValue: String) : EstimateEvent
    data class OnElectricityConsumptionChange(val newValue: String) : EstimateEvent
    data class OnGasConsumptionChange(val newValue: String) : EstimateEvent
    
}