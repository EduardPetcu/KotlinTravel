package com.example.travel.data.budget

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.travel.data.Budget
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.repository.DatabaseRepository
import com.example.travel.repository.DatabaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

class BudgetViewModel(private val budgetLocationRepository: BudgetRepository = BudgetRepositoryImpl(),
                      private val userDatabase: DatabaseRepository = DatabaseRepositoryImpl(),
                      private val user: String = FirebaseAuth.getInstance().currentUser!!.uid) : ViewModel() {

    lateinit var budgetID: String
    var allValidationsPassed = mutableStateOf(false)
    var budgetUIState = mutableStateOf(BudgetUIState())
    var creationBudgetInProgress = mutableStateOf(false)
    fun onEvent(event: BudgetUIEvent) {
        when (event) {
            is BudgetUIEvent.BudgetNameChanged -> {
                budgetUIState.value = budgetUIState.value.copy(
                    name = event.name
                )
            }
            is BudgetUIEvent.BudgetCurrencyChanged -> {
                budgetUIState.value = budgetUIState.value.copy(
                    currency = event.currency
                )
            }
            is BudgetUIEvent.BudgetTotalChanged -> {
                budgetUIState.value = budgetUIState.value.copy(
                    total = event.total
                )
            }
            is BudgetUIEvent.BudgetDateChanged -> {
                budgetUIState.value = budgetUIState.value.copy(
                    // intervalDate is of format startDate - endDate
                    startDate = event.intervalDate.split(" - ")[0],
                    endDate = event.intervalDate.split(" - ")[1],
                    isStartDateValid = event.intervalDate.split(" - ")[0].isNotEmpty(),
                    isEndDateValid = event.intervalDate.split(" - ")[1].isNotEmpty()
                )
            }
            is BudgetUIEvent.BudgetCreation -> {
                createBudget()
            }
        }
        validateBudgetData()
    }

    fun resetBudgetUIState() {
        budgetUIState.value = BudgetUIState()
    }

    private fun createBudget() {
        creationBudgetInProgress.value = true
        val newBudget = Budget(
            author = user,
            name = budgetUIState.value.name,
            currency = budgetUIState.value.currency,
            total = budgetUIState.value.total,
            startDate = budgetUIState.value.startDate,
            endDate = budgetUIState.value.endDate
        )
        budgetID = newBudget.id
        // at the end of the function set the value to false
        budgetLocationRepository.insertBudget(newBudget).also {
            creationBudgetInProgress.value = false
            // clear the state
            resetBudgetUIState()
        }
        userDatabase.updateUserData(newBudget.id)
    }

     fun validateBudgetData() {
        val nameResult = budgetUIState.value.name.length >= 2
        val totalResult = budgetUIState.value.total > 0.0
        val startDateResult = budgetUIState.value.startDate.isNotEmpty()
        val endDateResult = budgetUIState.value.endDate.isNotEmpty()

        allValidationsPassed.value = nameResult && totalResult && startDateResult && endDateResult
    }

}