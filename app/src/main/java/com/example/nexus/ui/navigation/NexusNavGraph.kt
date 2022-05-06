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


sealed class Screen(open val route: String){
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
    override val route: String
) : Screen(route) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object GameDetail : LeafScreen("game/{gameId}"){
        fun createRoute(root : Screen, gameId: Long) : String {
            return "${root.route}/game/$gameId"
        }
    }


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
    object Home : LeafScreen("home")
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
        addNotificationsScreen(navController)
        addListScreenTopLevel(navController)
        addFriendsScreen(navController)
        addProfileScreen(navController)
        addSearchScreenTopLevel(navController)
        addSettingsScreenTopLevel(navController)
        addHomeScreenTopLevel(navController)
    }
}

@ExperimentalComposeUiApi
private fun NavGraphBuilder.addHomeScreenTopLevel(
    navController: NavHostController
){
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute(Screen.Home)
    ){
        addHomeScreen(navController, Screen.Home)
        addGameDetails(navController, Screen.Home)
    }
}

@ExperimentalComposeUiApi
private fun NavGraphBuilder.addHomeScreen(
    navController: NavHostController,
    root : Screen
){
    composable(
        route = LeafScreen.Home.createRoute(root),
    ){
        NexusHomeRoute(vM = hiltViewModel(), navController,
            onOpenGameDetails = {
                    gameId -> navController.navigate(LeafScreen.GameDetail.createRoute(root, gameId))
            })
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
            vM = hiltViewModel(), navController = navController
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
    NexusSearchRoute(vM = hiltViewModel(), navController,
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
        NexusGameDetailRoute(vM = hiltViewModel(), navController, onOpenGameDetails = {
                gameId -> navController.navigate(LeafScreen.GameDetail.createRoute(root, gameId))
        })
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

private fun NavGraphBuilder.addNotificationsScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Notifications.route,
    ){
        NexusNotificationsRoute(vM = hiltViewModel(), navController)
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
        NexusListRoute(vM = hiltViewModel(), navController,
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
        NexusFriendsRoute(vM = hiltViewModel(), navController)
    }
}

private fun NavGraphBuilder.addProfileScreen(
    navController: NavHostController,
){
    composable(
        route = Screen.Profile.route,
    ){
        NexusProfileRoute(vM = hiltViewModel(), navController, vMList = hiltViewModel())
    }
}

val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
