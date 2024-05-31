import com.example.travel.data.register.RegisterViewModel
import com.example.travel.data.register.RegisterUIEvent
import com.example.travel.data.rules.Validator
import com.example.travel.repositories.FakeDatabaseRepository
import com.example.travel.repository.DatabaseRepositoryImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel
    private val mockDatabaseRepositoryImpl = FakeDatabaseRepository()

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(mockDatabaseRepositoryImpl)
    }

    @Test
    fun onEvent_emailChanged_updatesEmail() {
        val event = RegisterUIEvent.EmailChanged("test@example.com")
        registerViewModel.onEvent(event)
        assert(registerViewModel.registrationUIState.value.email == "test@example.com")
    }

    @Test
    fun onEvent_usernameChanged_updatesUsername() {
        val event = RegisterUIEvent.UsernameChanged("testUser")
        registerViewModel.onEvent(event)
        assert(registerViewModel.registrationUIState.value.username == "testUser")
    }

    @Test
    fun onEvent_passwordChanged_updatesPassword() {
        val event = RegisterUIEvent.PasswordChanged("testPassword")
        registerViewModel.onEvent(event)
        assert(registerViewModel.registrationUIState.value.password == "testPassword")
    }

    @Test
    fun validateDataWithRules_allValid_setsAllValidationsPassedToTrue() {
        registerViewModel.registrationUIState.value = registerViewModel.registrationUIState.value.copy(
            username = "validUsername",
            email = "validEmail@example.com",
            password = "validPassword123"
        )
        registerViewModel.validateDataWithRules()
        assert(registerViewModel.allValidationsPassed.value)
    }

    @Test
    fun validateDataWithRules_invalidUsername_setsAllValidationsPassedToFalse() {
        registerViewModel.registrationUIState.value = registerViewModel.registrationUIState.value.copy(
            username = "i",
            email = "validEmail@example.com",
            password = "validPassword123"
        )
        registerViewModel.validateDataWithRules()
        assert(!registerViewModel.allValidationsPassed.value)
    }

    @Test
    fun validateDataWithRules_invalidEmail_setsAllValidationsPassedToFalse() {
        registerViewModel.registrationUIState.value = registerViewModel.registrationUIState.value.copy(
            username = "validUsername",
            email = "invalidEmail",
            password = "validPassword123"
        )
        registerViewModel.validateDataWithRules()
        assert(!registerViewModel.allValidationsPassed.value)
    }

    @Test
    fun validateDataWithRules_invalidPassword_setsAllValidationsPassedToFalse() {
        registerViewModel.registrationUIState.value = registerViewModel.registrationUIState.value.copy(
            username = "validUsername",
            email = "validEmail@example.com",
            password = "bad"
        )
        registerViewModel.validateDataWithRules()
        assert(!registerViewModel.allValidationsPassed.value)
    }
}