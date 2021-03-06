package com.example.nexus.data.repositories.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nexus.data.dataClasses.ListEntry
import com.example.nexus.data.dataClasses.SortOptions
import com.example.nexus.data.db.list.FirebaseListDao
import com.example.nexus.ui.routes.lists.ListCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepository @Inject constructor(
    private val firebaseListDao: FirebaseListDao
) {
    private val descendingOrAscendingIcon = mutableStateOf(Icons.Default.ArrowDropUp)
    private val selectedCategory = mutableStateOf(ListCategory.ALL)

    fun storeListEntry(entry: ListEntry) = firebaseListDao.storeListEntry(entry)

    fun deleteListEntry(entry: ListEntry) = firebaseListDao.deleteListEntry(entry)

    private val descending = mutableStateOf(false)

    private val sortOption = mutableStateOf(SortOptions.STATUS.value)


    fun onSelectedCategoryChanged(category: ListCategory){
        selectedCategory.value = category
    }

    fun getSelectedCategory(): ListCategory {
        return selectedCategory.value
    }

    fun toggleDescendingOrAscendingIcon(){
        if(isDescending()){
            setDescending(false)
            descendingOrAscendingIcon.value = Icons.Default.ArrowDropUp
        } else {
            setDescending(true)
            descendingOrAscendingIcon.value = Icons.Default.ArrowDropDown
        }
    }

    fun getDescendingOrAscendingIcon(): ImageVector {
        return descendingOrAscendingIcon.value
    }

    fun getAllGames(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getAll())
    }

    fun getPlaying(): Flow<List<ListEntry>> {
        return removeStatusSortOption(firebaseListDao.getPlaying())
    }

    fun getCompleted(): Flow<List<ListEntry>> {
        return removeStatusSortOption(firebaseListDao.getCompleted())
    }

    fun getPlanned(): Flow<List<ListEntry>> {
        return removeStatusSortOption(firebaseListDao.getPlanned())
    }

    fun getDropped(): Flow<List<ListEntry>> {
        return removeStatusSortOption(firebaseListDao.getDropped())
    }

    fun setDescending(boolean: Boolean){
        descending.value = boolean
    }

    fun setSortOption(option : String){
        sortOption.value = option
    }

    fun getSortOption(): String {
        return sortOption.value
    }

    fun isDescending(): Boolean {
        return descending.value
    }

    private fun setDescAsc(entries: Flow<List<ListEntry>>): Flow<List<ListEntry>> {
        return if(descending.value){
            entries.map { it.reversed() }
        } else {
            entries
        }
    }

    fun sortGames(entries: Flow<List<ListEntry>>): Flow<List<ListEntry>> {
        val sortedGames = when(sortOption.value){
            SortOptions.ALPHABETICALLY.value -> setDescAsc(entries.map { it.sortedBy { game -> game.title } })
            SortOptions.SCORE.value -> setDescAsc(entries.map { it.sortedBy { game -> game.score } })
            SortOptions.TIME_PLAYED.value -> setDescAsc(entries.map { it.sortedBy { game -> game.minutesPlayed } })
            SortOptions.RELEASE_DATE.value -> setDescAsc(entries.map { it.sortedBy { game -> game.releaseDate } })
            else -> setDescAsc(firebaseListDao.getAll()) // STATUS
        }
        return sortedGames
    }

    private fun removeStatusSortOption(entries: Flow<List<ListEntry>>): Flow<List<ListEntry>> {
        if(sortOption.value == SortOptions.STATUS.value){
            sortOption.value = SortOptions.ALPHABETICALLY.value
        }
        return sortGames(entries)
    }

    fun getCategoryByName(category: String): Flow<List<ListEntry>> {
        val games: Flow<List<ListEntry>> =
            when(category){
                ListCategory.PLAYING.value -> firebaseListDao.getPlaying()
                ListCategory.COMPLETED.value -> firebaseListDao.getCompleted()
                ListCategory.PLANNED.value -> firebaseListDao.getPlanned()
                ListCategory.DROPPED.value -> firebaseListDao.getDropped()
                else -> {firebaseListDao.getAll()}
            }
        return games
    }


    val favorites = firebaseListDao.getFavorites()

    val doneFetching = firebaseListDao.doneFetching

    suspend fun getTop10Favorites() = firebaseListDao.getTop10Favorites()

    suspend fun getAllGamesAsState() = firebaseListDao.getAllGamesAsState()

    fun updateUser() = firebaseListDao.updateUser()

}