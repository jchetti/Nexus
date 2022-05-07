package com.example.nexus.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.nexus.data.repositories.ListRepository
import com.example.nexus.data.repositories.gameData.GameDetailRepository
import com.example.nexus.viewmodels.games.NexusGameDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun AgeConfirmComponent(
    vM: NexusGameDetailViewModel,
    navController: NavHostController,
){

    Column() {
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()){
            Text("You need to be over 18 to view this game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()){
            Button(onClick = {
                vM.onAgeVerifOpenChange(false)
            }, Modifier.padding(10.dp)){
                Text("Confirm", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Button(onClick = {
                navController.navigateUp()
            }, Modifier.padding(10.dp)){
                Text("Cancel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}

//@Preview
//@Composable
//fun preview(){
//    AgeConfirmComponent()
//}