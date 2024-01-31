package earth.darkwhite.feature.onboarding

interface OnBoardingEvent {
    object OnSkipClicked: OnBoardingEvent
    object OnNextClicked: OnBoardingEvent
}
