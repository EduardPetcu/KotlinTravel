package com.example.travel.components.BudgetComponents

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.data.Budget
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.ui.theme.TravelTheme

@Composable
fun BudgetList(budgets: List<Budget>) {
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
    Card(
        // add Color(0xFFD5C28C) as background color for the Card
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD5C28C),
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = budget.name, style = MaterialTheme.typography.headlineMedium) // Budget name
            Text(text = "Total budget: ${budget.total} ${budget.currency}", style = MaterialTheme.typography.bodyLarge) // Total budget
            Text(text = "Remaining budget: ${budget.totalLeft} ${budget.currency}", style = MaterialTheme.typography.bodyLarge) // Remaining budget
            Text(text = "Date: ${budget.startDate} - ${budget.endDate}", style = MaterialTheme.typography.bodyLarge) // Date
            Button(onClick = { /*TODO: this will display the budget details*/ }) {
                Text(text = "View details and graph", color = Color(0xFFD5C28C))
            }
        }
    }
}

@Composable
fun BudgetHeader() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD5C28C)),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),) {
        Row() {
            Text(
                text = "Insert new travel budget:",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                onClick = { navigateTo(Screen.BudgetScreen) },
                modifier = Modifier.padding(16.dp).size(48.dp)
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
        val budget1: Budget = Budget(
            author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
            name = "Busteni",
            currency = "RON",
            startDate = "06-04-2024",
            endDate = "09-04-2024",
            total = 1000.0
        )
        val budget2: Budget = Budget(
            author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
            name = "Bran",
            currency = "RON",
            startDate = "06-04-2024",
            endDate = "09-04-2024",
            total = 1000.0
        )
        val budget3: Budget = Budget(
            author = "1HzBn1QQnYa4bQxJQ0EyO1FHtFl2",
            name = "Sinaia",
            currency = "RON",
            startDate = "06-04-2024",
            endDate = "09-04-2024",
            total = 1000.0
        )
        val budgets = listOf(budget1, budget2, budget3)
        BudgetList(budgets = budgets)
    }
}

