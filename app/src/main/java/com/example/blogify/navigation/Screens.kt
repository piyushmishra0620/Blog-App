package com.example.blogify.navigation

import androidx.navigation3.runtime.*
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen:NavKey

@Serializable
data object Auth:AppScreen

@Serializable
data object Home:AppScreen

@Serializable
data object Explore:AppScreen

@Serializable
data object Blog: AppScreen

@Serializable
data object Profiles:AppScreen

@Serializable
data object Search:AppScreen

@Serializable
data object Activity:AppScreen

@Serializable
data object Profile:AppScreen
