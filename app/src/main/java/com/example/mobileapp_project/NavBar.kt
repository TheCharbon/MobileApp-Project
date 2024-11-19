package com.example.mobileapp_project

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Navbar(navController: NavController){
    EmailButton()
    HomeButton(navController)
    AnalyticsButton(navController)
    EntryButton(navController)
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