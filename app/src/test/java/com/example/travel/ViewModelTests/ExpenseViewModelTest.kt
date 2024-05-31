import com.example.travel.data.Budget
import com.example.travel.data.Expense
import com.example.travel.data.expense.ExpenseUIEvent
import com.example.travel.data.expense.ExpenseViewModel
import com.example.travel.repositories.FakeBudgetRepository
import com.example.travel.repositories.FakeExpenseRepository
import org.junit.Before
import org.junit.Test

class ExpenseViewModelTest {
    private lateinit var expenseViewModel: ExpenseViewModel
    private val mockExpenseRepository = FakeExpenseRepository()
    private val mockBudgetRepository = FakeBudgetRepository()
    private lateinit var budget: Budget
    @Before
    fun setup() {
        expenseViewModel = ExpenseViewModel(mockExpenseRepository, mockBudgetRepository)
        budget = Budget(author = "testAuthor", name = "testName", currency = "testCurrency", total = 100.0, startDate = "2022-01-01", endDate = "2022-12-31")
    }

    @Test
    fun onEvent_expensePriceChanged_updatesPrice() {
        val event = ExpenseUIEvent.ExpensePriceChanged(50.0)
        expenseViewModel.onEvent(event, budget)
        assert(expenseViewModel.expenseUIState.value.price == 50.0)
    }

    @Test
    fun onEvent_expenseCategoryChanged_updatesCategory() {
        val event = ExpenseUIEvent.ExpenseCategoryChanged("Food")
        expenseViewModel.onEvent(event, budget)
        assert(expenseViewModel.expenseUIState.value.category == "Food")
    }
    @Test
    fun onEvent_expenseDescriptionChanged_updatesDescription() {
        val event = ExpenseUIEvent.ExpenseDescriptionChanged("Lunch")
        expenseViewModel.onEvent(event, budget)
        assert(expenseViewModel.expenseUIState.value.description == "Lunch")
    }

    @Test
    fun onEvent_expenseCreation_createsExpense() {
        val event = ExpenseUIEvent.ExpensePriceChanged(50.0)
        expenseViewModel.onEvent(event, budget)
        val event2 = ExpenseUIEvent.ExpenseCategoryChanged("Food")
        expenseViewModel.onEvent(event2, budget)
        val event3 = ExpenseUIEvent.ExpenseDescriptionChanged("Lunch")
        expenseViewModel.onEvent(event3, budget)
        val expectedExpense = Expense(budgetId = budget.id, price = expenseViewModel.expenseUIState.value.price, category = expenseViewModel.expenseUIState.value.category, description = expenseViewModel.expenseUIState.value.description)
        val event4 = ExpenseUIEvent.ExpenseCreation
        expenseViewModel.onEvent(event4, budget)
        val listExpenses = mockExpenseRepository.expenses
        assert(listExpenses.size == 1)
        assert(listExpenses[0].category == expectedExpense.category)
        assert(listExpenses[0].price == expectedExpense.price)
        assert(listExpenses[0].description == expectedExpense.description)
    }
}