package com.example.nexus.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nexus.ui.routes.*
import com.example.nexus.ui.routes.list.NexusListRoute
import com.example.nexus.ui.routes.search.NexusGameDetailRoute
import com.example.nexus.ui.routes.search.NexusSearchRoute
import proto.Game


sealed class Screen(val route: String){
    object Login : Screen("login")
    object Home : Screen("home")
    object Notifications : Screen("notifications")
    object List : Screen("list")
    object Friends : Screen("friends")
    object Profile : Screen("profile")
    object Search: Screen("search")
    object Settings: Screen("settings")
}

sealed class LeafScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

//    object GameDetail : LeafScreen("{gameOrList}/{gameId}"){
//        fun createRoute(root : Screen, gameId: Long) : String {
//            val route = if(root == Screen.Search){
//                "game"
//            } else { // root == Screen.List
//                "list"
//            }
//            return "${root.route}/$route/$gameId"
//        }
//    }

    object GameDetail : LeafScreen("game/{gameId}"){
        fun createRoute(root : Screen, gameId: Long) : String {
            return "${root.route}/game/$gameId"
        }
    }

//    object GameDetailFromList : LeafScreen("list/{gameId}"){
//        fun createRoute(root : Screen, gameId: Long) : String {
//            return "${root.route}/list/$gameId"
//        }
//    }

//    object GameForm : LeafScreen("game/{gameId}/{gameName}"){
//        fun createRoute(root: Screen, gameId : Long, gameName: String) : String{
//            return "${root.route}/game/$gameId/$gameName"
//        }
//    }

    //TODO navigation van de verschillende settings pages
//    object SettingsPages : LeafScreen("settings/{settingScreen}"){
//        fun createRoute(root: Screen, settingsPage: String): String {
//            return "${root.route}/$settingsPage"
//        }
//    }

    object Settings: LeafScreen("settings")
    object Search : LeafScreen("search")
    object List : LeafScreen("list")
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun NexusNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
    modifier: Modifier
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ){
        addLoginScreen(navController)
        addHomeScreen(navController)
        addNotificationsScreen(navController)
        addListScreenTopLevel(navController)
        addFriendsScreen(navController)
        addProfileScreen(navController)
        addSearchScreenTopLevel(navController)
        addSettingsScreenTopLevel(navController)
    }
}
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addSettingsScreenTopLevel(
    navController: NavHostController,
){
    navigation(
        route = Screen.Settings.route,
        startDestination = LeafScreen.Settings.createRoute(Screen.Settings)
    ) {
        addSettingsScreen(navController, Screen.Settings)
    }
}

private fun NavGraphBuilder.addSettingsScreen(
    navController: NavHostController,
    root: Screen
){
    composable(
        route = LeafScreen.Settings.createRoute(root)
    ){
        NexusSettingsRoute(
            vM = hiltViewModel()
        )
    }

}
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addSearchScreenTopLevel(
    navController: NavHostController
){
    navigation(
        route = Screen.Search.route,
        startDestination = LeafScreen.Search.createRoute(Screen.Search)
    ) {
        addSearchScreen(navController, Screen.Search)
        addGameDetails(navController, Screen.Search)
    }
//    composable(
//        route = Screen.Search.route
//    ){
//        NexusSearchRoute(vM = hiltViewModel(),
//        onOpenGameDetails = {
//            gameId -> navController.navigate(LeafScreen.GameDetail.createRoute(Screen.Search, gameId))
//        })
//    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addListScreenTopLevel(
    navController: NavHostController
){
    navigation(
        route = Screen.List.route,
        startDestination = LeafScreen.List.createRoute(Screen.List)
    ) {
        addListScreen(navController, Screen.List)
        addGameDetails(navController, Screen.List)
    }
}


@ExperimentalComposeUiApi
private fun NavGraphBuilder.addSearchScreen(
    navController: NavHostController,
    root : Screen
){
    composable(
        route = LeafScreen.Search.createRoute(root)
    ){
    NexusSearchRoute(vM = hiltViewModel(),
        onOpenGameDetails = {
                gameId -> navController.navigate(LeafScreen.GameDetail.createRoute(root, gameId))
        })
    }
}

private fun NavGraphBuilder.addGameDetails(
    navController: NavHostController,
    root: Screen
){
    composable(
        route = LeafScreen.GameDetail.createRoute(root),
        arguments = listOf(
            navArgument("gameId"){type = NavType.LongType}
        )
    ){
        NexusGameDetailRoute(vM = hiltViewModel())
    }
}

private fun NavGraphBuilder.addLoginScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Login.route,
    ){
        NexusLoginRoute(vM = hiltViewModel())
    }
}

@ExperimentalComposeUiApi
private fun NavGraphBuilder.addHomeScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Home.route,
    ){
        NexusHomeRoute(vM = hiltViewModel())
    }
}

private fun NavGraphBuilder.addNotificationsScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Notifications.route,
    ){
        NexusNotificationsRoute(vM = hiltViewModel())
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addListScreen(
    navController: NavHostController,
    root : Screen
){
    composable(
        route = LeafScreen.List.createRoute(root)
    ){
        NexusListRoute(vM = hiltViewModel(),
            onOpenGameDetails = {
                    gameId -> navController.navigate(LeafScreen.GameDetail.createRoute(root, gameId))
            })
    }
}

private fun NavGraphBuilder.addFriendsScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Friends.route,
    ){
        NexusFriendsRoute(vM = hiltViewModel())
    }
}

private fun NavGraphBuilder.addProfileScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Profile.route,
    ){
        NexusProfileRoute(vM = hiltViewModel(), vMList = hiltViewModel())
    }
}

val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
