package com.example.mobileapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileAppProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeView(navController) }
                    composable("analytics") { AnalyticsView(navController) }
                    composable("entry") { EntryView(navController) }
                }
            }
        }
    }
}

@Composable
fun HomeView(navController: NavController){
    Column {
        Row {
            Navbar(navController)
        }
    }
}

@Composable
fun AnalyticsView(navController: NavController){
    Column {
        Row {
            Navbar(navController)
        }
    }
}

@Composable
fun EntryView(navController: NavController){
    Column {
        Row {
            Navbar(navController)
        }
        Text(text = "Email")
    }
}