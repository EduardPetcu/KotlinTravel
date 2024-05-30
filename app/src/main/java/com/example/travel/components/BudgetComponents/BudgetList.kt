package com.example.travel.components.BudgetComponents

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.DesignComponents.checkIfBudgetIsValid
import com.example.travel.data.Budget
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.ui.theme.*
import com.example.travel.ui.theme.TravelTheme

class BudgetList {

    private var budgets: List<Budget> by mutableStateOf(emptyList())
    private val userRepository = DatabaseRepositoryImpl()
    @Composable
    fun BudgetListGenerator(budgetListGiven: List<Budget>) {
        this.budgets = budgetListGiven
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState()
        ) {
            item {
                BudgetHeader()
            }
            items(budgets.size) { index ->
                BudgetItem(budget = budgets[index])
            }
        }
    }

    @Composable
    fun BudgetItem(budget: Budget) {
        val budgetRepository = BudgetRepositoryImpl()
        val isValid = checkIfBudgetIsValid(budget.endDate)
        var showDialog by remember { mutableStateOf(false) }
        val context: Context = LocalContext.current
        Card(
            colors = CardDefaults.cardColors(
                containerColor = isValid.let{
                    if (it) {
                        ContainerYellow
                    } else {
                        DisabledYellow
                    }
                },
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = budget.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    ) // Budget name
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = DeclineRed,
                            contentDescription = "Delete budget",
                        )
                    }
                }
                if (showDialog) {
                    LaunchAlert(
                        "Delete budget",
                        "Are you sure you want to delete this budget?",
                        "Yes",
                        "No",
                        {
                            if (!budget.id.isNullOrEmpty()) {
                                budgetRepository.deleteBudget(budget.id)
                                userRepository.deleteElement("budgets", budget.id)
                                showDialog = false
                                Toast.makeText(context, "Budget deleted", Toast.LENGTH_SHORT).show()
                                budgets = budgets.filter { it != budget }
                            } else {
                                Toast.makeText(context, "Budget id is null", Toast.LENGTH_SHORT).show()
                            }
                        },
                        { showDialog = false })
                }
                Text(
                    text = "Total budget: ${budget.total} ${budget.currency}",
                    style = MaterialTheme.typography.bodyLarge
                ) // Total budget
                Text(
                    text = "Remaining budget: ${budget.totalLeft} ${budget.currency}",
                    style = MaterialTheme.typography.bodyLarge
                ) // Remaining budget
                Text(
                    text = "Date: ${budget.startDate} - ${budget.endDate}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = isValid.let {
                        if (it) {
                            Color.Black
                        } else {
                            DeclineRed
                        }
                    }
                ) // Date
                Button(onClick = { navigateTo(Screen.BudgetViewScreen, budget.id) }) {
                    Text(text = "View details and graph", color = ContainerYellow)
                }
            }
        }
    }

    @Composable
    fun BudgetHeader() {
        Card(
            colors = CardDefaults.cardColors(containerColor = ContainerYellow),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row {
                Text(
                    text = "Insert new travel budget:",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { navigateTo(Screen.BudgetScreen) },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new budget",
                        Modifier.size(48.dp)
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewBudgetScreen() {
        TravelTheme {
            val budget1 = Budget(
                author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
                name = "Busteni",
                currency = "RON",
                startDate = "06-04-2024",
                endDate = "09-04-2024",
                total = 1000.0
            )
            val budget2 = Budget(
                author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
                name = "Bran",
                currency = "RON",
                startDate = "06-04-2024",
                endDate = "09-04-2024",
                total = 1000.0
            )
            val budget3 = Budget(
                author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
                name = "Sinaia",
                currency = "RON",
                startDate = "06-04-2024",
                endDate = "09-04-2024",
                total = 1000.0
            )
            val budgets3 = listOf(budget1, budget2, budget3)
            BudgetListGenerator(budgets3)
        }
    }
}