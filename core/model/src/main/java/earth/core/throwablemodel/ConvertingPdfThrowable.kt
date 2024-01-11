package earth.core.throwablemodel

sealed class ConvertingPdfThrowable : Throwable() {
    
    data class UnhandledSignInResponse(val data: String) : Throwable()
    data class UnhandledPdfFormat(val data: List<String>) : Throwable()
    data class BadPdfFormat(val data: List<String>) : Throwable()
    data class BadPdfFormatWithStateSupport(val data: List<String>) : Throwable()
    
}