package earth.core.throwablemodel

sealed class ConvertingPdfThrowable : Throwable() {
    
    data class UnhandledSignInResponse(val data: String) : Throwable(message = data)
    data class BadPdfFormat(val data: String) : Throwable(message = data)
    data object PdfTextExtractorError : Throwable()
    
}