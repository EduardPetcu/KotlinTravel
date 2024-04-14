package com.example.travel.screens.budget


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.data.Budget
import com.example.travel.data.Expense
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.BudgetRepository
import com.example.travel.repository.BudgetRepositoryImpl
import com.example.travel.repository.ExpenseRepositoryImpl
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import kotlinx.coroutines.async

@Composable
fun BudgetViewScreen(idBudget: String) {
    TravelTheme {
        val budgetRepository: BudgetRepository = BudgetRepositoryImpl()
        var budget by remember { mutableStateOf<Budget?>(null) }
        var expenses by remember { mutableStateOf<List<Expense>?>(null) }
        val expensesRepository = ExpenseRepositoryImpl()
        BackHandler (
            onBack = {
                TravelAppRouter.navigateTo(Screen.CalculateScreen)
            })
        LaunchedEffect(key1 = true) {
            val budgetDeferred = async { budgetRepository.getBudgetById(idBudget) }
            budget = budgetDeferred.await()
            val expensesDeferred = async { expensesRepository.getExpensesFromBudgetId(idBudget) }
            expenses = expensesDeferred.await()
            Log.d("BudgetViewScreen", "Expenses: $expenses")
        }
        Column (
            modifier = Modifier.background(color = Color.hsl(236f, 0.58f, 0.52f))
        ) {
            UserProfile()
            if (budget == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            budget?.let {
                BudgetHeaderCard(budget = it)
            }
            if (expenses != null) {
                BudgetBodyCard(expenses = expenses!!, currency = budget!!.currency)
            }
        }
    }
}

@Composable
fun BudgetHeaderCard(budget: Budget) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFD5C28C))) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(
                    budget.name, style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp).weight(1f),
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = "Start: ${budget.startDate} \n End: ${budget.endDate}",
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
                    text = "${budget.totalLeft} ${budget.currency}", // current budget
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "/${budget.total} ${budget.currency}", // total budget
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = { navigateTo(Screen.ExpenseInsertScreen, budget.id)},
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("Add Expense")
            }
        }
    }
}

@Composable
fun BudgetBodyCard(expenses: List<Expense>, currency: String) {
    LazyColumn {
        item {
            Text(text = "Expenses", style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.padding(8.dp))
        }
        expenses.forEach {
            Log.d("BudgetViewScreen", "Expense: $it")
            item {
                ExpenseCard(expense = it, currency = currency)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }

    }
}

@Composable
fun ExpenseCard(expense: Expense, currency: String) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFD5C28C)))
    {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
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
            Column (modifier = Modifier.weight(1f)) {
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
            Text(
                "-${expense.price} $currency",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BudgetHeaderCardPreview() {
    Column(Modifier.background(color = Color.hsl(236f, 0.58f, 0.52f))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                "Paris Budget Trip", style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFD5C28C),
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Start
            )
            Text(
                text = "Start: 24-04-2024 End: 30-04-2024",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                textAlign = TextAlign.End
            )
        }
        Text(
            "Balance", style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
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
                text = "1000 RON", // current budget
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFD5C28C),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = " /1500 RON", // total budget
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFD5C28C),
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center
            )
        }
        TravelTheme {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Expense", color = Color(0xFFD5C28C))
            }
        }
        HorizontalDivider(color = Color.White, thickness = 0.5.dp)
        Text(text = "Expenses", style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.padding(8.dp))
        Card(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFD5C28C)))
        {
            Row(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.food),
                    contentDescription = "Food",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                )
                Column (modifier = Modifier.weight(1f)) {
                    Text(
                        "Food",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        "Cina de seara",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Text(
                    "-100 RON",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Row(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.car),
                    contentDescription = "Transport",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                )
                Column (modifier = Modifier.weight(1f)) {
                    Text(
                        "Transport",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        "Drum de dus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Text(
                    "-250 RON",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}