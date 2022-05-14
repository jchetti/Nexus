package com.example.nexus.ui.routes

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.example.nexus.R;
import com.example.nexus.data.dataClasses.ListEntry
import com.example.nexus.data.dataClasses.getUserId
import com.example.nexus.ui.components.NexusTopBar
import com.example.nexus.ui.components.profileStats.ProfileStats
import com.example.nexus.ui.theme.Playing
import com.example.nexus.ui.theme.Completed
import com.example.nexus.ui.theme.Planned
import com.example.nexus.ui.theme.Dropped
import com.example.nexus.viewmodels.NexusProfileViewModel
import kotlin.math.roundToLong

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NexusProfileRoute(
    vM: NexusProfileViewModel,
    navController: NavHostController,
    onOpenGameDetails: (gameId: Long) -> Unit
){
    vM.setUserid("")
   InitProfile(vM = vM, navController = navController, onOpenGameDetails = onOpenGameDetails)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InitProfile(vM: NexusProfileViewModel,
                    navController: NavHostController,
                    onOpenGameDetails: (gameId: Long) -> Unit){
    val focusManager = LocalFocusManager.current
    println("////////////////////////////////////////////profile route")

    Scaffold(
        topBar = { NexusTopBar(navController = navController, canPop = true, focusManager) }
    ) {
        ProfileScreen(
            vM, onOpenGameDetails
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(vM: NexusProfileViewModel, onOpenGameDetails: (gameId: Long) -> Unit){
    val background = vM.getUser().profileBackground
    Scaffold( ) {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.fillMaxWidth().height(200.dp)

            ) {
                if (background != "") {
                    Image(
                        painter = rememberAsyncImagePainter(background),
                        contentDescription = "profile-background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(200.dp).fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RectangleShape)
                            .background(Color.LightGray)
                    )
                }
            }
            ProfilePicture(vM, onOpenGameDetails)

        }
    }

}


@Composable
fun ProfilePicture(vM: NexusProfileViewModel, onOpenGameDetails: (gameId: Long) -> Unit){
    val profilePic = vM.getUser().profilePicture
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            if (profilePic != ""){
            Image(
                painter = rememberAsyncImagePainter(profilePic),
                contentDescription = "profile_picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            }else {
                Image(
                    painter = rememberAsyncImagePainter(R.mipmap.ic_launcher_round),
                    contentDescription = "profile_picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

            }

            Text(text = vM.getUsername(), modifier = Modifier.padding(10.dp))

            ProfileStats(vM)
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
            horizontalAlignment = Alignment.Start) {
            FavoriteList(vM, onOpenGameDetails)
        }
    }
}

@Composable
fun FavoriteList(vM: NexusProfileViewModel, onOpenGameDetails: (gameId: Long) -> Unit){
    val favorites by vM.favorites.collectAsState();
    Column(
        Modifier.padding(start = 5.dp, top = 5.dp, bottom = 10.dp)
    ){
        Text("Favourites:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row(Modifier.horizontalScroll(rememberScrollState())){
                favorites.forEach { entry -> Row{
                    FavoriteListComponent(entry, onOpenGameDetails, LocalFocusManager.current)
                    }
                }
            }
    }
}

@Composable
fun FavoriteListComponent(entry: ListEntry, onOpenGameDetails: (gameId: Long) -> Unit, focusManager: FocusManager) {
    Column(modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                onOpenGameDetails(entry.gameId)
                focusManager.clearFocus()
            })
        }
        .width(127.dp)
    ){
        Image(
            painter = rememberAsyncImagePainter(entry.coverUrl),
            contentDescription = "cover image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(127.dp, 150.dp)
                .padding(end = 5.dp)
        )
        Text(entry.title)
    }
}

