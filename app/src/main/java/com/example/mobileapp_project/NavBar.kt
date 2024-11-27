package com.example.mobileapp_project

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapp_project.data.Entry
import com.example.mobileapp_project.data.EntryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun Navbar(navController: NavController, dao: EntryDao){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.Center
    ){
        EmailButton(navController, dao)
        HomeButton(navController)
        AnalyticsButton(navController)
        EntryButton(navController)
    }
}

@Composable
private fun HomeButton(navController: NavController){
    Button(onClick = {
        navController.navigate("home")
    }) {
        Text(text = "Home")
    }
}

@Composable
private fun AnalyticsButton(navController: NavController){
    Button(onClick = {
        navController.navigate("analytics")
    }) {
        Text(text = "Analytics")
    }
}

@Composable
private fun EntryButton(navController: NavController){
    Button(onClick = {
        navController.navigate("entry")
    }) {
        Text(text = "Entry")
    }
}


@Composable
private fun EmailButton(navController: NavController, dao: EntryDao){
    val context = LocalContext.current
    Button(onClick = {
        val result = StringBuilder()
        var analytics: Flow<List<Entry>> = emptyFlow()
        CoroutineScope(Dispatchers.IO).launch{
            analytics = dao.getAll()
            analytics.collect { list ->
                list.forEach { string ->
                    result.append(string).append("\n")
                }
            }
        }
        shareContent(context, result.toString())
    }) {
        Text(text = "Email")
    }
}

private fun shareContent(context: Context, content: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, content)
        putExtra(Intent.EXTRA_TEXT, content)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            "Share Via"
        )
    )
}