package earth.core.domain.home

import earth.core.data.BillRepository
import earth.core.data.UserRepository
import earth.core.database.BillPreview
import earth.core.database.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val billRepository: BillRepository,
) {
    
    operator fun invoke(): Flow<List<User>> =
        userRepository.getUsers().addBillsPreview(billRepository.getBillsPreview())
    
}

private fun Flow<List<User>>.addBillsPreview(billsPreview: Flow<List<BillPreview>>):
    Flow<List<User>> = combine(billsPreview) { users, bills ->
    users.map { user ->
        user.copy(billsPreview = bills.filter { it.reference == user.reference })
    }
}
