package com.example.nexus.ui.routes


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nexus.ui.components.loginComponents.*
import com.example.nexus.viewmodels.NexusLoginViewModel

@Composable
fun NexusLoginRoute(vM: NexusLoginViewModel, navController: NavHostController) {
    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            EmailTextField(
                { vM.getEmail() },
                { e -> vM.setEmail(e) },
                { vM.getIsEmailValidTest() },
                { vM.getUserAlreadyExists() },
                { vM.getUserDoesNotExists() }
            )
            PasswordTextField(
                { vM.getPassword() },
                { e -> vM.setPassword(e) },
                { vM.getIsPasswordValidTest() },
                { e -> vM.setIsPasswordVisible(e) },
                { vM.getIsPasswordVisible() }
            )
            Spacer(modifier = Modifier.height(70.dp))
            LoginButton(
                { vM.getIsEmailValid() },
                { vM.getIsPasswordValid() },
                { vM.signIn() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CreateAccountButton (
                navController
            )
        }
    }
}
