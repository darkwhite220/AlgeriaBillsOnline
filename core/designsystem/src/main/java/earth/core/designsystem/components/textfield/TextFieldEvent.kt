package earth.core.designsystem.components.textfield

sealed interface TextFieldEvent {
    data object OnKeyboardPreviousActions : TextFieldEvent
    data object OnKeyboardNextActions : TextFieldEvent
    data object OnKeyboardDoneActions : TextFieldEvent
    
    data object OnReferenceIconClick : TextFieldEvent
}