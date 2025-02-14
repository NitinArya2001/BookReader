package com.example.bookreader.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookreader.R
import com.example.bookreader.navigation.ReaderScreens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavController){
    val scale = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(durationMillis = 1500
            , easing = {
                OvershootInterpolator(8f)
                    .getInterpolation(it)
                })
        )
        delay(1000)
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate((ReaderScreens.LoginScreen.name))
        }else{
            navController.navigate((ReaderScreens.HomeScreen.name))
        }

//        navController.navigate((ReaderScreens.LoginScreen.name))

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular Image with Border
            Image(
                painter = painterResource(id = R.drawable.splash_book), // Replace with your image name
                contentDescription = "Splash Image",
                modifier = Modifier
                    .size(150.dp) // Adjust size as needed
                    .clip(CircleShape) // Clip image to a circle
                    .border(4.dp, Color.White, CircleShape), // Add border
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(25.dp)) // Space between image and text

            ReaderLogo()

        }
    }


}

@Composable
fun ReaderLogo(){
    Text(
        text = "Book Reader",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}