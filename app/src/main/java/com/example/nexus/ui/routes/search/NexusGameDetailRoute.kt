package com.example.nexus.ui.routes.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.example.nexus.data.web.ListEntry
import com.example.nexus.ui.components.GameFormComponent
import com.example.nexus.ui.routes.list.ListCategory
import com.example.nexus.viewmodels.games.NexusGameDetailViewModel
import kotlin.math.roundToInt

@Composable
fun NexusGameDetailRoute(
    vM: NexusGameDetailViewModel
){
    vM.onGetGameEvent()
    if(vM.gameList.value.isNotEmpty()) {
        val game = vM.gameList.value[0]
        if(!vM.getGameFormOpen()){
            Column (Modifier.verticalScroll(rememberScrollState())) {
                Row (Modifier.height(200.dp)){
                    Image(
                        painter = rememberAsyncImagePainter(game.cover
                            ?.let {
                                imageBuilder(
                                    it.imageId,
                                    ImageSize.COVER_BIG,
                                    ImageType.JPEG
                                )
                            }),
                        contentDescription = "cover image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(169.dp, 200.dp)
                            .padding(5.dp)
                    )
                    Column {
                        Text(game.name, fontSize = 20.sp)
                        Row {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "star icon")
                            if (game.ratingCount > 0) {
                                Text(
                                    text = " " + ((game.rating * 10).roundToInt()
                                        .toDouble() / 100).toString()
                                )
                            } else {
                                Text(text = " N/A")
                            }
                        }
                        Text(text = game.ratingCount.toString() + " votes", fontSize = 10.sp)

                        Row{
                            Row(Modifier.horizontalScroll(rememberScrollState())){
                                Text("platforms: ")
                                for(platform in game.platformsList){
                                    Text(platform.abbreviation + ", ")
                                }
                            }
                        }

                        Row{
                            Text("genres: ")
                            Row(Modifier.horizontalScroll(rememberScrollState())){
                                for(genre in game.genresList){
                                    Text(genre.name + ", ")
                                }
                            }
                        }

                    }
                }

                Text("Summary:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                Text(game.summary, Modifier.padding(bottom = 10.dp))
                Button(onClick = {vM.onGameFormOpenChanged(true)}){
                    Text(text = "Add game")
                }

                Text("Screenshots", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .height(300.dp)){
                    for(screenshot in game.screenshotsList){
                        Image(
                            painter = rememberAsyncImagePainter(screenshot?.let {
                                    imageBuilder(it.imageId, ImageSize.SCREENSHOT_BIG,
                                        ImageType.JPEG
                                    )
                                }),
                            contentDescription = "cover image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size((screenshot.width / screenshot.height * 500).dp, 500.dp)
                                .padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        } else {
            GameFormComponent(game, vM)
        }
    } else {
        Text("Loading game...")
    }
}