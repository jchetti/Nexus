package com.example.nexus.data.repositories

import androidx.compose.runtime.mutableStateOf
import com.example.nexus.data.db.FirebaseListDao
import com.example.nexus.data.dataClasses.ListEntry
import com.example.nexus.data.dataClasses.SortOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepository @Inject constructor(
    private val firebaseListDao: FirebaseListDao
) {
    suspend fun storeListEntry(entry: ListEntry) = firebaseListDao.storeListEntry(entry)

    suspend fun deleteListEntry(entry: ListEntry) = firebaseListDao.deleteListEntry(entry)

    private val descending = mutableStateOf(false)

    private val sortOption = mutableStateOf(SortOptions.ALPHABETICALLY.value)

    fun getAllGames(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getAll())
    }

    fun getPlaying(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getPlaying())
    }

    fun getCompleted(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getCompleted())
    }

    fun getPlanned(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getPlanned())
    }

    fun getDropped(): Flow<List<ListEntry>> {
        return sortGames(firebaseListDao.getDropped())
    }

    fun setDescending(boolean: Boolean){
        descending.value = boolean
    }

    fun setSortOption(option : String){
        sortOption.value = option
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

    private fun sortGames(entries: Flow<List<ListEntry>>): Flow<List<ListEntry>> {
        val sortedGames = when(sortOption.value){
            SortOptions.ALPHABETICALLY.value -> setDescAsc(entries.map { it.sortedBy { game -> game.title } })
            SortOptions.SCORE.value -> setDescAsc(entries.map { it.sortedBy { game -> game.score } })
            SortOptions.TIME_PLAYED.value -> setDescAsc(entries.map { it.sortedBy { game -> game.minutesPlayed } })
            SortOptions.RELEASE_DATE.value -> setDescAsc(entries.map { it.sortedBy { game -> game.releaseDate } })
            else -> setDescAsc(firebaseListDao.getAll()) // STATUS
        }
        return sortedGames
    }

    val favorites = firebaseListDao.getFavorites()

    val doneFetching = firebaseListDao.doneFetching

    suspend fun getTop10Favorites() = firebaseListDao.getTop10Favorites()

    suspend fun getAllGamesAsState() = firebaseListDao.getAllGamesAsState()
}