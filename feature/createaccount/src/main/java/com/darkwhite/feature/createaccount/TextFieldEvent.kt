package com.darkwhite.feature.createaccount

sealed interface TextFieldEvent {
    data object OnKeyboardPreviousActions : TextFieldEvent
    data object OnKeyboardNextActions : TextFieldEvent
    data object OnKeyboardDoneActions : TextFieldEvent
    
    data object OnReferenceIconClick : TextFieldEvent
}