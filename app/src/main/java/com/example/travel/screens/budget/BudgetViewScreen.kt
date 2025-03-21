package com.example.travel.screens.budget


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.DesignComponents.checkIfBudgetIsValid
import com.example.travel.data.Budget
import com.example.travel.data.Expense
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.ExpenseRepositoryImpl
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlinx.coroutines.async
import java.util.Locale

@Composable
fun BudgetViewScreen(idBudget: String) {
    TravelTheme {
        val budgetRepository: BudgetRepository = BudgetRepositoryImpl()
        var budget by remember { mutableStateOf<Budget?>(null) }
        var expenses by remember { mutableStateOf<List<Expense>?>(null) }
        val expensesRepository = ExpenseRepositoryImpl()
        val expenseList = ExpensesList()
        val databaseRepositoryImpl = DatabaseRepositoryImpl()
        BackHandler (
            onBack = {
                navigateTo(Screen.CalculateScreen)
            })
        LaunchedEffect(key1 = true) {
            val budgetDeferred = async { budgetRepository.getBudgetById(idBudget) }
            budget = budgetDeferred.await()
            val expensesDeferred = async { expensesRepository.getExpensesFromBudgetId(idBudget) }
            expenses = expensesDeferred.await()
        }
        Column (
            modifier = Modifier.background(color = BackgroundBlue)
        ) {
            UserProfile(databaseRepositoryImpl = databaseRepositoryImpl)
            if (budget == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                expenseList.BudgetBodyCard(expensesStartingList = expenses, budget = budget!!)
            }
        }
    }
}

class ExpensesList {

    private var expenses: List<Expense> by mutableStateOf(emptyList())
    private var budgetUpdatable by mutableStateOf(Budget())
    private val listCategories = listOf("Food", "Transport", "Drink", "Entertainment", "Others")

    @Composable
    fun BudgetHeaderCard(budget: Budget) {
        val isValid: Boolean = checkIfBudgetIsValid(budget.endDate)
        if (this.budgetUpdatable.id != budget.id) {
            this.budgetUpdatable = budget
        }
        Log.d("BudgetViewScreen", "Budget: $budgetUpdatable")
        Card(colors = CardDefaults.cardColors(containerColor = ContainerYellow)) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(
                        budgetUpdatable.name, style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        text = "Start: ${budgetUpdatable.startDate} \n End: ${budgetUpdatable.endDate}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    "Balance", style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${budgetUpdatable.totalLeft} ${budgetUpdatable.currency}", // current budget
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "/${budgetUpdatable.total} ${budgetUpdatable.currency}", // total budget
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { navigateTo(Screen.ExpenseInsertScreen, budgetUpdatable)},
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = isValid
                ) {
                    Text("Add Expense")
                }
            }
        }
    }
    @Composable
    fun BudgetBodyCard(expensesStartingList: List<Expense>?, budget: Budget) {
        expenses = expensesStartingList ?: emptyList()
        this.budgetUpdatable = budget
        val context: Context = LocalContext.current
        val expenseRepository = ExpenseRepositoryImpl()
        val budgetRepository = BudgetRepositoryImpl()
        LazyColumn {
            item {
                BudgetPieChart(expenseList = expenses)
            }
            item {
                BudgetHeaderCard(budget = budget)
            }
            item {
                Text(
                    text = "Expenses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
            expenses.forEachIndexed { index, item ->
                Log.d("BudgetViewScreen", "Expense: $item")
                item {
                    ExpenseCard(
                        expense = expenses[index],
                        budget = budgetUpdatable,
                        context = context,
                        expenseRepository,
                        budgetRepository
                    )
                    HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    fun BudgetPieChart(expenseList: List<Expense>) {
        // create a long list of different colors
        val colorList = listOf(
            Color.Red,
            Color.Green,
            Color.Yellow,
            Color.Magenta,
            Color.Cyan,
        )

        val pieChartDataList = expenseList.map { expense ->
            PieChartData.Slice(
                value = expense.price.toFloat(),
                color = colorList[this.listCategories.indexOf(expense.category) % colorList.size]
            )
        }

        // remove slices with duplicate colors and sum their values
        val pieChartDataListFiltered = pieChartDataList.groupBy { it.color }
            .map { entry ->
                PieChartData.Slice(
                    value = entry.value.sumOf { it.value.toDouble() }.toFloat(),
                    color = entry.key
                )
            }
        val listCategoriesColors = colorList.zip(listCategories)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
           horizontalArrangement = Arrangement.Center
        ) {
            if (pieChartDataListFiltered.isEmpty()) {
                // make the text appear in the center of the row
                Text(
                    text = "No expenses",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                PieChart(
                    pieChartData = PieChartData(pieChartDataListFiltered),
                    animation = simpleChartAnimation(),
                    sliceDrawer = SimpleSliceDrawer(),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = 150.dp)
                        .align(Alignment.CenterVertically)
                )
                Column {
                    // Make a legend for the pie chart
                    Text(
                        text = "Legend",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                    pieChartDataListFiltered.forEach { slice ->
                        val budgetUsed = budgetUpdatable.total - budgetUpdatable.totalLeft
                        val percentage =
                            String.format(Locale.US, "%.2f", (slice.value / budgetUsed) * 100)
                        val category = listCategoriesColors.find { it.first == slice.color }?.second
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(color = slice.color)
                            )
                            Text(
                                text = "${category}: ${percentage}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ExpenseCard(
        expense: Expense,
        budget: Budget,
        context: Context,
        expenseRepository: ExpenseRepositoryImpl,
        budgetRepository: BudgetRepositoryImpl
    ) {
        val isValid: Boolean = checkIfBudgetIsValid(budget.endDate)
        val showDeleteDialog = remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ContainerYellow)
        )
        {
            SwipeToDeleteContainer(item = expense,
                showDeleteDialog = showDeleteDialog,
                isValid = isValid,
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    when (expense.category) {
                        "Food" -> {
                            Image(
                                painter = painterResource(id = R.drawable.food),
                                contentDescription = "Food",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                            )
                        }

                        "Transport" -> {
                            Image(
                                painter = painterResource(id = R.drawable.car),
                                contentDescription = "Transport",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                            )
                        }

                        "Drink" -> {
                            Image(
                                painter = painterResource(id = R.drawable.drink),
                                contentDescription = "Drink",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                            )
                        }

                        "Entertainment" -> {
                            Image(
                                painter = painterResource(id = R.drawable.entertainment),
                                contentDescription = "Entertainment",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                            )
                        }

                        else -> {
                            Image(
                                painter = painterResource(id = R.drawable.others),
                                contentDescription = "Others",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = expense.category,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            expense.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Column {
                        Text(
                            "-${expense.price} ${budget.currency}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.End
                        )
                        if (isValid) {
                            Icon(imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable {
                                        showDeleteDialog.value = true
                                    })
                        }
                    }
                    if (showDeleteDialog.value) {
                        LaunchDeleteExpenseAlert(
                            expense,
                            context,
                            expenseRepository,
                            budgetRepository,
                            showDeleteDialog
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun <T> SwipeToDeleteContainer(
        item: T,
        showDeleteDialog: MutableState<Boolean>,
        isValid: Boolean,
        content: @Composable (T) -> Unit
    ) {
        val state = rememberDismissState(
            confirmStateChange = {
                if (it == DismissValue.DismissedToStart && isValid) {
                    Log.d("BudgetViewScreen", "Item to be removed")
                    showDeleteDialog.value = true
                    false
                } else {
                    false
                }
            }
        )

        AnimatedVisibility(
            visible = state.currentValue != DismissValue.DismissedToStart,
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 500),
                shrinkTowards = Alignment.Top
            ) + fadeOut()
        ) {
            SwipeToDismiss(state = state, background = {
                DeleteBackground(swipeDismissState = state, isValid = isValid)
            }, dismissContent = { content(item) },
                directions = setOf(DismissDirection.EndToStart))
        }

        if (showDeleteDialog.value) {
            LaunchDeleteExpenseAlert(
                item as Expense,
                LocalContext.current,
                ExpenseRepositoryImpl(),
                BudgetRepositoryImpl(),
                showDeleteDialog,
                state
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DeleteBackground(
        swipeDismissState: DismissState,
        isValid: Boolean
    ) {
        val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart && isValid) {
            Color.Red
        } else {
            Color.Transparent
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(8.dp),
        ) {
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun LaunchDeleteExpenseAlert(expense: Expense, context: Context, expenseRepository: ExpenseRepositoryImpl, budgetRepository: BudgetRepositoryImpl,
                                 showDeleteDialog: MutableState<Boolean>,
                                 state: DismissState? = null) {
        LaunchAlert(
            "Delete expense",
            "Are you sure you want to delete expense: \"" + expense.description + "\"?",
            "Yes",
            "No",
            onConfirm = {
                if (expense.id.isNotEmpty()) {
                    expenseRepository.deleteExpense(expense.id)
                    val newUpdatedBudget = budgetUpdatable.copy(totalLeft = budgetUpdatable.totalLeft + expense.price)
                    budgetUpdatable = newUpdatedBudget
                    budgetRepository.updateBudget(newUpdatedBudget)
                    showDeleteDialog.value = false
                    Toast.makeText(context, "Expense deleted", Toast.LENGTH_SHORT).show()
                    expenses = expenses.filter { it != expense }
                } else {
                    Log.d("BudgetViewScreen", "Expense id is null")
                }
                showDeleteDialog.value = false
            },
            onDismissRequest = {
                showDeleteDialog.value = false
            }
        )
        LaunchedEffect(key1 = showDeleteDialog.value) {
            state?.reset()
        }
    }

}