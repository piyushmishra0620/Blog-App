package com.example.blogify.navigation

import androidx.navigation3.runtime.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.*
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.compose.runtime.snapshots.*
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.*

class NavigationState(
    val startRoute: NavKey,
    val topLevelRoute: MutableState<NavKey>,
    val backStack: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelDestination by topLevelRoute
    var stacksInUse = mutableStateListOf(startRoute)
}

@Composable
fun rememberNavigationState(startRoute:NavKey,topLevelRoutes:List<NavKey>):NavigationState{
    val topRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        configuration=config,
        serializer=MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ){
        mutableStateOf(startRoute)
    }
    val backstacks = topLevelRoutes.associateWith{ele->
        rememberNavBackStack(config,ele)
    }
    return remember(){
        NavigationState(startRoute,topRoute,backstacks)
    }
}

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Home::class)
            subclass(Explore::class)
            subclass(Blog::class)
            subclass(Profiles::class)
            subclass(Search::class)
            subclass(Activity::class)
            subclass(Profile::class)
        }
    }
}

class Navigator(val state:NavigationState){
    fun navigate(route:NavKey){
        if(route in state.backStack.keys){
            state.topLevelDestination=route
            if(route==Home && state.stacksInUse.size!=1){
                if(state.stacksInUse.lastIndexOf(route)!=0){
                    state.stacksInUse.removeAt(state.stacksInUse.lastIndexOf(route))
                }
                state.stacksInUse.add(route)
            }else if(route!=Home){
                if(state.stacksInUse.contains(route)==true){
                    state.stacksInUse.remove(route)
                }
                state.stacksInUse.add(route)
            }
        }else{
            state.backStack[state.topLevelDestination]?.add(route)
        }
    }

    fun goBack(){
        val currentBackStack = state.backStack[state.topLevelDestination]
        val lastRoute = currentBackStack?.last()
        if(lastRoute in state.backStack.keys){
            state.stacksInUse.removeLastOrNull()
            state.topLevelDestination=state.stacksInUse.last()
        }else{
            currentBackStack?.removeLastOrNull()
        }
    }
}

@Composable
fun NavigationState.toEntries(NavProvider:(NavKey)->NavEntry<NavKey>):SnapshotStateList<NavEntry<NavKey>>{
    val decoratedEntries= backStack.mapValues{
        (_,stack)->
        val decorators = listOf(rememberSaveableStateHolderNavEntryDecorator<NavKey>(),rememberViewModelStoreNavEntryDecorator())
        rememberDecoratedNavEntries(stack,decorators,NavProvider)
    }
    return stacksInUse.flatMap{decoratedEntries[it]?:emptyList()}.toMutableStateList()
}
