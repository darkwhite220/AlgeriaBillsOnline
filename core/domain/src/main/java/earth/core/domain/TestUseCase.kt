package earth.core.domain

import earth.core.data.UserDataRepository
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class TestUseCase @Inject constructor(
  private val userDataRepo: UserDataRepository,
) {
  operator fun invoke(): Flow<UserData> = userDataRepo.userData
}