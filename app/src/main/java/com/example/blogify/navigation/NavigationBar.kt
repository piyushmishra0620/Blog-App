package com.example.blogify.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

data class BottomBars(
    val icon: ImageVector,
    val key:NavKey
)

val topLevelDestination= listOf(
    BottomBars(
        icon=Icons.Rounded.Home,
        key=Home
    ),
    BottomBars(
        icon=Icons.Filled.PersonSearch,
        key=Explore
    ),
    BottomBars(
        icon=Icons.Filled.Search,
        key=Search
    ),
    BottomBars(
        icon=Icons.Rounded.Bookmark,
        key=Activity
    ),
    BottomBars(
        icon= Image(),
        key=Profile
    )
)

@Composable
fun TopNavBar(
    selectedKey:NavKey,
    onSelected:(NavKey)->Unit,
    modifier:Modifier= Modifier
){
    BottomAppBar(modifier=Modifier.navigationBarsPadding(),containerColor=MaterialTheme.colorScheme.background,contentColor=MaterialTheme.colorScheme.surface){
        topLevelDestination.forEach{
            NavigationBarItem(
                selected=selectedKey==it.key,
                onClick={onSelected(it.key)},
                alwaysShowLabel=false,
                colors= NavigationBarItemDefaults.colors(
                    selectedIconColor=Color.Unspecified,
                    selectedTextColor=Color.Unspecified,
                    unselectedTextColor=Color.Unspecified,
                    indicatorColor=MaterialTheme.colorScheme.tertiary
                ),
                icon={
                    Icon(it.icon,contentDescription="")
                }
            )
        }
    }
}
