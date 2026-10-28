package com.example.blogify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation3.ui.*
import androidx.navigation3.runtime.*
import com.example.blogify.ui.theme.BlogifyTheme
import com.example.blogify.screens.*
import com.example.blogify.navigation.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationState = rememberNavigationState(Home,listOf(Home,Profiles,Explore,Activity,Profile))
            val navigator = Navigator(navigationState)
            BlogifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),bottomBar={
                    if(navigationState.backStack[navigationState.topLevelDestination]!!.last() !in listOf(Blog,Profiles)) {
                        AnimatedVisibility(visible = true) {
                            TopNavBar(
                                selectedKey = navigationState.topLevelDestination,
                                onSelected = { navigator.navigate(it) },
                                modifier = Modifier
                            )
                        }
                    }},topBar= {
                        TopAppBar(title={
                          Text(text="Blogify",color=MaterialTheme.colorScheme.surface,fontWeight=FontWeight.Bold,fontSize=23.sp)
                        },scrollBehavior=TopAppBarDefaults.pinnedScrollBehavior())
                    }
                ) {
                    innerPadding->
                    NavDisplay(
                        entries = navigationState.toEntries(
                            NavProvider = entryProvider{
                                entry<Auth>{
                                    AuthScreen()
                                }
                                entry<Home>{
                                    HomeScreen()
                                }
                                entry<Explore>{
                                    ExploreScreen()
                                }
                                entry<Blog>{
                                    BlogScreen()
                                }
                                entry<Profiles>{
                                    ProfilesScreen()
                                }
                                entry<Search>{
                                    SearchScreen()
                                }
                                entry<Activity>{
                                    ActivityScreen()
                                }
                                entry<Profile>{
                                    ProfileScreen()
                                }
                            }
                        ),
                        modifier=Modifier.padding(innerPadding),
                        onBack=navigator::goBack
                    )
                }
            }
        }
    }
}
