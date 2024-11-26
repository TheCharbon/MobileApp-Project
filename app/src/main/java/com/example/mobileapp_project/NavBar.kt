package com.example.mobileapp_project

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.concurrent.Flow

@Composable
fun Navbar(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.Center
    ){
        EmailButton()
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
public fun AnalyticsButton(navController: NavController){
    Button(onClick = {
        navController.navigate("analytics")
    }) {
        Text(text = "Analytics")
    }
}

@Composable
public fun EntryButton(navController: NavController){
    Button(onClick = {
        navController.navigate("entry")
    }) {
        Text(text = "Entry")
    }
}


@Composable
private fun EmailButton(){
    Button(onClick = {
        val result = StringBuilder()
        var analytics =
        //analytics.collect { list ->
        //    list.forEach { string ->
        //        result.append(string).append("\n")
        //    }
        //}
        shareContent("")
    }) {
        Text(text = "Email")
    }
}

fun shareContent(content: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // Specify type (text, image, etc.)
        putExtra(Intent.EXTRA_TEXT, content) // Add content
    }
    // Show the Share Sheet
    val chooser = Intent.createChooser(intent, "Share via")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure it works from composables
    //chooser.startActivity()
}