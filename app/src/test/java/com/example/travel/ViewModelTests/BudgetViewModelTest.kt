import com.example.travel.data.budget.BudgetViewModel
import com.example.travel.data.budget.BudgetUIEvent
import com.example.travel.repositories.FakeBudgetRepository
import com.example.travel.repositories.FakeDatabaseRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BudgetViewModelTest {

    private lateinit var viewModel: BudgetViewModel
    private var budgetRepository = FakeBudgetRepository()
    private var databaseRepository = FakeDatabaseRepository()
    private var fakeUserID = "fakeUserID"
    @Before
    fun setup() {
        viewModel = BudgetViewModel(budgetRepository, databaseRepository, fakeUserID)
    }

    @Test
    fun `BudgetNameChanged event updates name in UI state`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetNameChanged("Test Budget"))
        Assert.assertEquals("Test Budget", viewModel.budgetUIState.value.name)
    }

    @Test
    fun `BudgetCurrencyChanged event updates currency in UI state`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetCurrencyChanged("USD"))
        Assert.assertEquals("USD", viewModel.budgetUIState.value.currency)
    }

    @Test
    fun `BudgetTotalChanged event updates total in UI state`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetTotalChanged(1000.0))
        Assert.assertEquals(1000.0, viewModel.budgetUIState.value.total, 0.0)
    }

    @Test
    fun `BudgetDateChanged event updates start and end date in UI state`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetDateChanged("2022-01-01 - 2022-12-31"))
        Assert.assertEquals("2022-01-01", viewModel.budgetUIState.value.startDate)
        Assert.assertEquals("2022-12-31", viewModel.budgetUIState.value.endDate)
    }

    @Test
    fun `BudgetCreation event creates new budget and resets UI state`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetCreation)
        Assert.assertFalse(viewModel.creationBudgetInProgress.value)
        Assert.assertEquals("", viewModel.budgetUIState.value.name)
        Assert.assertEquals("", viewModel.budgetUIState.value.currency)
        Assert.assertEquals(0.0, viewModel.budgetUIState.value.total, 0.0)
        Assert.assertEquals("", viewModel.budgetUIState.value.startDate)
        Assert.assertEquals("", viewModel.budgetUIState.value.endDate)
        Assert.assertEquals(budgetRepository.getBudgetsFromUserName(fakeUserID)?.size ?: 0, 1)
    }

    @Test
    fun `validateBudgetData sets allValidationsPassed to true when all fields are valid`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetNameChanged("Test Budget"))
        viewModel.onEvent(BudgetUIEvent.BudgetCurrencyChanged("USD"))
        viewModel.onEvent(BudgetUIEvent.BudgetTotalChanged(1000.0))
        viewModel.onEvent(BudgetUIEvent.BudgetDateChanged("2022-01-01 - 2022-12-31"))
        viewModel.validateBudgetData()
        Assert.assertTrue(viewModel.allValidationsPassed.value)
    }

    @Test
    fun `validateBudgetData sets allValidationsPassed to false when any field is invalid`() = runTest {
        viewModel.onEvent(BudgetUIEvent.BudgetNameChanged(""))
        viewModel.onEvent(BudgetUIEvent.BudgetCurrencyChanged("USD"))
        viewModel.onEvent(BudgetUIEvent.BudgetTotalChanged(1000.0))
        viewModel.onEvent(BudgetUIEvent.BudgetDateChanged("2022-01-01 - 2022-12-31"))
        viewModel.validateBudgetData()
        Assert.assertFalse(viewModel.allValidationsPassed.value)
    }
}