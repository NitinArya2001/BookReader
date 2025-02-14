package com.example.bookreader.screens.stats

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.ReaderApp
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderStatsScreen(navController: NavController,viewModel: HomeScreenViewModel = hiltViewModel()){

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ){
                navController.popBackStack()
            }
        },
    ) {
        Surface {
            books = if(!viewModel.data.value.data.isNullOrEmpty()){
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else{
                emptyList()
            }

            val readBookList: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
                books.filter { mBook ->
                    (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                }
            }else{
                emptyList()
            }

            val readingBooks = books.filter { mBook ->
                (mBook.startReading != null) && (mBook.finishedReading == null)
            }


            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Icon Box
                    Box(
                        modifier = Modifier
                            .size(55.dp) // Slightly larger for better proportion
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = "Profile Icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // User Info Column
                    Column {
                        Text(
                            text = "Hi, ${currentUser?.email?.split("@")?.get(0)?.uppercase(Locale.getDefault()) ?: "User"}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Welcome Back!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {



                    Column(modifier = Modifier.padding(start = 25.dp,top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start) {

                        Text(text = "Your Stats", style = MaterialTheme.typography.headlineSmall)
                        Divider()
                        Text(text = "You are reading: ${readingBooks.size} books", style = MaterialTheme.typography.headlineSmall)
                        Text(text = "You've read ${readBookList.size} books", style = MaterialTheme.typography.headlineSmall)


                    }

                }
                if(viewModel.data.value.loading == true){
                    CircularProgressIndicator()
                }else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(readBookList) { book -> // ✅ Correct syntax
                            BookRow(book = book, navController = navController)
                        }

                    }
                }
            }
        }
    }

}

@Composable
fun BookRow(book: MBook, navController: NavController) {

    Log.e("chintu", "BookRow: ${book.googleBookId}", )
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailsScreen.name+"/${book.googleBookId}")
        }
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(top = 13.dp),
        shape = RectangleShape,

        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.Top) {

            val imageUrl:String = book.photoUrl.toString()

            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .padding(end = 4.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title on the left
                    book.title?.let {
                        Text(
                            text = it,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f) // Takes available space
                        )
                    }

                    // Thumb Up Icon on the right (only if rating is 4 or more)
                    if (book.rating!! >= 4) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumb Up",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(35.dp) // Adjust icon size
                                .padding(start = 8.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Authors: ${book.authors}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Published: ${book.publishedDate}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Category: ${book.categories}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }


        }
    }

}
