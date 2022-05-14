package com.example.nexus.data.db

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.nexus.data.dataClasses.User
import com.example.nexus.data.dataClasses.getUserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserDao @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth

){
    private val userId = mutableStateOf(getUserId(auth.currentUser))

    private val userRef = mutableStateOf(database.getReference("user/${userId.value}"))

    private val newUser = mutableStateOf(User("",
        "NewUser",
        "",
        "",))

    private val eventListener =
        object: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
//                 This method is called once with the initial value and again
//                 whenever data at this location is updated.
            if (snapshot.children.count() >= 4  && snapshot.child("email").value != null) {
                println("in de if statement")
                newUser.value.email = snapshot.child("email").value as String
                newUser.value.username = snapshot.child("username").value as String
                newUser.value.profilePicture = snapshot.child("profilePicture").value as String
                newUser.value.profileBackground = snapshot.child("profileBackground").value as String
            }
            println("current user id = ${getUserId(auth.currentUser)}")
            println("newuser emial = " + newUser.value.email)
            println("newuser username = " + newUser.value.username)

        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }


    }

    private val realtimeEntries = mutableStateOf(userRef.value.addValueEventListener(eventListener))

    fun setUserid(id: String) {
        if (id == "") {
            userId.value = getUserId(auth.currentUser)
        } else {
            userId.value = id
        }
        println("new user id = $id")
        updateUser()
    }

    fun updateUser(){
        userRef.value = database.getReference("user/${userId.value}")
        println("user/${userId.value}")
        realtimeEntries.value = userRef.value.addValueEventListener(eventListener)
    }

    private val TAG = "UserRepository"

    fun getUser() : User{
        return newUser.value
    }

    fun storeNewUser(user : User)  {
        updateUser()
        userRef.value.child("email").setValue(user.email)
        userRef.value.child("username").setValue(user.username)
        userRef.value.child("profilePicture").setValue(user.profilePicture)
        userRef.value.child("profileBackground").setValue(user.profileBackground)
    }

    fun changeUsername(username: String) {
        userRef.value.child("username").setValue(username)
    }

    fun changeProfilePic(url: String){
        userRef.value.child("profilePicture").setValue(url)
    }

    fun changeBackground(url: String){
        userRef.value.child("profileBackground").setValue(url)
    }

    fun getProfilePicture(): String {
        return newUser.value.profilePicture
    }

    fun getBackground(): String {
        return newUser.value.profileBackground
    }
}