package ru.mixail_akulov.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import ru.mixail_akulov.navcomponent.ui.screens.AppNavigationBar
import ru.mixail_akulov.navcomponent.ui.screens.edit.EditItemScreen
import ru.mixail_akulov.navcomponent.ui.screens.AppToolbar
import ru.mixail_akulov.navcomponent.ui.screens.ItemsGraph
import ru.mixail_akulov.navcomponent.ui.screens.ItemsGraph.AddItemRoute
import ru.mixail_akulov.navcomponent.ui.screens.ItemsGraph.EditItemRoute
import ru.mixail_akulov.navcomponent.ui.screens.ItemsGraph.ItemsRoute
import ru.mixail_akulov.navcomponent.ui.screens.LocalNavController
import ru.mixail_akulov.navcomponent.ui.screens.MainTabs
import ru.mixail_akulov.navcomponent.ui.screens.NavigationUpAction
import ru.mixail_akulov.navcomponent.ui.screens.ProfileGraph
import ru.mixail_akulov.navcomponent.ui.screens.ProfileGraph.ProfileRoute
import ru.mixail_akulov.navcomponent.ui.screens.SettingsGraph
import ru.mixail_akulov.navcomponent.ui.screens.SettingsGraph.SettingsRoute
import ru.mixail_akulov.navcomponent.ui.screens.add.AddItemScreen
import ru.mixail_akulov.navcomponent.ui.screens.items.ItemsScreen
import ru.mixail_akulov.navcomponent.ui.screens.profile.ProfileScreen
import ru.mixail_akulov.navcomponent.ui.screens.routeClass
import ru.mixail_akulov.navcomponent.ui.screens.settings.SettingsScreen
import ru.mixail_akulov.navcomponent.ui.theme.NavigationComponentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationComponentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes = when (currentBackStackEntry?.routeClass()) {
        ItemsRoute::class -> R.string.items_screen
        AddItemRoute::class -> R.string.add_item_screen
        EditItemRoute::class -> R.string.edit_item_screen
        SettingsRoute::class -> R.string.settings_screen
        ProfileRoute::class -> R.string.profile_screen
        else -> R.string.app_name
    }

    Scaffold(
        topBar = {
            AppToolbar(
                navigationUpAction = if (navController.previousBackStackEntry == null) {
                    NavigationUpAction.Hidden
                } else {
                    NavigationUpAction.Visible(
                        onClick = { navController.navigateUp() }
                    )
                },
                titleRes = titleRes
            )
        },
        floatingActionButton = {
            if (currentBackStackEntry.routeClass() == ItemsRoute::class) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddItemRoute) }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
        bottomBar = {
            AppNavigationBar(navController = navController, tabs = MainTabs)
        }
    ) {
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = ProfileGraph,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                navigation<ItemsGraph>(startDestination = ItemsRoute) {
                    composable<ItemsRoute> { ItemsScreen() }
                    composable<AddItemRoute> { AddItemScreen() }
                    composable<EditItemRoute> { entry->
                        val route: EditItemRoute = entry.toRoute()
                        EditItemScreen(index = route.index)
                    }
                }
                navigation<SettingsGraph>(startDestination = SettingsRoute) {
                    composable<SettingsRoute> { SettingsScreen() }
                }
                navigation<ProfileGraph>(startDestination = ProfileRoute) {
                    composable<ProfileRoute> { ProfileScreen() }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun NavAppPreview() {
    NavigationComponentTheme {
        NavApp()
    }
}
