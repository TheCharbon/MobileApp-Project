package com.example.mobileapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Label
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

/**
 * Home page
 *
 * Shows the home page,
 * has access to analytics and entry pages
 */
@Composable
fun HomeView(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Row{
            Navbar(navController)
        }

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
@Composable
fun AnalyticsView(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Row{
            Navbar(navController)
        }

        //title
        TitleLabel(title = "Analytics")

        //graph, etc...
    }
}

/**
 * Entry page
 *
 * Show the entry fields necessary to input a new
 * record into the database.
 */
@Composable
fun EntryView(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ){
        //navbar
        Row{
            Navbar(navController)
        }

        //title
        TitleLabel(title = "New Record")

        //text fields
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
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(text = title)
    }
}