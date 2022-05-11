package com.example.nexus.data.repositories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.nexus.data.db.FirebaseFriendsDao
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val firebaseFriendsDao: FirebaseFriendsDao
) {

    val searchTerm = mutableStateOf("")
    var searching: Lazy<MutableState<Boolean>> = lazy { mutableStateOf(false) }

    fun getFriends(): Flow<List<String>>{
        return firebaseFriendsDao.getFriends()
    }

    fun storeFriend(f: String) = firebaseFriendsDao.storeFriend(f)

    fun updateUser() = firebaseFriendsDao.updateUser()

    fun setSearchTerm(term: String){
        this.searchTerm.value = term
    }
}