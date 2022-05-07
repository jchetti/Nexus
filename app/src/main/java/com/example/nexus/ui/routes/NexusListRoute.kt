package com.example.nexus.ui.routes

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexus.viewmodels.NexusListViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.navigation.NavHostController
import com.example.nexus.ui.components.NexusTopBar
import com.example.nexus.ui.components.list.ListCategoryComponent
import com.example.nexus.ui.theme.NexusBlue
import com.example.nexus.ui.theme.NexusGray

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun NexusListRoute(
    vM: NexusListViewModel,
    navController: NavHostController,
    onOpenGameDetails : (gameId: Long) -> Unit,
){
    Scaffold(
        topBar = { NexusTopBar(navController = navController, canPop = false) }
    ) {
        Scaffold(topBar = {
            TopNavigationBar(vM.getSelectedCategory(), vM::onSelectedCategoryChanged)
        }){
            ListCategoryComponent(category = vM.getSelectedCategory(), vM,
                onOpenGameDetails = onOpenGameDetails)
        }
    }

}

//scrollable TopNavigationbar
@Composable
fun TopNavigationBar(
    selectedCategory: ListCategory,
    onSelectedCategoryChanged: (ListCategory) -> Unit
){

    LazyRow(modifier = Modifier
        .fillMaxWidth(),
        state = rememberLazyListState()
    ){
        items(getAllListCategories()) { it ->
            TopNavigationBox(
                category = it,
                isSelected = it == selectedCategory,
                onSelectedCategoryChanged = {onSelectedCategoryChanged(it)})
        }
    }
}

//items in the TopNavigationBar
@Composable
fun TopNavigationBox(
    category: ListCategory,
    isSelected: Boolean,
    onSelectedCategoryChanged: (ListCategory) -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if(isSelected) NexusBlue else NexusGray
    ){
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedCategoryChanged(category)
                }
            )
        ) {
            Text(
                text = category.value,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(5.dp),
                fontSize = 22.sp)
        }

    }
}

enum class ListCategory(val value: String){
    ALL("All"),
    PLAYING("Playing"),
    COMPLETED("Completed"),
    PLANNED("Planned"),
    DROPPED("Dropped")
}

fun getAllListCategories(): List<ListCategory> =
    listOf(ListCategory.ALL, ListCategory.PLAYING, ListCategory.COMPLETED,
        ListCategory.PLANNED, ListCategory.DROPPED
    )