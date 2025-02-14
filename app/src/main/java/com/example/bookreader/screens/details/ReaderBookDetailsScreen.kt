package com.example.bookreader.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.components.RoundedButton
import com.example.bookreader.data.Resources
import com.example.bookreader.model.Items
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
){

    Scaffold(
        topBar = {
            ReaderAppBar(title = "Book Details",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ){
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),

        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = remember { mutableStateOf<Resources<Items>>(Resources.Loading(null)) }

                LaunchedEffect(bookId) {  // Triggers recomposition when bookId changes
                    bookInfo.value = viewModel.getBookInfo(bookId)
                }
                when (bookInfo.value) {
                    is Resources.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resources.Success -> {
                        val book = (bookInfo.value as Resources.Success).data
                        BookDetailsInfo(bookInfo.value,navController)
                    }
                    is Resources.Error -> {
                        Log.e("nitin", "BookDetailsScreen: ${(bookInfo.value as Resources.Error).message}", )
                        Text(text = "Error: ${(bookInfo.value as Resources.Error).message}")
                    }
                }
            }


        }
    }

}

@Composable
fun BookDetailsInfo(bookInfo: Resources<Items>, navController: NavController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .shadow(4.dp, CircleShape), // ✅ Use shadow in Material 3
            shape = CircleShape
        ){
            AsyncImage(
                model = bookData?.imageLinks?.thumbnail ?: "https://via.placeholder.com/150",
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )



        }
        Text(
            text = bookData?.title.toString(),
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )



        Text(text = "Author: ${bookData?.authors.toString()}")
        Text(text = "Page Count: ${bookData?.pageCount.toString()}")
        Text(text = "Categories: ${bookData?.categories.toString()}",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center)
        Text(text = "Published: ${bookData?.publishedDate.toString()}")
        Spacer(modifier = Modifier.height(5.dp))

        val cleanDescription =
            bookData?.description?.let { HtmlCompat.fromHtml(it,HtmlCompat.FROM_HTML_MODE_LEGACY).toString() }

        val LocalDims = LocalContext.current.resources.displayMetrics


        Surface(
            modifier = Modifier
                .height(LocalDims.heightPixels.dp.times(0.09f))
                .padding(4.dp),
            shape = RectangleShape,
            border = BorderStroke(1.dp,Color.DarkGray)
        ) {
            LazyColumn(modifier = Modifier.padding(3.dp)) {
                item {
                    if (cleanDescription != null) {
                        Text(
                            text = cleanDescription,
                            modifier = Modifier.padding(6.dp))
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround) {

            RoundedButton(label = "Save"){

                val book = MBook(
                    title = bookData?.title,
                    authors = bookData?.authors.toString(),
                    photoUrl = bookData?.imageLinks?.thumbnail,
                    description = bookData?.description,
                    categories = bookData?.categories,
                    publishedDate = bookData?.publishedDate,
                    pageCount = bookData?.pageCount,
                    averageRating = bookData?.averageRating,
                    rating = bookData?.ratingsCount,
                    googleBookId = googleBookId,
                    userId = FirebaseAuth.getInstance().currentUser?.uid

                    )
                if (googleBookId != null) {
                    checkAndSaveBook(googleBookId, book, navController)
                }
            }
            Spacer(modifier = Modifier.width(25.dp))
            RoundedButton(label = "Cancel"){
                navController.popBackStack()
            }

        }

    }




}

fun saveToFireBase(book: MBook,navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String,Any>)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            navController.popBackStack()
                        }
                    }
            }.addOnFailureListener{
                Log.e("nitin", "saveToFireBase: error updating: $it", )
            }
    }
}

fun checkAndSaveBook(googleBookId: String, book: MBook, navController: NavController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()
    val booksRef = db.collection("books")

    booksRef
        .whereEqualTo("googleBookId", googleBookId)
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                saveToFireBase(book, navController)
            } else {
                Log.e("Firebase", "Book already exists in Firestore, not adding.")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error checking book existence: ", e)
        }
}

