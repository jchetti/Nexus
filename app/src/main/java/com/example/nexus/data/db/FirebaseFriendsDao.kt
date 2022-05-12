package com.example.nexus.data.db

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.nexus.data.dataClasses.User
import com.example.nexus.data.dataClasses.getUserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFriendsDao @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
){
    private val friendsRef = mutableStateOf(database.getReference("user/${getUserId(auth.currentUser)}/friends"))

    private val friends = MutableStateFlow(emptyList<String>())

    private val friendsData = MutableStateFlow(ArrayList<User>())

    private var friendsReferences = ArrayList<DatabaseReference>()

    private val eventListener =
        object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                 This method is called once with the initial value and again
//                 whenever data at this location is updated.

            val newFriends = mutableListOf<String>()
            friendsReferences = ArrayList()
            friendsData.update { ArrayList() }
            for(child in snapshot.children){
                newFriends.add(child.value as String)
                val ref = database.getReference(("user/${child.value as String}"))
                friendsReferences.add(ref)
                ref.addValueEventListener(friendEventListener)

            }
                friends.update{newFriends}
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        }

    private val friendEventListener =
        object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                 This method is called once with the initial value and again
//                 whenever data at this location is updated.
                lateinit var newUser: User
                newUser.email = snapshot.child("email").value as String
                newUser.username = snapshot.child("username").value as String
                newUser.profilePicture = snapshot.child("profilePicture").value as String
                newUser.profileBackground = snapshot.child("profileBackground").value as String
                friendsData.value.add(newUser)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        }

    private val realtimeFriends = mutableStateOf(friendsRef.value.addValueEventListener(eventListener))

    fun updateUser(){
        friendsRef.value = database.getReference("user/${getUserId(auth.currentUser)}/friends")
        realtimeFriends.value = friendsRef.value.addValueEventListener(eventListener)
    }

    private val TAG = "UserRepository"

    fun getFriends(): StateFlow<List<String>> {
        return friends
    }

    suspend fun getFriendProfilePicture(friendId: String): String {
        val tempuser = database.getReference("user/${friendId}")
        val profilePic = CompletableDeferred("")
        tempuser.child("profilePicture").get().addOnSuccessListener {
            println("TESTESTESTESTESTSE PROFILE PICc")
            profilePic.complete(it.value.toString())
            profilePic.toString()
            println("pic = $profilePic")

        }
        return profilePic.await()
    }

    suspend fun getFriendProfileBackground(friendId: String): String {
        val tempuser = database.getReference("user/${friendId}")
        val background = CompletableDeferred("")
        tempuser.child("profileBackground").get().addOnSuccessListener {
            background.complete(it.value.toString())
        }
        return background.await()
    }


    fun storeFriend(id: String){
        friendsRef.value.child(id).setValue(id)
    }

    suspend fun getFriendUsername(friendId: String): String {
        val tempuser = database.getReference("user/${friendId}")
        val friendUsername = CompletableDeferred("")
        tempuser.child("username").get().addOnSuccessListener {
            friendUsername.complete(it.value.toString())
        }
        return friendUsername.await()
    }
}