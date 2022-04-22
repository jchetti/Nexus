package com.example.nexus.data.repositories.gameData

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.exceptions.RequestException
import com.api.igdb.request.IGDBWrapper
import com.api.igdb.request.covers
import com.api.igdb.request.games
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import proto.Cover
import proto.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor() {
    var gameList : MutableState<List<Game>> = mutableStateOf(ArrayList())
    val searchTerm = mutableStateOf("")
    init {
        IGDBWrapper.setCredentials("trt599r053jhg3fmjnhehpyzs3xh4w", "tm3zxdsllw4czte0n4mmqkly6crehf")
    }

    suspend fun getGames() = withContext(Dispatchers.IO){
        val apicalypse = APICalypse().fields("cover.*,*").search(searchTerm.value)
        gameList.value = emptyList()
        try{
            val games: List<Game> = IGDBWrapper.games(apicalypse)
            gameList.value = games
        } catch(e: RequestException) {
            print("NEXUS API FETCH ERROR:")
            println(e.result)
        }
    }

    fun setSearchTerm(term: String){
        this.searchTerm.value = term
    }
}