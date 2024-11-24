package com.example.mobileapp_project

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
private fun EmailButton(){
    Button(onClick = {

    }) {
        Text(text = "Email")
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