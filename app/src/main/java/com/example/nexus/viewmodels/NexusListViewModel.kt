package com.example.nexus.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexus.data.repositories.ListRepository
import com.example.nexus.ui.routes.list.ListCategory
import com.example.nexus.data.dataClasses.ListEntry
import com.example.nexus.data.dataClasses.SortOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NexusListViewModel  @Inject constructor(private val repo: ListRepository) : ViewModel(){

    private val selectedCategory = mutableStateOf(ListCategory.ALL)

    private val descendingOrAscendingIcon = mutableStateOf(Icons.Default.ArrowDropDown)

    fun toggleDescendingOrAscendingIcon(){
        if(repo.isDescending()){
            repo.setDescending(false)
            descendingOrAscendingIcon.value = Icons.Default.ArrowDropDown
        } else {
            repo.setDescending(true)
            descendingOrAscendingIcon.value = Icons.Default.ArrowDropUp
        }
    }

    fun getDescendingOrAscendingIcon(): ImageVector {
        return descendingOrAscendingIcon.value
    }

    fun setSortOption(option : String) = repo.setSortOption(option)

    fun onSelectedCategoryChanged(category: ListCategory){
        selectedCategory.value = category
    }

    fun getSelectedCategory(): ListCategory {
        return selectedCategory.value
    }

    fun getCategoryByName(category: String): StateFlow<List<ListEntry>> {
        val games: StateFlow<List<ListEntry>> by lazy {
            when(category){
                ListCategory.PLAYING.value -> repo.getPlaying().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                ListCategory.COMPLETED.value -> repo.getCompleted().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                ListCategory.PLANNED.value -> repo.getPlanned().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                ListCategory.DROPPED.value -> repo.getDropped().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                else -> {repo.getAllGames().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())}
            }
        }
        return games
    }
}