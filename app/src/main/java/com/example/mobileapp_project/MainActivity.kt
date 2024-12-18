package com.example.mobileapp_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapp_project.data.Entry
import com.example.mobileapp_project.data.EntryDao
import com.example.mobileapp_project.data.FinanceDatabase
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color
import com.example.mobileapp_project.data.CategoryExpense

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = FinanceDatabase.getDatabase(this.applicationContext)
        val entryDao = db.entryDao()
        super.onCreate(savedInstanceState)
        setContent {
            MobileAppProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeView(navController, entryDao) }
                    composable("analytics") { AnalyticsView(navController, entryDao) }
                    composable("entry") { EntryView(navController, entryDao) }
                }
            }
        }
    }
}

/**
 * Home page
 *
 * Shows the home page,
 * has access to analytics and entry pages
 */
@Composable
fun HomeView(navController: NavController, dao : EntryDao){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Navbar(navController, dao)

        //title
        TitleLabel(title = "Welcome to the finance tracker app!")

        //buttons
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            //analytics
            Button(onClick = {
                navController.navigate("analytics")
            }){
                Text(text = "View Analytics")
            }

            //entry
            Button(onClick = {
                navController.navigate("entry")
            }){
                Text(text = "Add new record")
            }
        }

    }
}

/**
 * Analytics page
 *
 * Shows the analytics gotten from
 * the database and processes it into a graph
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsView(navController: NavController, dao : EntryDao){
    val dateVal = rememberDatePickerState()
    var income by remember { mutableDoubleStateOf(0.00) }
    var expenses by remember { mutableDoubleStateOf(0.00) }

    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Navbar(navController, dao)

        //title
        TitleLabel(title = "Analytics")
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { showDialog = true }) {
                    Text(text = "Pick a Date")
                }

                if (showDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = "OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = "Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = dateVal)
                    }
                }
                //Button to generate the graph
                Button(onClick = {
                    //lanch a coroutine for dao operations
                    CoroutineScope(Dispatchers.IO).launch {
                        //null check
                        if (dateVal.selectedDateMillis != null && dateVal.selectedDateMillis!! > 0) {

                            //Format selected date
                            val epoch = dateVal.selectedDateMillis
                            val date = epoch?.let { Date(it) }
                            val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                            val formattedDate = date?.let { sdf.format(it) }

                            //Query dao and sum expenses and incomes
                            formattedDate?.let { month ->
                                dao.getItem(month).collect { entries ->
                                    var totalExpenses = 0.0
                                    var totalIncomes = 0.0

                                    for (entry in entries) {
                                        if (entry.expense) {
                                            totalExpenses += entry.amount
                                        } else {
                                            totalIncomes += entry.amount
                                        }
                                    }
                                    // set state
                                    income = totalIncomes
                                    expenses = totalExpenses
                                }
                            }
                        }
                    }
                }
                ) {
                    Text(text = "Get Stats (Pick Date First)")
                }

            }

            // display incomes and expenses
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Income:")
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(text = income.toString())
                Spacer(modifier = Modifier.padding(horizontal = 20.dp))
                Text(text = "Expenses:")
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(text = expenses.toString())
            }
            PieChart(income = income, expenses = expenses)

            val entries by dao.getExpensesGroupedByCategory().collectAsState(initial = emptyList())


            // expense table
            LazyColumn {
                items(entries) { entry ->
                    EntryCard(entry)
                }
            }
        }
        

    }
}

/**
 * Entry page
 *
 * Show the entry fields necessary to input a new
 * record into the database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryView(navController: NavController, dao : EntryDao){
    var inputDescription by remember { mutableStateOf("") }
    var inputAmount by remember { mutableStateOf("") }
    var inputCategory by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    val dateVal = rememberDatePickerState()

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Navbar(navController, dao)

        //title
        TitleLabel(title = "New Record")

        // Date picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(onClick = { showDialog = true }) {
                Text(text = "Pick a Date")
            }

            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(state = dateVal)
                }
            }
        }



        // Check box for the salary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Expense")

            Checkbox(
                checked = isIncome,
                onCheckedChange = { isIncome = it }
            )
        }

        // Category text field
        CustomTextField(
            labelStr = "Category:",
            placeHolder = "Food, Transport or Other",
            value = inputCategory,
            onValueChange = { newValue -> inputCategory = newValue }
        )

        // Amount text field
        CustomTextField(
            labelStr = "Amount:",
            placeHolder = "Example: 34.30",
            value = inputAmount,
            onValueChange = { newValue -> inputAmount = newValue }
        )

        // Description text field
        CustomTextField(
            labelStr = "Description:",
            placeHolder = "Enter text",
            value = inputDescription,
            onValueChange = { newValue -> inputDescription = newValue }
        )

        // Save button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(onClick = {
                Log.d("test", "EntryView: clicked")
                val input = dateVal.selectedDateMillis?.let {
                    Entry(0,isIncome,inputCategory,inputDescription,inputAmount.toDouble(),
                        it
                    )
                }
                CoroutineScope(Dispatchers.IO).launch{
                    if (input != null) {
                        dao.insert(input)
                    }
                }



            }) {
                Text(text = "Save")
            }
        }
    }
}

/**
 * Title composable
 * 
 * This creates the title label for each page
 */
@Composable
fun TitleLabel(title: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = title,
            fontSize = 30.sp,
        )
    }
}

/**
 *
 */
@Composable
fun CustomTextField(labelStr: String, placeHolder: String, value: String, onValueChange: (String) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = labelStr,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = placeHolder)},
            modifier = Modifier.width(250.dp),
        )
    }
}
/*
*Expense income pie chart
 */

@Composable
fun PieChart(income: Double, expenses: Double) {
    //get arc angles
    val total = income + expenses
    val incomeAngle = if (total > 0) (income / total) * 360 else 0f
    val expensesAngle = if (total > 0) (expenses / total) * 360 else 0f

    Canvas(modifier = Modifier.size(300.dp)) {
        val radius = size.minDimension / 2
        val center = Offset(x = size.width / 2, y = size.height / 2)

        // Draw Income part (Green)
        drawArc(
            color = Color.Green,
            startAngle = 0f,
            sweepAngle = incomeAngle.toFloat(),
            useCenter = true,
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )

        // Draw Expenses part (Red)
        drawArc(
            color = Color.Red,
            startAngle = incomeAngle.toFloat(),
            sweepAngle = expensesAngle.toFloat(),
            useCenter = true,
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )
    }
}
/*
*For use in expense table
 */
@Composable
fun EntryCard(entry: CategoryExpense) {
    Text(
        text = "Category: ${entry.category}, Value: ${entry.totalExpenses}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
}


