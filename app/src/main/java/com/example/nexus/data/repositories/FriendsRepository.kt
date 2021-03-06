package com.example.nexus.data.repositories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.nexus.data.dataClasses.Friend
import com.example.nexus.data.db.FirebaseFriendsDao
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val firebaseFriendsDao: FirebaseFriendsDao,
    private val firebaseUpdater : FirebaseUpdater
) {

    val searchTerm = mutableStateOf("")
    var searching: Lazy<MutableState<Boolean>> = lazy { mutableStateOf(false) }

    fun getFriends(): StateFlow<List<String>>{
        return firebaseFriendsDao.getFriends()
    }

    fun storeFriend(f: String){
        firebaseFriendsDao.storeFriend(f)
    }

    fun updateUser() = firebaseFriendsDao.updateUser()

    fun getFriendsData(): StateFlow<List<Friend>> {
        firebaseUpdater.updateFirebaseData()
        return firebaseFriendsDao.getFriendData()
    }


    fun setSearchTerm(term: String){
        this.searchTerm.value = term
        firebaseFriendsDao.searchTerm.value = term
    }

    fun removeFriend(f: Friend) {
        firebaseFriendsDao.removeFriend(f)
    }

    fun getUserMatches() : StateFlow<List<Friend>> {
        return firebaseFriendsDao.getSearchResult()
    }

    fun storeYourself(id:String){
        firebaseFriendsDao.storeYourself(id)
    }


    fun eventTrigger(){
        firebaseFriendsDao.doneFetching.value = false
        firebaseFriendsDao.eventTrigger()
    }

    fun doneFetching() : Boolean {
        return firebaseFriendsDao.doneFetching.value
    }

    fun getFetchedFriends() : Int {
        return firebaseFriendsDao.getfetchedFriends()
    }
    fun doneFetchingFriend() : Boolean {
        return firebaseFriendsDao.doneFetchingFriend.value
    }
    fun emptyList() {
        firebaseFriendsDao.emptyList()
    }
}