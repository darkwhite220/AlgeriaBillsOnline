package earth.core.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CreateAccountUseCase @Inject constructor(

) {
    
    operator fun invoke(): Flow<String> =
        flowOf("")
    
}